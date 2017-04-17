package tech.soft.notemaster.ui.acti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.core.deps.guava.reflect.TypeToken;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.models.sp_view.BackgroundS;
import tech.soft.notemaster.models.sp_view.ImageS;
import tech.soft.notemaster.ui.adapter.PenColorAdapter;
import tech.soft.notemaster.ui.calback.ISetTextNote;
import tech.soft.notemaster.ui.customview.ColorDialog;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

/**
 * Created by dee on 23/03/2017.
 */

public class TextNoteActivity extends BaseActivity implements View.OnClickListener,IConstand {
    public static final String TAG = "mTextNoteActivity";
    private static final int RQ_GALL = 1;

    private List<String> listColor;
    private List<Integer> listWord;
    private EditText edtLabel;
    private EditText edtBody;
    private ImageView ivMenu;
    private FloatingActionButton btnAdd;
    private Spinner spPenColor;
    private PenColorAdapter mPenColorAdapter;
    private int currentColor;
    private Spannable mSpannableText;
    public Note note;
    private boolean isEdit;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



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

        listColor = Utils.initListColor();
        mPenColorAdapter = new PenColorAdapter(this, listColor);
        currentColor = getResources().getColor(R.color.black);
        mSpannableText = new SpannableString("");
        listWord = new ArrayList<>();


    }

    @Override
    protected void initViews() {
        spPenColor = (Spinner) findViewById(R.id.spPenColor);
        edtLabel = (EditText) findViewById(R.id.edtLabelText);
        edtBody = (EditText) findViewById(R.id.edtBobyTextNote);
        ivMenu = (ImageView) findViewById(R.id.ivMenuText);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAddTextNote);
        ivMenu.setOnClickListener(this);

        btnAdd.setOnClickListener(this);

        spPenColor.setAdapter(mPenColorAdapter);
        edtBody.setTextColor(currentColor);
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
        if (isEdit){
            edtLabel.setText(note.getLabel());
            SpannableStringBuilder builder =
                    new SpannableStringBuilder(note.getBody());

             Type type1 = new TypeToken<ArrayList<ImageS>>() {}.getType();
             ArrayList<ImageS> finalOutputString = new Gson().fromJson(note.getImageS(), type1);

            Type type2 = new TypeToken<ArrayList<BackgroundS>>() {
            }.getType();
            ArrayList<BackgroundS> outBackground = new Gson().fromJson(note.getBackgroundS(), type2);

            Type type3 = new TypeToken<ArrayList<BackgroundS>>() {
            }.getType();
            ArrayList<BackgroundS> outForeGround = new Gson().fromJson(note.getMutilColor(), type3);

            if (null == outForeGround || outForeGround.size() == 0) {

                Log.d(TAG, "MEKIPP");
            } else {


                for (BackgroundS item : outForeGround) {

                    builder.setSpan(new ForegroundColorSpan(item.getColor()), item.getStart(),
                            item.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


            }

            if (null == outBackground || outBackground.size() == 0) {

                Log.d(TAG, "MEKIPP");
            } else {


                for (BackgroundS item : outBackground) {

                    builder.setSpan(new BackgroundColorSpan(item.getColor()), item.getStart(),
                            item.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


            }

            if (null == finalOutputString){

            }
            else  if (finalOutputString.size() == 0){

            } else {


                for (ImageS item: finalOutputString){
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.
                                Media.getBitmap(this.getContentResolver(), Uri.parse(item.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    builder.setSpan(new ImageSpan(getResizedBitmap(bitmap,800,800)),
                            item.getS(),item.getE(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

            }

            edtBody.setText(builder);


            for (int i = 0 ; i < Utils.initListColor().size() ; i ++){
                if (Color.parseColor(Utils.initListColor().get(i)) == note.getTextColor() ){
                    spPenColor.setSelection(i);
                    break;
                }
            }

        }

        edtBody.setCustomSelectionActionModeCallback(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add("H_L");
                menu.add("Color");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getTitle().equals("H_L")) {
                    final int start = edtBody.getSelectionStart();
                    final int end = edtBody.getSelectionEnd();
                    String selectStr = edtBody.getText().
                            toString().substring(start, end);
                    ColorDialog dialog = new ColorDialog(TextNoteActivity.this,
                            android.R.style.Theme_Translucent, 1, start, end
                    );
                    dialog.setiSetTextNote(new ISetTextNote() {
                        @Override
                        public void setText(int color, int s, int e) {
                            setUpHighLightText(start, end, color);
                        }
                    });
                    dialog.show();
                    mode.finish();

                } else if (item.getTitle().equals("Color")) {
                    final int start = edtBody.getSelectionStart();
                    final int end = edtBody.getSelectionEnd();

                    ColorDialog dialog = new ColorDialog(TextNoteActivity.this,
                            android.R.style.Theme_Translucent, 2, start, end
                    );
                    dialog.setiSetTextNote(new ISetTextNote() {
                        @Override
                        public void setText(int color, int s, int e) {
                            setUpColorText(start, end, color);
                        }
                    });
                    dialog.show();
                    mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


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

    private void setUpMenu(){
        PopupMenu popupMenu = new PopupMenu(this,ivMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_note,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.insert_image:
                        selectImage();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();

    }

    private void selectImage() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");
        startActivityForResult(intentGallery, RQ_GALL);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleHeight, scaleHeight);

        // "RECREATE" THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RQ_GALL:
                if (resultCode == RESULT_OK){
                    Uri uriImage  = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImage);
                        ImageSpan imageSpan = new ImageSpan(bitmap);
                        bitmap = getResizedBitmap(bitmap,800,800);
                        int selectionCursor = edtBody.getSelectionStart();
                        TextPaint paint = edtBody.getPaint();
                        int wordwidth=(int)paint.measureText("m",0,1);
                        int screenwidth = getResources().getDisplayMetrics().widthPixels;
                        int num = screenwidth/wordwidth;
                        String uriString = uriImage.toString();
                        Log.d(TAG,"Sizeurii" +  uriString.length() + "   " + num);
                        String a = uriImage.toString();

                        SpannableStringBuilder builder = new SpannableStringBuilder(edtBody.getText());

                        builder.insert(selectionCursor,"\n" + a + "\n");
                        builder.setSpan(new ImageSpan(bitmap),
                                selectionCursor + 1 ,selectionCursor + 1 + a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );


                        edtBody.setText(builder);
                        edtBody.setSelection(selectionCursor + a.length() + 2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void setEventViews() {

    }

    @Override
    protected void setViewRoot() {
        setContentView(R.layout.
                activity_text_note);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddTextNote:

                if (!isEdit){
                    Log.d("zaooo","vaoed");
                    saveNote();
                    Toast.makeText(this,"Save Success !!",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                } else {
                    update();


                    Intent intent = new Intent(this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                }


                break;
            case R.id.ivMenuText:
                Log.d(TAG,"ivMenuText");
                setUpMenu();
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

    private void saveNote() {
        Editable editable = edtBody.getText();
        SpannableString spannableString = new SpannableString(editable);
        String html = Html.toHtml(spannableString);
        SpannableString spanned = new SpannableString(Html.fromHtml(html));

        List<ImageS> imageS = new ArrayList<>();
        ImageSpan[] imageSpen = spannableString.getSpans(0,edtBody.length(),ImageSpan.class);
        for (ImageSpan imageSpan : imageSpen){
            int s = edtBody.getText().getSpanStart(imageSpan);
            int e = edtBody.getText().getSpanEnd(imageSpan);
            String uri = edtBody.getText().toString().substring(s,e);
            imageS.add(new ImageS(s,e,uri));
        }

        Gson gson = new Gson();
        String inputString= gson.toJson(imageS);


        System.out.println("inputString= " + inputString);
        Log.d("aaaaaaaaaaaaaaaaaaa",inputString);

        String lable = edtLabel.getText().toString();
        String body = edtBody.getText().toString();
        int currColr = currentColor;
        int type = TYPE_TEXT;
        Note noteTmp = new Note(lable, body, type, currColr, inputString, getListBackgroundS(), getListColorS());
        DatabaseHelper.getINSTANCE(this).insertData(noteTmp);

    }
    private void update() {
        Editable editable = edtBody.getText();
        SpannableString spannableString = new SpannableString(editable);
        String html = Html.toHtml(spannableString);
        SpannableString spanned = new SpannableString(Html.fromHtml(html));
        List<ImageS> imageS = new ArrayList<>();
        ImageSpan[] imageSpen = spannableString.getSpans(0,edtBody.length(),ImageSpan.class);
        for (ImageSpan imageSpan : imageSpen){
            int s = edtBody.getText().getSpanStart(imageSpan);
            int e = edtBody.getText().getSpanEnd(imageSpan);
            String uri = edtBody.getText().toString().substring(s,e);
            imageS.add(new ImageS(s,e,uri));
        }
        Gson gson = new Gson();

        String inputString= gson.toJson(imageS);


        System.out.println("inputString= " + inputString);
        Log.d("aaaaaaaaaaaaaaaaaaa",inputString);

        String lable = edtLabel.getText().toString();
        String body = edtBody.getText().toString();
        int currColr = currentColor;
        int type = TYPE_TEXT;
        Note noteTmp = new Note(note.getId(), lable, body,
                type, currColr, inputString, getListBackgroundS(), getListColorS());

        if (DatabaseHelper.getINSTANCE(this).updateData(noteTmp)){
            Toast.makeText(this,"Update Success !!",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"Update Failer  !!",Toast.LENGTH_LONG).show();
        }

    }
}
