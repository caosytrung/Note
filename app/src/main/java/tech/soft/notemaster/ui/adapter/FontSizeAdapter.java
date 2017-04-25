package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tech.soft.notemaster.R;

/**
 * Created by dee on 23/04/2017.
 */

public class FontSizeAdapter extends BaseAdapter {
    private List<Integer> fontList;
    private Context mContext;

    public FontSizeAdapter(Context context, List<Integer> listColor) {
        mContext = context;
        this.fontList = listColor;
    }

    @Override
    public int getCount() {
        return fontList.size();
    }

    @Override
    public Object getItem(int position) {
        return fontList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FontSizeAdapter.PenColorHolder penColorHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_text_size, null);
            penColorHolder = new FontSizeAdapter.PenColorHolder();
            penColorHolder.tvPen = (TextView) convertView.findViewById(R.id.tvItemTextSize);
            penColorHolder.tvPen.setTextSize(fontList.get(position));

            convertView.setTag(penColorHolder);
        } else {
            penColorHolder = (FontSizeAdapter.PenColorHolder) convertView.getTag();
        }

        penColorHolder.tvPen.setText("A+");
        penColorHolder.tvPen.setTextSize(fontList.get(position));


        return convertView;
    }

    private static class PenColorHolder {
        TextView tvPen;
    }
}
