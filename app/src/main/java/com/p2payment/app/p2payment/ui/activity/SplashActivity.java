package com.p2payment.app.p2payment.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.p2payment.app.p2payment.R;

import com.p2payment.app.p2payment.models.User;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.ui.fragments.EntryFragment;

import butterknife.OnClick;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash);

        /*
        * Check if user has been authenticated, authenticate user if not
        * */
        User user = User.currentUser();
        if (user != null) {

            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, 2000);
        }else {
            FragmentManager manager = getSupportFragmentManager();
            if (manager != null) {
                manager.beginTransaction()
                        .replace(R.id.fragment_container_splash_activity, EntryFragment.newInstance())
                        .commit();
            }
        }
    }
}
