var partOfSpeechMap = new Map();

function setPartOfSpeech(cardInputsCollection) {
    var partOfSpeech = cardInputsCollection.partOfSpeech.val();
    var word = cardInputsCollection.word.val();
    var transcription = cardInputsCollection.transcription.val();
    var definition = cardInputsCollection.definition.val();
    var inputListExamples = cardInputsCollection.inputListExamples;

    let examples = [];

    inputListExamples.each(function () {
        examples.push($(this).val());
    });

    partOfSpeechMap.set(partOfSpeech, {word, transcription, definition, examples})
}

function clearCard(cardInputsCollection) {
    cardInputsCollection.word.val("");
    cardInputsCollection.transcription.val("");
    cardInputsCollection.definition.val("");

    var firstExample = $(cardInputsCollection.inputListExamples[0]);

    firstExample.val("");
    firstExample.closest("li").siblings().remove()
}

function getPartOfSpeech(cardInputsCollection, clickedPartOfSpeechBtm){
    var clickedPartOfSpeechBtmText = $(clickedPartOfSpeechBtm).text().trim();

    if (partOfSpeechMap.has(clickedPartOfSpeechBtmText)) {
        formPartOfSpeech = partOfSpeechMap.get(clickedPartOfSpeechBtmText);

        cardInputsCollection.word.val(formPartOfSpeech.word);
        cardInputsCollection.transcription.val(formPartOfSpeech.transcription);
        cardInputsCollection.definition.val(formPartOfSpeech.definition);
        var listExamples = cardInputsCollection.inputListExamples.closest(".card-list-examples");

        formPartOfSpeech.examples.forEach(function (value, index) {
            if (index == 0) {
                $(cardInputsCollection.inputListExamples[0]).val(value);
            } else {
                addExample(listExamples, value);
            }
        })
    }
}

function getCardInputCollection(cardElem){
    var closeCard = $(cardElem).closest(".card");

    var partOfSpeech = closeCard.find(".active").find(".input-card-part-of-speech");
    var word = closeCard.find(".input-card-word");
    var transcription = closeCard.find(".input-card-transcription");
    var definition = closeCard.find(".input-card-definition");
    var inputListExamples = closeCard.find(".input-card-list-examples-item");

    return {partOfSpeech, word, transcription, definition, inputListExamples};
}

function changeInputPartOfSpeech(btn) {

    var cardInputsCollection = getCardInputCollection(btn);

    setPartOfSpeech(cardInputsCollection);
    clearCard(cardInputsCollection);
    getPartOfSpeech(cardInputsCollection, btn);

    var input = $(btn).find(".input-card-part-of-speech");
    input.prop("checked", true);
    setActiveButtonsClass();
}