package com.example.administrator.testguid.view.parallax;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.testguid.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liyeyu on 2016/7/4.
 */
public class ParallaxView extends RelativeLayout {
    public static final int SCROLL_IN = 1;
    public static final int SCROLL_OUT = 2;
    private ViewPager mViewPager;
    private ParallaxAdapter mAdapter;
    private List<ParallaxFragment> mFragments = new ArrayList<>();

    public ParallaxView(Context context) {
        super(context);
    }
    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ParallaxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initPager(AppCompatActivity activity, int ...children) {
        mViewPager = new ViewPager(getContext());
        mViewPager.setId(R.id.parallax_pager);
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        for(int i=0;i<children.length;i++){
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", children[i]);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        mAdapter = new ParallaxAdapter(activity.getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Logger.i("position:"+position+" positionOffset:"+positionOffset+" positionOffsetPixels:"+positionOffsetPixels);
                List<View> viewsOut = mFragments.get(position).getViews();
                startParallaxIn(SCROLL_OUT,viewsOut,positionOffset,positionOffsetPixels);
                if(mAdapter.getCount()>position+1){
                    List<View> viewsIn = mFragments.get(position+1).getViews();
                    startParallaxIn(SCROLL_IN,viewsIn,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(mViewPager,0);
    }

    public void startParallaxIn(int scrollState,List<View> views, float positionOffset, int positionOffsetPixels){
        if(views!=null && !views.isEmpty()){
            for (View view:views) {
                ParallaxTag tag = (ParallaxTag) view.getTag();
                if(tag!=null){
                    if(scrollState==SCROLL_IN){
                        if(tag.a_in<=0){
                            tag.a_in = 1;
                        }
                        ViewHelper.setAlpha(view,tag.a_in*positionOffset);
                        ViewHelper.setTranslationX(view,-Math.abs(tag.x_in*(1-positionOffset)*positionOffsetPixels));
                        ViewHelper.setTranslationY(view,-Math.abs(tag.y_in*(1-positionOffset)*positionOffsetPixels));
//                        Logger.i("tag.a_in:"+tag.a_in*positionOffset
//                                +"  tag.x_in:"+view.getTranslationX()
//                        +" tag.y_in:"+view.getTranslationY());
                    }else if(scrollState==SCROLL_OUT){
                        if(tag.a_out<=0){
                            tag.a_out = 1;
                        }
                        ViewHelper.setAlpha(view,tag.a_out*(1-positionOffset));
                        ViewHelper.setTranslationX(view,-Math.abs(tag.x_out*positionOffset*positionOffsetPixels));
                        ViewHelper.setTranslationY(view,-Math.abs(tag.y_out*positionOffset*positionOffsetPixels));
//                        Logger.i("tag.a_out:"+tag.a_in*positionOffset
//                                +"  tag.x_out:"+view.getTranslationX()
//                                +" tag.y_out:"+view.getTranslationY());
                    }
                }
            }
        }
    }
}
