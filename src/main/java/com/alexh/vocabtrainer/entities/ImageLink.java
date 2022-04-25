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
public class ImageLink extends AbstractEntity{

    public String url;

    @JsonIgnore
    @ManyToOne
    public Card card;

}
