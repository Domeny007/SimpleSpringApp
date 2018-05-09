package database.entity.review;

import database.entity.Identified;
import database.entity.User;
import java.io.Serializable;

public interface Review extends Identified,Serializable {

    public String getTitle();

    public void setTitle(String title);

    public String getContent();

    public void setContent(String content);

    public User getWriter();

    public void setWriter(User writer);
}
