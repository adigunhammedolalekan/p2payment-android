package com.p2payment.app.p2payment.models;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.p2payment.app.p2payment.app.RepositoryManager;
import com.p2payment.app.p2payment.utils.L;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class User {

    public String userId = "";
    public String phone = "";
    public String password = "";
    public String fullname = "";
    public String token = "";

    public static final String KEY = "User";

    public String JSON() {
        return new Gson().toJson(this);
    }

    /*
    * Save current user's details into app prefs, including the JWT token retrieved from the backend
    * */
    public void persist() {

        SharedPreferences.Editor editor = RepositoryManager.manager().preferences().edit();
        if (!token.isEmpty()) {
            editor.putString(RepositoryManager.TOKEN_KEY, token);
        }
        editor.putString(KEY, JSON()).apply();
    }

    /*
    * returns currently logged in user, returns @null if there's no logged in user.
    * */
    public static User currentUser() {

        String user = RepositoryManager.manager().preferences().getString(KEY, "");
        if (user.isEmpty())
            return null;

        return new Gson().fromJson(user, User.class);

    }
}
