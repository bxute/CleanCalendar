/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

public class CalendarConfig {
  private int cellSize;
  private int dateTextColor;
  private int eventIndicatorColor;
  private int otherDateSelectionIndicatorColor;
  private int dayLabelFormat;
  private int dayNameTextSize;
  private int dateTextSize;
  private int dayNameTextStyle;

  public CalendarConfig(int cellSize,
                        int dateTextColor,
                        int eventIndicatorColor,
                        int otherDateSelectionIndicatorColor,
                        int dayLabelFormat,
                        int dayNameTextSize,
                        int dateTextSize,
                        int dayNameTextStyle) {
    this.cellSize = cellSize;
    this.dateTextColor = dateTextColor;
    this.eventIndicatorColor = eventIndicatorColor;
    this.otherDateSelectionIndicatorColor = otherDateSelectionIndicatorColor;
    this.dayLabelFormat = dayLabelFormat;
    this.dayNameTextSize = dayNameTextSize;
    this.dateTextSize = dateTextSize;
    this.dayNameTextStyle = dayNameTextStyle;
  }

  public int getCellSize() {
    return cellSize;
  }

  public void setCellSize(int cellSize) {
    this.cellSize = cellSize;
  }

  public int getDateTextColor() {
    return dateTextColor;
  }

  public void setDateTextColor(int dateTextColor) {
    this.dateTextColor = dateTextColor;
  }

  public int getDayNameTextSize() {
    return dayNameTextSize;
  }

  public void setDayNameTextSize(int dayNameTextSize) {
    this.dayNameTextSize = dayNameTextSize;
  }

  public int getDateTextSize() {
    return dateTextSize;
  }

  public void setDateTextSize(int dateTextSize) {
    this.dateTextSize = dateTextSize;
  }

  public int getDayNameTextStyle() {
    return dayNameTextStyle;
  }

  public void setDayNameTextStyle(int dayNameTextStyle) {
    this.dayNameTextStyle = dayNameTextStyle;
  }

  public int getEventIndicatorColor() {
    return eventIndicatorColor;
  }

  public void setEventIndicatorColor(int eventIndicatorColor) {
    this.eventIndicatorColor = eventIndicatorColor;
  }

  public int getOtherDateSelectionIndicatorColor() {
    return otherDateSelectionIndicatorColor;
  }

  public void setOtherDateSelectionIndicatorColor(int otherDateSelectionIndicatorColor) {
    this.otherDateSelectionIndicatorColor = otherDateSelectionIndicatorColor;
  }

  public int getDayLabelFormat() {
    return dayLabelFormat;
  }

  public void setDayLabelFormat(int dayLabelFormat) {
    this.dayLabelFormat = dayLabelFormat;
  }
}
