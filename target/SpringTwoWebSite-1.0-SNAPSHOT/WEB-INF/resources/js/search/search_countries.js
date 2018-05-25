$(document).on("keyup", "#searchInput", function() {
    var queryCountry = document.getElementById('searchInput').value;
    queryCountry = queryCountry.trim();
    if(queryCountry !== "") {
        var url = "/searchCountries?query=" + queryCountry;

        $.get(url, function (data) {
            $("#dataDiv").html(data);
            changeDocHeight();
        });
    }
});
