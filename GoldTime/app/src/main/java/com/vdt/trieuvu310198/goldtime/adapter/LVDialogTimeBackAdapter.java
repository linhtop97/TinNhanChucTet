package com.vdt.trieuvu310198.goldtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.model.ModelLVDiaLogAmBao;

import java.util.List;

// dùng lại model, itemview của bên music

public class LVDialogTimeBackAdapter extends ArrayAdapter<ModelLVDiaLogAmBao> {
    private List<ModelLVDiaLogAmBao> listTime;
    private LayoutInflater inflater;
    private OnDialogListClickListener mlistener;

    public LVDialogTimeBackAdapter(@NonNull Context context, int resource, @NonNull List<ModelLVDiaLogAmBao> objects,
                                   OnDialogListClickListener listener) {
        super(context, resource, objects);
        this.listTime = objects;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.mlistener = listener;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LVDialogTimeBackAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new LVDialogTimeBackAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_am_bao, parent, false);
            holder.nameTimeBack = convertView.findViewById(R.id.txt_name_item_am_bao);
            holder.rbMusic = convertView.findViewById(R.id.rb_item_am_bao);
            convertView.setTag(holder);

        } else {
            holder = (LVDialogTimeBackAdapter.ViewHolder) convertView.getTag();
        }

        final ModelLVDiaLogAmBao modelLVDiaLogAmBao = listTime.get(position);
        holder.nameTimeBack.setText(modelLVDiaLogAmBao.getNameMusic());
        holder.rbMusic.setChecked(modelLVDiaLogAmBao.isIscheckedMusic());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemClick(position);
            }
        });
        return convertView;
    }





    public static class ViewHolder {
        public TextView nameTimeBack;
        public RadioButton rbMusic;
    }

    public interface OnDialogListClickListener {
        void onItemClick(int position);
    }
}
