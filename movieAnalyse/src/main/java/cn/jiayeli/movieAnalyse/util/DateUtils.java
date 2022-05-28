package cn.jiayeli.movieAnalyse.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);


    /**
     * format english date string e.g: 01-May-2021 to 2021-05-01
     * @param dateStr
     * @return yyyy-mm-dd pattern time
     * @throws ParseException
     */
    public static String dataFormatByEnglish(String dateStr){
        if (dateStr.isBlank()) {
            return "";
        }
        long time = 0;
        try {
            time = simpleDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateFormatUtils.format(time, "yyyy-MMM-dd");
    }
}
