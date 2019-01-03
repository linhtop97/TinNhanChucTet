package com.tinnhantet.nhantin.hengio.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.listeners.OnContactClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Contact> mContacts;
    private Context mContext;
    private OnContactClickListener mListener;

    public PhoneNumberAdapter(Context context, List<Contact> contacts) {
        mContacts = contacts;
        mContext = context;
    }

    public void setOnContactListener(OnContactClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = mLayoutInflater.inflate(R.layout.item_phone_number, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(mContacts.get(position));
    }


    public List<Contact> getContacts() {
        return mContacts;
    }

    @Override
    public int getItemCount() {
        return mContacts != null ? mContacts.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;

        ViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    mListener.onItemClick(mContacts.get(pos), pos);
                }
            });
        }

        void bindData(Contact contact) {
            if (contact == null) return;
            String name = contact.getName();
            if (name.equals("")) {
                name = contact.getPhone();
            }
            mTextName.setText(name);
        }
    }
}
