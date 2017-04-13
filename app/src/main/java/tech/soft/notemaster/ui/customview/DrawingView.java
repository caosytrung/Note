package tech.soft.notemaster.ui.customview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import tech.soft.notemaster.models.BrushStrock;
import tech.soft.notemaster.utils.IConstand;

import static android.R.attr.defaultWidth;

/**
 * Created by dee on 12/04/2017.
 */

public class DrawingView extends ImageView implements IConstand {
    private Paint mPaint;
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Path sentPath;
    private Paint   mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private IDrawByFinger iDrawByFinger;
    private ArrayList<BrushStrock> listPath = new ArrayList<>();
    private boolean isDrawing;

    public void setIDrawF(IDrawByFinger iDrawF){
        iDrawByFinger = iDrawF;
    }

    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context=c;
        //mPath = new Path();
        sentPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        initPain();
        isDrawing =false;

    }

    public void setUpWidthPaint(int width){
        mPaint.setStrokeWidth(width);
    }
    public void setUpColorPain(String color){
        mPaint.setColor(Color.parseColor(color));
    }

    private void initPain(){


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(4);

    }

    public DrawingView(Context c) {
        super(c);
        context=c;
        sentPath = new Path();
        //     mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(2f);
        initPain();
        isDrawing =false;

    }
    public int w,h;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        // canvas.drawPath( sentPath,  mPaint);
        if (isDrawing && sentPath != null) {
            canvas.drawPath(sentPath,mPaint);
        }
        for (int i = 0 ; i < listPath.size(); i ++){
            BrushStrock brushStrock = listPath.get(i);
            Log.d(listPath.size() + " ","rewrwerwerwer");
            Paint tmp =  new Paint();


            tmp.setAntiAlias(true);
            tmp.setDither(true);
            tmp.setColor(brushStrock.getColor());
            tmp.setStyle(Paint.Style.STROKE);
            tmp.setStrokeJoin(Paint.Join.ROUND);
            tmp.setStrokeCap(Paint.Cap.ROUND);
            tmp.setStrokeWidth(brushStrock.getWith());

            canvas.drawPath(listPath.get(i).getPath(),tmp);
            tmp = null;
        }

    }

    private float mX, mY,zX,zY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {

        sentPath.moveTo(x,y);
        zX = x;
        zY = y;
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//            mPath.quadTo(mX , mY , (x + mX)/2, (y + mY)/2);
            sentPath.quadTo(mX  , mY, (x + mX )/2 , (y + mY )/2 );
            // sentPath.lineTo(x,y);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 60, Path.Direction.CW);
        }
    }


    private void touch_up() {

        sentPath.lineTo(mX ,mY);
//        circlePath.reset();
//        // commit the path to our offscreen
//        mCanvas.drawPath(mPath,  mPaint);
//        // kill this so we don't double draw
//        mPath.reset();

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                isDrawing  = false;
                listPath.add(new BrushStrock((int)mPaint.getStrokeWidth(),mPaint.getColor(),sentPath));
                sentPath = new Path();
                invalidate();
                //   paths.add(mPath);
                // mPath = new Path();
                //  mCanvas.drawPath(sentPath,mPaint);
                break;
        }
        return true;
    }

    public void undo(){

        if (listPath.size() > 0 && listPath != null){
            listPath.remove(listPath.size() - 1);
            sentPath = new Path();
            invalidate();
        }

    }
    public void delete(){
        listPath.clear();
        sentPath = new Path();
        //   mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
        //      Log.d(getClass().getSimpleName(),rectF.width() + " " + rectF.height());

    }

    public interface IDrawByFinger{
        void getPath(Path path);
    }
}