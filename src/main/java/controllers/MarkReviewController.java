package controllers;

import database.entity.User;
import database.entity.review.AuthorReview;
import database.entity.review.BookReview;
import database.entity.review.Review;
import exceptions.AddReviewException;
import exceptions.SetMarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import services.AddReviewService;
import services.SetVoteService;
import utils.Const;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static utils.Const.REVIEWS_ATTR;

@Controller
@PreAuthorize("isAuthenticated()")
public class MarkReviewController {

    private static final String SET_MARK = "/setReviewedMark";
    private static final String ADD_REVIEW = "/addReview";

    private static final String NAME = "MRC#";

    @Autowired
    private SetVoteService voteService;

    @Autowired
    private AddReviewService addReviewService;

    @RequestMapping(value = SET_MARK,method = RequestMethod.POST)
    public String setMark(Model model,
                          @RequestParam String mark,
                          @RequestParam String markId,
                          @RequestParam String userId,
                          @RequestParam String typeMark){
        try {
            Integer commonMark = voteService.addMark(mark, markId, userId, typeMark);
            model.addAttribute("flag", true);
            model.addAttribute("commonMark", commonMark);
            model.addAttribute("userMark", mark);
            System.out.println("flag = " + true + " comMark = " + commonMark + " mark = " + mark);
        } catch (Exception e) {
            throw new SetMarkException(typeMark);
        }

        return "xml_parts/user/set_mark";
    }

    @RequestMapping(value = ADD_REVIEW,method = RequestMethod.POST)
    public String addReview(Model model,
                            @RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String objId,
                            @RequestParam String type,
                            @AuthenticationPrincipal User writer){

        Integer objectId = Integer.valueOf(objId);
        try {
            switch (type) {
                case Const.KEY_BOOK_TYPE:
                    addReviewService.addBookReview(title,content,objId,writer);
                    List<BookReview> bookReviews = addReviewService.getBookReviews(objectId);
                    model.addAttribute(REVIEWS_ATTR,bookReviews);
                    System.out.println("reviews = " + (bookReviews.size()));
                    break;

                case Const.KEY_AUTHOR_TYPE:
                    addReviewService.addAuthorReview(title,content,objId,writer);
                    List<AuthorReview> authorReviews = addReviewService.getAuthorReviews(objectId);
                    model.addAttribute(REVIEWS_ATTR,authorReviews);
                    System.out.println("reviews = " + (authorReviews.size()));
                    break;
            }
        } catch (Exception e) {
            throw new AddReviewException(type,objectId,e.getMessage());

        }

        return "xml_parts/user/add_review";
    }
}
