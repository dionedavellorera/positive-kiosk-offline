package com.nerdvana.positiveoffline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.model.SunmiPrinterModel;

import java.util.List;

public class PrintersArrayAdapter extends ArrayAdapter<SunmiPrinterModel> {
    private Context context;
    private List<SunmiPrinterModel> list;
    public PrintersArrayAdapter(Context context, int textViewResourceId,
                                List<SunmiPrinterModel> objects) {

        super(context, textViewResourceId, objects);
        this.context = context;
        this.list = objects;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater ();  // we get a reference to the activity
        View row=inflater.inflate(R.layout.list_item_array, parent, false);
        TextView label=(TextView)row.findViewById(R.id.name);
        if (list.get(position).getType().equalsIgnoreCase(SunmiPrinterModel.PRINTER_BUILT_IN)) {
            label.setText("BUILT IN PRINTER");
        } else {
            label.setText(list.get(position).getDevice().getNickName().toUpperCase());
        }


        return row;
    }

}