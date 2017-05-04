package com.kslau.nexus.nexuscalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shen-mini-itx on 5/2/2017.
 */

public class NexusCalendarView extends LinearLayout {

    private final String TAG = this.getClass().getSimpleName();

    //Data
    private Context mContext;
    private int mSelectedMonth;
    private int mSelectedYear;
    private MonthModel monthModel;
    private List<DayModel> dayModelListWithSpanSize;

    //View element
    @ColorInt
    private int layoutBackgroundColor;
    private float headerTextSize;
    @ColorInt
    private int headerTextColor;
    @ColorInt
    private int headerBackgroundColor;
    @ColorInt
    private int headerArrowBackgroundColor;
    @ColorInt
    private int weekTitleTextColor;
    @ColorInt
    private int dayTextColor;
    private float dayTextSize;
    @ColorInt
    private int dayBackgroundColor;
    @ColorInt
    private int dayBlankBackgroundColor;
    @ColorInt
    private int dotColor;

    //View
    private View mView;
    private GridView mGridView;
    private TextView mMonthTitle;

    private NexusCalendarGridAdapter calenderGridAdapter;

    private OnDateClickListener onDateClickListener;
    private OnDateLongClickListener onDateLongClickListener;

    //================================================================================
    // Constructors
    //================================================================================

    public NexusCalendarView(Context context) {
        super(context);
        init(context, null);
    }

