package com.example.administrator.testguid.view.parallax;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Liyeyu on 2016/7/4.
 */
public class ParallaxFragment extends Fragment {

    private ParallaxLayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id = getArguments().getInt("id");
        mInflater = new ParallaxLayoutInflater(inflater,getActivity());
        View inflate = mInflater.inflate(id, container,false);
        return inflate;
    }

    public List<View> getViews() {
        if(mInflater==null){
            return null;
        }
        return mInflater.getViews();
    }
}
