package tech.soft.notemaster.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import tech.soft.notemaster.R;

/**
 * Created by dee on 23/03/2017.
 */

public class LinedEditText extends EditText {
    private Rect mRect;
    private Paint mPaint;



    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
//
//        mRect = new Rect();
//        mPaint = new Paint();
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setColor(Color.parseColor("#C0C0C0")); //SET YOUR OWN COLOR HERE
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //int count = getLineCount();
//
        int height = getHeight();
        int line_height = getLineHeight();
//
        int count = height / line_height;
//
//        if (getLineCount() > count)
//            count = getLineCount();//for long text with scrolling
//
//        Rect r = mRect;
//        Paint paint = mPaint;
//        int baseline = getLineBounds(0, r);//first line
        int baseline;
        baseline = getLineHeight() * 2;
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.black_semi_transparent));
        for (int i = 0; i < count; i++) {


            canvas.drawLine(0, baseline -40 , getWidth(), baseline -40
                    , mPaint);
            baseline += getLineHeight();//next line
        }

        super.onDraw(canvas);
    }

}
