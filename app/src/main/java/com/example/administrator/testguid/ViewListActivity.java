package com.example.administrator.testguid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.testguid.test.RectActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewListActivity extends AppCompatActivity {

    @BindView(R.id.lv_view)
    ListView mLvView;
    private String[] views = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        ButterKnife.bind(this);
        Logger.init("liyeyu").setMethodCount(4);


        for (int i=0;i<views.length;i++){
            views[i] = "android"+i;
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,views);
        mLvView.setAdapter(adapter);
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
    }

}
