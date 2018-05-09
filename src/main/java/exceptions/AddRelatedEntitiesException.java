package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AddRelatedEntitiesException extends RuntimeException{

    private String type;

    public AddRelatedEntitiesException(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
