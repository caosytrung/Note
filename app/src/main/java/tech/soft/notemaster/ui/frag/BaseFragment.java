package tech.soft.notemaster.ui.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mam  Mam on 12/12/2016.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getViews(inflater,container);
        initComponents();
        initViews(view);
        setEventViews();
        return view;
    }
    public abstract View getViews(LayoutInflater inflater,ViewGroup viewGroup);
    public abstract void initViews(View view);
    public abstract void initComponents();
    public abstract void setEventViews();

}
