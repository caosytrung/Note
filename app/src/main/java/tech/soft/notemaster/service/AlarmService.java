package tech.soft.notemaster.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import tech.soft.notemaster.R;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.ui.acti.TextNoteActivity;

import static tech.soft.notemaster.ui.acti.MainActivity.DATA;
import static tech.soft.notemaster.ui.acti.MainActivity.EDIT_NOTE;

/**
 * Created by dee on 24/04/2017.
 */

public class AlarmService extends Service {
    private PendingIntent pendingIntentOpenNote;
    private Note mNote;
    private OpenNoteReiceiver mOpenNoteReiceiver;

    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {

        Bundle bundle = intent.getExtras();
        mNote = (Note) bundle.getSerializable("NOTE");
        createNotification();
        mOpenNoteReiceiver = new OpenNoteReiceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("OPEN_NOTE");
        registerReceiver(mOpenNoteReiceiver, filter);
        return START_NOT_STICKY;

    }

    private void createNotification() {
        Intent intentOpen = new Intent();
        intentOpen.setAction("OPEN_NOTE");
        pendingIntentOpenNote = PendingIntent.
                getBroadcast(this, 1, intentOpen,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_pencil);

        builder.setContentTitle(mNote.getLabel());
        builder.setContentText(mNote.getBody());
        builder.setContentIntent(pendingIntentOpenNote);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class OpenNoteReiceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "OPEN_NOTE":
                    Log.d("aaaa", "davaoroinhe");
                    Toast.makeText(context, "zzz", Toast.LENGTH_LONG).show();
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(1);
                    Intent intentTextNote = new Intent(context, TextNoteActivity.class);
                    intentTextNote.setAction(EDIT_NOTE);
                    intentTextNote.putExtra(DATA, mNote);
                    intentTextNote.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentTextNote);
//                    QuickWorkService.this.stopSelf();
//                    stopForeground(true);
//                    mWindowManager.removeView(imageView);
//                    if (isBigContent) {
//                        mWindowManager.removeView(bigContent);
//                    }
                    break;
            }
        }
    }
}
