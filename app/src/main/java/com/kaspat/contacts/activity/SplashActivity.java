package com.kaspat.contacts.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kaspat.contacts.BuildConfig;
import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;
import com.kaspat.contacts.utils.AppController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    List<Contacts> contacts = new ArrayList<>();
    private final int MY_PERMISSIONS =124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        beginPermission();
        //earlier LoadContacts is placed now it resides inside Permissions
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

    //For Prtmissions
    public void beginPermission(){
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)) {

            Log.i("1", "Permission is not granted");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Log.i("REQUEST","Requesting permission....");
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS);

            }
        } else {
            Log.i("1", "Permission is Granted");
            new LoadContacts().execute(" ");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("1", "Permission is granted");
                    new LoadContacts().execute(" ");

                } else {
                    AlertDialogBuilder();
                    //UNABLE TO IMPORT SNACKBAR from android.support.design.widgets.Snackbar, So I used Alert Dialog instead

                    /**
                    Log.i("1", "Permission is again not granted");
                    Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Please ennable the permissions", Snackbar.LENGTH_SHORT);
                    mySnackbar.setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                        }
                    });
                    mySnackbar.show();
                     **/

                }
                return;
            }
        }
    }

    public void AlertDialogBuilder(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permissions Required")
                .setMessage("You have denied some of the required permissions " +
                        "for the app without these app cannot run. Please open settings, go to permissions and allow them and then open the app.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishAndRemoveTask();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
