package com.p2payment.app.p2payment.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.Contact;
import com.p2payment.app.p2payment.models.payloads.TransactionPayload;
import com.p2payment.app.p2payment.models.payloads.TransactionResponse;
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
 * Created by Lekan Adigun on 6/27/2018.
 */

public class SendMoneyActivity extends BaseActivity {

    @BindView(R.id.edt_amount_send_money_activity)
    MaterialEditText amountEditText;
    @BindView(R.id.edt_beneficiary_phone_send_money_activity)
    MaterialEditText beneficiaryPhoneNumberEditText;

    public static final int RC_CHOOSE_CONTACT = 151, RC_PERMISSION_CODE = 152;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_send_money);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Send Money");
        }
    }

    @OnClick(R.id.btn_add_contact_from_phonebook) public void onClickContactPicker() {

        requestRuntimePermission();

    }

    @OnClick(R.id.btn_send_activity_send_money) public void onSendClick() {

        String phone = Util.textOf(beneficiaryPhoneNumberEditText);
        String amount = Util.textOf(amountEditText);

        if (phone.length() < 10 || phone.length() > 12) {
            beneficiaryPhoneNumberEditText.setError("Invalid phone number");
            return;
        }

        if (amount.isEmpty()) {
            amountEditText.setError("Enter amount");
            return;
        }

        int amt = 0;
        try {
            amt = Integer.parseInt(amount);
        }catch (Exception e) {
        }

        Util.hideKeyboard(this);
        TransactionPayload payload = new TransactionPayload();
        payload.amount = amt;
        payload.phone = phone;

        P2PaymentService service = RestClient.createService();
        service.transaction(payload).enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        TransactionResponse model = response.body();
                        if (model != null && model.status) {
                            snack(model.message);
                        }
                    }else {
                        snack(Util.message());
                    }
                }catch (Exception e) {}
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                snack(Util.message());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case RC_CHOOSE_CONTACT:
                    Contact contact = new Contact(); //There's no need for contact object here, but a need might arise later in future.
                    contact.phone = data.getStringExtra(Contact.KEY_PHONE);
                    contact.name = data.getStringExtra(Contact.KEY_NAME);

                    beneficiaryPhoneNumberEditText.setText(contact.phone);
                    break;
            }
        }
    }

    private void requestRuntimePermission() {

        int status = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, RC_PERMISSION_CODE);
        }else {

            Intent intent = new Intent(this, ContactChooserActivity.class);
            startActivityForResult(intent, RC_CHOOSE_CONTACT);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(this, ContactChooserActivity.class);
            startActivityForResult(intent, RC_CHOOSE_CONTACT);
        }else {
            showDialog("Permission Denied",
                    "P2Payment has been denied permission to read contact.");
        }
    }
}
