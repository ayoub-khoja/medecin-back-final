package com.example.goldengymback.serviceImpl;

import com.example.goldengymback.model.MammaryScan;
import com.example.goldengymback.repository.MammaryScanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class BreastCancerService implements com.example.goldengymback.service.BreastCancerService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.endpoint}")
    private String openaiApiEndpoint;

    @Value("${openai.api.deployment}")
    private String openaiApiDeployment;

    @Autowired
    private MammaryScanRepo mammaryScanRepository;

    @Override
    public String getAcrScore(Long scanId) {
        MammaryScan scan = mammaryScanRepository.findById(scanId)
                .orElseThrow(() -> new RuntimeException("Scan not found for ID: " + scanId));

        String prompt = createPrompt(scan);
        String aiResponse = callOpenAiApi(prompt);
        updateScanWithAiResponse(aiResponse, scan);

        return aiResponse;
    }

    @Override
    public String getDiagnosticFromData(String description) {
        String aiResponse = callOpenAiApi(description);

        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            throw new RuntimeException("Réponse IA vide ou invalide.");
        }

        return aiResponse;
    }

    private void updateScanWithAiResponse(String aiResponse, MammaryScan scan) {
        Pattern pattern = Pattern.compile("ACR\\s*[:\\-]?\\s*(\\d)\\s*\\(Type\\s*([ABC])\\).*?Action recommandée\\s*[:\\-]?\\s*(.+)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(aiResponse);

        if (matcher.find()) {
            String acrScore = matcher.group(1).trim(); // Exemple : "3"
            String acrType = matcher.group(2).trim();  // Exemple : "B"
            String conduite = matcher.group(3).trim(); // Exemple : "Biopsie"


            List<String> validConduites = List.of("Surveillance", "Biopsie", "Ablation chirurgicale", "Traitement médical");

            if (!validConduites.contains(conduite)) {
                throw new RuntimeException("Conduite invalide reçue de l'IA : " + conduite);
            }

            scan.setConclusionIA(acrScore);
            scan.setAcrType(acrType);
            scan.setConduiteATenir(conduite);

            mammaryScanRepository.save(scan);
        } else {
            throw new RuntimeException("Format de réponse IA invalide pour le scan ID: " + scan.getId() + ". Réponse: " + aiResponse);
        }
    }

    private String callOpenAiApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestBody.put("max_tokens", 300);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String apiUrl = String.format("%s/openai/deployments/%s/chat/completions?api-version=2025-01-01-preview",
                openaiApiEndpoint, openaiApiDeployment);

        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        }

        throw new RuntimeException("Invalid response from OpenAI API");
    }

    private String createPrompt(MammaryScan scan) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Compte rendu de l'examen mammaire du patient :\n");
        prompt.append("- Densité mammaire : ").append(scan.getDensiteMammaire()).append("\n");
        prompt.append("- Asymétrie : ").append(scan.isAsymetrie() ? "Oui" : "Non").append("\n");
        if (scan.isAsymetrie()) {
            prompt.append("- Type d'asymétrie : ").append(scan.getTypeAsymetrie()).append("\n");
        }
        prompt.append("- Distorsion architecturale : ").append(scan.isDistorsionArchitecturale() ? "Oui" : "Non").append("\n");
        if (scan.isDistorsionArchitecturale()) {
            prompt.append("- Détail de la distorsion architecturale : ").append(scan.getOptionDistorsionArchitecturale()).append("\n");
        }
        prompt.append("- Calcifications : ").append(scan.isCalcifications() ? "Oui" : "Non").append("\n");
        if (scan.isCalcifications()) {
            prompt.append("- Types de calcifications : ").append(scan.getTypesCalcifications()).append("\n");
            prompt.append("- Calcifications bénignes : ").append(scan.getCalcificationsBenignes()).append("\n");
            prompt.append("- Calcifications suspectes : ").append(scan.getCalcificationsSuspectes()).append("\n");
            prompt.append("- Distribution des microcalcifications : ").append(scan.getDistributionMicrocalcifications()).append("\n");
        }

        prompt.append("- Signes associés à la mammographie : ").append(scan.getSignesAssociesMammographie()).append("\n");
        prompt.append("- Échostructure mammaire : ").append(scan.getEchostructureMammaire()).append("\n");
        prompt.append("- Signes associés à l'échographie : ").append(scan.getSignesAssociesEchostructure()).append("\n");
        prompt.append("- Cas spéciaux : ").append(scan.getCasSpeciaux()).append("\n");

        prompt.append("\nEn te basant sur les informations ci-dessus, merci de fournir :\n");
        prompt.append("- Le score ACR (entre 1 et 5)\n");
        prompt.append("- Le type de densité mammaire associé (A, B ou C)\n");

        prompt.append("- L'action clinique recommandée parmi : 'Surveillance', 'Biopsie', 'Ablation chirurgicale', 'Traitement médical'\n");
        prompt.append("Réponds uniquement en français.\n");
        prompt.append("Format de réponse attendu : 'ACR : X (Type A). Action recommandée : [UNE DES 4 OPTIONS]'");

        return prompt.toString();
    }
}
