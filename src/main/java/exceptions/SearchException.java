package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SearchException extends RuntimeException{

    private String typeFirst;

    public SearchException() {
        super();
    }

    public SearchException(String typeFirst) {
        super();
        this.typeFirst = typeFirst;
    }

    public String getTypeFirst() {
        return typeFirst;
    }

    public void setTypeFirst(String typeFirst) {
        this.typeFirst = typeFirst;
    }
}

