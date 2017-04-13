package tech.soft.notemaster.ui.acti;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import tech.soft.notemaster.R;
import tech.soft.notemaster.utils.Utils;

/**
 * Created by dee on 13/04/2017.
 */

public class StartActivity extends BaseActivity {
    @Override
    protected void initComponents() {

        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            } else {
                if (Utils.checkPermissioinGoogleMap(this,1)){
                    startMainActivity();
                    finish();
                }
            }
        } else {

            startMainActivity();
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!Settings.canDrawOverlays(this)){
            finish();
        } else {
            if (Utils.checkPermissioinGoogleMap(this,1)){
                startMainActivity();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for ( int i = 0; i < grantResults.length; i++ ) {
            if ( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                finish();
                return;
            }
        }
        startMainActivity();
        finish();
    }

    private void startMainActivity(){
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setEventViews() {

    }

    @Override
    protected void setViewRoot() {
        setContentView(R.layout.activity_start);
    }
}
