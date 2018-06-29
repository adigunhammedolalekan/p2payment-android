package com.p2payment.app.p2payment.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p2payment.app.p2payment.R;
import com.p2payment.app.p2payment.models.Contact;
import com.p2payment.app.p2payment.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Lekan Adigun on 6/27/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> mContacts;
    private IClickCallBack clickCallBack;

    public ContactAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    public ContactAdapter() {}

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.layout_contact, parent, false));
    }

    public void setClickCallBack(IClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        final Contact contact = mContacts.get(position);
        holder.nameTextView.setText(contact.name);
        holder.phoneTextView.setText(contact.phone);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null)
                    clickCallBack.onClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ContactViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_name_contact_layout)
        TextView nameTextView;
        @BindView(R.id.tv_phone_contact_layout)
        TextView phoneTextView;
        @BindView(R.id.layout_contact_root)
        LinearLayout root;

        public ContactViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface IClickCallBack {
        void onClick(Contact contact);
    }
}
