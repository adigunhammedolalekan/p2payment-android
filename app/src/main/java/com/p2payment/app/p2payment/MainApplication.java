package com.p2payment.app.p2payment;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class MainApplication extends Application {

    private static MainApplication application;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MainApplication getApplication() {
        return application;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
