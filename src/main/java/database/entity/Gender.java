package database.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;


public enum Gender {

    FEMALE,MALE;

    @Component
    public static class MessageSourceInjector {

        @Autowired
        private MessageSource messageSource;

        @PostConstruct
        public void postConstruct() {
            for (Gender gender : EnumSet.allOf(Gender.class))
                gender.setMessageSource(messageSource);
        }
    }

    private MessageSource messageSource;

    private String value;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getValue(){

        String genderString = "";

        switch (this){

            case MALE :
                genderString = messageSource.getMessage("gender.male",null, LocaleContextHolder.getLocale());
                break;

            case FEMALE:
                genderString = messageSource.getMessage("gender.female",null,LocaleContextHolder.getLocale());
                break;
        }

        return genderString;
    }
}
