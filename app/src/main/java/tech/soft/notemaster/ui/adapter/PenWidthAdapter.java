package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tech.soft.notemaster.R;


import java.util.List;

/**
 * Created by dee on 10/03/2017.
 */

public class PenWidthAdapter extends BaseAdapter {
    private List<Integer> itemPenWitdths;
    private Context mContext;
    public PenWidthAdapter(Context context){
        mContext = context;
    }

    public void setItemPenWitdths(List<Integer> itemPenWitdths) {
        this.itemPenWitdths = itemPenWitdths;
    }

    @Override
    public int getCount() {
        return itemPenWitdths.size();
    }

    @Override
    public Object getItem(int position) {
        return itemPenWitdths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pen_width,null);
            myViewHolder = new MyViewHolder();
            myViewHolder.tvWidth = (TextView) convertView.findViewById(R.id.tvItemPenWIdth);


            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = myViewHolder.tvWidth.getLayoutParams();
        params.height = itemPenWitdths.get(position);
        myViewHolder.tvWidth.setLayoutParams(params);

        return convertView;
    }


    private static  class MyViewHolder{
        TextView tvWidth;
    }
}
