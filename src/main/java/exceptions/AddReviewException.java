package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AddReviewException extends RuntimeException{

    protected String type;

    private Integer objId;

    public AddReviewException() {
        super();
    }

    public AddReviewException(String message) {
        super(message);
    }

    public AddReviewException(String type, Integer objId,String message) {
        super(message);
        this.type = type;
        this.objId = objId;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getObjId() {
        return objId;
    }

    public void setObjId(Integer objId) {
        this.objId = objId;
    }
}

