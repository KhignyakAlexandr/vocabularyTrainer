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

function getEmptyExample(value) {
    var example;

    if(value == null){
        $.ajax({
            type: "get",
            url: "/empty_example",
            async: false
        }).done(function (response) {
            example = response;
        });
    } else {
        $.ajax({
            data: {
                example: value
            },
            type: "get",
            url: "/example",
            async: false
        }).done(function (response) {
            example = response;
        });
    }

    return example;
}

function deleteCardByWord(word){
    $.ajax({
        data: {
            word: word
        },
        type: "get",
        url: "/delete_card",
        async: false
    }).done(function (response) {
        console.log(response);
    });
}

function saveCard(form){
    var btn = $(form).find(".active");
    var cardInputsCollection = getCardInputCollection(btn);
    setPartOfSpeech(cardInputsCollection);

    var json = JSON.stringify(Object.fromEntries(partOfSpeechMap.entries()));

    $.ajax({
        data : json,
        type : "post",
        url :  "/add_card",
        contentType: "application/json",
        dataType: 'json',
        async: false
    }).done(function (response) {

    });

    form.submit();
}
