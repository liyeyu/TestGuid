package com.example.administrator.testguid.view.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.administrator.testguid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liyeyu on 2016/7/4.
 */
public class ParallaxLayoutInflater extends LayoutInflater implements Serializable{
    private LayoutInflater original;
    private Context mContext;
    private String[] prefixs = new String[]{
            "android.view.",
            "android.widget.",
            "android.appwidget."};
    private final ParallaxLayoutInflaterFactory mParallaxLayoutInflaterFactory;
    private List<View> mViews = new ArrayList<>();

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
        this.mContext = newContext;
        this.original = original;
        mParallaxLayoutInflaterFactory = new ParallaxLayoutInflaterFactory();
        setFactory2(mParallaxLayoutInflaterFactory);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return this;
    }

    public View createViewOnParallax(String name, AttributeSet attrs){
        if(TextUtils.isEmpty(name))
            return null;
        try{
            if(name.contains(".")){
                return  createView(name,null,attrs);
            }else{
                return createViewOnParallaxByPrefix(name,attrs);
            }
        }catch (ClassNotFoundException e){

        }
        return null;
    }

    public View createViewOnParallaxByPrefix(String name, AttributeSet attrs){
        View view = null;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.parallax_attr);
        ParallaxTag tag = null;
        if(typedArray!=null && typedArray.length()>0){
            tag = new ParallaxTag();
            tag.a_in = typedArray.getFloat(R.styleable.parallax_attr_a_in,0);
            tag.a_out = typedArray.getFloat(R.styleable.parallax_attr_a_out,0);
            tag.x_in = typedArray.getFloat(R.styleable.parallax_attr_x_in,0);
            tag.x_out = typedArray.getFloat(R.styleable.parallax_attr_x_out,0);
            tag.y_in = typedArray.getFloat(R.styleable.parallax_attr_y_in,0);
            tag.y_out = typedArray.getFloat(R.styleable.parallax_attr_y_out,0);
            typedArray.recycle();
        }
        for (String prefix:prefixs) {
            try {
                view = createView(name, prefix, attrs);
                if(view!=null && tag!=null && tag.hasValue()){
                    mViews.add(view);
                    break;
                }
            } catch (ClassNotFoundException e) {

            }
        }
        if(view!=null){
            view.setTag(tag);
        }
        return view;
    }

    class ParallaxLayoutInflaterFactory implements LayoutInflater.Factory2{
        public ParallaxLayoutInflaterFactory() {
        }
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return createViewOnParallax(name,attrs);
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            return createViewOnParallax(name,attrs);
        }
    }
    public List<View> getViews() {
        return mViews;
    }
}
