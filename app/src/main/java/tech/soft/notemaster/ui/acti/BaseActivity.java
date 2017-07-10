package tech.soft.notemaster.ui.acti;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Mam  Mam on 12/11/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewRoot();
        initComponents();
        initViews();
        setEventViews();
    }
    protected abstract void initComponents();
    protected abstract void initViews();
    protected abstract void setEventViews();
    protected abstract void setViewRoot();

}
