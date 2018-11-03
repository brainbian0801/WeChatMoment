package com.brain.neuron.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.brain.neuron.R;
import com.brain.neuron.adapter.MomentsAdapter;
import com.brain.neuron.presenter.moments.MomentsContract;
import com.brain.neuron.util.CommonUtils;
import com.brain.neuron.util.MomentsConstants;
import com.brain.neuron.widget.refresh.SwipyRefreshLayout;

public class MomentsFragment extends Fragment
        implements SwipyRefreshLayout.OnRefreshListener, MomentsContract.View {

    private SwipyRefreshLayout pullToRefreshListView;
    private ListView actualListView;
    private MomentsContract.Presenter mPresenter;
    private MomentsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_moments, container, false);
        pullToRefreshListView = (SwipyRefreshLayout) root.findViewById(R.id.pull_refresh_list);
        actualListView = (ListView) root.findViewById(R.id.refresh_list);
        pullToRefreshListView.setOnRefreshListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadData();
        mAdapter = new MomentsAdapter(getActivity(),mPresenter.getData());
        actualListView.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh(int index){
        //TODO:needs reload data act
        mPresenter.firstPage();
    }

    @Override
    public void onLoad(int index){
        index++;
        mPresenter.nextPage(index);
    }

    @Override
    public void setPresenter(MomentsContract.Presenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public Context getBaseContext(){
        return getContext();
    }

    @Override
    public Activity getBaseActivity(){
        return getActivity();
    }

    @Override
    public void onRefreshComplete(){
        pullToRefreshListView.setRefreshing(false);
    }

    @Override
    public void refreshListView(){
        mAdapter.notifyDataSetChanged();
    }
}
