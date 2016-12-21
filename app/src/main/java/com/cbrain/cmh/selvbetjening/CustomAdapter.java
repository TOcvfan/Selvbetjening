package com.cbrain.cmh.selvbetjening;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Selfservice tempValues = null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder to contain inflated xml file elements ***********/
    public static class ViewHolder{

        public TextView title;
        public ImageView link;

    }

    /*********** Depends upon data size called for each row , Create each ListView row ***********/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView == null){

            /********** Inflate tabitem.xml file for each row ( Defined below ) ************/
            vi = inflater.inflate(R.layout.list_item, null);

            /******** View Holder Object to contain tabitem.xml file elements ************/
            holder = new ViewHolder();
            holder.title = (TextView)vi.findViewById(R.id.title);

            holder.link = (ImageView)vi.findViewById(R.id.link);

            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            //holder.title.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (Selfservice) data.get(position);

            /************  Set Model values in Holder elements ***********/
            holder.title.setText(tempValues.getTitle());
            holder.link.setImageResource(res.getIdentifier("com.cbrain.cmh.selvbetjening:drawable/"+tempValues.getLink(),null,null));

            /******** Set Item Click Listner for LayoutInflater for each row ***********/
            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            Controller con = (Controller) activity;
            con.onItemClick(mPosition);
        }
    }
}