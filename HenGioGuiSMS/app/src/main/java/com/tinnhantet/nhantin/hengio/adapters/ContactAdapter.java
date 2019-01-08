package com.tinnhantet.nhantin.hengio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsKey;
import com.tinnhantet.nhantin.hengio.listeners.OnItemClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Contact> mContacts;
    private Context mContext;
    private OnItemClickListener mListener;
    private int mSize;
    private int mSizeHolder;
    private int mPositionSelect;
    private boolean mIsSelectAll;
    private List<Contact> mContactsHolder;
    private SharedPrefsImpl mSharedPrefs;

    public ContactAdapter(Context context, List<Contact> contacts) {
        mContacts = contacts;
        //mContactsHolder = contacts;
        mSize = contacts.size();
        mSizeHolder = mSize;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = mLayoutInflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(mContacts.get(position));
        Contact contact = mContacts.get(position);
//        Bitmap bm = contact.openPhoto(mContext, (long) contact.getId());
//        if (bm != null) {
//            holder.mImgAvatar.setImageBitmap(bm);
//        } else {
//            holder.mImgAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.avatar));
//        }
        holder.mIsSelected.setChecked(contact.isSelected());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.contains("payload")) {
            Contact contact = mContacts.get(position);
//            Bitmap bm = contact.openPhoto(mContext, (long) contact.getId());
//            if (bm != null) {
//                holder.mImgAvatar.setImageBitmap(bm);
//            }else {
//                holder.mImgAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.avatar));
//            }
            holder.mIsSelected.setChecked(contact.isSelected());
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public List<Contact> getContacts() {
        return mContacts;
    }

    @Override
    public int getItemCount() {
        return mContacts != null ? mContacts.size() : 0;
    }


    public void setSelectedAll() {
        if (!mIsSelectAll) {
            for (int i = 0; i < mSize; i++) {
                mContacts.get(i).setSelected(true);
            }
            mIsSelectAll = true;
            notifyDataSetChanged();
        }
    }

    public void removeSelectedAll() {
        if (mIsSelectAll) {
            for (int i = 0; i < mSize; i++) {
                mContacts.get(i).setSelected(false);
            }
            mIsSelectAll = false;
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextPhone;
        private ImageView mImgAvatar;
        private CheckBox mIsSelected;

        ViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_name);
            mTextPhone = itemView.findViewById(R.id.text_phone);
            mImgAvatar = itemView.findViewById(R.id.img_avartar);
            mIsSelected = itemView.findViewById(R.id.cb_select_contact);
            mIsSelected.setClickable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPositionSelect = getAdapterPosition();
                    Boolean b = mIsSelected.isChecked();
                    Contact contact = mContacts.get(mPositionSelect);
                    mContacts.get(mPositionSelect).setSelected(!b);
//                    for (int i = 0; i < mSizeHolder; i++) {
//                        if (mContactsHolder.get(i).getPhone().equals(contact.getPhone())) {
//                            mContactsHolder.get(i).setSelected(!b);
//                        }
//                    }
                    mListener.onItemClick(mPositionSelect);
                    notifyItemChanged(mPositionSelect, "changed");
                }
            });
        }

        void bindData(Contact contact) {
            if (contact == null) return;
            mTextName.setText(contact.getName());
            mTextPhone.setText(contact.getPhone());
        }
    }

    public void setContacts(List<Contact> contacts) {
        mContacts.clear();
        mContacts = contacts;
        mSize = mContacts.size();
        notifyDataSetChanged();
    }

    public void filterContact(String contactName) {
        if (contactName.isEmpty()) {
            mContactsHolder = mSharedPrefs.getListContact(SharedPrefsKey.KEY_LIST_CONTACT_HOLDER);
            setContacts(mContactsHolder);
            return;
        }
        List<Contact> results = new ArrayList<>();
        contactName = contactName.toLowerCase();
        for (int i = 0; i < mContacts.size(); i++) {
            Contact contact = mContacts.get(i);
            if (contact.getPhone().contains(contactName) || contact.getName().contains(contactName)) {
                results.add(contact);
            }
        }
        setContacts(results);
    }
}
