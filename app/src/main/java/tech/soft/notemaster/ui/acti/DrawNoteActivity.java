package tech.soft.notemaster.ui.acti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tech.soft.notemaster.R;
import tech.soft.notemaster.broadcast.AlarmReceiver;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.adapter.BGColorAdapter;
import tech.soft.notemaster.ui.adapter.PenColorAdapter;
import tech.soft.notemaster.ui.adapter.PenWidthAdapter;
import tech.soft.notemaster.ui.customview.AlramDialog;
import tech.soft.notemaster.ui.customview.DrawingView;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

import static tech.soft.notemaster.R.id.dvDraw;

/**
 * Created by dee on 12/04/2017.
 */

public class DrawNoteActivity extends BaseActivity implements View.OnClickListener, IConstand, AlramDialog.ISetupAlarm {
    private PenColorAdapter mPenColorAdapter;
    private PenWidthAdapter mPenWidthAdapter;
    private BGColorAdapter mBgColorAdapter;

    private Spinner spColorPen;
    private Spinner spWidthPen;
    private Spinner spColorBG;

    private ImageView ivBack;
    private TextView tvAddNote;
    private TextView tvUndo;
    private EditText edtLabel;
    private TextView tvArarm;

    private DrawingView dvDrawNote;
    private Bitmap mBitmap;
    private int curentColor;

    private boolean isEdit;
    private Note note;

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntentAlarm;
    private Calendar calendar;
    private boolean isVibrate;




    @Override
    protected void initComponents() {
        isVibrate = true;
        switch (getIntent().getAction()){
            case MainActivity.WRITE:
                isEdit = false;
                break;
            default:
                isEdit = true;
                note = (Note) getIntent().getSerializableExtra(MainActivity.DATA);
                break;
        }

        mPenColorAdapter = new PenColorAdapter(this, Utils.initListColor());

        mPenWidthAdapter = new PenWidthAdapter(this);
        mPenWidthAdapter.setItemPenWitdths(Utils.initWidthColor());

        mBgColorAdapter = new BGColorAdapter(this);
        mBgColorAdapter.setItemPenWitdths(Utils.initBGColor());



    }

    @Override
    protected void initViews() {
        dvDrawNote = (DrawingView) findViewById(dvDraw);

        spColorBG = (Spinner) findViewById(R.id.spPenColorBackGround);
        spColorPen = (Spinner) findViewById(R.id.ivPenColorDraw);
        spWidthPen = (Spinner) findViewById(R.id.spPenWidth);


        spColorPen.setAdapter(mPenColorAdapter);
        spWidthPen.setAdapter(mPenWidthAdapter);
        spColorBG.setAdapter(mBgColorAdapter);

        mPenColorAdapter.notifyDataSetChanged();
        mPenWidthAdapter.notifyDataSetChanged();
        mBgColorAdapter.notifyDataSetChanged();

        tvUndo = (TextView) findViewById(R.id.tvUndoDraw);
        tvAddNote = (TextView) findViewById(R.id.tvAddDraw);
        edtLabel = (EditText) findViewById(R.id.edtLabelDrawNote);
        ivBack = (ImageView) findViewById(R.id.ivbackDraw);
        tvArarm = (TextView) findViewById(R.id.tvAlarmDrawNote);
        Utils.setFontAnswesSomeTextView(tvArarm, this);
        tvArarm.setText("\uf017");


        setupDrawNote();

    }

    private void setupDrawNote() {
        spColorBG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dvDrawNote.setBackgroundColor(Color.
                        parseColor(Utils.initBGColor().
                                get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spWidthPen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dvDrawNote.setUpWidthPaint(Utils.initWidthColor().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spColorPen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dvDrawNote.setUpColorPain(Utils.initListColor().get(position));
                curentColor = Color.parseColor(Utils.initListColor().get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (isEdit){
            Log.d("asdasdasdddd","aaddda");
            Bitmap bitmap = Utils.stringToBitMap(note.getBody());
            dvDrawNote.setImageBitmap(bitmap);
            edtLabel.setText(note.getLabel());
        }
    }

    @Override
    protected void setEventViews() {
        tvUndo.setOnClickListener(this);
        tvAddNote.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvArarm.setOnClickListener(this);
    }

    @Override
    protected void setViewRoot() {
        setContentView(R.layout.activity_draw_note);
    }

    public Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( dvDrawNote.w,
                dvDrawNote
                        .h, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddDraw:
                if (!isEdit){
                    dvDrawNote.post(new Runnable() {
                        @Override
                        public void run() {
                            mBitmap = loadBitmapFromView(dvDrawNote);
                            String data = Utils.bitMapToString(mBitmap);
                            saveNote(data);
                            Toast.makeText(DrawNoteActivity.this,"Save Success !!",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(DrawNoteActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            DrawNoteActivity.this.finish();
                        }
                    });
                    dvDrawNote.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                        @Override
                        public void onGlobalFocusChanged(View oldFocus, View newFocus) {

                        }
                    });
                } else {
                    dvDrawNote.post(new Runnable() {
                        @Override
                        public void run() {
                            mBitmap = loadBitmapFromView(dvDrawNote);
                            String data = Utils.bitMapToString(mBitmap);
                            update(data);
                            Toast.makeText(DrawNoteActivity.this,
                                    "Save Success !!",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(DrawNoteActivity.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            DrawNoteActivity.this.finish();
                        }
                    });
                    dvDrawNote.getViewTreeObserver().
                            addOnGlobalFocusChangeListener(new ViewTreeObserver.
                                    OnGlobalFocusChangeListener() {
                        @Override
                        public void onGlobalFocusChanged(View oldFocus, View newFocus) {

                        }
                    });

                }
                break;
            case R.id.tvUndoDraw:
                dvDrawNote.undo();
                break;
            case R.id.ivbackDraw:
                Intent intent = new Intent(DrawNoteActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                DrawNoteActivity.this.finish();
                break;
            case R.id.tvAlarmDrawNote:
                AlramDialog dialog = new AlramDialog(this
                );
                dialog.setiSetupAlarm(this);
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void update(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lable = edtLabel.getText().toString();
        String body = data;
        int currColr = curentColor;
        int type = TYPE_HAND_DWRAW;
        String dateS = simpleDateFormat.format(new Date());
        Note noteTmp = new Note(note.getId(), lable, body, type, currColr, dateS, "", "", "", 1, 1);
        DatabaseHelper.getINSTANCE(this).updateData(noteTmp);
        startAlarm(dateS);
    }

    private void saveNote(String data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lable = edtLabel.getText().toString();
        String body = data;
        int currColr = curentColor;
        int type = TYPE_HAND_DWRAW;
        String dateS = simpleDateFormat.format(new Date());
        Note noteTmp = new Note(lable, body, type, currColr, dateS, "", "", "", 1, 1);
        DatabaseHelper.getINSTANCE(this).insertData(noteTmp);
        startAlarm(dateS);
    }

    private void startAlarm(String dateS) {
        if (null != mAlarmManager) {
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.setAction("hahaha");
            intent.putExtra("VIBRATE", isVibrate);
            intent.putExtra("DATE_S", dateS);
            mPendingIntentAlarm = PendingIntent.
                    getBroadcast(this, 0, intent, 0);
            Log.d("asdasd", "qqsadasldasasdas");
            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), mPendingIntentAlarm);

        }
    }

    @Override
    public void setupAlarm(int minute, int hour, int day, int month, int year, boolean isVibrate) {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        this.isVibrate = isVibrate;
        Log.d("asdasd", "q11qsadasldasasdas");
    }
}
