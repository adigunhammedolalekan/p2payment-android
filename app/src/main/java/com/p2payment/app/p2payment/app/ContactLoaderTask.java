package com.p2payment.app.p2payment.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;

import com.p2payment.app.p2payment.MainApplication;
import com.p2payment.app.p2payment.models.Contact;
import com.p2payment.app.p2payment.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lekan Adigun on 6/27/2018.
 */

public class ContactLoaderTask implements Runnable {

    /*
    * A simple Runnable to load user's contact from android contact database.
    * */

    private Handler handler = new Handler(Looper.getMainLooper());
    private CallBack mCallBack;
    private Context mContext;
    private Map<String, Contact> mContactMap = new HashMap<>();

    public ContactLoaderTask(CallBack callBack) {
        mCallBack = callBack;
        mContext = MainApplication.getApplication().getApplicationContext();
    }
    public interface CallBack {

        void success(List<Contact> contacts);

    }
    @Override
    public void run() {

        String[] projections = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};

        ContentResolver resolver = mContext.getContentResolver();
        final List<Contact> result = new ArrayList<>();

        if (resolver != null) {
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                    projections, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                int idIndex = cursor.getColumnIndex(projections[0]);
                int nameIndex = cursor.getColumnIndex(projections[1]);

                do {
                    Contact contact = new Contact();
                    contact.name = cursor.getString(nameIndex);
                    mContactMap.put(cursor.getString(idIndex), contact);
                }while (cursor.moveToNext());
            }

            if (cursor != null)
                cursor.close();

            final String[] phoneNumberProjections = new String[]{
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            };

            cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    phoneNumberProjections, null, null,null);
            if (cursor != null && cursor.moveToFirst()) {

                int numberIndex = cursor.getColumnIndex(phoneNumberProjections[0]);
                int contactIdIndex = cursor.getColumnIndex(phoneNumberProjections[1]);

                do {
                    Contact contact = mContactMap.get(cursor.getString(contactIdIndex));
                    if (contact == null)
                        continue;

                    contact.phone = cursor.getString(numberIndex);
                    result.add(contact);
                }while (cursor.moveToNext());

                cursor.close();
            }
        }

        //Send result back to mainthread
        if (mCallBack != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.success(result);
                }
            });
        }
    }
}
