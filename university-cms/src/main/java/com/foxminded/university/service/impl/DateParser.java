package com.foxminded.university.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.foxminded.university.service.Parseable;

@Service
public class DateParser implements Parseable {

    @Override
    public String getCurrentDateAsString(LocalDate currentDate) {
        return currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public int getDaysUntilEndOfWeekAsInt(LocalDate currentDate) {
        return DayOfWeek.SUNDAY.getValue() - currentDate.getDayOfWeek().getValue();
    }

    @Override
    public int getDaysUntilEndOfMonthAsInt(LocalDate currentDate) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = currentDate.getDayOfMonth();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        int lastDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return lastDayOfMonth - currentDay;
    }

}
