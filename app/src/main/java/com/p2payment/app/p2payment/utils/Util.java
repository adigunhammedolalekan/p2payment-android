package com.p2payment.app.p2payment.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public final class Util {

    /*
    * Handy utils
    * */

    public static String textOf(EditText editText) {

        if(editText == null) return "";

        return editText.getText().toString().trim();
    }

    public static ProgressDialog dialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);

        return progressDialog;
    }

    public static String message() {
        return "Failed to connect to server. Please retry";
    }

    public static boolean isSuccess(JSONObject jsonObject) throws JSONException {
        return jsonObject.has("status") && jsonObject.getBoolean("status");
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e) {
        }
    }
}
