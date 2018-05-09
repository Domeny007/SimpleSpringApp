package services;

import database.hibernate_repository.AuthorRepository;
import database.hibernate_repository.BookRepository;
import exceptions.SetMarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.Const;

@Service
public class SetVoteService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public Integer addMark(String markStr, String markIdStr, String userIdStr, String type) throws Exception {

        Integer commonMark = null;

        Integer mark = Integer.valueOf(markStr);

        Integer markId = Integer.valueOf(markIdStr);

        Integer userId = Integer.valueOf(userIdStr);

        switch (type){

            case Const.KEY_BOOK_TYPE:
                commonMark = addAndGetBookMark(mark,markId,userId);
                break;

            case Const.KEY_AUTHOR_TYPE:
                commonMark = addAndGetAuthorMark(mark,markId,userId);
                break;
        }

        if(commonMark == null){
            throw new SetMarkException();
        }

        return commonMark;
    }


    private Integer addAndGetAuthorMark(Integer mark, Integer markId, Integer userId) {

        authorRepository.addUserMark(mark,markId,userId);

        return authorRepository.getCurrentMark(markId);
    }

    private Integer addAndGetBookMark(Integer mark, Integer markId, Integer userId) {

        bookRepository.addUserMark(mark,markId,userId);

        return bookRepository.getCurrentMark(markId);
    }

}
