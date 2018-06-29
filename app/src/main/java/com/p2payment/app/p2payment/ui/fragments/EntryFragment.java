package com.p2payment.app.p2payment.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.ui.activity.CreateAccountActivity;
import com.p2payment.app.p2payment.ui.base.BaseFragment;

import butterknife.OnClick;

/**
 * Created by Lekan Adigun on 6/27/2018.
 */

public class EntryFragment extends BaseFragment {


    public static EntryFragment newInstance() {

        Bundle args = new Bundle();

        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_entry_fragment, container, false);
    }

    @OnClick(R.id.btn_get_started) public void onGetStartedClick() {
        startActivity(new Intent(getActivity(), CreateAccountActivity.class));
    }
}
