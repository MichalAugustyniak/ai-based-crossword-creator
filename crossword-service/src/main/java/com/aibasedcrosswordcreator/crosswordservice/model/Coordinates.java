package com.aibasedcrosswordcreator.crosswordservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates")
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer x;
    @Column(nullable = false)
    private Integer y;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crossword_id", referencedColumnName = "id", nullable = false)
    private Crossword crossword;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clue_id", referencedColumnName = "id", nullable = false)
    private Clue clue;
    @Column(nullable = false)
    private String orientation;
    private String identifier;
}
