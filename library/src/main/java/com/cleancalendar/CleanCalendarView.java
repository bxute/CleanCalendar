/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CleanCalendarView extends ViewGroup {

  private final int MONTH_VIEW_HEIGHT_DP = 36;
  private final int TILE_HEIGHT_DP = 48;
  private final int TILE_WIDTH_DP = 48;
  private Context mContext;
  //Show or hide month name row
  private boolean showMonthBar = true;
  private int visibleWeeksCount = 6;
  private int daysInaWeek = 7;
  /*Day on which week starts
    Mon = 1, Tue = 2, Wed = 3, Thu = 4, Fri = 5, Sat = 6, Sun = 7
  */
  private int firstDayOfWeek = 1;
  private ViewPager viewPager;
  private MonthViewAdapter monthViewAdapter;

  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };
  private View monthNameView;
  private int monthViewHeight;
  private int totalWeeksHeight;


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
      showMonthBar = a.getBoolean(R.styleable.CleanCalendarView_showMonthBar, true);
      a.recycle();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    totalWeeksHeight = visibleWeeksCount * inPx(TILE_HEIGHT_DP);
    monthViewHeight = inPx(MONTH_VIEW_HEIGHT_DP);
    monthViewAdapter = new MonthViewAdapter();
    View view = LayoutInflater.from(context).inflate(R.layout.calendar_top_bar, null, false);
    monthNameView = view.findViewById(R.id.month_name);
    viewPager = new ViewPager(mContext);
    viewPager.setId(R.id.pager);
    viewPager.setBackgroundColor(Color.parseColor("#009988"));
    viewPager.setOffscreenPageLimit(1);
    viewPager.setOnPageChangeListener(pageChangeListener);
    viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
      @Override
      public void transformPage(@NonNull View page, float position) {
        position = (float) Math.sqrt(1 - Math.abs(position));
        page.setAlpha(position);
      }
    });
    viewPager.setAdapter(monthViewAdapter);
    viewPager.setCurrentItem(50);
    addView(monthNameView);
    addView(viewPager);
  }

  private int inPx(int dp) {
    return (int) TypedValue
     .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
      .getDisplayMetrics());
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
    setMeasuredDimension(parentWidth, totalWeeksHeight + monthViewHeight);
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = getChildAt(i);
      int childWidthMeasureSpec;
      int childHeightMeasureSpec;
      if (i == 0) {
        //month view
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(monthViewHeight, MeasureSpec.EXACTLY);
      } else {
        //pager
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(totalWeeksHeight, MeasureSpec.AT_MOST);
      }
      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
  }
}
