package com.example.administrator.testguid;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.administrator.testguid.act.RectActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewListActivity extends AppCompatActivity {

    @BindView(R.id.lv_view)
    ListView mLvView;
    @BindView(R.id.gv_view)
    GridView mGvView;
    private ArrayAdapter mAdapter;
    private int index = 1;
    private List<String> mList = new ArrayList<>();
    private LayoutTransition mTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        ButterKnife.bind(this);
        Logger.init("liyeyu").setMethodCount(4);
        for (int i=1;i<=3;i++) {
            mList.add("" + i);
        }
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mList);
        mLvView.setAdapter(mAdapter);
        mLvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewListActivity.this, RectActivity.class);
                intent.putExtra("tag",position);
                startActivity(intent);
            }
        });
        mLvView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.e("tag","firstVisibleItem "+firstVisibleItem+" visibleItemCount "+visibleItemCount+" totalItemCount "+totalItemCount);
            }
        });
//        mLvView.setLayoutAnimation(getAnimationController());
        mTransition = new LayoutTransition();
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX",0.8f,1.0f);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY",0.8f,1.0f);
        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(this,holderX,holderY);
        mTransition.setAnimator(LayoutTransition.APPEARING,a);
        mLvView.setLayoutTransition(mTransition);
        mLvView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                v.startAnimation(getAnimation());
            }
        });
//        mLvView.startAnimation(getAnimation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"add").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==0){
//            mList.add(item.getTitle().toString());
            mAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    protected LayoutAnimationController getAnimationController() {
        LayoutAnimationController controller;
// AnimationSet set = new AnimationSet(true);
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);// 从0.5倍放大到1倍
        anim.setDuration(1500);
        controller = new LayoutAnimationController(anim, 0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    protected Animation getAnimation() {
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);// 从0.5倍放大到1倍
        anim.setDuration(1500);
        return anim;
    }
}