    public NexusCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NexusCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //================================================================================
    // init
    //================================================================================
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        dayModelListWithSpanSize = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //If we're on good Android versions, turn off clipping for cool effects
            setClipToPadding(false);
            setClipChildren(false);
        } else {
            //Old Android does not like _not_ clipping view pagers, we need to clip
            setClipChildren(true);
            setClipToPadding(true);
        }

        initView(attrs);
        initValue();
        initLayout();
        initGridView();
        updateUI();
    }

    private void initView(AttributeSet attrs) {

        layoutBackgroundColor = Color.LTGRAY;
        headerTextSize = 20;
        headerTextColor = Color.BLACK;
        headerBackgroundColor = Color.WHITE;
        headerArrowBackgroundColor = Color.BLACK;
        weekTitleTextColor = Color.BLACK;
        dayTextColor = Color.BLACK;
        dayTextSize = 14;
        dayBackgroundColor = Color.WHITE;
        dayBlankBackgroundColor = Color.GRAY;
        dotColor = Color.LTGRAY;

        if (attrs != null) {
            TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.NexusCalendarView, 0, 0);
            try {
                layoutBackgroundColor = typedArray.getColor(R.styleable.NexusCalendarView_layoutBackgroundColor, Color.LTGRAY);
                headerTextSize = typedArray.getDimension(R.styleable.NexusCalendarView_headerTextSize, 20);
                headerTextColor = typedArray.getColor(R.styleable.NexusCalendarView_headerTextColor, Color.BLACK);
                headerBackgroundColor = typedArray.getColor(R.styleable.NexusCalendarView_headerBackgroundColor, Color.WHITE);
                headerArrowBackgroundColor = typedArray.getColor(R.styleable.NexusCalendarView_headerArrowBackgroundColor, Color.BLACK);
                weekTitleTextColor = typedArray.getColor(R.styleable.NexusCalendarView_weekTitleTextColor, Color.BLACK);
                dayTextColor = typedArray.getColor(R.styleable.NexusCalendarView_dayTextColor, Color.WHITE);
                dayTextSize = typedArray.getDimension(R.styleable.NexusCalendarView_dayTextSize, 14);
                dayBackgroundColor = typedArray.getColor(R.styleable.NexusCalendarView_dayBackgroundColor, Color.WHITE);
                dayBlankBackgroundColor = typedArray.getColor(R.styleable.NexusCalendarView_dayBlankBackgroundColor, Color.GRAY);
                dotColor = typedArray.getColor(R.styleable.NexusCalendarView_dotColor, Color.LTGRAY);

            } finally {
                typedArray.recycle();
            }
        }

    }

    private void initValue() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mSelectedMonth = calendar.get(Calendar.MONTH);
        mSelectedYear = calendar.get(Calendar.YEAR);
    }

    private void initLayout() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.nexus_calendar_view, this, true);
        monthModel = new MonthModel(mSelectedMonth);

        LinearLayout mLayoutBackground = (LinearLayout) mView.findViewById(R.id.nexus_calendar_layout);
        mLayoutBackground.setBackgroundColor(layoutBackgroundColor);

        LinearLayout mHeaderBackground = (LinearLayout) mView.findViewById(R.id.nexus_calendar_header_layout);
        mHeaderBackground.setBackgroundColor(headerBackgroundColor);

        mMonthTitle = (TextView) mView.findViewById(R.id.nexus_calendar_month_title_tv);
        mMonthTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize);
        mMonthTitle.setTextColor(headerTextColor);

        ImageButton mBackButton = (ImageButton) mView.findViewById(R.id.nexus_calendar_back_btn);
        mBackButton.setBackgroundColor(headerArrowBackgroundColor);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentMonth(mSelectedMonth - 1);
            }
        });
        ImageButton mNextButton = (ImageButton) mView.findViewById(R.id.nexus_calendar_next_btn);
        mNextButton.setBackgroundColor(headerArrowBackgroundColor);
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentMonth(mSelectedMonth + 1);
            }
        });

        TextView dayOfWeek1 = (TextView) mView.findViewById(R.id.dayOfWeek1);
        dayOfWeek1.setTextColor(weekTitleTextColor);
        TextView dayOfWeek2 = (TextView) mView.findViewById(R.id.dayOfWeek2);
        dayOfWeek2.setTextColor(weekTitleTextColor);
        TextView dayOfWeek3 = (TextView) mView.findViewById(R.id.dayOfWeek3);
        dayOfWeek3.setTextColor(weekTitleTextColor);
        TextView dayOfWeek4 = (TextView) mView.findViewById(R.id.dayOfWeek4);
        dayOfWeek4.setTextColor(weekTitleTextColor);
        TextView dayOfWeek5 = (TextView) mView.findViewById(R.id.dayOfWeek5);
        dayOfWeek5.setTextColor(weekTitleTextColor);
        TextView dayOfWeek6 = (TextView) mView.findViewById(R.id.dayOfWeek6);
        dayOfWeek6.setTextColor(weekTitleTextColor);
        TextView dayOfWeek7 = (TextView) mView.findViewById(R.id.dayOfWeek7);
        dayOfWeek7.setTextColor(weekTitleTextColor);

    }


    private void initGridView() {
        mGridView = (GridView) mView.findViewById(R.id.nexus_calendar_grid_view);
        calenderGridAdapter = new NexusCalendarGridAdapter(mContext, monthModel.getDayModelList());
        calenderGridAdapter.setDayBackgroundColor(dayBackgroundColor);
        calenderGridAdapter.setDayBlankBackgroundColor(dayBlankBackgroundColor);
        calenderGridAdapter.setDayTextColor(dayTextColor);
        calenderGridAdapter.setDayTextSize(dayTextSize);
        calenderGridAdapter.setDotColor(dotColor);

        mGridView.setAdapter(calenderGridAdapter);


        bindOnDateClickListener();

    }

    private void bindOnDateClickListener() {
        if (onDateClickListener != null) {
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onDateClickListener.onDateClickListener(parent, view, monthModel.getDayModelList().get(position));
                }
            });
        }
        if (onDateLongClickListener != null)
            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    onDateLongClickListener.onDateLongClickListener(parent, view, monthModel.getDayModelList().get(position));
                    return false;
                }
            });
    }

    public void setCurrentMonth(int month) {
        if (month > 11) {
            this.mSelectedMonth = month - 12;
            this.mSelectedYear++;
        } else if (month <= 0) {
            this.mSelectedMonth = month + 11;
            this.mSelectedYear--;
        } else {
            this.mSelectedMonth = month;
        }
        updateUI();
    }

    public void setCurrentYear(int year) {
        this.mSelectedYear = year;
        updateUI();
    }

    public void updateUI() {
        monthModel = new MonthModel(mSelectedMonth, mSelectedYear);
        dayModelListWithSpanSize = recomputeSpanSizeRatio(dayModelListWithSpanSize);
        monthModel.appendDayModelsWithSpanSize(dayModelListWithSpanSize);
        String strMonthYear = new DateFormatSymbols().getMonths()[mSelectedMonth] + " " + mSelectedYear;
        mMonthTitle.setText(strMonthYear);
        calenderGridAdapter.setDayModelList(monthModel.getDayModelList());
        calenderGridAdapter.notifyDataSetChanged();
    }

    public void addDotValueToDate(float size, Date date) {
        DayModel dayModel = new DayModel(date);
        dayModel.setSpanSize(size);

        dayModelListWithSpanSize.add(dayModel);
    }

    private List<DayModel> recomputeSpanSizeRatio(List<DayModel> list) {
        //find largest value
        float max = 0;
        float min = 0;

        for (DayModel model : list) {
            if (max < model.getSpanSize()) {
                max = model.getSpanSize();
            }

            if (min > model.getSpanSize()) {
                min = model.getSpanSize();
            }
        }

        float ratio;

        if (min == 0) {
            ratio = 1 / max;
        } else {
            ratio = min / max;
        }

        for (DayModel model : list) {
            model.setSpanSizeRatio(model.getSpanSize() * ratio);
        }

        return list;
    }

    public OnDateClickListener getOnDateClickListener() {
        return onDateClickListener;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
        bindOnDateClickListener();
    }

    public OnDateLongClickListener getOnDateLongClickListener() {
        return onDateLongClickListener;
    }

    public void setOnDateLongClickListener(OnDateLongClickListener onDateLongClickListener) {
        this.onDateLongClickListener = onDateLongClickListener;
        bindOnDateClickListener();
    }

    public interface OnDateClickListener {
        void onDateClickListener(AdapterView<?> parent, View view, DayModel dayModel);
    }

    public interface OnDateLongClickListener {
        boolean onDateLongClickListener(AdapterView<?> parent, View view, DayModel dayModel);
    }
}

