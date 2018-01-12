package com.kaspat.contacts.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;
import com.kaspat.contacts.utils.AppController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    List<Contacts> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new LoadContacts().execute("");
    }

    private class LoadContacts extends AsyncTask<String, Void, List<Contacts>> {

        @Override
        protected List<Contacts> doInBackground(String... params) {
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    Contacts contact = new Contacts();
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    contact.setId(id);
                    contact.setDisplayName(name);

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contact.setPhoneNumber(phoneNo);
                        }
                        pCur.close();
                    }

                    String email = null;
                    Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (ce != null && ce.moveToFirst()) {
                        email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        contact.setEmail(email);
                        ce.close();
                    }

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String photoUri = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        contact.setProfilePicture(photoUri);
                    }
                    pCur.close();
                    contacts.add(contact);
                }
            }
            if(cur!=null){
                cur.close();
            }

            return contacts;
        }

        @Override
        protected void onPostExecute(List<Contacts> contacts) {
            Collections.sort(contacts);
            AppController.getInstance().setContacts(contacts);
//            mp.stop();
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {
//            mp = MediaPlayer.create(SplashActivity.this, R.raw.power2);
//            mp.setLooping(true);
//            mp.start();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
