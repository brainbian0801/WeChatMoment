package com.brain.neuron.presenter.moments;

import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brain.neuron.R;
import com.brain.neuron.bean.MomentBean;
import com.brain.neuron.bean.UserInfoBean;
import com.brain.neuron.util.CommonUtils;
import com.brain.neuron.util.MomentsConstants;
import com.brain.neuron.util.OKHttpUtils;


import java.util.ArrayList;
import java.util.List;


public class MomentsPresenter implements MomentsContract.Presenter {


    private MomentsContract.View monmentsView;
    private List<MomentBean> mData = new ArrayList<MomentBean>();
    private List<MomentBean> mAllData = new ArrayList<MomentBean>();

    public MomentsPresenter(MomentsContract.View view) {
        monmentsView = view;
        monmentsView.setPresenter(this);
    }

    @Override
    public List<MomentBean> getData(){
        return mData;
    }

    @Override
    public List<MomentBean> getAllData(){
        return mAllData;
    }

    @Override
    public void nextPage(int index) {
        int totalSize = getAllData().size();
        int totalPage = totalSize % MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE != 0
                ? totalSize / MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE + 1
                : totalSize / MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE;
        if(index > totalPage){
            CommonUtils.showToastShort(monmentsView.getBaseContext(),R.string.no_more_tweets);
        }else{
            if(index != totalPage) {
                mData.addAll(mAllData.subList((index - 1) * MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE
                        , index * MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE));
            }else{
                mData.addAll(mAllData.subList((index - 1) * MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE
                        , totalSize));
            }
        }
        monmentsView.refreshListView();
        monmentsView.onRefreshComplete();
    }

    @Override
    public void firstPage() {
        mData.clear();
        mData.addAll(mAllData.subList(0,MomentsConstants.PER_PAGE_SHOW_TWEET_SIZE));
        monmentsView.refreshListView();
        monmentsView.onRefreshComplete();
    }

    @Override
    public void start(){

    }

    @Override
    public void loadData(){
        OKHttpUtils utils = new OKHttpUtils(monmentsView.getBaseContext());
        //Get all tweets
        utils.post(MomentsConstants.URL_USER_TWEETS, new OKHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(String jsonStr) {
                List<JSONObject> list = JSONArray.parseArray(jsonStr, JSONObject.class);
                if(list != null && list.size() > 0){
                    //mData.clear();
                    for(JSONObject obj : list){
                        if(!obj.containsKey("error") && !obj.containsKey("unknown error")){
                            MomentBean tweet = obj.toJavaObject(MomentBean.class);
                            if((tweet.getContent() == null || "".equals(tweet.getContent()))
                                    && (tweet.getImages() == null || tweet.getImages().size() == 0)){
                                continue;
                            }
                            mAllData.add(tweet);
                        }
                    }
                }
                mData.addAll(mAllData.subList(0,5));
                monmentsView.refreshListView();
                monmentsView.onRefreshComplete();
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(),
                        monmentsView.getBaseContext().getResources().getString(R.string.request_failed_msg) + errorMsg,
                        Toast.LENGTH_SHORT).show();
            }
        });
        //Get profile info
        /*utils.post(MomentsConstants.URL_USER_INFO, new OKHttpUtils.HttpCallBack() {
            @Override
            public void onResponse(String jsonStr) {
                if(jsonStr != null && !"".equals(jsonStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr, JSONObject.class);
                    String profileImage = jsonObject.getString("profile-image");
                    String avatar = jsonObject.getString("avatar");
                    String nick = jsonObject.getString("nick");
                    String userName = jsonObject.getString("username");
                    UserInfoBean profileInfo = new UserInfoBean();
                    profileInfo.setProfileimage(profileImage);
                    profileInfo.setAvatar(avatar);
                    profileInfo.setNick(nick);
                    profileInfo.setUsername(userName);
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(monmentsView.getBaseContext(),
                        monmentsView.getBaseContext().getResources().getString(R.string.request_failed_msg) + errorMsg,
                        Toast.LENGTH_SHORT).show();
            }
        });*/
    }

}
