package com.zzia.rxjavademo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzia.rxjavademo.base.BaseActivity;
import com.zzia.rxjavademo.base.recyclerview.CommonAdapter;
import com.zzia.rxjavademo.base.recyclerview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/11/24.
 */

public class SmartRefreshLayoutActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLaout)
    SmartRefreshLayout refreshLayout;
    List<String> mList = new ArrayList<>();
    CommonAdapter<String> mAdapter;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_smartrefresh_layout;
    }

    @Override
    public void initViewsAndEvents() {
        initAdapter();
        initData();
        initListener();
    }

    private void initListener() {

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                for (int i = 0; i < 10; i++) {
                    mList.add("加载数据，第" + i + "个");
                }
                mAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAdapter<String>(this, R.layout.item_demo, mList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.context, s);

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            mList.add("原始数据，第" + i + "个");
        }
        mAdapter.notifyDataSetChanged();
    }
}
