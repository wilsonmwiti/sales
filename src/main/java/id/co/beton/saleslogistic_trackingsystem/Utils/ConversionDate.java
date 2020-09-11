package id.co.beton.saleslogistic_trackingsystem.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class ConversionDate
 * to get data Date
 */
public class ConversionDate {

    private static ConversionDate instance = null;

    /**
     * Instantiates a new Conversion date.
     */
    public ConversionDate() {
    }

    /**
     * Get instance conversion date.
     *
     * @return the conversion date
     */
    public static ConversionDate getInstance(){
        if(instance==null){
            instance = new ConversionDate();
        }

        return instance;
    }

    /**
     * Full format date string.
     *
     * @param stringDate the string date
     * @return the string
     */
    public String fullFormatDate(String stringDate){
        String text="";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(stringDate));
            // Then get the day of week from the Date based on specific locale.
            text= new SimpleDateFormat("EE, yyyy-MM-dd", Locale.ENGLISH).format(date);
        }catch (ParseException e){

        }
        return text;
    }

    /**
     * Today string.
     *
     * @return the string
     */
    public String today(){
        String text="";
        Date date =new Date();
        text= new SimpleDateFormat("EE, yyyy-MM-dd", Locale.ENGLISH).format(date);
        return text;
    }

    /**
     * Time now string.
     *
     * @return the string
     */
    public String timeNow(){
        try{
            String text="";
            Date date =new Date();
            // Then get the day of week from the Date based on specific locale.
            text= new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(date);
            return text;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
