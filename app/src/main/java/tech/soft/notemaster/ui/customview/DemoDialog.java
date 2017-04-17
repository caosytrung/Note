package tech.soft.notemaster.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import tech.soft.notemaster.R;

/**
 * Created by dee on 15/04/2017.
 */

public class DemoDialog extends Dialog {

    public DemoDialog(Context context) {
        super(context);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

        setContentView(R.layout.dialog);
        TextView textView = (TextView) findViewById(R.id.tvDemo);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "DKMMMMMM", Toast.LENGTH_LONG).show();
            }
        });
    }

    public DemoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
}
