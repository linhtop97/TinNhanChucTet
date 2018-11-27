package com.example.linhnb.project1zergitas.screen.ringtone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.linhnb.project1zergitas.R;
import com.example.linhnb.project1zergitas.data.model.Ringtone;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsImpl;
import com.example.linhnb.project1zergitas.data.source.local.sharedprf.SharedPrefsKey;
import com.example.linhnb.project1zergitas.screen.base.adapter.IAdapterView;
import com.example.linhnb.project1zergitas.screen.base.adapter.ListAdapter;
import com.example.linhnb.project1zergitas.screen.base.adapter.OnItemClickListener;

import java.util.List;

public class RingtoneAdapter extends ListAdapter<Ringtone> {

    private int lastSelectedPosition;
    private SharedPrefsImpl mSharedPrefs;

    public RingtoneAdapter(Context context, List data) {
        super(context, data);
        mSharedPrefs = new SharedPrefsImpl(context);
        lastSelectedPosition = mSharedPrefs.get(SharedPrefsKey.RINGTONE_SELECTED_INDEX, Integer.class);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RingtoneViewHolder ringtoneViewHolder = (RingtoneViewHolder) holder;
        ringtoneViewHolder.bind(mData.get(position));
        ringtoneViewHolder.mRadioSelected.setChecked(lastSelectedPosition == position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ringtone, parent, false);
        return new RingtoneViewHolder(view, mItemClickListener);
    }

    public class RingtoneViewHolder extends RecyclerView.ViewHolder implements IAdapterView<Ringtone> {

        private TextView mName;
        private RadioButton mRadioSelected;
        private OnItemClickListener mOnItemClickListener;

        public RingtoneViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.text_ringtone_name);
            mRadioSelected = itemView.findViewById(R.id.radio_button);
            mOnItemClickListener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(lastSelectedPosition);

                }
            });
        }

        @Override
        public void bind(Ringtone item) {
            if (item == null) {
                return;
            }
            mName.setText(item.getName().replace(".aac", "").replace("_", " "));
        }
    }
}
