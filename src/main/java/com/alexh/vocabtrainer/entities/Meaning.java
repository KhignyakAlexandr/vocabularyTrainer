package com.alexh.vocabtrainer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Meaning {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    Card card;

    public String partOfSpeech;

    @OneToMany
    public List<Definition> definitions;
    @OneToMany
    public List<Example> examples;
    @OneToMany
    public List<Synonym> synonyms;
    @OneToMany
    public List<Antonym> antonyms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
