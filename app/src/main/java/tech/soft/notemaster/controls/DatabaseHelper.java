package tech.soft.notemaster.controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.utils.IConstand;
import tech.soft.notemaster.utils.Utils;

import static android.R.attr.id;

/**
 * Created by dee on 03/04/2017.
 */

public class DatabaseHelper implements IConstand {

    private static final String TAG = "mDatabaseHelper";
    private static final String DB_NAME = "NoteDb.sqlite";
    private static final String TABLE_NAME = "note";
    private static final String DB_PATH_SUFF = "/databases/" ;

    public static  final  String ID_ROW = "id";
    public static  final  String LABLE_ROW = "label";
    public static  final  String BODY_ROW = "body";
    public static  final  String TYPE_ROW = "type";
    public static  final  String TEXT_COLOR__ROW = "textColor";
    public static  final  String DATE_ROW = "dateCreate";
    public static  final  String IMAGE_S_ROW = "imageS";

    private SimpleDateFormat dateFormat ;
    private Context mContext;
    private SQLiteDatabase mSqLiteDatabase;



    public static DatabaseHelper INSTANCE;

    private DatabaseHelper(Context context){
        mContext = context;
    }

    public static synchronized DatabaseHelper getINSTANCE(Context context){
        if (null == INSTANCE){
            INSTANCE = new DatabaseHelper(context);
        }
         return INSTANCE;
    }

    public void openDatabase(){
        mSqLiteDatabase = mContext.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
    }

    public boolean deleteRow(int id){
        openDatabase();
        return  mSqLiteDatabase.delete(TABLE_NAME,"id = '" + id + "'",null) > 0;

    }
    public int deleteAll(){
        openDatabase();
        return mSqLiteDatabase.delete(TABLE_NAME,null,null);
    }

    public List<Note> getListNote(){
        openDatabase();
        List<Note> noteList = new ArrayList<>();

        String query = "SELECT * FROM  note";
        Cursor c = mSqLiteDatabase.rawQuery(query,null);

        int indexId = c.getColumnIndex(ID_ROW);
        int indexLable = c.getColumnIndex(LABLE_ROW);
        int indexBody = c.getColumnIndex(BODY_ROW);
        int indexType = c.getColumnIndex(TYPE_ROW);
        int indextTextColor= c.getColumnIndex(TEXT_COLOR__ROW);
        int indexDate = c.getColumnIndex(DATE_ROW);
        int indexImageS = c.getColumnIndex(IMAGE_S_ROW);

        c.moveToFirst();

        while (!c.isAfterLast()){
            String body = c.getString(indexBody);
            String lable = c.getString(indexLable);
            int id = c.getInt(indexId);
            int type = c.getInt(indexType);
            int textColor = c.getInt(indextTextColor);
            Date date = new Date(c.getLong(indexDate));
            String imageS= c.getString(indexImageS);

            Note tmp = new Note(id,lable,body,type,textColor,imageS,date);
            noteList.add(tmp);
            Log.d(TAG,tmp.getImageS());

            c.moveToNext();
        }
        c.close();
        closeDatabase();

        return noteList;
    }

    public int insertData(Note note){
        ContentValues contentValues  = new ContentValues();
        contentValues.put(LABLE_ROW,note.getLabel());
        contentValues.put(BODY_ROW,note.getBody());
        contentValues.put(TYPE_ROW,note.getType());
        contentValues.put(TEXT_COLOR__ROW,note.getTextColor());
        contentValues.put(DATE_ROW, Utils.currentDate());
        contentValues.put(IMAGE_S_ROW,note.getImageS());
        Log.d(TAG,note.getImageS());

        openDatabase();
        int a = (int) mSqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        Log.d(TAG,a+"");
        closeDatabase();
        return a;

    }

    public boolean updateData(Note note){
        openDatabase();

        ContentValues contentValues  = new ContentValues();
        contentValues.put(LABLE_ROW,note.getLabel());
        contentValues.put(BODY_ROW,note.getBody());
        contentValues.put(TYPE_ROW,note.getType());
        contentValues.put(TEXT_COLOR__ROW,note.getTextColor());
        contentValues.put(DATE_ROW, Utils.currentDate());
        contentValues.put(IMAGE_S_ROW,note.getImageS());

        return mSqLiteDatabase.update(TABLE_NAME,contentValues,"id = '" + note.getId() + "'",null) > 0;

    }

    public void closeDatabase(){
        mSqLiteDatabase.close();
    }

    public void copyNoteDatabaseToSystem(){

        File dbFile = mContext.getDatabasePath(DB_NAME);
        if (!dbFile.exists()){
            InputStream inS = null;
            OutputStream outS = null;

            try {
                inS = mContext.getAssets().open(DB_NAME);
                String outPath  = getPathSystem();
                File fileSys =  new File(mContext.getApplicationInfo().dataDir + DB_PATH_SUFF);
                if (!fileSys.exists()){
                    fileSys.mkdir();
                }

                outS =new FileOutputStream(outPath);
                byte[] bytes = new byte[1024];


                int tmp = 0;

                while ((tmp = inS.read(bytes))  > 0){
                    outS.write(bytes,0,tmp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private String getPathSystem(){

        return mContext.getApplicationInfo().dataDir  +
                DB_PATH_SUFF + DB_NAME;
    }
}