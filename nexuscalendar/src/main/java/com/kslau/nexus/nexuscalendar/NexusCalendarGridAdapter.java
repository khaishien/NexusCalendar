package com.kslau.nexus.nexuscalendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shen-mini-itx on 5/2/2017.
 */

public class NexusCalendarGridAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private List<DayModel> dayModelList;
    private LayoutInflater layoutInflater;

    @ColorInt
    private int dayTextColor = Color.WHITE;
    private float dayTextSize = 14;
    @ColorInt
    private int dayBackgroundColor = Color.WHITE;
    @ColorInt
    private int dayBlankBackgroundColor = Color.GRAY;
    @ColorInt
    private int dotColor = Color.LTGRAY;

    public NexusCalendarGridAdapter(Context context, List<DayModel> dayModelList) {
        this.context = context;
        this.dayModelList = dayModelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dayModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.nexus_calendar_day_grid_cell, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.nexus_calendar_day_grid_cell_tv);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (!dayModelList.get(position).isEmptyView()) {
            String dayStr = String.valueOf(dayModelList.get(position).getDay());
            viewHolder.textView.setTextColor(dayTextColor);
            viewHolder.textView.setTextSize(dayTextSize);
            viewHolder.textView.setBackgroundColor(dayBackgroundColor);
            viewHolder.textView.setText("", TextView.BufferType.EDITABLE);
            Editable editableText = viewHolder.textView.getEditableText();

            float spanRatio = dayModelList.get(position).getSpanSizeRatio();
            float spanSize = getSpanSizeOfDayModel(context, spanRatio, (GridView) parent);

            appendTextViewDotSpan(editableText, dayStr, spanSize, dotColor);
        } else {
            //empty view
            viewHolder.textView.setText("");
            viewHolder.textView.setBackgroundColor(dayBlankBackgroundColor);

        }

        return view;
    }


    private class ViewHolder {
        TextView textView;
    }

    private float getSpanSizeOfDayModel(Context mContext, float spanRatio, GridView parentView) {
        int columnSize = parentView.getColumnWidth();
        float dip = convertPixelsToDp(columnSize, mContext);
        return dip * spanRatio;
    }

    private void appendTextViewDotSpan(Editable editableText, String strToAppend,
                                       float spanSize, int color) {
        final int start = editableText.length();
        editableText.append(strToAppend);
        final int end = editableText.length();
        editableText.setSpan(new DotSpan(spanSize, color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void setDayModelList(List<DayModel> dayModelList) {
        this.dayModelList = dayModelList;
    }

    public void setDayTextColor(int dayTextColor) {
        this.dayTextColor = dayTextColor;
    }

    public void setDayTextSize(float dayTextSize) {
        this.dayTextSize = dayTextSize;
    }

    public void setDayBackgroundColor(int dayBackgroundColor) {
        this.dayBackgroundColor = dayBackgroundColor;
    }

    public void setDayBlankBackgroundColor(int dayBlankBackgroundColor) {
        this.dayBlankBackgroundColor = dayBlankBackgroundColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }
}
