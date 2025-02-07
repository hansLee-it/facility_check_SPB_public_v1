package hans.firefighting.check.facility_check.util;

import hans.firefighting.check.facility_check.notice.NoticeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil{
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    /**
     * <pre>
     * 1. 메소드명 : stringToTime
     * 2. 작성일   : 2024. 04. 09.
     * 3. 작성자   : itHans
     * 4. 설명     : Convert String format date to LocalDateTime format
     * Default("yyyy-MM-dd")
     * Can be change with format parameter
     * * </pre>
     * @return
     */
    public static LocalDateTime stringToTime(String string_date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDate.parse(string_date,formatter).atStartOfDay();
        return date;
    }
    public static LocalDateTime stringToTime(String string_date,String format) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime date = LocalDate.parse(string_date,formatter).atStartOfDay();
        return date;
    }

    /**
     * <pre>
     * 1. 메소드명 : stringToDate
     * 2. 작성일   : 2024. 04. 15.
     * 3. 작성자   : itHans
     * 4. 설명     : Convert String format date to LocalDate format
     * Default("yyyy-MM-dd")
     * Can be change with format parameter
     * * </pre>
     * @return
     */
    public static LocalDate stringToDate(String string_date) throws ParseException {
        return LocalDate.parse(string_date.trim());
    }
    public static LocalDate stringToDate(String string_date,String format) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(string_date.trim(),formatter);

    }
    /**
     * <pre>
     * 1. 메소드명 : stringRangetoDate
     * 2. 작성일   : 2024. 04. 09.
     * 3. 작성자   : itHans
     * 4. 설명     : Convert String format date range to LocalDateTime format
     * Default("yyyy-MM-dd")
     * Can be change with format parameter
     * * </pre>
     * @return
     */
    public static LocalDate[] stringRangetoDate(String[] string_dates) throws ParseException {
        LocalDate from_date = LocalDate.parse(string_dates[0].trim());
        LocalDate to_date = LocalDate.parse(string_dates[1].trim());

        return new LocalDate[]{from_date, to_date};
    }
    public static LocalDate[] stringRangetoDate(String[] string_dates,String format) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate from_date = LocalDate.parse(string_dates[0].trim(),formatter);
        LocalDate to_date = LocalDate.parse(string_dates[1].trim(),formatter);


        return new LocalDate[]{from_date, to_date};
    }

}