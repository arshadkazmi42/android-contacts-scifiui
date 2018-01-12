package com.kaspat.contacts.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;

/**
 * Created by root on 31/12/17.
 */

public class Popups {

    public static void showContactPopup(final Context mContext, Contacts contact) {

        final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.popup_1);
        mp.start();

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_contact_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvName = (TextView) dialog.findViewById(R.id.tvContactName);
        TextView tvNumber = (TextView) dialog.findViewById(R.id.tvContactNumber);
        TextView tvEmail = (TextView) dialog.findViewById(R.id.tvEmail);
        ImageView ivPhoto = (ImageView) dialog.findViewById(R.id.ivContactImage);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivCall = (ImageView) dialog.findViewById(R.id.ivCall);
        ImageView ivSms = (ImageView) dialog.findViewById(R.id.ivSms);
        ImageView ivWhatsApp = (ImageView) dialog.findViewById(R.id.ivWhatsapp);
        tvName.setText(contact.getDisplayName());
        tvNumber.setText(contact.getPhoneNumber());
        if(contact.getEmail() != null) {
            tvEmail.setText(contact.getEmail());
        }
        if(contact.getProfilePicture() != null) {
            ivPhoto.setImageURI(Uri.parse(contact.getProfilePicture()));
        }

        CallPopupClickListener callPopupClickListener
                = new CallPopupClickListener(mContext, dialog, mp, contact);

        ivClose.setOnClickListener(callPopupClickListener);
        ivCall.setOnClickListener(callPopupClickListener);
        ivSms.setOnClickListener(callPopupClickListener);
        ivWhatsApp.setOnClickListener(callPopupClickListener);

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
