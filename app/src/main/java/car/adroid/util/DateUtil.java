package car.adroid.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import car.adroid.config.AppConfig;

/**
 * Created by jjh on 2016-12-08.
 */

public class DateUtil {
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat(AppConfig.DATE_FORMAT);

    public static Date getDate(String strDt) throws  Exception {
        Date date = mDateFormat.parse(strDt);
        return date;
    }

    public static long getTimeGapMiliseconds(String dt1 , String dt2) throws  Exception{
        return getDate(dt1).getTime() - getDate(dt2).getTime();
    }

    public static String getCurrent(){
        return mDateFormat.format(new Date());
    }
}
