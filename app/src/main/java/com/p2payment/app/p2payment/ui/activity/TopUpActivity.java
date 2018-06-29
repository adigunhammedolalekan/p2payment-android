package com.p2payment.app.p2payment.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.BaseResponse;
import com.p2payment.app.p2payment.models.Wallet;
import com.p2payment.app.p2payment.models.payloads.CreditTopUpPayload;
import com.p2payment.app.p2payment.network.P2PaymentService;
import com.p2payment.app.p2payment.network.RestClient;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lekan Adigun on 6/29/2018.
 */

public class TopUpActivity extends BaseActivity {

    @BindView(R.id.edt_amount_top_up_activity)
    EditText amountEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_top_up);
    }

    @OnClick(R.id.btn_do_top_up) public void onTopUpClick() {

        String amount = Util.textOf(amountEditText);
        if (amount.isEmpty()) {
            toast("Enter amount");
            return;
        }

        CreditTopUpPayload payload = new CreditTopUpPayload();
        try {
            payload.amount = Integer.parseInt(amount);
        }catch (Exception e) {}

        Util.hideKeyboard(this);

        final ProgressDialog progressDialog = Util.dialog(this, "Crediting wallet...");
        progressDialog.show();

        P2PaymentService service = RestClient.createService();
        service.creditWallet(payload).enqueue(new Callback<BaseModel<Wallet>>() {
            @Override
            public void onResponse(Call<BaseModel<Wallet>> call, Response<BaseModel<Wallet>> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {

                    BaseModel<Wallet> responseBaseModel = response.body();
                    if (responseBaseModel != null
                            && responseBaseModel.data != null) {

                        snack("Your account has been credited");
                        Intent intent = new Intent(TopUpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Wallet>> call, Throwable t) {

            }
        });
    }
}
