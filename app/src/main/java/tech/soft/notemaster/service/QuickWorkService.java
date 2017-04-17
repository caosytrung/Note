package tech.soft.notemaster.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.models.sp_view.BackgroundS;
import tech.soft.notemaster.ui.adapter.PenColorAdapter;
import tech.soft.notemaster.ui.customview.MyViewGroup;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by dee on 04/04/2017.
 */

public class QuickWorkService extends Service implements View.OnClickListener, IConstand {
    private static final int TOUCH_TIME_THRESHOLD = 150;
    private static final String STOP_SERVICE ="STOP_SERVICE" ;
    private long lastTime;
    private MyViewGroup bigContent;
    private MyViewGroup smallContent;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private float xView, yView, xFirstScreen, yFirstScreen;
    private boolean isBigContent;

    private EditText edtLable;
    private EditText edtBody;
    private Spinner spTextColor;
    private int currentColor;
    private List<String> listColor;
    private PenColorAdapter mPenColorAdapter;

    private NotificationCompat notificationCompat;
    private PendingIntent pendingIntentStopService;
    private StopServiceBroadcast mStopServiceBroadcast;
    private boolean isShowSmall;
    private Button btnAdd;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        createNotification();
        mStopServiceBroadcast = new StopServiceBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_SERVICE);
        registerReceiver(mStopServiceBroadcast,filter);

        //  mQuickWorkDialog.setCancelable(false);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        initLayoutParams();
        initSmallView();
        initBigView();
        return START_NOT_STICKY ;
    }

    private void createNotification(){
        Intent intentStop = new Intent();
        intentStop.setAction(STOP_SERVICE);
        pendingIntentStopService = PendingIntent.
                getBroadcast(this, 1, intentStop, PendingIntent.FLAG_UPDATE_CURRENT);
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
                            isBigContent = true;
                            mWindowManager.addView(bigContent, mParams);
                            setFocusView(bigContent);
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
                //  mWindowManager.addView(imageView,mParams);
                mWindowManager.removeView(bigContent);
                isBigContent = false;

            }
        });

        View v = View.inflate(this,R.layout.wd_manager_big_content,bigContent);
        btnAdd = (Button) v.findViewById(R.id.btnAddTextNoteBW);
        btnAdd.setOnClickListener(this);
        edtLable = (EditText) v.findViewById(R.id.edtLableBW);
        edtBody = (EditText) v.findViewById(R.id.edtBodyBW);
        listColor = Utils.initListColor();
        spTextColor = (Spinner) v.findViewById(R.id.spPenColorBW);
        mPenColorAdapter = new PenColorAdapter(this, listColor);
        spTextColor.setAdapter(mPenColorAdapter);
        spTextColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentColor = Color.parseColor(listColor.get(position));
                edtBody.setTextColor(currentColor);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        edtBody.setCustomSelectionActionModeCallback(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                menu.add("H_L");
//                menu.add("Color");
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                if (item.getTitle().equals("H_L")){
//                    final int  start = edtBody.getSelectionStart();
//                    final int  end = edtBody.getSelectionEnd();
//                    String selectStr = edtBody.getText().
//                            toString().substring(start,end);
//                    ColorDialog dialog = new ColorDialog(QuickWorkService.this,
//                            android.R.style.Theme_Translucent,1,start,end
//                    );
//                    dialog.setiSetTextNote(new ISetTextNote() {
//                        @Override
//                        public void setText(int color, int s, int e) {
//                            setUpHighLightText(start,end, color);
//                        }
//                    });
//                    dialog.show();
//                    mode.finish();
//
//                } else if (item.getTitle().equals("Color")){
//                    final int  start = edtBody.getSelectionStart();
//                    final int  end = edtBody.getSelectionEnd();
//
//                    ColorDialog dialog = new ColorDialog(QuickWorkService.this,
//                            android.R.style.Theme_Translucent,2,start,end
//                    );
//                    dialog.setiSetTextNote(new ISetTextNote() {
//                        @Override
//                        public void setText(int color, int s, int e) {
//                            setUpColorText(start,end, color);
//                        }
//                    });
//                    dialog.show();
//                    mode.finish();
//                }
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });
    }

    private void setUpHighLightText(int start, int end, int color) {
        String selected = edtBody.getText().toString().substring(start, end);
        SpannableStringBuilder builder = new SpannableStringBuilder(edtBody.getText());
        builder.setSpan(new BackgroundColorSpan(color),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        edtBody.setText(builder);
    }

    private void setUpColorText(int start, int end, int color) {
        String selected = edtBody.getText().toString().substring(start, end);
        SpannableStringBuilder builder = new SpannableStringBuilder(edtBody.getText());
        builder.setSpan(new ForegroundColorSpan(color),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        edtBody.setText(builder);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSpeicial:

                break;
            case R.id.btnAddTextNoteBW:
                int result = saveNote();
                edtBody.setText("");
                edtLable.setText("");
                if (-1 == result) {
                    Toast.makeText(QuickWorkService.this,
                            "ERROR,Please Try Again !!!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuickWorkService.this,
                            "Successfully to Save Note ",
                            Toast.LENGTH_LONG).show();
                    mWindowManager.removeView(bigContent);
                    isBigContent = false;
                }
                break;
            default:
                break;
        }
    }

    private String getListBackgroundS() {
        SpannableString spannableString = new SpannableString(edtBody.getText());
        List<BackgroundS> backgroundSes = new ArrayList<>();
        BackgroundColorSpan[] backgroundColorSpen =
                spannableString.getSpans(0, edtBody.length(), BackgroundColorSpan.class);
        for (BackgroundColorSpan item : backgroundColorSpen) {
            int s = edtBody.getText().getSpanStart(item);
            int e = edtBody.getText().getSpanEnd(item);
            backgroundSes.add(new BackgroundS(s, e, item.getBackgroundColor()));
        }
        Gson gson = new Gson();
        String inputString = gson.toJson(backgroundSes);
        Log.d(TAG, "inootSTRing " + inputString);
        return inputString;
    }

    private String getListColorS() {
        SpannableString spannableString = new SpannableString(edtBody.getText());
        List<BackgroundS> backgroundSes = new ArrayList<>();
        ForegroundColorSpan[] foregroundColorSpen =
                spannableString.getSpans(0, edtBody.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan item : foregroundColorSpen) {
            int s = edtBody.getText().getSpanStart(item);
            int e = edtBody.getText().getSpanEnd(item);
            backgroundSes.add(new BackgroundS(s, e, item.getForegroundColor()));
        }
        Gson gson = new Gson();
        String inputString = gson.toJson(backgroundSes);
        Log.d(TAG, "inootSTRing " + inputString);
        return inputString;
    }

    private int saveNote() {
            isShowSmall = true;
            String lable = edtLable.getText().toString();
            String body = edtBody.getText().toString();
            int type = TYPE_TEXT;
        Note noteTmp = new Note(lable, body, type, currentColor, "", getListBackgroundS(), getListColorS());
        return DatabaseHelper.getINSTANCE(this).insertData(noteTmp);

    }


//    private class QuickWorkDialog extends Dialog implements View.OnClickListener {
//        private Spinner spPenColor;
//        private PenColorSmallAdapter mPenColorAdapter;
//        private EditText edtBody;
//        private EditText edtLable;
//        private Button btnAddText;
//        private List<String> listColor;
//        private int currentColor;
//
//        private void setUpHighLightText(int start, int end,int color) {
//            String selected =  edtBody.getText().toString().substring(start,end);
//            SpannableStringBuilder builder = new
//                    SpannableStringBuilder(edtBody.getText());
//            builder.setSpan(new BackgroundColorSpan(color),
//                    start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            edtBody.setText(builder);
//        }
//        private void setUpColorText(int start, int end,int color) {
//            String selected =  edtBody.getText().toString().substring(start,end);
//            SpannableStringBuilder builder = new SpannableStringBuilder(edtBody.getText());
//            builder.setSpan(new ForegroundColorSpan(color),
//                    start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            edtBody.setText(builder);
//        }
//
//        public QuickWorkDialog(Context context) {
//            super(context);
//            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            getWindow().setGravity(Gravity.CENTER|Gravity.TOP);
//            getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT);
//            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//            setContentView(R.layout.wd_manager_big_content);
//
//
//            this.setOnKeyListener(new OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog,
//                                     int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK){
//                        mQuickWorkDialog.dismiss();
//                        mWindowManager.addView(imageView,mParams);
//                        isShowSmall = true;
//                    }
//                    return false;
//                }
//            });
//
//            spPenColor = (Spinner) findViewById(R.id.spPenColorBW);
//            edtBody = (EditText) findViewById(R.id.edtBodyBW);
//            edtLable = (EditText) findViewById(R.id.edtLableBW);
//            btnAddText = (Button) findViewById(R.id.btnAddTextNoteBW);
//            btnAddText.setOnClickListener(this);
//
//            edtBody.setCustomSelectionActionModeCallback(new AbsListView.MultiChoiceModeListener() {
//                @Override
//                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//
//                }
//
//                @Override
//                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                    menu.add("HL");
//                    return true;
//                }
//
//                @Override
//                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//
//                    return false;
//                }
//
//                @Override
//                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                    if (item.getTitle().equals("HL")){
//                        mWindowManager.addView(bigContent,mParams);
//                    }
//                    return false;
//                }
//
//                @Override
//                public void onDestroyActionMode(ActionMode mode) {
//
//                }
//            });
//            initComponent();
//
//        }
//
//        private void initComponent(){
//            listColor = Utils.initListColor();
//            mPenColorAdapter = new PenColorSmallAdapter(getContext(),listColor);
//            spPenColor.setAdapter(mPenColorAdapter);
//            mPenColorAdapter.notifyDataSetChanged();
//
//
//            spPenColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    currentColor = Color.parseColor(listColor.get(position));
//                    edtBody.setTextColor(currentColor);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case  R.id.btnAddTextNoteBW:
//                    int result = saveNote();
//                    edtBody.setText("");
//                    edtLable.setText("");
//                    if (-1 == result){
//                        Toast.makeText(getContext(),"ERROR,Please Try Again !!!",Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getContext(),"Successfully to Save Note ",Toast.LENGTH_LONG).show();
//                        mQuickWorkDialog.dismiss();
//                        mWindowManager.addView(imageView,mParams);
//                    }
//
//                    break;
//                default:
//                    break;
//
//            }
//        }
//
//
//
//        private int  saveNote() {
//            isShowSmall = true;
//            String lable = edtLable.getText().toString();
//            String body = edtBody.getText().toString();
//            int currColr = currentColor;
//            int type = TYPE_TEXT;
//            Note noteTmp = new Note(lable,body,type,currColr,"","","");
//            return  DatabaseHelper.getINSTANCE(getContext()).insertData(noteTmp);
//
//        }
//    }

    private class StopServiceBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case STOP_SERVICE:
                    Log.d("aaaa","davaoroinhe");
                    Toast.makeText(context,"zzzzzzzzzzzzzzzzzzzzzzzzzz",Toast.LENGTH_LONG).show();

                    QuickWorkService.this.stopSelf();
                    stopForeground(true);
                    mWindowManager.removeView(imageView);
                    if (isBigContent) {
                        mWindowManager.removeView(bigContent);
                    }
                    break;
            }
        }
    }
}
