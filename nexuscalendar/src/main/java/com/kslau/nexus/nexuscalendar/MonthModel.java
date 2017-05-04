package com.kslau.nexus.nexuscalendar;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shen-mini-itx on 5/2/2017.
 */

public class MonthModel {

    private final String TAG = this.getClass().getSimpleName();

    private int month;
    private int year;
    private List<DayModel> dayModelList;
    private Date startOfMonth;
    private Date startOfNextMonth;

    public MonthModel(int month) {
        this.month = month;

        dayModelList = new ArrayList<>();

        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        startOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        startOfNextMonth = calendar.getTime();

        Date theDate = startOfMonth;
        calendar.setTime(theDate);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < firstDayOfWeek; i++) {
            dayModelList.add(new DayModel(true));
        }


        while (theDate.before(startOfNextMonth)) {
            dayModelList.add(new DayModel(theDate));
            calendar.setTime(theDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            theDate = calendar.getTime();
        }

        //add remaining space blank
        int countOfBlank = 7 - (dayModelList.size() % 7);
        if (countOfBlank != 7) {
            for (int i = 0; i < countOfBlank; i++) {
                dayModelList.add(new DayModel(true));
            }
        }
    }

    public MonthModel(int month, int year) {
        this.month = month;

        dayModelList = new ArrayList<>();

        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        startOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        startOfNextMonth = calendar.getTime();

        Date theDate = startOfMonth;
        calendar.setTime(theDate);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < firstDayOfWeek; i++) {
            dayModelList.add(new DayModel(true));
        }


        while (theDate.before(startOfNextMonth)) {
            dayModelList.add(new DayModel(theDate));
            calendar.setTime(theDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            theDate = calendar.getTime();
        }

        //add remaining space blank
        int countOfBlank = 7 - (dayModelList.size() % 7);
        if (countOfBlank != 7) {
            for (int i = 0; i < countOfBlank; i++) {
                dayModelList.add(new DayModel(true));
            }
        }
    }

    public void appendDayModelsWithSpanSize(List<DayModel> list) {
        for (DayModel dayModel : list) {
            int indexOfDayModel = this.dayModelList.indexOf(dayModel);

            if (indexOfDayModel >= 0 && indexOfDayModel < this.dayModelList.size()) {
                this.dayModelList.get(indexOfDayModel).setSpanSize(dayModel.getSpanSize());
                this.dayModelList.get(indexOfDayModel).setSpanSizeRatio(dayModel.getSpanSizeRatio());
            }
        }
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

    public List<DayModel> getDayModelList() {
        return dayModelList;
    }

    public void setDayModelList(List<DayModel> dayModelList) {
        this.dayModelList = dayModelList;
    }
}
