package com.alexh.vocabtrainer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Meaning extends AbstractEntity{

    public String partOfSpeech;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    public Set<Definition> definitions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    public Set<Example> examples;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    public List<Synonym> synonyms;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    public List<Antonym> antonyms;

    @JsonIgnore
    @ManyToOne(targetEntity = Card.class)
    public Card card;
}
