package com.kaspat.contacts.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;
import com.kaspat.contacts.utils.AppController;
import com.kaspat.contacts.widgets.RegularTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private RegularTextView tvInfo;

    private final int PERMISSION_REQUEST_READ_CONTACTS = 1;

    List<Contacts> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Check permissions here
        tvInfo = (RegularTextView) findViewById(R.id.tvContactNumber);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission hasn't been granted, ask the user
            final Activity activity = this;
            tvInfo.setText(getString(R.string.splash_permission_required));
            tvInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
                }
            });
        }
        else {
            standardLoad();
        }
    }

    private void standardLoad() {
        tvInfo.setText(getString(R.string.splash_loading_contacts));
        new LoadContacts().execute("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_CONTACTS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    standardLoad();
                }
                else {
                    Toast.makeText(this, getString(R.string.toast_permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
