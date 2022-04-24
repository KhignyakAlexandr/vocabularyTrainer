package com.alexh.vocabtrainer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AudioLink {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    public Card card;

    public String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
