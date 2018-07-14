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

public class LVDialogMusicAdapter extends ArrayAdapter<ModelLVDiaLogAmBao> {
    private List<ModelLVDiaLogAmBao> listMusic;
    private LayoutInflater inflater;
    private OnDialogListClickListener mlistener;

    public LVDialogMusicAdapter(@NonNull Context context, int resource, @NonNull List<ModelLVDiaLogAmBao> objects,
                                OnDialogListClickListener listener) {
        super(context, resource, objects);
        this.listMusic = objects;
        this.mlistener = listener;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {//lần đầu tiên item đó được tạo ra
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_am_bao, parent, false);
            holder.nameMusic = convertView.findViewById(R.id.txt_name_item_am_bao);
            holder.rbMusic = convertView.findViewById(R.id.rb_item_am_bao);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ModelLVDiaLogAmBao modelLVDiaLogAmBao = listMusic.get(position);
        holder.nameMusic.setText(modelLVDiaLogAmBao.getNameMusic());
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
        public TextView nameMusic;
        public RadioButton rbMusic;
    }

    public interface OnDialogListClickListener {
        void onItemClick(int position);
    }
}
