package com.example.goldengymback.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CasSpecial {
    private String nom;
    private String localisation;
}
