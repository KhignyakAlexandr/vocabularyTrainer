package com.alexh.vocabtrainer.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartOfSpeechRegistrationForm {

    public String word;
    public String transcription;
    public String definition;
    public List<String> examples;
}
