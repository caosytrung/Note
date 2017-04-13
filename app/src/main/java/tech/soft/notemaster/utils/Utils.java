package tech.soft.notemaster.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dee on 24/03/2017.
 */

public class Utils {

    public static void setFontAnswesSomeTextView(TextView tv, Context context){
        Typeface tp = Typeface.createFromAsset(context.getAssets(),"fontawesome-webfont.ttf");
        tv.setTypeface(tp);
    }

    public static List<String> initListColor(){
        List<String> listColor = new ArrayList<>();
        listColor.add("#000000");
        listColor.add("#F44336");
        listColor.add("#9C27B0");
        listColor.add("#673AB7");
        listColor.add("#2196F3");
        listColor.add("#8BC34A");
        listColor.add("#CDDC39");
        listColor.add("#FF9800");
        listColor.add("#FFEB3B");

        return listColor;

    }

    public static List<String> initBGColor(){
        List<String> listColor = new ArrayList<>();
        listColor.add("#FFEB3B");
        listColor.add("#000000");
        listColor.add("#F44336");
        listColor.add("#9C27B0");
        listColor.add("#673AB7");
        listColor.add("#2196F3");
        listColor.add("#8BC34A");
        listColor.add("#CDDC39");
        listColor.add("#FF9800");
        return listColor;

    }
    public static List<Integer> initWidthColor(){
        List<Integer> listColor = new ArrayList<>();
        listColor.add(4);
        listColor.add(8);
        listColor.add(16);
        return listColor;

    }
    public static String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public static Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }



    public static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-mm-dd hh:mm:ss");

        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
    public static boolean checkPermissioinGoogleMap(Activity act, int requestCode) {
        // tra ve true thi permission can check duoc kich hoat
        // tra ve false thi permission can check chua duoc kich hoat
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        // danh cho 23 tro len
        List<String> permissionRequestS = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(act,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            permissionRequestS.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }



        if (permissionRequestS.size() == 0) {
            return true;
        } else {
            String per[] = new String[permissionRequestS.size()];
            for (int i = 0; i < permissionRequestS.size(); i++) {
                per[i] = permissionRequestS.get(i);
            }
            //show dialog
            ActivityCompat.requestPermissions(act, per, requestCode);
            return false;
        }


    }
}
