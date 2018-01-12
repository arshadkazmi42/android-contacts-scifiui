package com.kaspat.contacts.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;

/**
 * Created by root on 3/1/18.
 */

public class CallPopupClickListener implements View.OnClickListener {

    private Dialog dialog;
    private MediaPlayer mp;
    private Contacts contact;
    private Context mContext;

    public CallPopupClickListener(Context mContext, Dialog dialog, MediaPlayer mp, Contacts contact) {
        this.dialog = dialog;
        this.mp = mp;
        this.contact = contact;
        this.mContext = mContext;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCall:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                mContext.startActivity(intent);
                break;
            case R.id.ivSms:
//                mContext.startActivity(new Intent(Intent.ACTION_SEND, Uri.fromParts("sms", contact.getPhoneNumber(), null)));
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("address", contact.getPhoneNumber());
                mContext.startActivity(sendIntent);
                break;
            case R.id.ivWhatsapp:
                PackageManager pm = mContext.getPackageManager();
                try{
                    // Raise exception if whatsapp doesn't exist
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra("address", contact.getPhoneNumber());
                    mContext.startActivity(waIntent);
                }
                catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Whatsapp not installed on your device", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.ivClose:
                try {
                    final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.popup_close);
                    mp.start();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
