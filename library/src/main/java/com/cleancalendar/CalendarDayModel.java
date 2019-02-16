/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

public class CalendarDayModel {
  private LocalDate date;

  public CalendarDayModel(int year, int month, int day) {
    date = LocalDate.of(year, month, day);
  }

  public CalendarDayModel(LocalDate date) {
    this.date = date;
  }

  public static CalendarDayModel today() {
    return from(LocalDate.now());
  }

  public static CalendarDayModel from(LocalDate date) {
    if (date == null)
      return null;
    return new CalendarDayModel(date);
  }

  /**
   * Returns total months in given range of year.
   *
   * @param minYear min year.
   * @param maxYear max year.
   * @return total months.
   */
  public static int totalMonthsInRange(int minYear, int maxYear) {
    return 12 * (maxYear - minYear);
  }

  /**
   * @param monthNumber index of month in entire range of months between MIN_YEAR : MAX_YEAR
   * @return instance of this class configured with calculated month.
   */
  public static CalendarDayModel fromMonthNumber(int monthNumber) {
    LocalDate date = LocalDate.of(CleanCalendarView.MIN_YEAR, 1, 1);
    return from(date.plusMonths(monthNumber - 1));
  }

  public int monthNumber() {
    if (this.getYear() >= CleanCalendarView.MIN_YEAR && this.getYear() <= CleanCalendarView.MAX_YEAR) {
      int yearDiff = (this.getYear() - CleanCalendarView.MIN_YEAR);
      int months = yearDiff * 12 + this.getMonth();
      return months;
    }
    return -1;
  }

  public int getYear() {
    return date.getYear();
  }

  public int getMonth() {
    return date.getMonthValue();
  }

  public int getDay() {
    return date.getDayOfMonth();
  }

  public boolean isInRange(CalendarDayModel min, CalendarDayModel max) {
    return (min != null && !min.isAfter(this))
     && max != null && !max.isBefore(this);
  }

  /**
   * Determine if this day is after given day.
   *
   * @param day
   * @return true if this day is after given day, false otherwise.
   */
  public boolean isAfter(CalendarDayModel day) {
    return date.isAfter(day.getDate());
  }

  /**
   * Determine if this day is before given day.
   *
   * @param day
   * @return true if this day is before given day, false otherwise.
   */
  public boolean isBefore(CalendarDayModel day) {
    return date.isBefore(day.getDate());
  }

  public LocalDate getDate() {
    return date;
  }

  public DayOfWeek dayOfWeek() {
    return date.getDayOfWeek();
  }

  public int daysInMonth() {
    LocalDate _date = this.date;
    int dayCount = 0;
    while (true) {
      LocalDate newDate = _date.plusDays(1);
      dayCount++;
      if (newDate.getMonthValue() > this.date.getMonthValue() || newDate.getYear() > this.date.getYear()) {
        break;
      }
      _date = newDate;
    }
    return dayCount;
  }

  public int weeksCount() {
    LocalDate firstDayOfMonth = LocalDate.of(this.date.getYear(), this.date.getMonth(), 1);
    LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
    TemporalField temporalField = WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth();
    int weekCount = lastDayOfMonth.get(temporalField);
    return weekCount;
  }
}
