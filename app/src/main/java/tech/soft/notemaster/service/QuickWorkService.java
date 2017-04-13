package tech.soft.notemaster.service;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.adapter.PenColorAdapter;
import tech.soft.notemaster.ui.adapter.PenColorSmallAdapter;
import tech.soft.notemaster.ui.customview.MyViewGroup;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.width;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by dee on 04/04/2017.
 */

public class QuickWorkService extends Service implements View.OnTouchListener, View.OnClickListener,IConstand {
    private static final int TOUCH_TIME_THRESHOLD = 150;
    private static final String STOP_SERVICE ="STOP_SERVICE" ;
    private long lastTime;
    private MyViewGroup bigContent;
    private MyViewGroup smallContent;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private float xView, yView, xFirstScreen, yFirstScreen;
    private boolean isSmallContent;
    private ImageView btnSmallView;
    private EditText edtLable;
    private QuickWorkDialog mQuickWorkDialog;
    private NotificationCompat notificationCompat;
    private PendingIntent pendingIntentStopService;
    private StopServiceBroadcast mStopServiceBroadcast;
    private boolean isShowSmall;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        createNotification();
        mStopServiceBroadcast = new StopServiceBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE);
        registerReceiver(mStopServiceBroadcast,filter);

        mQuickWorkDialog = new QuickWorkDialog(this);
        mQuickWorkDialog.setCanceledOnTouchOutside(false);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        initLayoutParams();
        initSmallView();
        initBigView();
        return START_NOT_STICKY ;
    }

    private void createNotification(){
        Intent intentStop = new Intent();
        intentStop.setAction(STOP_SERVICE);
        pendingIntentStopService = PendingIntent.getBroadcast(this,1,intentStop,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_pencil);

        builder.setContentTitle("Close quick note");
        builder.setContentText("Click here to Close Quick Note");
        builder.setContentIntent(pendingIntentStopService);
        startForeground(1,builder.build());
    }

    ImageView imageView;
    private void initSmallView(){
        imageView = new ImageView(this);

        imageView.setImageResource(R.drawable.pencil_104);
        mWindowManager.addView(imageView,mParams );
        isShowSmall = true;
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d("aaaa","zazzazza");
                        xView = mParams.x;
                        yView = mParams.y;
                        xFirstScreen = event.getRawX();
                        yFirstScreen = event.getRawY();
                        lastTime = System.currentTimeMillis();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX()-xFirstScreen;
                        float deltaY = event.getRawY()-yFirstScreen;
                        mParams.x = (int)(xView +deltaX);
                        mParams.y = (int)(yView +deltaY);
                        mWindowManager.updateViewLayout(imageView, mParams);

                        break;
                    case MotionEvent.ACTION_UP:

                        if (System.currentTimeMillis() - lastTime < TOUCH_TIME_THRESHOLD) {
//                    WindowManager.LayoutParams mWindowsParams = new WindowManager.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT//WindowManager.LayoutParams.WRAP_CONTENT,
//                            ,//WindowManager.LayoutParams.WRAP_CONTENT,
//                            //WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                            WindowManager.LayoutParams.TYPE_PHONE,
//                            //WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, // Not displaying keyboard on bg activity's EditText
//                            //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, //Not work with EditText on keyboard
//                            PixelFormat.TRANSLUCENT);
//                    mWindowManager.addView(bigContent,mWindowsParams);
//                    mWindowManager.removeView(smallContent);
//                    setFocusView(bigContent);
//                    edtLable.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(edtLable
//                            , InputMethodManager.SHOW_IMPLICIT);
                            mWindowManager.removeView(imageView);
                            mQuickWorkDialog.show();
                            isShowSmall = false;
                        }


                        break;

                }

                return true;
            }
        });

