package com.brain.neuron.presenter.moments;

import com.alibaba.fastjson.JSONObject;
import com.brain.neuron.bean.MomentBean;
import com.brain.neuron.presenter.BasePresenter;

import java.util.List;

public interface MomentsContract {



    public interface View extends BaseView<Presenter> {
        void onRefreshComplete();
        void refreshListView();
    }

    interface Presenter extends BasePresenter {
        //获取数据
        List<MomentBean> getData();
        List<MomentBean> getAllData();
        void nextPage(int index);
        void firstPage();
        void loadData();

        /*String getBackgroudMoment();
        void getCacheTime();
        void loadeData(int pageIndex);
        void setGood(int position, String aid);
        void comment(int position, String aid, String content);
        void cancelGood(int position, String gid);
        void deleteComment(int position, String cid);
        void onBarRightViewClicked();
        void deleteItem(int position, String aid);
        void startToPhoto(int type);
        void startToAlbum(int type);
        void onResult(int requestCode, int resultCode, Intent data);
        String getCurrentTime();*/
    }

}
