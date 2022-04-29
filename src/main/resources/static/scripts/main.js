function changeMode(img) {

}

function addExampleFromImage(img) {
    var listExamples = $(img).closest(".card-list-examples");
    var emptyExample = getEmptyExample();
    var divRow = $(emptyExample).find(".card-list-examples");
    listExamples.append(divRow);
}

function addExample(listExamples, value) {
    var emptyExample = getEmptyExample(value);
    var divRow = $(emptyExample).find(".card-list-examples");
    listExamples.append(divRow);
}

function deleteExample(img) {
    $(img).closest(".card-list-examples-item").remove();
}

function addCard() {
    var row = $(".content-row");
    var emptyCardPage = getEmptyCardPage();
    var divRow = $(emptyCardPage).find(".content-row");
    row.prepend(divRow);
}

function deleteCard(img) {
    var closeCol = $(img).closest(".col");
    var word = closeCol.find(".card-word").text().trim();
    closeCol.remove();
    deleteCardByWord(word);
}

function changePartOfSpeech(btn) {

    var input = $(btn).find(".part-of-speech-radio");
    var partOfSpeech = $(btn).text().trim();
    var closeCard = input.closest(".card");
    var definition = closeCard.find(".card-definition");
    var listExamples = closeCard.find(".card-list-examples");
    var word = closeCard.find(".card-word").text().trim();

    var meaning = getMeaningByCardWordAndPartOfSpeech(word, partOfSpeech);

    input.prop("checked", true);

    setActiveButtonsClass();
    changeDefinition(meaning, definition);
    changeExamples(meaning, listExamples);
}

function changeDefinition(meaning, definition) {
    definition.text(meaning.definition);
}

function changeExamples(meaning, listExamples) {
    listExamples.children(".card-list-examples-item").remove();

    meaning.examples.forEach(example => {
        var divStr = "<li class='card-list-examples-item'>" + example.text + "</li>";
        var div = $(divStr);
        listExamples.append(div[0]);
    });
}

function setActiveButtonsClass() {
    var inputs = $(".part-of-speech-btn").children();

    inputs.each(function () {

        btn = $(this).parent();

        if ($(this).prop('checked')) {
            $(btn).addClass("active");
        } else {
            $(btn).removeClass("active");
        }
    });
}