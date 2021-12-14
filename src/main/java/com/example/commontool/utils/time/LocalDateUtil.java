package com.example.commontool.utils.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

/**
 * @ClassName: LocalDateUtil
 * @Description: localDate 工具方法
 * @Author: th_legend
 **/
public class LocalDateUtil {

    public static LocalDate firstDayOfYear(LocalDate date){
        return date.with(TemporalAdjusters.firstDayOfYear());
    }
    public static LocalDate lastDayOfYear(LocalDate date){
        return date.with(TemporalAdjusters.lastDayOfYear());
    }
    public static LocalDate firstDayOfMonth(LocalDate date){
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }
    public static LocalDate lastDayOfMonth(LocalDate date){
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
    public static LocalDate getOneDayOfWeek(LocalDate date){
        return getDayOfWeek(date,1);
    }
    public static LocalDate getSevenDayOfWeek(LocalDate date){
        return getDayOfWeek(date,7);
    }





    /**
     * 获取一周中的某一天日期
     * @param today 这周内任意一天的日期
     * @param day 想要获取一周中的第几天
     * @return LocalDate
     */
    private static LocalDate getDayOfWeek(TemporalAccessor today, int day){
        TemporalField fieldIso = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        LocalDate localDate = LocalDate.from(today);
        return localDate.with(fieldIso, day);
    }
}
