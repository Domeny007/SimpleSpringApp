package utils.converters;

import utils.Const;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringDateConverter extends AbstractTwoWayConverter<String, Date> {

    private final static SimpleDateFormat dateFormatOne = new SimpleDateFormat("dd.MM.yyyy");

    private final static SimpleDateFormat dateFormatTwo = new SimpleDateFormat("yyyy-MM-dd");

    public Date parseYearDateFromStr(String year) throws IllegalArgumentException, ParseException {

        return dateFormatTwo.parse(year);
    }

    public String parseYearDateToStr(Date year){

        String result = "null";

        if(year != null) {
            result =  dateFormatOne.format(year);
        }
        return result;
    }

    @Override
    protected Date convert(String dateStr) {

        Date date = null;
        try {
            date =  parseYearDateFromStr(dateStr);
        } catch (ParseException e) {
            Const.sys(e.getMessage());
        }

        return date;
    }

    @Override
    protected String convertBack(Date target) {
        return parseYearDateToStr(target);
    }


}
