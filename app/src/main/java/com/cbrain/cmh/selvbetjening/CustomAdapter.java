package com.cbrain.cmh.selvbetjening;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements OnClickListener {
    private String TAG = CustomAdapter.class.getSimpleName();
    Context context;
    List<Selfservice> data;

    public Resources res;
    Selfservice self = null;
    private static LayoutInflater inflater;


    public CustomAdapter(Context context, List<Selfservice> dataList, Resources resources) {
        res = resources;
        data = dataList;
        this.context = context;
    }

    private class Holder {
        TextView title;
        TextView link;
    }

    @Override
    public int getCount() {
        if(data.size() == 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        Holder holder;

        if(rowView == null){

            rowView = inflater.inflate(R.layout.list_item, null);
            holder = new Holder();
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.link = (TextView) rowView.findViewById(R.id.link);

            rowView.setTag(holder);

        }else{
            holder = (Holder)rowView.getTag();
        }
        if(data.size() <= 0){
            holder.title.setText("did not work");
        }else{
            self = null;
            self = (Selfservice) data.get(position);
            holder.title.setText(self.getTitle());
            holder.link.setText(self.getLink());
            //Log.i(TAG, "adapter");
        }

        return rowView;
        }

    @Override
    public void onClick(View v){
        Log.v("CustomAdapter", "row clicked");
    }

}