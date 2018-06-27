package com.example.nttungg.passwordgenarator.screens.loginscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.screens.homescreen.HomeActivity;
import com.example.nttungg.passwordgenarator.utils.Utils;

/**
 * Login Screen.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View,View.OnClickListener {

    private LoginContract.Presenter mPresenter;
    private EditText mEditTextPass;
    private Button mButtonEnter;
    private ImageView mImageViewEye;
    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter();
        if (!Utils.isFirst(this)) {
            toLoginScreen();
            return;
        }
        initUI();
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
