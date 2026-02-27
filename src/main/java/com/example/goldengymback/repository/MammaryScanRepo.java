package com.example.goldengymback.repository;

import com.example.goldengymback.model.MammaryScan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MammaryScanRepo extends JpaRepository<MammaryScan, Long> {

    // Define method to find MammaryScan by densiteMammaire
    List<MammaryScan> findByDensiteMammaire(String densiteMammaire);

    // Define method to find MammaryScan by asymetrie
    List<MammaryScan> findByAsymetrie(Boolean asymetrie);

}
