var state = false;

function changeSession() {
    if(state) {

        var url = "/saveBookData";

        var name = $("input[name ='name']").val();
        var description = $("textarea[name ='description']").val();
        var genresId = $("input[name ='genresId']").val();
        var countriesId = $("input[name ='countriesId']").val();
        var authorsId = $("input[name ='authorsId']").val();

        $.post(url,
                {"name": name,
                 "description": description,
                 "genresId" : genresId,
                 "countriesId": countriesId,
                 "authorsId": authorsId
                 });
    }
}

// window.onbeforeunload  = changeSession;

$(document).ready(function() {
    $(".button").each(clickFunc);
});


function clickFunc() {
    $(this).click(function () {

        state = true;
        changeSession();

    })

}

