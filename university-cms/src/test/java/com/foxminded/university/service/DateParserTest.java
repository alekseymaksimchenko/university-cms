package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.university.service.impl.DateParser;

class DateParserTest {

    private DateParser dateParser;

    @BeforeEach
    void init() {
        dateParser = new DateParser();
    }

    @Test
    void testDateParserTest_getCurrentDateAsInt_shouldReturnLocalDateAsIntValue() {
        LocalDate currentDate = LocalDate.of(2023, 10, 04);

        String actual = dateParser.getCurrentDateAsString(currentDate);
        String expected = "2023-10-04";

        assertEquals(expected, actual);
    }

    @Test
    void testDateParserTest_getDaysUntilEndOfWeekAsInt_shouldReturnNumberOfDaysTillEndOfWeekAccordingToDate() {
        LocalDate currentDate = LocalDate.of(2023, 10, 04);

        int actual = dateParser.getDaysUntilEndOfWeekAsInt(currentDate);
        int expected = 4;

        assertEquals(expected, actual);
    }

    @Test
    void testDateParserTest_getDaysUntilEndOfMont_shouldReturnNumberOfDaysTillEndOfMonthAccordingToDate() {
        LocalDate currentDate = LocalDate.of(2023, 10, 04);

        int actual = dateParser.getDaysUntilEndOfMonthAsInt(currentDate);
        int expected = 27;

        assertEquals(expected, actual);
    }

}
