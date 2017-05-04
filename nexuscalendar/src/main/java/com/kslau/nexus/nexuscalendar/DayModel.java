package com.kslau.nexus.nexuscalendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shen-mini-itx on 5/2/2017.
 */

public class DayModel {

    private int day;
    private int month;
    private int year;
    private float spanSize;
    private float spanSizeRatio;// 0 - 10
    private boolean emptyView;

    public DayModel(boolean emptyView) {
        this.setEmptyView(emptyView);
    }

    public DayModel(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        this.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        this.setMonth(calendar.get(Calendar.MONTH));
        this.setYear(calendar.get(Calendar.YEAR));
        this.setSpanSize(0);
        this.setSpanSizeRatio(0);
        this.setEmptyView(false);
    }

    public DayModel(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.setSpanSize(0);
        this.setSpanSizeRatio(0);
        this.setEmptyView(false);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DayModel that = (DayModel) obj;
        return day == that.day && month == that.month && year == that.year;
    }

    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        this.setMonth(calendar.get(Calendar.MONTH));
        this.setYear(calendar.get(Calendar.YEAR));
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(float spanSize) {
        this.spanSize = spanSize;
    }

    public float getSpanSizeRatio() {
        return spanSizeRatio;
    }

    public void setSpanSizeRatio(float spanSizeRatio) {
        this.spanSizeRatio = spanSizeRatio;
    }

    public boolean isEmptyView() {
        return emptyView;
    }

    public void setEmptyView(boolean emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public String toString() {
        return "DayModel{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
