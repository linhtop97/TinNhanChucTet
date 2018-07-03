package com.example.nttungg.passwordgenarator.screens.homescreen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.screens.listscreen.ListPassActivity;
import com.example.nttungg.passwordgenarator.screens.savescreen.SaveActivity;
import com.example.nttungg.passwordgenarator.utils.Constant;
import com.example.nttungg.passwordgenarator.utils.Utils;

import org.w3c.dom.Text;

/**
 * Home Screen.
 */
public class HomeActivity extends AppCompatActivity implements HomeContract.View,View.OnClickListener {

    public static Intent getHomeIntent(Context context){
        Intent mIntent = new Intent(context,HomeActivity.class);
        return mIntent;
    }

    private HomeContract.Presenter mPresenter;
    private EditText mEditTextResult;
    private TextView mTextViewStrengh;
    private CheckBox mCheckBoxSimilarCharacter;
    private CheckBox mCheckBoxOption;
    private CheckBox mCheckBoxCapital;
    private CheckBox mCheckBoxLowercase;
    private CheckBox mCheckBoxNumber;
    private CheckBox mCheckBoxSign;
    private CheckBox mCheckBoxOptinalCharacter;
    private EditText mEditTextOptinalCharacter;
    private Button mButtonGen;
    private Button mButtonSave;
    private ImageView mImageviewList;
    private NumberPicker mNumberPicker;
    private Boolean isCap;
    private Boolean isLower;
    private Boolean isNumber;
    private Boolean isNotSimilar;
    private Boolean isOption;
    private Boolean isOptionCharacter;
    private Boolean isSign;

    private int mNumberOfCharacter = Constant.defaulNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPresenter = new HomePresenter(this);
        initUI();
        checkBoxListener();
    }

    private void initUI() {
        mEditTextResult = findViewById(R.id.edittext_result);
        mCheckBoxSimilarCharacter = findViewById(R.id.checkBox_similarchar);
        mCheckBoxOption = findViewById(R.id.checkBox_option);
        mCheckBoxCapital = findViewById(R.id.checkBox_capital);
        mCheckBoxLowercase = findViewById(R.id.checkBox_lowercase);
        mCheckBoxNumber = findViewById(R.id.checkBox_number);
        mCheckBoxSign = findViewById(R.id.checkBox_sign);
        mCheckBoxOptinalCharacter = findViewById(R.id.checkBox_optionalchar);
        mEditTextOptinalCharacter = findViewById(R.id.editText_optinal);
        mNumberPicker = findViewById(R.id.numberPicker);
        mImageviewList = findViewById(R.id.imageView_listpass);
        mButtonGen = findViewById(R.id.buttonGen);
        mButtonSave = findViewById(R.id.buttonSave);
        mTextViewStrengh = findViewById(R.id.textView_passstrength);

        mButtonGen.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mCheckBoxSimilarCharacter.setChecked(true);
        mCheckBoxOption.setChecked(true);
        mEditTextOptinalCharacter.setFocusable(false);
        mImageviewList.setOnClickListener(this);
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal)
            {
                mNumberOfCharacter = newVal;
            }
        });
        mNumberPicker.setMaxValue(24);
        mNumberPicker.setMinValue(8);
    }

    @Override
    protected void onResume() {
        mEditTextResult.setText("");
        mTextViewStrengh.setText("");
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonGen:
                checkBoxOption();
                mPresenter.RandomString(isNotSimilar,isCap,isLower,isNumber,isOptionCharacter,isOption,isSign,mNumberOfCharacter);
                break;
            case R.id.buttonSave:
                if (!mEditTextResult.getText().toString().equals("")){
                    String password = mEditTextResult.getText().toString();
                    startActivityForResult(SaveActivity.getSaveIntent(password,this),10);
                }else{
                    Toast.makeText(this, "Generate your key", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageView_listpass:
                startActivity(ListPassActivity.getListIntent(this));
                break;

            case R.id.imageView_closedialog:

        }
    }


    public void checkBoxListener(){
        mCheckBoxOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!mCheckBoxOption.isChecked()){
                    mCheckBoxCapital.setEnabled(false);
                    mCheckBoxLowercase.setEnabled(false);
                    mCheckBoxNumber.setEnabled(false);
                    mCheckBoxSign.setEnabled(false);
                    mCheckBoxOptinalCharacter.setEnabled(false);
                    mEditTextOptinalCharacter.setFocusable(false);
                    setBackGround(mEditTextOptinalCharacter,false);
                }else{
                    mCheckBoxCapital.setEnabled(true);
                    mCheckBoxLowercase.setEnabled(true);
                    mCheckBoxNumber.setEnabled(true);
                    mCheckBoxSign.setEnabled(true);
                    mCheckBoxOptinalCharacter.setEnabled(true);
                    if (mCheckBoxOptinalCharacter.isChecked()){
                        mEditTextOptinalCharacter.setFocusableInTouchMode(true);
                        setBackGround(mEditTextOptinalCharacter,true);
                    }
                }
            }
        });
        mCheckBoxOptinalCharacter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mCheckBoxOptinalCharacter.isChecked()){
                    mEditTextOptinalCharacter.setFocusableInTouchMode(true);
                    setBackGround(mEditTextOptinalCharacter,true);
                }else{
                    mEditTextOptinalCharacter.setFocusable(false);
                    setBackGround(mEditTextOptinalCharacter,false);
                }
            }
        });
    }

    public void setBackGround(EditText editText, boolean isFocus){
        Drawable dw1 = getApplicationContext().getResources().getDrawable(R.drawable.layout_boder_grey);
        Drawable dw2 = getApplicationContext().getResources().getDrawable(R.drawable.layout_boder);
        int sdk = android.os.Build.VERSION.SDK_INT;
        int jellyBean = android.os.Build.VERSION_CODES.JELLY_BEAN;
        if (isFocus){
            if(sdk < jellyBean) {
                editText.setBackgroundDrawable(dw2);
            } else {
                editText.setBackground(dw2);
            }
        }else {
            if(sdk < jellyBean) {
                editText.setBackgroundDrawable(dw1);
            } else {
                editText.setBackground(dw1);
            }
        }

    }

    public void checkBoxOption(){
        if (mCheckBoxCapital.isChecked()){
            isCap = true;
        }else{
            isCap = false;
        }
        if (mCheckBoxLowercase.isChecked()){
            isLower = true;
        }else{
            isLower = false;
        }
        if (mCheckBoxNumber.isChecked()){
            isNumber = true;
        }else{
            isNumber = false;
        }
        if (mCheckBoxSimilarCharacter.isChecked()){
            isNotSimilar = true;
        }else{
            isNotSimilar = false;
        }
        if (mCheckBoxOption.isChecked()){
            isOption  = true;
        }else{
            isOption = false;
        }
        if (mCheckBoxOptinalCharacter.isChecked()){
            isOptionCharacter = true;
        }else{
            isOptionCharacter = false;
        }
        if (mCheckBoxSign.isChecked()){
            isSign = true;
        }else {
            isSign =false;
        }
    }

    @Override
    public String getOptinalString() {
        String optinalString = mEditTextOptinalCharacter.getText().toString();
        return optinalString;
    }

    @Override
    public void randomSuccess(String result) {
        Utils.calculatePasswordStrength(this,result,mTextViewStrengh);
        mEditTextResult.setText(result);
    }

    @Override
    public void showEmptyDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Empty")
                .setMessage("Your characters are empty?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showLengthDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Number too large")
                .setMessage("\n" +
                        "Number Of Your Characters Exceeds Allowed")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
