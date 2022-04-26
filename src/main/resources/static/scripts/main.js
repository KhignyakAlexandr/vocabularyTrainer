function changeMode(img) {

}

function deleteExample(img){
    $(img).closest(".card-list-examples-item").remove();
}

function addExample(img) {
    var listExamples = $(img).closest(".card-list-examples");
    var emptyExample = getEmptyExample();
    var divRow = $(emptyExample).find(".card-list-examples");
    listExamples.append(divRow);
}

function addCard() {
    var row = $(".content-row");
    var emptyCardPage = getEmptyCardPage();
    var divRow = $(emptyCardPage).find(".content-row");
    row.prepend(divRow);
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
    definition.text(meaning.definitions[0].text);
}

function changeExamples(meaning, listExamples) {
    listExamples.children(".card-list-examples-item").remove();

    meaning.examples.forEach(example => {
        var divStr = "<li class='card-list-examples-item'>" + example.text + "</li>";
        var div = $(divStr);
        listExamples.append(div[0]);
    });
}

function getMeaningByCardWordAndPartOfSpeech(word, partOfSpeech) {
    var meaning;

    $.ajax({
        type: "get",
        url: "/meaning",
        async: false,
        data: {
            word: word,
            partOfSpeech: partOfSpeech
        }
    }).done(function (response) {
        meaning = response;
    });

    return meaning;
}

function getEmptyCardPage() {
    var emptyCardPage;

    $.ajax({
        type: "get",
        url: "/empty_card",
        async: false
    }).done(function (response) {
        emptyCardPage = response;
    });

    return emptyCardPage;
}

function getEmptyExample() {
    var emptyExample;

    $.ajax({
        type: "get",
        url: "/empty_example",
        async: false
    }).done(function (response) {
        emptyExample = response;
    });

    return emptyExample;
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