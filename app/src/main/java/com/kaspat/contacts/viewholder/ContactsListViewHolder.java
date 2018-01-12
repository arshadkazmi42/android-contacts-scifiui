package com.kaspat.contacts.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kaspat.contacts.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Arshad on 08-03-2016.
 */
public class ContactsListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cvMain)
    public CardView cvMain;
    @BindView(R.id.tvText)
    public TextView tvTitle;

    public ContactsListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
