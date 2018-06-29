package com.p2payment.app.p2payment.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.User;
import com.p2payment.app.p2payment.network.P2PaymentService;
import com.p2payment.app.p2payment.network.RestClient;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.utils.L;
import com.p2payment.app.p2payment.utils.Util;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lekan Adigun on 6/27/2018.
 */

public class SignInActivity extends BaseActivity {

    @BindView(R.id.edt_phone_login)
    MaterialEditText phoneEditText;
    @BindView(R.id.edt_password_login)
    MaterialEditText passwordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
    }

    @OnClick(R.id.btn_sign_in_login) public void onLoginClick() {

        String phone = Util.textOf(phoneEditText);
        String password = Util.textOf(passwordEditText);

        if (phone.length() < 10 || phone.length() > 12) {
            phoneEditText.setError("Invalid phone number");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Invalid password");
            return;
        }

        Util.hideKeyboard(this);
        final ProgressDialog progressDialog = Util.dialog(this, "Signing in...");
        progressDialog.show();

        User user = new User();
        user.phone = phone;
        user.password = password;
        P2PaymentService service = RestClient.createService();
        service.login(user).enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, Response<BaseModel<User>> response) {

                progressDialog.cancel();

                if (response.isSuccessful()) {

                    BaseModel<User> baseModel = response.body();
                    if (baseModel != null && baseModel.status) {

                        User loggedInUser = baseModel.data;
                        loggedInUser.persist();

                    }else {
                        snack(baseModel != null ? baseModel.message : Util.message());
                    }
                }else {
                    snack(Util.message());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                progressDialog.cancel();
                L.wtf(t);
                snack(Util.message());
            }
        });
    }

    @OnClick(R.id.btn_create_account_login) public void onCreateAccountClick() {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }
}
