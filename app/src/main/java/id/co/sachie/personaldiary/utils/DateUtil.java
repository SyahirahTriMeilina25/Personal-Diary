package id.co.sachie.personaldiary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getCurrentTimestamp(){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            String currentDateTime = dateFormat.format(new Date());
            return currentDateTime;
        }catch (Exception e){
            return null;
        }
    }

    public static String getMonthFromNumber(String monthNumber){
        switch (monthNumber){
            case "01" :{return "Jan";}
            case "02" :{return "Feb";}
            case "03" :{return "Mar";}
            case "04" :{return "Apr";}
            case "05" :{return "Mei";}
            case "06" :{return "Jun";}
            case "07" :{return "Jul";}
            case "08" :{return "Agu";}
            case "09" :{return "Sep";}
            case "10" :{return "Okt";}
            case "11" :{return "Nov";}
            case "12" :{return "Des";}
            default: {return "error";}
        }
    }
}
