package com.example.nttungg.passwordgenarator.screens.listscreen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.models.sources.DataSource;
import com.example.nttungg.passwordgenarator.screens.savescreen.SaveActivity;
import com.example.nttungg.passwordgenarator.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nttungg on 6/22/18.
 */

public class DataUserAdapter extends RecyclerView.Adapter<DataUserAdapter.ViewHolder>{
    private ArrayList<UserData> mUserDatas = new ArrayList<>();
    private ItemClickListener mItemClickListener;
    private LayoutInflater mLayoutInflater;
    private ListPassContract.Presenter mListPassPresenter;
    private Context mContext;

    public DataUserAdapter(ListPassContract.Presenter listPassPresenter,Context context, ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
        mContext = context;
        mListPassPresenter = listPassPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View itemview = mLayoutInflater.inflate(R.layout.item_rv_list, parent, false);
        return new ViewHolder(itemview, mItemClickListener);
    }

    public void replaceData(@NonNull List<UserData> userData) {
        if (mUserDatas == null) {
            mUserDatas = (ArrayList<UserData>) userData;
        } else {
            mUserDatas = new ArrayList<>();
            mUserDatas.addAll(userData);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mUserDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private UserData mUserData;
        private TextView mTextViewTitle;
        private View mViewCat;
        private ImageView mImageViewEdit;
        private ImageView mImageViewDelete;
        ViewHolder(View itemView, final ItemClickListener mItemClickListener) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.textView_itemTitle);
            mViewCat = itemView.findViewById(R.id.view_category);
            mImageViewEdit = itemView.findViewById(R.id.imageView_edit);
            mImageViewDelete = itemView.findViewById(R.id.imageView_delete);
            mImageViewEdit.setOnClickListener(this);
            mImageViewDelete.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClicked(mUserData);
                }
            });
        }
        public void bindData(UserData userData) {
            if (userData != null) {
                mUserData = userData;
                mTextViewTitle.setText(mUserData.getTitle());
                setCategory(userData.getCatogory());
            }
        }


        public void setCategory(String category){
            if (category.equals(Constant.color_blue)){
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue));
            }else if (category.equals(Constant.color_green)){
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            }else if (category.equals(Constant.color_grey)){
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
            }else if (category.equals(Constant.color_red)){
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            }else if (category.equals(Constant.color_purple)){
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorPurple));
            }else{
                mViewCat.setBackgroundColor(mContext.getResources().getColor(R.color.colorOrange));
            }
        }

        public void showDialog(){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(mContext);
            }
            builder.setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mListPassPresenter.deleteData(getPosition());
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_edit:
                    ((Activity) mContext).startActivityForResult(SaveActivity.getSaveIntent(getPosition(),mUserData,mContext),15);
                    break;
                case R.id.imageView_delete:
                    showDialog();
                    break;
            }
        }
    }


    public interface ItemClickListener {
        void onItemClicked(UserData userData);
    }
}
