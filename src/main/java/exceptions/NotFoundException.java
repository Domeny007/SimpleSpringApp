package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    protected String type;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String entity) {
        super();
        this.type = entity;
    }

    public String getType(){
        return type;
    }
}
