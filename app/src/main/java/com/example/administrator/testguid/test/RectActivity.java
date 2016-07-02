package com.example.administrator.testguid.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.testguid.QQActivity;
import com.example.administrator.testguid.R;
import com.example.administrator.testguid.algorithm.Test1;
import com.example.appupdatet.PathActivity;
import com.wnafee.vector.MorphButton;

public class RectActivity extends AppCompatActivity {

    private RelativeLayout mParent;
    private TextView mText;
    private Typeface mTypeface;
    private String TAG = "liyeyu";

//    static {
//        System.loadLibrary("jnitest");
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(mTypeface==null){
            mTypeface = Typeface.createFromAsset(getAssets(), "xingshu.ttf");
        }
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if(view!=null && view instanceof TextView){
                    ((TextView)view).setTypeface(mTypeface);
                }

                return view;
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect);
        mParent = (RelativeLayout) findViewById(R.id.parent);
        mText = (TextView) findViewById(R.id.tv_text);
        int tag = getIntent().getIntExtra("tag", 0);
        switch (tag){
            case 0:
                testAndroid();
                break;
            case 1:
                testAlgorithm();
                break;
            case 2:
                startActivity(new Intent(RectActivity.this, QQActivity.class));
                finish();
                break;
        }
    }

    private void testAlgorithm(){
//        double count1 = Test1.JumpFloor1(5);
//        StringBuffer buffer = new StringBuffer();
//        int[][] arrs = new int[4][5];
//        for (int i=0;i<arrs.length;i++){
//            for (int j=0;j<arrs[i].length;j++){
//                arrs[i][j] = i+j;
//                if(j==arrs[i].length-1){
//                    buffer.append(arrs[i][j]+"\n");
//                }else{
//                    buffer.append(arrs[i][j]+",");
//                }
//            }
//        }
//        Log.e(TAG,buffer.toString());
//        String num = Test1.findNum(arrs, 7);
//        String num = Test1.replaceSpance(" 你好 " +
//                "哈 哈 ");
//        List<Integer> list = Test1.prinftNode1(new ListNode(1, new ListNode(2, new ListNode(3, null))),null);
//        for (int i:list) {
//            buffer.append(i+",");
//        }
//        String num = buffer.substring(0,buffer.length()-1);
//        int[] pre = {1,2,4,7,3,5,6,8};
//        int[] in = {4,7,2,1,5,3,8,6};
//        TreeNode treeNode = Test1.reConstructBinaryTree(pre, in);
//        String num = Test1.popQueue(new int[]{1, 2, 3, 4, 5});
//        Test1.rotateArray(new int[]{3, 4, 5, 1, 2});
        int num = Test1.printfFibonacci(6,0);
        mText.setText("--"+num+"--");
    }

    private void testAndroid(){
        //        new JNITest().getText();
        try {
            mText.setText("差分升级，增量更新 version:"+getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RectActivity.this, PathActivity.class));
            }
        });

//        ImageView view = (ImageView) findViewById(R.id.morph_id);
//        view.setBackgroundResource(R.drawable.vectalign_animated_vector_drawable_start_to_end);
//        final Drawable drawable = view.getBackground();
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(drawable!=null && drawable instanceof Animatable){
//                    Animatable animatable = (Animatable) drawable;
//                    animatable.start();
//                }
//            }
//        },1000);

//        swapView(createMorphableView(R.drawable.vectalign_animated_vector_drawable_start_to_end,R.drawable.vectalign_animated_vector_drawable_end_to_start));

    }

//    private void swapView(View newView){
//        View toRemove = mParent.findViewById(R.id.morph_id);
//        mParent.removeView(toRemove);
//        newView.setId(R.id.morph_id);
//        mParent.addView(newView, toRemove.getLayoutParams());
//    }

    private View createMorphableView(int startDrawable, int endDrawable){
        MorphButton mb = new MorphButton(this);
//        mb.setForegroundTintList(ColorStateList.valueOf(color));
        mb.setForegroundTintMode(PorterDuff.Mode.MULTIPLY);
        mb.setBackgroundColor(Color.TRANSPARENT);
        mb.setStartDrawable(startDrawable);
        mb.setEndDrawable(endDrawable);
        mb.setState(MorphButton.MorphState.START);
        return  mb;
    }
}
