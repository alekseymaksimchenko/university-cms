package com.foxminded.university.service;

import java.time.LocalDate;

public interface Parseable {

    String getCurrentDateAsString(LocalDate currentDate);

    int getDaysUntilEndOfWeekAsInt(LocalDate currentDate);

    int getDaysUntilEndOfMonthAsInt(LocalDate currentDate);

}
