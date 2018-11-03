package com.brain.neuron.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.brain.neuron.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpUtils {

    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private HttpCallBack mHttpCallBack;
    private static final int RESULT_ERROR = 1000;
    private static final int RESULT_SUCESS = 2000;

    public OKHttpUtils(Context context){
        this.mContext = context;
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L,TimeUnit.MILLISECONDS).build();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int reusltCode = msg.what;
            switch (reusltCode) {
                case RESULT_ERROR:
                    mHttpCallBack.onFailure((String) msg.obj);
                    Log.d("result----->", (String) msg.obj);
                    break;
                case RESULT_SUCESS:
                    String result = (String) msg.obj;
                    Log.d("result----->", result);
                    try {
                        mHttpCallBack.onResponse(result);

                    } catch (JSONException e) {
                        mHttpCallBack.onFailure((String) msg.obj);
                    }
                    break;
            }

        }
    };

    public void post(String url,HttpCallBack httpCallBack){
        this.mHttpCallBack = httpCallBack;
        Request request = new Request.Builder()
                .url(url).build();
        sendRequest(request);
    }

    private void sendRequest(Request request){
        if(CommonUtils.isNetWorkConnected(mContext)){
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = handler.obtainMessage();
                    message.what = RESULT_ERROR;
                    message.obj = e.getMessage().toString();
                    message.sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message message = handler.obtainMessage();
                    message.what = RESULT_SUCESS;
                    message.obj = response.body().string();
                    message.sendToTarget();
                }
            });
        }else{
            CommonUtils.showToastShort(mContext,R.string.the_current_network);
        }
    }

    public interface HttpCallBack{
        //void onResponse(JSONObject jsonObject);
        void onResponse(String jsonStr);

        void onFailure(String errorMsg);
    }
}