//        smallContent = new MyViewGroup(this);
//        View v = View.inflate(this,
//                R.layout.wd_manager_small_button,smallContent);
//
//        btnSmallView = (ImageView) v.findViewById(R.id.ivSpeicial);
//        btnSmallView.setOnClickListener(this);
//        isSmallContent = true;
//
//        mWindowManager.addView(smallContent,mParams );
//        smallContent.setOnTouchListener(this);
//        btnSmallView.setOnTouchListener(this);
    }

    private void removeFocusView(MyViewGroup myViewGroup) {

        mParams.flags |=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.updateViewLayout(myViewGroup, mParams);
    }

    private void initBigView(){
        bigContent = new MyViewGroup(this);
        bigContent.setiCallback(new MyViewGroup.ICallback() {
            @Override
            public void callback() {
                removeFocusView(bigContent);

                mWindowManager.removeView(bigContent);
                mWindowManager.addView(smallContent,mParams);


            }
        });

        View v = View.inflate(this,R.layout.wd_manager_big_content,bigContent);




    }
    private void setFocusView(View myViewGroup) {
        mParams.flags &=~ WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManager.updateViewLayout(myViewGroup, mParams);
    }

    private void initLayoutParams(){

       mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        mParams.x =50;
        mParams.y = 100;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xView = mParams.x;
                yView = mParams.y;
                xFirstScreen = event.getRawX();
                yFirstScreen = event.getRawY();
                lastTime = System.currentTimeMillis();

                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getRawX()-xFirstScreen;
                float deltaY = event.getRawY()-yFirstScreen;
                mParams.x = (int)(xView +deltaX);
                mParams.y = (int)(yView +deltaY);
                mWindowManager.updateViewLayout(imageView, mParams);

                break;
            case MotionEvent.ACTION_UP:

                if (System.currentTimeMillis() - lastTime < TOUCH_TIME_THRESHOLD) {
//                    WindowManager.LayoutParams mWindowsParams = new WindowManager.LayoutParams(
//                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT//WindowManager.LayoutParams.WRAP_CONTENT,
//                            ,//WindowManager.LayoutParams.WRAP_CONTENT,
//                            //WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                            WindowManager.LayoutParams.TYPE_PHONE,
//                            //WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, // Not displaying keyboard on bg activity's EditText
//                            //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, //Not work with EditText on keyboard
//                            PixelFormat.TRANSLUCENT);
//                    mWindowManager.addView(bigContent,mWindowsParams);
//                    mWindowManager.removeView(smallContent);
//                    setFocusView(bigContent);
//                    edtLable.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(edtLable
//                            , InputMethodManager.SHOW_IMPLICIT);
                    mWindowManager.removeView(imageView);
                    mQuickWorkDialog.show();
                }
                

                break;

        }

        return true;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSpeicial:


                break;
            default:
                break;
        }
    }

    private class QuickWorkDialog extends Dialog implements View.OnClickListener {
        private Spinner spPenColor;
        private PenColorSmallAdapter mPenColorAdapter;
        private EditText edtBody;
        private EditText edtLable;
        private Button btnAddText;
        private List<String> listColor;
        private int currentColor;

        public QuickWorkDialog(Context context) {
            super(context);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            getWindow().setGravity(Gravity.CENTER|Gravity.TOP);
            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            setContentView(R.layout.wd_manager_big_content);


            this.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK){
                        mQuickWorkDialog.dismiss();
                        mWindowManager.addView(imageView,mParams);
                        isShowSmall = true;
                    }
                    return false;
                }
            });

            spPenColor = (Spinner) findViewById(R.id.spPenColorBW);
            edtBody = (EditText) findViewById(R.id.edtBodyBW);
            edtLable = (EditText) findViewById(R.id.edtLableBW);
            btnAddText = (Button) findViewById(R.id.btnAddTextNoteBW);
            btnAddText.setOnClickListener(this);

            initComponent();

        }

        private void initComponent(){
            listColor = Utils.initListColor();
            mPenColorAdapter = new PenColorSmallAdapter(getContext(),listColor);
            spPenColor.setAdapter(mPenColorAdapter);
            mPenColorAdapter.notifyDataSetChanged();


            spPenColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentColor = Color.parseColor(listColor.get(position));
                    edtBody.setTextColor(currentColor);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.btnAddTextNoteBW:
                    int result = saveNote();
                    if (-1 == result){
                        Toast.makeText(getContext(),"ERROR,Please Try Again !!!",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),"Successfully to Save Note ",Toast.LENGTH_LONG).show();
                        mQuickWorkDialog.dismiss();
                        mWindowManager.addView(imageView,mParams);
                    }
                    break;
                default:
                    break;

            }
        }

        private int  saveNote() {
            String lable = edtLable.getText().toString();
            String body = edtBody.getText().toString();
            int currColr = currentColor;
            int type = TYPE_TEXT;
            Note noteTmp = new Note(lable,body,type,currColr,"");
            return  DatabaseHelper.getINSTANCE(getContext()).insertData(noteTmp);

        }
    }

    private class StopServiceBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case STOP_SERVICE:
                    Log.d("aaaa","davaoroinhe");
                    Toast.makeText(context,"zzzzzzzzzzzzzzzzzzzzzzzzzz",Toast.LENGTH_LONG).show();

                    QuickWorkService.this.stopSelf();
                    stopForeground(true);
                    if (mQuickWorkDialog.isShowing()){
                        mQuickWorkDialog.dismiss();
                    }
                    if (isShowSmall){
                        mWindowManager.removeView(imageView);
                    }
                    break;
            }
        }
    }
}
