package com.password.creator.passwordgenerator.screens.loginscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.password.creator.passwordgenerator.R;
import com.password.creator.passwordgenerator.screens.homescreen.HomeActivity;
import com.password.creator.passwordgenerator.utils.Utils;

/**
 * Login Screen.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View,View.OnClickListener {
    private EditText mEditTextPass;
    private Button mButtonEnter;
    private ImageView mImageViewEye;
    private ImageView mImageAppIcon;
    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!Utils.isFirst(this)) {
            toLoginScreen();
            return;
        }
        initUI();
        editTextFilter();
    }

    public void editTextFilter(){
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String filtered = "";
                for (int i = start; i < end; i++) {
                    char character = source.charAt(i);
                    if (!Character.isWhitespace(character)) {
                        filtered += character;
                    }
                }
                return filtered;
            }

        };
        mEditTextPass.setFilters(new InputFilter[] { filter });
    }

    private void toLoginScreen() {
        startActivity(HomeActivity.getHomeIntent(this));
        finish();
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

    private void initUI() {
        mEditTextPass = findViewById(R.id.editText_mypass);
        mButtonEnter = findViewById(R.id.button_enter);
        mImageViewEye = findViewById(R.id.imageView_eyelogin);
        mImageAppIcon = findViewById(R.id.imageView_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageAppIcon.setElevation(10);
        }
        mImageViewEye.setOnClickListener(this);
        mButtonEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_enter:
                if (mEditTextPass.getText().toString().equals("")){
                    Toast.makeText(this,"Enter Your Pass", Toast.LENGTH_LONG).show();
                }else{
                    Utils.setFirstTime(this,mEditTextPass.getText().toString());
                    startActivity(HomeActivity.getHomeIntent(this));
                    Toast.makeText(this,"Success", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case R.id.imageView_eyelogin:
                showhidePass();
                break;
        }
    }
}
