package com.alexh.vocabtrainer.controller;

import com.alexh.vocabtrainer.dtos.DictionaryDto;
import com.alexh.vocabtrainer.entities.*;
import com.alexh.vocabtrainer.forms.PartOfSpeechRegistrationForm;
import com.alexh.vocabtrainer.repositories.CardRepository;
import com.alexh.vocabtrainer.repositories.MeaningRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    public MeaningRepository meaningRepository;

    private static final String MAIN_PAGE = "main";
    private static final String EMPTY_CARD_PAGE = "empty_card";
    private static final String EMPTY_EXAMPLE_PAGE = "empty_example";
    private static final String CARDS_ATTR = "cards";

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
            String definition = null;
            List<Antonym> antonyms = new ArrayList<>();
            List<Synonym> synonyms = new ArrayList<>();
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
                JSONObject definitionJSON = definitionsJson.getJSONObject(d);

                if(d == 0) {
                    String definitionOpt = definitionJSON.optString("definition");
                    if (!definitionOpt.isEmpty()){
                        definition = definitionOpt;
                    }
                }

                String exampleOpt = definitionJSON.optString("example");
                if (!exampleOpt.isEmpty()) {
                    examples.add(new Example().builder()
                            .text(exampleOpt)
                            .build());
                }
            }

            meanings.add(Meaning.builder()
                    .partOfSpeech(partOfSpeech)
                    .definition(definition)
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

    Card createOfferCard(String word) {
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

    @GetMapping("/")
    public String main(Model model) {
        List<Card> cards = cardRepository.findAll();
        model.addAttribute(CARDS_ATTR, cards);
        return MAIN_PAGE; //view
    }

    @GetMapping("/empty_card")
    public String emptyCard(Model model) {
        String[] partsOfSpeech = new String[]{
                "noun",
                "pronoun",
                "verb",
                "adjective",
                "adverb",
                "preposition",
                "conjunction",
                "interjection"
        };

        model.addAttribute("partsOfSpeech", partsOfSpeech);

        return EMPTY_CARD_PAGE;
    }

    @GetMapping("/example")
    public String emptyExample(@RequestParam("example") String example, Model model) {
        model.addAttribute("example", example);
        return EMPTY_EXAMPLE_PAGE;
    }

    @GetMapping("/empty_example")
    public String emptyExample(Model model) {
        model.addAttribute("example", null);
        return EMPTY_EXAMPLE_PAGE;
    }

    @GetMapping("/meaning")
    @ResponseBody
    public Meaning findExamples(@RequestParam("word") String word,
                                @RequestParam("partOfSpeech") String partOfSpeech) {
        return meaningRepository.findByCardWordAndPartOfSpeech(word, partOfSpeech).get();
    }

    @GetMapping("/delete_card")
    @Transactional
    public String deleteCard(@RequestParam("word") String word) {
        cardRepository.deleteByWord(word);
        return "forward:/";
    }

    @PostMapping(value = "/add_card", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addCard(@RequestBody  Map<String, PartOfSpeechRegistrationForm> partOfSpeechMap) {
        String nounWord = partOfSpeechMap.get("noun").word;
        String nounTranscription = partOfSpeechMap.get("noun").transcription;

        Card offerCard = createOfferCard(nounWord);

        List<Meaning> meanings = new ArrayList<>();

        partOfSpeechMap.keySet().forEach(k -> {
            String definition = partOfSpeechMap.get(k).definition;
            List<String> examples = partOfSpeechMap.get(k).examples;

            List<Example> exampleEntities = new ArrayList<>();

            examples.forEach(e -> exampleEntities.add(Example.builder()
                    .text(e)
                    .build())
            );

            meanings.add(Meaning.builder()
                    .partOfSpeech(k)
                    .definition(definition)
                    .examples(exampleEntities)
                    .build()
            );
        });

        Card card = Card.builder()
                .word(nounWord)
                .transcription(nounTranscription)
                .audioLinks(offerCard.audioLinks)
                .meanings(meanings)
                .imageLinks(offerCard.imageLinks)
                .build();

        cardRepository.save(card);

        return "forward:/";
    }
}
