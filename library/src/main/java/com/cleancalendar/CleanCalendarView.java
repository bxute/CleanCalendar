/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.TextStyle;

import java.util.Locale;

import static com.cleancalendar.MonthView.DEFAULT_WEEK_LABEL_HEIGHT_DP;

public class CleanCalendarView extends ViewGroup {
  public static final int MIN_YEAR = 1800;
  public static final int MAX_YEAR = 2200;
  public static final int TILE_HEIGHT_DP = 48;
  private final int MONTH_VIEW_HEIGHT_DP = 36;
  private int mCurrentPage = 0;
  private Context mContext;
  private int visibleWeeksCount = 6;
  /*Day on which week starts
    Mon = 1, Tue = 2, Wed = 3, Thu = 4, Fri = 5, Sat = 6, Sun = 7
  */
  private int firstDayOfWeek = 1;
  private ViewPager viewPager;
  //Adapter for providing event information.
  private CalendarEventAdapter eventAdapter;
  private MonthViewAdapter monthViewAdapter;
  private TextView monthNameView;
  private int monthNameViewHeight;
  private int totalWeeksHeight;
  private CalendarListener calendarListener;
  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      CalendarDayModel dayModel = CalendarDayModel.fromMonthNumber(position);
      if (calendarListener != null) {
        calendarListener.onMonthChanged(dayModel);
      }
      dispatchPrefetchCall(dayModel);
      mCurrentPage = position;
      updateUi();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  public CleanCalendarView(Context context) {
    this(context, null);
  }

  public CleanCalendarView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CleanCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    try {
      TypedArray a = context.getResources().obtainAttributes(attrs, R.styleable.CleanCalendarView);
      firstDayOfWeek = a.getInt(R.styleable.CleanCalendarView_firstDayOfWeek, 1);
      //showMonthBar = a.getBoolean(R.styleable.CleanCalendarView_showMonthBar, true);
      a.recycle();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    totalWeeksHeight = inPx(DEFAULT_WEEK_LABEL_HEIGHT_DP) + (6 * inPx(TILE_HEIGHT_DP));
    monthNameViewHeight = inPx(MONTH_VIEW_HEIGHT_DP);
    monthViewAdapter = new MonthViewAdapter();
    monthViewAdapter.setEventAdapter(eventAdapter);
    View view = LayoutInflater.from(context).inflate(R.layout.calendar_top_bar,
     null, false);
    monthNameView = view.findViewById(R.id.month_name);
    viewPager = new ViewPager(mContext);
    viewPager.setId(R.id.pager);
    viewPager.setOffscreenPageLimit(1);
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setPageTransformer(false,
     new ViewPager.PageTransformer() {
       @Override
       public void transformPage(@NonNull View page, float position) {
         position = (float) Math.sqrt(1 - Math.abs(position));
         page.setAlpha(position);
       }
     });
    viewPager.setAdapter(monthViewAdapter);
    CalendarDayModel today = CalendarDayModel.today();
    viewPager.setCurrentItem(today.monthNumber());
    dispatchPrefetchCall(today);
    addView(monthNameView);
    addView(viewPager);
  }

  private int inPx(int dp) {
    return (int) TypedValue
     .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
      .getDisplayMetrics());
  }


  private void dispatchPrefetchCall(CalendarDayModel today) {
    if (eventAdapter != null) {
      //current month
      eventAdapter.preFetchEventFor(today);
      LocalDate prevMonth = today.getDate().minusMonths(1);
      //for prev month
      eventAdapter.preFetchEventFor(CalendarDayModel.from(prevMonth));
      //for next month
      LocalDate nextMonth = today.getDate().plusMonths(1);
      eventAdapter.preFetchEventFor(CalendarDayModel.from(nextMonth));
    }
  }


  public void setEventAdapter(CalendarEventAdapter eventAdapter) {
    this.eventAdapter = eventAdapter;
    if (monthViewAdapter != null) {
      monthViewAdapter.setEventAdapter(eventAdapter);
    }
  }

  private void updateUi() {
    int monthNumber = viewPager.getCurrentItem();
    CalendarDayModel day = CalendarDayModel.fromMonthNumber(monthNumber);
    String year = String.valueOf(day.getYear());
    String month = Month.of(day.getMonth()).getDisplayName(TextStyle.FULL, Locale.getDefault());
    monthNameView.setText(String.format("%s, %s", month, year));
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int top = 0;
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == View.GONE)
        continue;
      int childHeight = child.getMeasuredHeight();
      child.layout(l, top, r, top + childHeight);
      top += childHeight;
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(parentWidth, totalWeeksHeight + monthNameViewHeight);
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = getChildAt(i);
      int childWidthMeasureSpec;
      int childHeightMeasureSpec;
      if (i == 0) {
        //month view
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(monthNameViewHeight, MeasureSpec.EXACTLY);
      } else {
        //pager
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(totalWeeksHeight, MeasureSpec.AT_MOST);
      }
      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
  }

  public void setCalendarListener(CalendarListener calendarListener) {
    this.calendarListener = calendarListener;
  }
}
