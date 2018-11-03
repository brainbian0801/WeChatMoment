package com.brain.neuron.widget;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brain.neuron.R;
import com.brain.neuron.bean.CommentBean;
import com.brain.neuron.bean.ImageBean;
import com.brain.neuron.bean.MomentBean;
import com.brain.neuron.bean.SenderBean;
import com.brain.neuron.util.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MomentsItemView extends LinearLayout {

    private ImageView ivAvatar;
    private TextView tvNick;
    private TextView tvContent;
    private LinearLayout linearLayout;
    private TextView tvLocation;
    private TextView tvDelete;
    private ImageView ivPop;
    private TextView tvGood;
    private TextView tvComment;
    private TextView tvTime;
    private PopupWindow mMorePopupWindow;
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;

    public MomentsItemView(Context context) {
        super(context);
        init(context);
    }

    public MomentsItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MomentsItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.layout_moments_item, this);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        tvContent = (TextView) findViewById(R.id.tv_content);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        ivPop = (ImageView) findViewById(R.id.iv_pop);
        tvGood = (TextView) findViewById(R.id.tv_good);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvTime = (TextView) findViewById(R.id.tv_time);

    }

    private JSONArray goodArray;
    private JSONArray commentArray;

    public void initView(MomentBean tweet) {

        String content = null;
        List<ImageBean> imagePics = null;
        SenderBean senderObj = null;
        List<CommentBean> comments = null;

        content = tweet.getContent();
        imagePics = tweet.getImages();
        senderObj = tweet.getSender();
        comments = tweet.getComments();

        if (imagePics != null && imagePics.size() > 0) {
            linearLayout.setVisibility(VISIBLE);
            linearLayout.removeAllViews();
            initImgaesView(imagePics, linearLayout);
        } else {
            linearLayout.setVisibility(GONE);
        }


        String nickName = senderObj.getNick();
        String avatar = senderObj.getAvatar();
        String username = senderObj.getUsername();

        tvNick.setText(nickName);
        tvContent.setText(content);

        /*ImageLoader.with(getContext())
                .load(avatar,ivAvatar);*/

        ImageLoader.with(getContext())
                .load("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",ivAvatar);


        //设置评论
        initCommentView(tvComment, comments);


    }

    private OnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }


    public interface OnMenuClickListener {
        void onUserClicked(String userId);

        void onGoodIconClicked(String aid);

        void onCommentIconClicked(String aid);

        void onCancelGoodClicked(String gid);

        void onCommentDeleteCilcked(String cid);

        void onDeleted(String aid);

        void onImageListClicked(int index, ArrayList<String> images);


    }

    public void updateGoodView(JSONArray data) {
        goodArray.clear();
        goodArray.addAll(data);
        //initGoodView(tvGood, goodArray);
    }

    public void updateCommentView(List<CommentBean> data) {
        commentArray.clear();
        commentArray.addAll(data);
        initCommentView(tvComment, data);
    }

    public void initImgaesView(List<ImageBean> images, LinearLayout linearLayout) {
        switch (images.size()) {
            case 1:
                initSingle(images.get(0), linearLayout);
                break;
            default:
                initFour(images, linearLayout);
                break;
        }

    }

    private void initSingle(final ImageBean url, LinearLayout linearLayout) {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.item_moments_sigle_image, null);
        final ImageView ivSingle= (ImageView) view.findViewById(R.id.iv_single);

        ImageLoader.with(getContext())
                .load("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",ivSingle);
        ivSingle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuClickListener != null) {
                    List<String> images = new ArrayList<String>();
                    images.add(url.getUrl());
                    onMenuClickListener.onImageListClicked(0, (ArrayList<String>) images);
                }
            }
        });

        linearLayout.addView(view);
    }


    private void initFour(final List<ImageBean> images, LinearLayout mainLinearLayout) {
        int numColumns = 3;
        if (images.size() == 4) {
            numColumns = 2;
        }
        int lines = images.size() % numColumns == 0 ? images.size() / numColumns : (images.size() / numColumns) + 1;
        for (int i = 0; i < lines; i++) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setWeightSum(3);
            for (int n = i * numColumns; n < (numColumns * (i + 1)); n++) {
                if (n >= images.size()) {
                    break;
                }
                SquareImageView imageView = new SquareImageView(getContext());
                imageView.setPadding(0, 20, 20, 0);
                String url = images.get(n).getUrl();
                //TODO:set image url
                linearLayout.addView(imageView);
                //linearLayout.addView(imageView, new LayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 1)));

                ImageLoader.with(getContext())
                        .load("http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",imageView);

            }
            mainLinearLayout.addView(linearLayout);
        }
    }

    private void initCommentView(TextView textView, List<CommentBean> comments) {
        if (comments == null || comments.size() == 0) {
            textView.setVisibility(View.GONE);
            return;
        } else {
            textView.setVisibility(View.VISIBLE);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int start = 0;

        for (int i = 0; i < comments.size(); i++) {
            CommentBean comment = comments.get(i);
            String content = comment.getContent();
            SenderBean senderObj = comment.getSender();
            String nick = senderObj.getNick();
            //String username = senderObj.getUsername();
            //String avatar = senderObj.getAvatar();

            String content_1 = ": " + content;
            String content_2 = ": " + content + "\n";
            if (i == (comments.size() - 1) || (comments.size() == 1 && i == 0)) {
                ssb.append(nick + content_1);
            } else {
                ssb.append(nick + content_2);
            }

            ssb.setSpan(new TextClickableSpan(nick, 0), start,
                    start + nick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            start = ssb.length();

        }

        textView.setText(ssb);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private class TextClickableSpan extends ClickableSpan {
        private String userId;
        private String cid;
        private int type;//0--名字,1--整行评论

        public TextClickableSpan(String id, int type) {
            this.type = type;
            switch (type) {
                case 0:
                    userId = id;
                    break;
                case 1:
                    cid = id;
                    break;
            }

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (type == 0) {
                ds.setColor(getResources().getColor(R.color.text_color));
                ds.setUnderlineText(false);
            }

        }

        @Override
        public void onClick(final View widget) {
            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.darker_gray));
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    }
                }, 500);
            }
        }
    }

    /**
     * 弹出点赞和评论框
     */
    /*private void ininPop(final ImageView imageView, final String aid) {

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMorePop(imageView, aid);
            }
        });
    }*/

    /*private TextView tvPopGood;
    private View conentView;

    private void showMorePop(ImageView imageView, final String aid) {

    //判断是显示取消还是取消点赞
        boolean isCancel = false;
        String pid = null;

        imageView.setTag(getContext().getString(R.string.good));
        for (int i = 0; i < goodArray.size(); i++) {
            JSONObject jsonObject = goodArray.getJSONObject(i);
            String userId = jsonObject.getString("userId");

            if (HTApp.getInstance().getUsername().equals(userId)) {
                isCancel = true;
                pid = jsonObject.getString("pid");
                break;
            }
        }
        final String finalPid = pid;
        final boolean finalIsCancel = isCancel;
        if (mMorePopupWindow == null) {

            mMorePopupWindow = new PopupWindow(getContext());

            conentView = LayoutInflater.from(getContext()).inflate(R.layout.popwindow_moments, null);

            // 设置SelectPicPopupWindow的View
            mMorePopupWindow.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            mMorePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            mMorePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            mMorePopupWindow.setFocusable(true);
            mMorePopupWindow.setOutsideTouchable(true);
            // 刷新状态
            mMorePopupWindow.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            mMorePopupWindow.setBackgroundDrawable(dw);

            // 设置SelectPicPopupWindow弹出窗体动画效果
            mMorePopupWindow.setAnimationStyle(R.style.AnimationPreview);

            conentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = conentView.getMeasuredWidth();
            mShowMorePopupWindowHeight = conentView.getMeasuredHeight();
            tvPopGood = (TextView) conentView.findViewById(R.id.tv_good);


            conentView.findViewById(R.id.ll_pl).setOnClickListener(new OnClickListener() {
                // 扫一扫 ，调出扫二维码 gongfan
                @Override
                public void onClick(View v) {
                    if (onMenuClickListener != null) {
                        onMenuClickListener.onCommentIconClicked(aid);
                    }
                    mMorePopupWindow.dismiss();
                }

            });
        }
        final boolean finalIsCancel1 = isCancel;
        final String finalPid1 = pid;
        conentView.findViewById(R.id.ll_zan).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mMorePopupWindow.dismiss();
                if (finalIsCancel1 && finalPid1 != null) {
                    if (onMenuClickListener != null) {
                        onMenuClickListener.onCancelGoodClicked(finalPid1);
                    }
                } else {
                    if (onMenuClickListener != null) {
                        onMenuClickListener.onGoodIconClicked(aid);
                    }
                }

            }

        });


        if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        } else {
            if (isCancel) {
                tvPopGood.setText(getContext().getString(R.string.cancel));
            } else {
                tvPopGood.setText(getContext().getString(R.string.good));
            }
            int heightMoreBtnView = imageView.getHeight();
            mMorePopupWindow.showAsDropDown(imageView, -mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }*/

}
