
$("#allForms").ready(changeDocHeight);

var CONSTANT_HEIGHT = 20;

var reviews;

var typeMarkId;

function changeDocHeight() {

    $("#general_inf").height($("#div_short_inf").outerHeight(true));

    var height = $('#menuDiv').outerHeight(true) + $("#allForms").outerHeight(true);
    document.documentElement.style.height = height;

    var markBtn = document.getElementById("setMarkBtn");
    if(markBtn !== null) {
        markBtn.onclick = setMovieMark;
    }

    var reviewBtn = document.getElementById("setReviewBtn");
    if(reviewBtn !== null) {
        reviewBtn.onclick = addReview;
    }

    reviews = document.getElementById("reviews");
    if(reviews !== null){
    // reviews.onclick  = setReviewMark;

    typeMarkId = $("#typeMark").val();
    }
}

function setMovieMark() {
    var mark = $("#movieSelect").val();

    var markId = $("#movieId").val();

    var userId = $("#userId").val();

    var token = $("meta[name='_csrf']").attr("content");

    var url = "/setReviewedMark";

    $.ajax({
        type: 'POST',
        url: url,
        headers: {'X-CSRF-TOKEN': token},
        data: { "mark" : mark,
            "markId" : markId,
            "userId" : userId,
            "typeMark" : typeMarkId
        },
        success: function(data) {
            changeMark(data);
        }

    });
}

function changeMark(data) {
    $("#div_mark_wr").remove();

    var mainFrame = $(".div_mark");

    mainFrame.html(data);

    flag = mainFrame.find("#flag").val();

    if(flag === 'true') {
        commonMark = mainFrame.find("#commonMark").val();

        $("#mark").html("Общая оценка : " + commonMark);
    }else {

    }
}

function addReview() {

    var title = $("#my_title").val();

    var content = $("#my_content").val();

    var authorId = $("#authorId").val();

    var objId = $("#obj_id").val();

    var reviewType = $("#review_type").val();

    var token = $("meta[name='_csrf']").attr("content");

    // alert(title + ' ' + objId + ' ' + reviewType);

    var url = "/addReview";

    $.ajax({
        type: 'POST',
        url: url,
        headers: {'X-CSRF-TOKEN': token},
        data: {
            "title" : title,
            "content" : content,
            "objId" : objId,
            "type" : reviewType
        },
        success: function(data) {
            changeReview(data);
        }

    });
}

function changeReview(data) {

    var myTitle = $("#my_title");
    var myContent = $("#my_content");

    myTitle.val("");
    myContent.val("");

    $(reviews).html(data);

}

function setReviewMark(event) {
    var target = event.target;

    while (target !== reviews) {
        if(event.target.tagName !== 'SELECT') {
        }
        if (target.tagName === 'INPUT') {
            var parent = target.parentNode;
            var reviewId = $(parent).find("#reviewId").val();
            var writerId = $(parent).find("#writerId").val();
            var reviewMark = $("#reviews").find("#" + reviewId).val();
            var url = "/setReviewMark";

            $.ajax({
                type: 'POST',
                url: url,
                data: { "mark" : reviewMark, "reviewId" : reviewId,  "writerId" : writerId}
            });
            return;
        }
        target = target.parentNode;
    }
}






