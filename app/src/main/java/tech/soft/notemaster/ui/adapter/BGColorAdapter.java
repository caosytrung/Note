package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import tech.soft.notemaster.R;

/**
 * Created by dee on 10/03/2017.
 */

public class BGColorAdapter extends BaseAdapter {
    private List<String> itemPenColors;
    private Context mContext;
    public BGColorAdapter(Context context){
        mContext = context;
    }

    public void setItemPenWitdths(List<String> itemPenWitdths) {
        this.itemPenColors = itemPenWitdths;
    }

    @Override
    public int getCount() {
        return itemPenColors.size();
    }

    @Override
    public Object getItem(int position) {
        return itemPenColors.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BGColorAdapter.MyViewHolder myViewHolder;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bg_draw,null);
            myViewHolder = new BGColorAdapter.MyViewHolder();
            myViewHolder.tvWidth = (TextView) convertView.findViewById(R.id.tvDrawBG);


            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (BGColorAdapter.MyViewHolder) convertView.getTag();
        }
        myViewHolder.tvWidth.
                setBackgroundColor(Color.parseColor(itemPenColors.
                        get(position)));

        return convertView;
    }

    private static  class MyViewHolder{
        TextView tvWidth;
    }
}
