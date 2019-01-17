package com.tinnhantet.nhantin.hengio.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Contact> mContacts;
    private Context mContext;
    private OnDataClickListener<Contact> mListener;
    private boolean mIsEdit;

    public PhoneNumberAdapter(Context context, List<Contact> contacts, boolean isEdit) {
        mContacts = contacts;
        mContext = context;
        mIsEdit = isEdit;
    }

    public void setOnContactListener(OnDataClickListener listener) {
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
        private ImageButton mImageButton;
        private ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.layout_par_number);
            mTextName = itemView.findViewById(R.id.txt_name);
            Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/text_app_bold.ttf");
            mTextName.setTypeface(font2);
            mImageButton = itemView.findViewById(R.id.btn_del);
            if (mIsEdit) {
                mImageButton.setVisibility(View.VISIBLE);
            } else {
                mImageButton.setVisibility(View.GONE);
            }
            mImageButton.setOnClickListener(new View.OnClickListener() {
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
            if (mImageButton.getVisibility() == View.GONE) {
                ConstraintLayout.LayoutParams layoutParamsTextLabel = (ConstraintLayout.LayoutParams) mTextName.getLayoutParams();
                layoutParamsTextLabel.topToTop = R.id.layout_par_number;
                layoutParamsTextLabel.bottomToBottom = R.id.layout_par_number;
                layoutParamsTextLabel.leftToLeft = R.id.layout_par_number;
                layoutParamsTextLabel.rightToRight = R.id.layout_par_number;
                layoutParamsTextLabel.leftMargin = (int) mContext.getResources().getDimension(R.dimen._6sdp);
                layoutParamsTextLabel.rightMargin = (int) mContext.getResources().getDimension(R.dimen._2sdp);
                layoutParamsTextLabel.width = (int) mContext.getResources().getDimension(R.dimen._80sdp);
                mTextName.setLayoutParams(layoutParamsTextLabel);
            }
            mTextName.setText(name);
        }
    }
}
