package com.alexh.vocabtrainer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    public String word;
    public String transcription;
    @OneToMany
    public List<Meaning> meanings;
    @OneToMany
    public List<AudioLink> audioLinks;
    @OneToMany
    public List<ImageLink> imageLinks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
