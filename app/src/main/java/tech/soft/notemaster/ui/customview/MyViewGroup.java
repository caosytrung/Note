package tech.soft.notemaster.ui.customview;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by dee on 05/04/2017.
 */

public class MyViewGroup extends RelativeLayout {
    private ICallback iCallback;
    public MyViewGroup(Context context) {
        super(context);
    }

    public void setiCallback(ICallback iCallback) {
        this.iCallback = iCallback;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Log.d("asdsa","asdadad");
            iCallback.callback();
        }
        return super.dispatchKeyEvent(event);
    }

    public interface ICallback{
        public void callback();
    }
}
