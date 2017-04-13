package tech.soft.notemaster.ui.acti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import tech.soft.notemaster.R;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.adapter.BGColorAdapter;
import tech.soft.notemaster.ui.adapter.PenColorAdapter;
import tech.soft.notemaster.ui.adapter.PenWidthAdapter;
import tech.soft.notemaster.ui.customview.DrawingView;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

import static android.R.attr.bitmap;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.type;
import static android.R.string.no;
import static android.os.Build.VERSION_CODES.N;
import static tech.soft.notemaster.R.id.dvDraw;
import static tech.soft.notemaster.R.id.tvAddDraw;
import static tech.soft.notemaster.utils.Utils.bitMapToString;

/**
 * Created by dee on 12/04/2017.
 */

public class DrawNoteActivity extends BaseActivity implements View.OnClickListener ,IConstand{
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

    private DrawingView dvDrawNote;
    private Bitmap mBitmap;
    private int curentColor;

    private boolean isEdit;
    private Note note;




    @Override
    protected void initComponents() {
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
            default:
                break;
        }
    }

    private void update(String data) {
        String lable = edtLabel.getText().toString();
        String body = data;
        int currColr = curentColor;
        int type = TYPE_HAND_DWRAW;
        Note noteTmp = new Note(note.getId(),lable,body,type,currColr,"");
        DatabaseHelper.getINSTANCE(this).updateData(noteTmp);
    }

    private void saveNote(String data) {

        String lable = edtLabel.getText().toString();
        String body = data;
        int currColr = curentColor;
        int type = TYPE_HAND_DWRAW;
        Note noteTmp = new Note(lable,body,type,currColr,"");
        DatabaseHelper.getINSTANCE(this).insertData(noteTmp);
    }
}
