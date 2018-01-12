package com.kaspat.contacts.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.kaspat.contacts.R;
import com.kaspat.contacts.model.Contacts;
import com.kaspat.contacts.utils.Popups;
import com.kaspat.contacts.viewholder.ContactsListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arshad on 08-03-2016.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListViewHolder> implements Filterable {

    private static final String TAG = ContactsListAdapter.class.getSimpleName();

    private Context mContext;
    private List<Contacts> contacts;
    private List<Contacts> filteredContacts;

    public ContactsListAdapter(Context mContext, List<Contacts> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
        this.filteredContacts = contacts;
    }

    @Override
    public int getItemCount() {
        if(filteredContacts == null) {
            return 0;
        }
        return filteredContacts.size();
    }

    @Override
    public void onBindViewHolder(final ContactsListViewHolder holder, final int position) {
        final Contacts contact = filteredContacts.get(position);
        holder.tvTitle.setText(contact.getDisplayName());

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Popups.showContactPopup(mContext, filteredContacts.get(position));
                final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.click_1);
                mp.start();
            }
        });

    }

    @Override
    public ContactsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from (mContext).inflate(R.layout.single_row_contact, viewGroup, false);
        return new ContactsListViewHolder(v);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Log.e(TAG, "HERE1");
                    filteredContacts = contacts;
                } else {
                    Log.e(TAG, "HERE2");
                    List<Contacts> filteredList = new ArrayList<>();
                    for (Contacts row : contacts) {
                        Log.e(TAG, row.getDisplayName());
                        Log.e(TAG, charString + " ");
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDisplayName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if(row.getPhoneNumber() != null && row.getPhoneNumber().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    filteredContacts = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredContacts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.e(TAG, "HERE11");
                filteredContacts = (ArrayList<Contacts>) filterResults.values;
                Log.e(TAG, "HERE12");
                notifyDataSetChanged();
            }
        };
    }

}
