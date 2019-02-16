/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthView extends ViewGroup {
  private final int DEFAULT_WEEKS_COUNT = 6;
  private final int DEFAULT_WEEK_LABEL_HEIGHT_DP = 48;
  private final Context mContext;
  private int mMonthIndex;
  private CalendarDayModel firstDayOfMonth;
  private int totalWeeks;
  private int totalHeight;
  private int dayColumnWidth;
  private int month;
  private int year;
  private int totalDays;
  private int totalWeeksHeight;
  private int weekLabelHeightPx;
  private int singleWeekHeight;

  public MonthView(Context context) {
    this(context, null);
  }

  public MonthView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
  }

  /**
   * Setter for month index.
   *
   * @param monthNumber index of month
   */
  public void setMonthNumber(int monthNumber) {
    this.mMonthIndex = monthNumber;
    firstDayOfMonth = CalendarDayModel.fromMonthNumber(monthNumber);
    totalWeeks = firstDayOfMonth.weeksCount();
    totalDays = firstDayOfMonth.daysInMonth();
    weekLabelHeightPx = inPx(DEFAULT_WEEK_LABEL_HEIGHT_DP);
    totalWeeksHeight = (6 * inPx(CleanCalendarView.TILE_HEIGHT_DP));
    singleWeekHeight = totalWeeksHeight / totalWeeks;
    totalHeight = totalWeeksHeight + weekLabelHeightPx;
    addWeekDays();
    addDayViews();
    requestLayout();
  }

  private int inPx(int dp) {
    return (int) TypedValue
     .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
      .getDisplayMetrics());
  }

  private void addWeekDays() {
    for (int i = 0; i < 7; i++) {
      TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.week_name_label_view,
       null, false);
      view.setText("Pos " + i);
      addView(view);
    }
  }

  private void addDayViews() {
    for (int i = 1; i <= totalDays; i++) {
      TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.day_view,
       null, false);
      view.setText("" + i);
      addView(view);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int top = t;
    int left = l;

    for (int i = 1; i <= childCount; i++) {
      View child = getChildAt(i - 1);
      if (child.getVisibility() == View.GONE)
        continue;
      float alpha = i;
      child.setAlpha(alpha / (i + 5));
      child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
      left += child.getMeasuredWidth();
      if (i > 0 && i % 7 == 0) {
        top += child.getMeasuredHeight();
        left = 0;
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredHeight = totalHeight;
    int width = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(width, measuredHeight);
    //measure childs
    dayColumnWidth = width / 7;
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      int childWidth = dayColumnWidth;
      int childHeight;
      if (i < 7) {
        childHeight = weekLabelHeightPx;
      } else {
        childHeight = singleWeekHeight;
      }
      int childHeighSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      view.measure(childWidthSpec, childHeighSpec);
    }
  }
}
