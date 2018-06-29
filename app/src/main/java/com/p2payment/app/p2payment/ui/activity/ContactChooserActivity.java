package com.p2payment.app.p2payment.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.p2payment.app.p2payment.MainApplication;
import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.app.ContactLoaderTask;
import com.p2payment.app.p2payment.models.Contact;
import com.p2payment.app.p2payment.ui.ContactAdapter;
import com.p2payment.app.p2payment.ui.base.BaseActivity;
import com.p2payment.app.p2payment.utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Lekan Adigun on 6/27/2018.
 */

public class ContactChooserActivity extends BaseActivity {

    @BindView(R.id.rv_choose_contacts)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout_select_contact)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Contact> mContacts = new ArrayList<>();
    private ContactAdapter contactAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_contact);

        setupRecyclerView();
        fetchContacts();
    }

    private void fetchContacts() {
        MainApplication.getApplication().getExecutorService().execute(new ContactLoaderTask(contactLoaderCallBack));
    }

    private void setupRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(mContacts);
        contactAdapter.setClickCallBack(clickCallBack);
        mRecyclerView.setAdapter(contactAdapter);

    }

    private ContactAdapter.IClickCallBack clickCallBack = new ContactAdapter.IClickCallBack() {
        @Override
        public void onClick(Contact contact) {

            Intent intent = getIntent();
            intent.putExtra(Contact.KEY_NAME, contact.name);
            intent.putExtra(Contact.KEY_PHONE, contact.phone);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private ContactLoaderTask.CallBack contactLoaderCallBack = new ContactLoaderTask.CallBack() {
        @Override
        public void success(List<Contact> contacts) {

            L.fine("Total => " + contacts.size());
            swipeRefreshLayout.setRefreshing(false);

            for (Contact contact : contacts)
                mContacts.add(contact);


            if (contactAdapter != null)
                contactAdapter.notifyDataSetChanged();
        }
    };
}
