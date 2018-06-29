package com.p2payment.app.p2payment.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.BaseModel;
import com.p2payment.app.p2payment.models.User;
import com.p2payment.app.p2payment.models.Wallet;
import com.p2payment.app.p2payment.network.P2PaymentService;
import com.p2payment.app.p2payment.network.RestClient;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.utils.L;
import com.p2payment.app.p2payment.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_loading_activity_main)
    TextView loadingTextView;
    @BindView(R.id.tv_wallet_balance_activity_main)
    TextView walletBalanceTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWalletBalance();
    }

    private void loadWalletBalance() {

        show(loadingTextView);
        P2PaymentService service = RestClient.createService();
        service.getWallet().enqueue(new Callback<BaseModel<Wallet>>() {
            @Override
            public void onResponse(Call<BaseModel<Wallet>> call, Response<BaseModel<Wallet>> response) {

                hide(loadingTextView);
                if (response.isSuccessful()) {
                    BaseModel<Wallet> model = response.body();
                    if (model != null) {
                        Wallet wallet = model.data;
                        if (wallet != null)
                            walletBalanceTextView.setText(String.valueOf("N" + wallet.balance));
                    }
                }else {
                    snack(Util.message());
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Wallet>> call, Throwable t) {
                hide(loadingTextView);
                snack(Util.message());
            }
        });
    }

    @OnClick(R.id.btn_send_money_main_activity) public void onSendMoneyClick() {
        startActivity(new Intent(this, SendMoneyActivity.class));
    }

    @OnClick(R.id.btn_topup_activity_main) public void onTopUpClick() {
        startActivity(new Intent(this, TopUpActivity.class));
    }
}
