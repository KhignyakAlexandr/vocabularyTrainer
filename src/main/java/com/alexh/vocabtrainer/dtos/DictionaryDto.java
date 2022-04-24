package com.alexh.vocabtrainer.dtos;

import com.alexh.vocabtrainer.entities.AudioLink;
import com.alexh.vocabtrainer.entities.Meaning;
import lombok.Builder;

import java.util.List;

@Builder
public class DictionaryDto {
    public String transcription;
    public List<Meaning> meanings;
    public AudioLink audioLink;
}
