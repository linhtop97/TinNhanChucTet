package com.example.nttungg.passwordgenarator.screens.listscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.models.sources.DataSource;
import com.example.nttungg.passwordgenarator.screens.savescreen.SaveActivity;
import com.example.nttungg.passwordgenarator.utils.Constant;
import com.example.nttungg.passwordgenarator.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nttungg on 6/22/18.
 */

public class DataUserAdapter extends RecyclerView.Adapter<DataUserAdapter.ViewHolder>{
    private ArrayList<UserData> mUserDatas = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private ListPassContract.Presenter mListPassPresenter;
    private Context mContext;
    private Dialog mDialog;
    private ImageView mImageViewClose;
    private Button mButtonEnter;
    private EditText mEditTextPass;
    private TextView mTextViewPassReult;
    private  Intent myIntent = new Intent("remove_window");
    private int mType;


    public DataUserAdapter(ListPassContract.Presenter listPassPresenter,Context context) {
        mContext = context;
        mListPassPresenter = listPassPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View itemview = mLayoutInflater.inflate(R.layout.item_rv_list, parent, false);
        return new ViewHolder(itemview);
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
        private ImageView mImageViewWindow;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.textView_itemTitle);
            mViewCat = itemView.findViewById(R.id.view_category);
            mImageViewEdit = itemView.findViewById(R.id.imageView_edit);
            mImageViewDelete = itemView.findViewById(R.id.imageView_delete);
            mImageViewWindow = itemView.findViewById(R.id.imageView_window);
            mImageViewWindow.setOnClickListener(this);
            mImageViewEdit.setOnClickListener(this);
            mImageViewDelete.setOnClickListener(this);
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
        public void checkPass(int type){
            if (!mEditTextPass.getText().toString().equals("")){
                if (mEditTextPass.getText().toString().equals(Utils.getPass(mContext))){
                    if (type == 1){
                        mListPassPresenter.deleteData(getPosition());
                    }else if (type == 0){
                        mContext.startActivity(SaveActivity.getSaveIntent(getPosition(),mUserData,mContext));
                    }else{
                        mListPassPresenter.showWindow(mUserData);
                    }
                    mDialog.dismiss();
                }else{
                    mTextViewPassReult.setText(Constant.wrong_pass);
                }
            }else{
                mTextViewPassReult.setText(Constant.empty_pass);
            }
        }
        public void showPassDialog(){
            mDialog = new Dialog(mContext);
            mDialog.setContentView(R.layout.layout_dialog_login);
            mDialog.show();
            initDialog();
        }

        private void initDialog() {
            mImageViewClose = mDialog.findViewById(R.id.imageView_closedialog);
            mButtonEnter = mDialog.findViewById(R.id.button_enter);
            mTextViewPassReult = mDialog.findViewById(R.id.textView_resultpass);
            mEditTextPass = mDialog.findViewById(R.id.editText_dilogpass);
            mButtonEnter.setOnClickListener(this);
            mImageViewClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_edit:
                    mContext.sendBroadcast(myIntent);
                    showPassDialog();
                    mType = 0;
                    break;
                case R.id.imageView_delete:
                    mContext.sendBroadcast(myIntent);
                    showPassDialog();
                    mType = 1;
                    break;
                case R.id.imageView_closedialog:
                    mDialog.dismiss();
                    break;
                case R.id.button_enter:
                    checkPass(mType);
                    break;
                case R.id.imageView_window:
                    if (!Constant.is_show){
                        showPassDialog();
                    }else {
                        mListPassPresenter.showWindow(mUserData);
                    }
                    mType = 2;
                    break;
            }
        }
    }
}
