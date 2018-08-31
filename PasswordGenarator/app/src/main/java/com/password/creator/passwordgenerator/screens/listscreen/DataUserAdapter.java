package com.password.creator.passwordgenerator.screens.listscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.password.creator.passwordgenerator.R;
import com.password.creator.passwordgenerator.models.UserData;
import com.password.creator.passwordgenerator.models.sources.DataSource;
import com.password.creator.passwordgenerator.screens.savescreen.SaveActivity;
import com.password.creator.passwordgenerator.utils.Constant;
import com.password.creator.passwordgenerator.utils.Utils;

import java.lang.reflect.Field;
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
        private ImageView mImageViewMore;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.textView_itemTitle);
            mViewCat = itemView.findViewById(R.id.view_category);
            mImageViewMore = itemView.findViewById(R.id.imageView_more);
            mImageViewMore.setOnClickListener(this);
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
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.show();
            initDialog();
        }

        private void initDialog() {
            mButtonEnter = mDialog.findViewById(R.id.button_enter);
            mTextViewPassReult = mDialog.findViewById(R.id.textView_resultpass);
            mEditTextPass = mDialog.findViewById(R.id.editText_dilogpass);
            mButtonEnter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView_more:
                    popUp(v);
                    break;
                case R.id.button_enter:
                    checkPass(mType);
                    break;
            }
        }
        public void popUp(View v){
            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.see_menu:
                            if (!Constant.is_show){
                                showPassDialog();
                            }else {
                                mListPassPresenter.showWindow(mUserData);
                            }
                            mType = 2;
                            return true;
                        case R.id.edit_menu:
                            mContext.sendBroadcast(myIntent);
                            showPassDialog();
                            mType = 0;
                            return true;
                        case R.id.delete_menu:
                            mContext.sendBroadcast(myIntent);
                            showPassDialog();
                            mType = 1;
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.inflate(R.menu.custom_menu);
            Object menuHelper;
            Class[] argTypes;
            try {
                Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                fMenuHelper.setAccessible(true);
                menuHelper = fMenuHelper.get(popupMenu);
                argTypes = new Class[] { boolean.class };
                menuHelper.getClass().getDeclaredMethod("setForceShowIcon" ,
                        argTypes).invoke(menuHelper, true);
            } catch (Exception e) {
                popupMenu.show();
                return;
            }
            popupMenu.show();
        }
    }
}
