package com.alexh.vocabtrainer.controller;

import com.alexh.vocabtrainer.dtos.DictionaryDto;
import com.alexh.vocabtrainer.entities.*;
import com.alexh.vocabtrainer.repositories.CardRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    public CardRepository cardRepository;

    List<ImageLink> parseImageLinksFromPexelsAPI(String responseBody) {
        List<ImageLink> imageLinks = new ArrayList<>();

        JSONObject bodyJson = new JSONObject(responseBody);
        JSONArray photos = bodyJson.getJSONArray("photos");

        for (int i = 0; i < photos.length(); i++) {
            JSONObject photo = photos.getJSONObject(i);
            JSONObject source = photo.getJSONObject("src");
            String imageLinkOpt = source.optString("tiny");

            if (!imageLinkOpt.isEmpty()) {
                imageLinks.add(ImageLink.builder()
                        .url(imageLinkOpt)
                        .build());
            }

        }

        return imageLinks;
    }

    DictionaryDto parseMeaningsFromDictionaryAPI(String responseBody) {
        String transcription = null;
        AudioLink wordAudioLink = null;
        List<Meaning> meanings = new ArrayList<>();

        JSONObject bodyJson = new JSONArray(responseBody).getJSONObject(0);

        JSONArray phoneticsJson = bodyJson.getJSONArray("phonetics");

        JSONObject phonetic = phoneticsJson.getJSONObject(0);
        String transcriptionOpt = phonetic.optString("text");
        String wordAudioLinkOpt = phonetic.optString("audio");

        if (!transcriptionOpt.isEmpty()) {
            transcription = transcriptionOpt;
        }
        if (!wordAudioLinkOpt.isEmpty()) {
            wordAudioLink = new AudioLink().builder()
                    .url(wordAudioLinkOpt)
                    .build();
        }


        JSONArray meaningsJson = bodyJson.getJSONArray("meanings");
        for (int i = 0; i < meaningsJson.length(); i++) {
            JSONObject meaning = meaningsJson.getJSONObject(i);

            String partOfSpeech = meaning.optString("partOfSpeech");
            List<Antonym> antonyms = new ArrayList<>();
            List<Synonym> synonyms = new ArrayList<>();
            List<Definition> definitions = new ArrayList<>();
            List<Example> examples = new ArrayList<>();

            JSONArray antonymsJson = meaning.getJSONArray("antonyms");
            for (int a = 0; a < antonymsJson.length(); a++) {
                String antonymOpt = antonymsJson.optString(a);
                if (!antonymOpt.isEmpty()) {
                    antonyms.add(new Antonym().builder()
                            .word(antonymOpt)
                            .build());
                }
            }

            JSONArray synonymsJson = meaning.getJSONArray("synonyms");
            for (int s = 0; s < synonymsJson.length(); s++) {
                String synonymOpt = synonymsJson.optString(s);
                if (!synonymOpt.isEmpty()) {
                    synonyms.add(new Synonym().builder()
                            .word(synonymOpt)
                            .build());
                }
            }

            JSONArray definitionsJson = meaning.getJSONArray("definitions");
            for (int d = 0; d < definitionsJson.length(); d++) {
                JSONObject definition = definitionsJson.getJSONObject(d);

                String definitionOpt = definition.optString("definition");
                if (!definitionOpt.isEmpty()) {
                    definitions.add(new Definition().builder()
                            .text(definitionOpt)
                            .build());
                }

                String exampleOpt = definition.optString("example");
                if (!exampleOpt.isEmpty()) {
                    examples.add(new Example().builder()
                            .text(exampleOpt)
                            .build());
                }
            }

            meanings.add(Meaning.builder()
                    .partOfSpeech(partOfSpeech)
                    .definitions(definitions)
                    .examples(examples)
                    .antonyms(antonyms)
                    .synonyms(synonyms)
                    .build());
        }

        return DictionaryDto.builder()
                .transcription(transcription)
                .audioLink(wordAudioLink)
                .meanings(meanings)
                .build();
    }

    @SneakyThrows
    String callAPI(Request request) {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @SneakyThrows
    List<ImageLink> searchImageLinks(String word) {
        Request requestPexelAPI = new Request.Builder()
                .url("https://api.pexels.com/v1/search?query=" + word + "&per_page=10")
                .get()
                .addHeader("Authorization", System.getenv("PEXEL_API_KEY"))
                .build();

        String responseBody = callAPI(requestPexelAPI);
        return parseImageLinksFromPexelsAPI(responseBody);
    }

    DictionaryDto getDictionaryDto(String word) {
        Request requestDictionaryAPI = new Request.Builder()
                .url("https://api.dictionaryapi.dev/api/v2/entries/en/" + word)
                .get()
                .build();

        String responseBody = callAPI(requestDictionaryAPI);
        return parseMeaningsFromDictionaryAPI(responseBody);
    }

    Card createCard(String word) {
        DictionaryDto dictionaryDto = getDictionaryDto(word);
        List<AudioLink> audioLinks = new ArrayList<>();
        audioLinks.add(dictionaryDto.audioLink);

        return Card.builder()
                .word(word)
                .transcription(dictionaryDto.transcription)
                .audioLinks(audioLinks)
                .meanings(dictionaryDto.meanings)
                .imageLinks(searchImageLinks(word))
                .build();
    }

    ArrayList<Card> createCards() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(createCard("Hello"));
        cards.add(createCard("Goodbye"));
        cards.add(createCard("Like"));
        cards.add(createCard("Angel"));
        cards.add(createCard("Devil"));
        cards.add(createCard("Maria"));
        cards.add(createCard("Mother"));
        cards.add(createCard("Sex"));
        cards.add(createCard("Love"));
        cards.add(createCard("Cat"));
        cards.add(createCard("Dog"));
        cards.add(createCard("Window"));

        return cards;
    }

    @GetMapping("/")
    public String main(Model model) {
        ArrayList<Card> cards = createCards();

        model.addAttribute("cards", cards);

        return "main"; //view
    }
}