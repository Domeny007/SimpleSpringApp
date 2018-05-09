package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SetMarkException extends RuntimeException{

    protected String type;

    public SetMarkException() {
        super();
    }

    public SetMarkException(String entity) {
        super();
        this.type = entity;
    }

    public String getType(){
        return type;
    }
}
