package tech.soft.notemaster.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
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

public class TextFontAdapter extends BaseAdapter {
    private List<Typeface> fontList;
    private Context mContext;

    public TextFontAdapter(Context context, List<Typeface> listColor) {
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
        TextFontAdapter.PenColorHolder penColorHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_text_font, null);
            penColorHolder = new TextFontAdapter.PenColorHolder();
            penColorHolder.tvPen = (TextView) convertView.findViewById(R.id.tvItemTextFont);

            penColorHolder.tvPen.setTypeface(fontList.get(position));

            convertView.setTag(penColorHolder);
        } else {
            penColorHolder = (TextFontAdapter.PenColorHolder) convertView.getTag();
        }

        return convertView;
    }

    private static class PenColorHolder {
        TextView tvPen;
    }
}
