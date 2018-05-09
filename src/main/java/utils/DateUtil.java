package utils;


import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public Date parseYearDateFromStr(String year) throws IllegalArgumentException{
        Date yearDate = new Date(year);

        System.out.println("yearDate = " + yearDate.getTime());

        return yearDate;
    }

    public String parseYearDateToStr(Date year){

        long time = year.getTime();

        return dateFormat.format(new Date(time));
    }

    public Time parseTimeFromStr(String timeStr){

        java.util.Date time = new java.util.Date();

        try {
            time =  timeFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Time(time.getTime());

    }

    public String parseTimeToStr(Time time){

        return time.toString();
    }
}
