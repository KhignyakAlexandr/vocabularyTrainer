package com.alexh.vocabtrainer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Synonym extends AbstractEntity{

    public String word;

    @JsonIgnore
    @ManyToOne(targetEntity = Meaning.class)
    public Meaning meaning;
}
