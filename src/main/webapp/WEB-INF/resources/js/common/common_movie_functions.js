
var listId = '';
var listNames = '';

var idList;
var namesList;

$("#allForms").ready(initializeList);

function initializeList() {
    idList = $('#idList');
    namesList = $('#namesList');
    changeDocHeight();
}

$(document).on("click", "#add", function() {
    listId = idList.val();
    listNames = namesList.val();
    $('.checkbox_add').each(function(){
        if($(this).is(':checked')) {
            var newName = $(this).prop('name');
            var newId = $(this).prop('value');
            var check = listId.search(newId);
            if (check === -1) {
                listNames += ',' + newName;
                listId += ',' + newId;
            }
        }

    });
    if(listNames !== ''){
        if(listNames.substring(0,1) === ","){
            listNames = listNames.substring(1,listNames.length);
            listId = listId.substring(1,listId.length);
        }
        $('#addList').text(listNames);
        idList.val(listId);
        namesList.val(listNames);
    }
});

$(document).on("click", "#delete", function() {
    listId = idList.val();
    listNames = namesList.val();
    $('.checkbox_del').each(function(){
        if($(this).is(':checked')){
            var delName = $(this).prop('name');
            var delId = $(this).prop('value');
            var check = listId.search(delId);
            if (check !== -1) {
                if(check === 0){
                    listNames = listNames.replace(delName,"");
                    listId = listId.replace(delId,"");
                }else {
                    listNames = listNames.replace(',' + delName, "");
                    listId = listId.replace(',' + delId, "");
                }
            }



        }

    });
    $('#addList').text(listNames);
    idList.val(listId);
    namesList.val(listNames);

});

var CONSTANT_HEIGHT = 20;

function changeDocHeight() {
    var height = $('#dataDiv').outerHeight(true) + $("#searchForm").outerHeight(true) + $("#menuDiv").outerHeight(true);
    // alert(height);
    document.documentElement.style.height = height + CONSTANT_HEIGHT;
}
