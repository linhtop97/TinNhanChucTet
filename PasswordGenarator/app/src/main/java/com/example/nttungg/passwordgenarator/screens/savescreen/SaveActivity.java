package com.example.nttungg.passwordgenarator.screens.savescreen;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.screens.listscreen.ListPassActivity;
import com.example.nttungg.passwordgenarator.utils.Constant;

/**
 * Save Screen.
 */
public class SaveActivity extends AppCompatActivity implements SaveContract.View,View.OnClickListener {

    public static Intent getSaveIntent(String password,Context context){
        Intent mIntent = new Intent(context,SaveActivity.class);
        mIntent.putExtra(Constant.key_pass_intent,password);
        return mIntent;
    }

    public static Intent getSaveIntent(int i,UserData userData, Context context){
        Intent mIntent = new Intent(context,SaveActivity.class);
        mIntent.putExtra(Constant.key_position_intent, i);
        mIntent.putExtra(Constant.key_user_intent, (Parcelable) userData);
        return mIntent;
    }

    private SaveContract.Presenter mPresenter;
    private Dialog mDialog;
    private String mPassword;
    private String mCategory;
    private boolean isShow = false;

    private UserData mUserData;
    private int mPosition;

    private EditText mEditTextPass;
    private ImageView mImageViewShowHidePass;
    private ImageView mImageViewCat;
    private EditText mEditTextTitle;
    private EditText mEditTextAccount;
    private Button mButtonSave;
    private ImageView mImageViewBack;
    private ImageView mImageViewCopy;

    private ImageView mImgageViewGreen;
    private ImageView mImgageViewRed;
    private ImageView mImgageViewBlue;
    private ImageView mImgageViewGrey;
    private ImageView mImgageViewPurple;
    private ImageView mImgageViewOrange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        mPresenter = new SavePresenter(this);
        mCategory = Constant.color_green;
        Intent setData = new Intent();
        setResult(RESULT_CANCELED, setData);
        initUI();
        getData();
    }

    private void getData(){
        Intent intent = getIntent();
        mPassword = intent.getStringExtra(Constant.key_pass_intent);
        mUserData = intent.getParcelableExtra(Constant.key_user_intent);
        mPosition = intent.getIntExtra(Constant.key_position_intent,0);
        if (mPassword != null){
            mEditTextPass.setText(mPassword);
        }else if(mUserData !=null){
            mEditTextPass.setText(mUserData.getPassword());
            setCategory(mUserData.getCatogory());
            mEditTextTitle.setText(mUserData.getTitle());
            mEditTextAccount.setText(mUserData.getAccount());
        }else{

        }
    }

    private void initUI(){
        mEditTextPass = findViewById(R.id.editText_pass);
        mImageViewShowHidePass = findViewById(R.id.imageView_showhidepass);
        mImageViewCat = findViewById(R.id.imageView_category);
        mEditTextAccount = findViewById(R.id.editText_account);
        mEditTextTitle = findViewById(R.id.edit_title);
        mButtonSave = findViewById(R.id.button_save);
        mImageViewBack = findViewById(R.id.image_back);
        mImageViewCopy = findViewById(R.id.imageView_copypass);
        mImageViewShowHidePass.setOnClickListener(this);
        mImageViewCat.setOnClickListener(this);
        mImageViewCopy.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
    }

    private void initDialog(){
        mImgageViewBlue = mDialog.findViewById(R.id.imageView_blue);
        mImgageViewGreen = mDialog.findViewById(R.id.imageView_green);
        mImgageViewRed = mDialog.findViewById(R.id.imageView_red);
        mImgageViewGrey = mDialog. findViewById(R.id.imageView_grey);
        mImgageViewPurple = mDialog.findViewById(R.id.imageView_purple);
        mImgageViewOrange = mDialog.findViewById(R.id.imageView_orange);
        mImgageViewOrange.setOnClickListener(this);
        mImgageViewBlue.setOnClickListener(this);
        mImgageViewRed.setOnClickListener(this);
        mImgageViewGreen.setOnClickListener(this);
        mImgageViewGrey.setOnClickListener(this);
        mImgageViewPurple.setOnClickListener(this);
    }

    private void showhidePass(){
        if (!isShow){
            mEditTextPass.setTransformationMethod(null);
            isShow = true;
        }else{
            mEditTextPass.setTransformationMethod(new PasswordTransformationMethod());
            isShow = false;
        }
    }

    public void showDialog(){
        mDialog = new Dialog(SaveActivity.this);
        mDialog.setContentView(R.layout.layout_dialog);
        mDialog.show();
        initDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_showhidepass:
                showhidePass();
                break;
            case R.id.imageView_category:
                showDialog();
                break;
            case R.id.imageView_blue:
                mCategory = Constant.color_blue;
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                mDialog.dismiss();
                break;
            case R.id.imageView_green:
                mCategory = Constant.color_green;
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDialog.dismiss();
                break;
            case R.id.imageView_red:
                mCategory = Constant.color_red;
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorRed));
                mDialog.dismiss();
                break;
            case R.id.imageView_grey:
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                mCategory = Constant.color_grey;
                mDialog.dismiss();
                break;
            case R.id.imageView_purple:
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                mCategory = Constant.color_purple;
                mDialog.dismiss();
                break;
            case R.id.imageView_orange:
                mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                mCategory = Constant.color_orange;
                mDialog.dismiss();
                break;
            case R.id.button_save:
                if (mPassword != null){
                    mPresenter.savePassword();
                }else if(mUserData !=null){
                    mPresenter.editUserData(mUserData,mPosition);
                    Intent setData = new Intent();
                    setData.putExtra("data", 0);
                    setResult(RESULT_OK, setData);
                    finish();
                }else{

                }
                break;
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.imageView_copypass:
                copyText(mEditTextPass.getText().toString());
                Toast.makeText(this, "Copied", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void copyText(String copyText){
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copy_text",copyText);
        clipboard.setPrimaryClip(clip);
    }

    public void setCategory(String category){
       if (category.equals(Constant.color_blue)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorBlue));
       }else if(category.equals(Constant.color_green)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
       }else if(category.equals(Constant.color_red)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorRed));
       }else if(category.equals(Constant.color_grey)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorGrey));
       }else if(category.equals(Constant.color_purple)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorPurple));
       }else if(category.equals(Constant.color_orange)){
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorOrange));
       }else{
           mImageViewCat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
       }
    }


    @Override
    public String getMyTitle() {
        return mEditTextTitle.getText().toString();
    }

    @Override
    public String getAccount() {
        return mEditTextAccount.getText().toString();
    }

    @Override
    public String getMyCategory() {
        return mCategory;
    }

    @Override
    public String getPassword() {
        return mEditTextPass.getText().toString();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onWriteSucess() {
        Intent setData = new Intent();
        setResult(RESULT_OK, setData);
        Toast.makeText(this, "Save Data Successed", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onEditSucess() {
        Intent setData = new Intent();
        setResult(RESULT_OK, setData);
        Toast.makeText(this, "Edit Data Successed", Toast.LENGTH_LONG).show();
        finish();
    }

}
