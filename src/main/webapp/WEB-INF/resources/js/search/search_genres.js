$(document).on("keyup", "#searchInput", function() {
    var queryGenre = document.getElementById('searchInput').value;

    queryGenre = queryGenre.trim();
    if(queryGenre !== "") {
        var url = "/searchGenres?query=" + queryGenre;

        $.get(url, function (data) {
            // $("#dataDiv").html($(responseXml).find("#data").html());
            $("#dataDiv").html(data);
            changeDocHeight();
        });
    }
});

