package com.kaspat.contacts.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.kaspat.contacts.R;
import com.kaspat.contacts.adapter.ContactsListAdapter;
import com.kaspat.contacts.model.Contacts;
import com.kaspat.contacts.utils.AppController;
import com.kaspat.contacts.utils.GlobalFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;
    @BindView(R.id.etSearchContact)
    EditText etSearchContact;
    @BindView(R.id.ivCancel)
    ImageView ivClear;

    ContactsListAdapter cAdapter;
    List<Contacts> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        getContactList();
        ivClear.setVisibility(View.INVISIBLE);
        ivClear.setOnClickListener(this);

        rvContacts.setHasFixedSize(true);
        rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        cAdapter = new ContactsListAdapter(MainActivity.this, AppController.getInstance().getContacts());
        rvContacts.setAdapter(cAdapter);

        etSearchContact.addTextChangedListener(filterTextWatched);
    }

    private void getContactList() {
//        final String[] PROJECTION = new String[] {
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.CommonDataKinds.Phone.NUMBER,
//                ContactsContract.CommonDataKinds.Email.DATA,
//                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
//        };
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
//
//        if (cursor != null) {
//            try {
//                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//                final int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
//                final int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//
//                while (cursor.moveToNext()) {
//                    Contacts contact = new Contacts();
//                    contact.setId(cursor.getString(idIndex));
//                    contact.setDisplayName(cursor.getString(nameIndex));
//                    contact.setPhoneNumber(cursor.getString(numberIndex));
//                    contact.setEmail(cursor.getString(emailIndex));
//                    contact.setProfilePicture(cursor.getString(photoIndex));
//                    contacts.add(contact);
//                }
//            } finally {
//                cursor.close();
//            }
//        }

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


//        setupViewPager(contacts);
        Collections.sort(contacts);
        rvContacts.setHasFixedSize(true);
        rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        cAdapter = new ContactsListAdapter(MainActivity.this, contacts);
        rvContacts.setAdapter(cAdapter);
    }

    TextWatcher filterTextWatched = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            ivClear.setVisibility(ImageView.VISIBLE);

            if(cAdapter != null) {
                cAdapter.getFilter().filter(charSequence);
            }

            if(charSequence.toString().length() == 0) {
                ivClear.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCancel:
                etSearchContact.setText("");
                GlobalFunctions.hideKeyboard(MainActivity.this, etSearchContact);
                break;
        }
    }
}
