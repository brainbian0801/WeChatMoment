package com.brain.neuron.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.brain.neuron.R;
import com.brain.neuron.bean.MomentBean;
import com.brain.neuron.widget.MomentsItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MomentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<MomentBean> mTweets;
    private ImageView iv_moment_bg;

    public MomentsAdapter(Activity context, List<MomentBean> tweets) {
        this.mContext = context;
        this.mTweets = tweets;
    }

    @Override
    public int getCount() {
        return mTweets.size();
    }

    @Override
    public Object getItem(int position) {
        return mTweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_moments, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.momentsItemView = (MomentsItemView) convertView.findViewById(R.id.main);
            convertView.setTag(holder);
        }

        MomentBean tweet = mTweets.get(position);

        final int realPosition = position - 1;
        // = new MomentsItemView(context);
        holder.momentsItemView.initView(tweet);
        setItemView(realPosition, (MomentsItemView) convertView);

        return convertView;
    }

    private class ViewHolder {
        MomentsItemView momentsItemView;
    }

    private Map<Integer, MomentsItemView> views = new HashMap<>();

    private void setItemView(int position, MomentsItemView momentsItemView) {
        views.put(position, momentsItemView);
    }

    public MomentsItemView getItemView(int position) {
        return views.get(position);
    }


    /*public void initHeaderView() {
        if (re_unread == null) {
            return;
        }
        int count = momentsMessageDao.getUnreadMoments();
        if (count > 0) {
            MomentsMessage momentsMessage = momentsMessageDao.getLastMomentsMessage();
            re_unread.setVisibility(View.VISIBLE);
            re_unread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MomentsNoticeActivity.class));
                }
            });
            ImageView imageView = (ImageView) re_unread.findViewById(R.id.msg_avatar);
            TextView tvCount = (TextView) re_unread.findViewById(R.id.tv_count);
            tvCount.setText(count + context.getString(R.string.msg_count));
            Glide.with(context).load(momentsMessage.getUserAvatar()).placeholder(R.drawable.default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else {
            re_unread.setVisibility(View.GONE);
        }
    }*/
}

