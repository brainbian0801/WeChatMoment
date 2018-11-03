package com.brain.neuron.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brain.neuron.fragment.MomentsFragment;
import com.brain.neuron.R;
import com.brain.neuron.presenter.moments.MomentsPresenter;


public class MomentsActivity extends AppCompatActivity {

    private MomentsFragment momentsFragment;
    private MomentsPresenter momentsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments_base);

        momentsFragment= (MomentsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(momentsFragment==null){
            momentsFragment = new MomentsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, momentsFragment);
            transaction.commit();
        }
        momentsPresenter = new MomentsPresenter(momentsFragment);
    }
}
