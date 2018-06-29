package com.p2payment.app.p2payment.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.User;
import com.p2payment.app.p2payment.models.payloads.CreateUserPlayload;
import com.p2payment.app.p2payment.network.P2PaymentService;
import com.p2payment.app.p2payment.network.RestClient;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.utils.Util;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class CreateAccountActivity extends BaseActivity {


    @BindView(R.id.edt_phone_create_account)
    MaterialEditText phoneEditText;
    @BindView(R.id.edt_name_create_account)
    MaterialEditText nameEditText;
    @BindView(R.id.edt_password_create_account)
    MaterialEditText passwordEditText;

    private CreateUserPlayload newUser = new CreateUserPlayload();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_account);

    }

    @OnClick(R.id.btn_create_account) public void onCreateAccountClick() {

        String phone = Util.textOf(phoneEditText);
        String name = Util.textOf(nameEditText);
        String password = Util.textOf(passwordEditText);

        if (phone.length() < 10 || phone.length() > 12) {
            phoneEditText.setError("Invalid mobile number");
            return;
        }

        if (name.isEmpty()) {
            nameEditText.setError("Enter your name");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Enter password");
            return;
        }

        newUser.fullname = name;
        newUser.password = password;
        newUser.phone = phone;

        final ProgressDialog progressDialog = Util.dialog(this, "Creating user...");
        progressDialog.show();
        P2PaymentService service = RestClient.createService();
        service.createUser(newUser).enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, Response<BaseModel<User>> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {

                    BaseModel<User> resp = response.body();
                    if (resp.status) {
                        User user = response.body().data;
                        user.persist();
                        snack("Welcome to p2Payment");

                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }else {
                        snack(resp.message);
                    }

                }else {

                    BaseModel<User> userBaseModel = response.body();
                    if (!userBaseModel.message.isEmpty()) {
                        snack(userBaseModel.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                progressDialog.cancel();
                snack(Util.message());
            }
        });
    }

    @OnClick(R.id.btn_sign_in_create_account) public void onSignInClick() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
