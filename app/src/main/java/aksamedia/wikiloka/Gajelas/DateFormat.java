package aksamedia.wikiloka.Gajelas;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormat {

    public static final long ONE_DAY_TIMEMILLIS = 1000 * 60 * 60 * 24;
    public static final long ONE_DAY = 1000;
    public static final long NIGHT = 60 * 60 * 24;

    //DIGUNAKAN UNTUK PROSES SISTEM
    public static final String SYSTEM_DATE_STANDART = "yyyy-MM-dd";
    public static final String SYSTEM_DATE_STANDART2 = "MM/dd/yyyy";
    public static final String SYSTEM_DATETIME_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String SYSTEM_DATE_DAY = "EEEE, dd MMMM yyyy";
    public static final String SYSTEM_DATE_DAY_TIME = "EEEE, dd MMMM yyyy HH:mm";
    public static final String SYSTEM_DATE_DAY_SHORT = "EEE, dd MMM yyyy";
    public static final String SYSTEM_TIME = "HH:mm";
    public static final String SYSTEM_DAY = "EEEE";

    //DIGUNAKAN UNTUK PROSES VIEW
    public static final String VIEW_DATE_STANDARD = "dd MMMM yyyy";
    public static final String VIEW_DATETIME_STANDARD = "dd/MM/yyyy HH:mm:ss";

    public static final SimpleDateFormat dateStandart2Formatter = new SimpleDateFormat(SYSTEM_DATE_STANDART2, Locale.US);
    public static final SimpleDateFormat dateDayFormatter = new SimpleDateFormat(SYSTEM_DATE_DAY, Locale.US);
    public static final SimpleDateFormat dateDayTimeFormatter = new SimpleDateFormat(SYSTEM_DATE_DAY_TIME, Locale.US);
    public static final SimpleDateFormat dayFormatter = new SimpleDateFormat(SYSTEM_DAY, Locale.US);
    public static final SimpleDateFormat dateStandardFormatter = new SimpleDateFormat(VIEW_DATE_STANDARD, Locale.US);
}

