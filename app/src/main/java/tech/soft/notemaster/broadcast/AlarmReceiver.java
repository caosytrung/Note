package tech.soft.notemaster.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.service.AlarmService;

/**
 * Created by dee on 24/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String date = intent.getStringExtra("DATE_S");
        boolean isVibrate = intent.getBooleanExtra("VIBRATE", true);
        if (isVibrate) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 1000, 1000, 1000, 1000, 1000, 1000};
            Log.d("Adava", "vaarrr");
            v.vibrate(pattern, -1);
        }
        Note note = DatabaseHelper.getINSTANCE(context).getNoteFromDate(date);
        if (null == note) {
            return;
        }
        Intent intent1 = new Intent(context, AlarmService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("NOTE", note);
        intent1.putExtras(bundle);
        context.startService(intent1);

    }
}
