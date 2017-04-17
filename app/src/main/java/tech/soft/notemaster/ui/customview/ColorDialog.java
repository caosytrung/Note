package tech.soft.notemaster.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.ui.adapter.DialogColorAdapter;
import tech.soft.notemaster.ui.calback.IOnItemClick;
import tech.soft.notemaster.ui.calback.ISetTextNote;
import tech.soft.notemaster.utils.Utils;

/**
 * Created by dee on 15/04/2017.
 */

public class ColorDialog extends Dialog implements IOnItemClick {
    private RecyclerView rvColor;
    private DialogColorAdapter dialogColorAdapter;
    private List<Integer> colorList;
    private int type, start, end;
    private ISetTextNote iSetTextNote;


    public ColorDialog(Context context, int theme, int tye, int start, int end) {
        super(context, theme);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(130);
        getWindow().setBackgroundDrawable(d);
        setContentView(R.layout.dialog_color);
        initControl();
        this.type = tye;
        this.start = start;
        this.end = end;
    }

    public void setiSetTextNote(ISetTextNote iSetTextNote) {
        this.iSetTextNote = iSetTextNote;
    }

    private void initControl() {
        rvColor = (RecyclerView) findViewById(R.id.rvColor);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        rvColor.setLayoutManager(manager);
        colorList = Utils.initDialogColor();
        dialogColorAdapter = new DialogColorAdapter(getContext(), this);
        dialogColorAdapter.setColorList(colorList);
        rvColor.setAdapter(dialogColorAdapter);
        dialogColorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int pos) {
        iSetTextNote.setText(colorList.get(pos), start, end);
        dismiss();
        Log.d("asdasda", "Moanooo");
    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }

}
