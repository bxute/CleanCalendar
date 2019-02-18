/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

/**
 * Abstract class to be implemented by user of {@code CleanCalendarView}
 */
public abstract class CalendarEventAdapter {
  public abstract int getEventCountOn(CalendarDayModel day);

  public abstract void preFetchEventFor(CalendarDayModel day);
}
