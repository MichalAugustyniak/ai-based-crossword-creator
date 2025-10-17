package com.aibasedcrosswordcreator.crosswordservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "standard_crosswords")
public class StandardCrossword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private Integer width;
    @Column(nullable = false)
    private Integer height;
    private String creator;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Theme theme;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Language language;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private ProviderModel providerModel;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "crossword", orphanRemoval = true)
    private Set<Coordinates> coordinates = new HashSet<>();
    @Column(nullable = false)
    private String type;
}