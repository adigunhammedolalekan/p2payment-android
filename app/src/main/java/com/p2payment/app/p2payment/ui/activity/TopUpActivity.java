package com.p2payment.app.p2payment.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.BaseResponse;
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
        P2PaymentService service = RestClient.createService();
        service.creditWallet(payload).enqueue(new Callback<BaseModel<BaseResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<BaseResponse>> call, Response<BaseModel<BaseResponse>> response) {

                progressDialog.cancel();
                if (response.isSuccessful()) {

                    BaseModel<BaseResponse> responseBaseModel = response.body();
                    if (responseBaseModel != null
                            && !responseBaseModel.message.isEmpty()) {
                        snack(responseBaseModel.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<BaseResponse>> call, Throwable t) {

                progressDialog.cancel();
                snack(Util.message());
            }
        });
    }
}
