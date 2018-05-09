$(document).on("input", "#menuInput", function() {
    var queryMovie = document.getElementById('menuInput').value;
    var searchType = document.getElementById('menuType').value;
    var autocomplete = $("#div_autocomplete");
    if(queryMovie !== ""){
    var url = "/searchBooks?query=" + queryMovie + "&"
        + "type=" + searchType + "&"
        + "numPage=" + 0;

        $.get(url, function(data) {
            // autocomplete.html($(responseXml).find("#data").html());
            autocomplete.html(data);
            // $("#dataDiv").html(data);
            autocomplete.css("display", "block");
        });
    }else{
        autocomplete.css("display","none");
    }

});

window.onload = initialize;

function initialize() {
    $(window).resize(changeSearchWitdh);
    changeSearchWitdh();

}

var CONSTANT_WITHD_DOP = 20;

function changeSearchWitdh() {
    var standartOtherWitdh = 452 + CONSTANT_WITHD_DOP;
    var menuSearch =  $("#menu_search");
    menuSearch.width($("#menuDiv").width() - standartOtherWitdh);
    $("#div_autocomplete").width(menuSearch.width());
}
