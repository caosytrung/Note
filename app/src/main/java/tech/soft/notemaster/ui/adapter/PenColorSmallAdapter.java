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
import tech.soft.notemaster.utils.Utils;

/**
 * Created by dee on 24/03/2017.
 */

public class PenColorSmallAdapter extends BaseAdapter {
    private List<String> listColor;
    private Context mContext;

    public PenColorSmallAdapter(Context context,List<String> listColor){
        mContext = context;
        this.listColor = listColor;
    }

    @Override
    public int getCount() {
        return listColor.size();
    }

    @Override
    public Object getItem(int position) {
        return listColor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PenColorHolder penColorHolder;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pen_color_small,null);
            penColorHolder = new PenColorHolder();
            penColorHolder.tvPen = (TextView) convertView.findViewById(R.id.tvItempenColorSM);

            convertView.setTag(penColorHolder);
        } else {
            penColorHolder = (PenColorHolder) convertView.getTag();
        }

        Utils.setFontAnswesSomeTextView(penColorHolder.tvPen,mContext);
        penColorHolder.tvPen.setText("\uf040");
        penColorHolder.tvPen.setTextColor(Color.
                parseColor(listColor.get(position)));

        return convertView;
    }

    private static class PenColorHolder{
        TextView tvPen;
    }
}
