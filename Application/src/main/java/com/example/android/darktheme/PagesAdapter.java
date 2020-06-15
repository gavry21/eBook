package com.example.android.darktheme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PagesAdapter extends BaseAdapter {
    Context context;
    ViewHolder viewHolder;
    private ArrayList<InfoObject> all_pages;
    String date;
    private int lastPosition = -1;

    public PagesAdapter( ArrayList<InfoObject> pages, @NonNull Context context) {
        //super(context, R.layout.all_history_el, pages);
        this.context = context;
        this.all_pages = pages;
    }

    public class ViewHolder {
        TextView pages_read;
        TextView date_cur;
        TextView speed_read;
    }

    @Override
    public int getCount() {
        return this.all_pages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.all_pages.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder =  null; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.all_history_el, parent, false);

            viewHolder.pages_read = (TextView) convertView.findViewById(R.id.read_pages);
            viewHolder.date_cur = (TextView) convertView.findViewById(R.id.dates);
            viewHolder.speed_read = (TextView) convertView.findViewById(R.id.speed_read);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;

        InfoObject infoObject = all_pages.get(position);

        viewHolder.pages_read.setText("Прочитано: "+Integer.toString(infoObject.getPages()) +" стр");
        viewHolder.date_cur.setText("Дата: "+infoObject.getDate());
        DecimalFormat format = new DecimalFormat("0.#");
        viewHolder.speed_read.setText("Режим скорости: "+format.format(infoObject.getSpeed()));
        // Return the completed view to render on screen
        return convertView;
    }
}
