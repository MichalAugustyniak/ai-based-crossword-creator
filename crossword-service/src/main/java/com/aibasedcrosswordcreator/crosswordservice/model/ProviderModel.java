package com.aibasedcrosswordcreator.crosswordservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "provider_model", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "model"})
})
public class ProviderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Provider provider;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Model model;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "providerModel", orphanRemoval = true)
    private Set<Clue> clues = new HashSet<>();
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "providerModel", orphanRemoval = true)
    private Set<StandardCrossword> crosswords = new HashSet<>();
}
