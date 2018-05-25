var pastQuery = "";
var numPage;

var back;
var forward;

$(document).on("keyup", "#searchInput", function() {
    numPage = $("#numPage");
    ajaxSearch();

});

function ajaxSearch(){
    var queryMovie = document.getElementById('searchInput').value;
    var searchType = document.getElementById('searchType').value;
    var url = "/searchBooks?query=" + queryMovie + "&"
        + "type=" + searchType + "&"
        + "numPage=" + numPage.val();

    $.get(url, function(data) {
        if(pastQuery !== queryMovie){
            numPage.val(0);
        }
        pastQuery = queryMovie;
        // $("#dataDiv").html($(responseXml).find("#data").html());
        $("#dataDiv").html(data);
        changeDocHeight();
    });
}

$("#allForms").ready(initializeList);

var CONSTANT_HEIGHT = 20;

function initializeList() {
    back = $("#back");
    forward = $("#forward");

    back.click(backAct);
    forward.click(forwardAct);

    changeDocHeight();
}

function changeDocHeight() {
    var allForms = $("#allForms");
    var MIN_HEIGHT = 715;
    var partHeight = Math.max($('#dataDiv').outerHeight(true) + $("#searchForm").outerHeight(true),MIN_HEIGHT);
    var height = partHeight + $("#menuDiv").outerHeight(true);

    document.documentElement.style.height = height + CONSTANT_HEIGHT;

    allForms.height(partHeight);

    back.css("padding-top",partHeight/2);
    forward.css("padding-top",partHeight/2);

}

function backAct() {
    numPage = $("#numPage");
    var check;
    if(numPage.val() > 0) {
        changeBackground(forward,true);
        changeBackground(back,true);
        numPage.val(+numPage.val() - 1);
        check = true;
    }else{
        check = false;
        changeBackground(back,false);
    }
    if(check){
        ajaxSearch();
    }
}

function forwardAct() {
    numPage = $("#numPage");
    var check;
    if($("#isLast").val() !== "last") {
        changeBackground(forward,true);
        changeBackground(back,true);
        numPage.val(+numPage.val() + 1);
        check = true;
    }else{
        check = false;
        changeBackground(forward,false);
    }
    if(check){
        ajaxSearch();
    }
}

function changeBackground(divJquery,state) {
    if(!state) {
        divJquery.hover(function () {
            $(this).css("background", "#d64949")
        }, function () {
            $(this).css("background-color", "white")
        });
    }else{
        divJquery.hover(function() {
            $(this).css("background","#fbfbfb")
        },function(){
            $(this).css("background-color", "white")});
    }
}


