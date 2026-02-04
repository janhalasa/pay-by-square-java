package io.github.janhalasa.paybysquare.model;

import java.time.LocalDate;

public class StandingOrder {

    private Integer day;
    private MonthClassifier month;
    private Periodicity periodicity;
    private LocalDate lastDate;

    public Integer getDay() {
        return day;
    }

    /**
     * This is the payment day. It‘s meaning depends on the periodicity, meaning
     * either day of the month (number between 1 and 31) or day of the week (1=Monday,
     * 2=Tuesday ... 7=Sunday).
     */
    public void setDay(Integer day) {
        this.day = day;
    }

    public MonthClassifier getMonth() {
        return month;
    }

    public void setMonth(MonthClassifier month) {
        this.month = month;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }
}
