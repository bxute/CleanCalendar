/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void testWeeksCount() {
    int expected = 5;
    CalendarDayModel calendarDayModel = new CalendarDayModel(2018, 2, 1);
    int weeksCount = calendarDayModel.weeksCount();
    assertEquals(expected, weeksCount);
  }

  @Test
  public void testDaysCount() {
    int expectedDay = 28;
    CalendarDayModel calendarDayModel = new CalendarDayModel(2019, 2, 1);
    int daysCount = calendarDayModel.daysInMonth();
    assertEquals(expectedDay, daysCount);
  }
}