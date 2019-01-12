package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tinnhantet.nhantin.hengio.Ads;
import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.ContactAdapter;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsKey;
import com.tinnhantet.nhantin.hengio.databinding.ActivityContactBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnItemClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ConfirmCancelDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemClickListener {
    private static final String CANCEL_DIALOG = "CANCEL_DIALOG";
    private ActivityContactBinding mBinding;
    private List<Contact> mContacts;
    private List<Contact> mContactSelected;
    private boolean mIsHasContactSelected;
    private ContactAdapter mAdapter;
    private int mSize;
    private Navigator mNavigator;
    private SharedPrefsImpl mSharedPrefs;
    private boolean isHasContactDefault;
    private int isSelectedStyle = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initAction();
    }

    private void initAction() {
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnOption.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
        mBinding.btnDone.setOnClickListener(this);
        mBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.getFilter().filter(s);
                    }
                }, 100);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        mContactSelected = new ArrayList<>();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact);
        Glide.with(this)
                .load(R.drawable.bg_main)
                .into(mBinding.imgBackground);
        mBinding.btnDone.setText(getString(R.string.done));
        mContacts = mSharedPrefs.getListContact(SharedPrefsKey.KEY_LIST_CONTACT);
        if (mContacts == null) {
            mContacts = new ArrayList<>();
        }
        Intent intent = getIntent();
        List<Contact> contactReceive = intent.getParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT);
        int sizeRoot = mContacts.size();
        if (contactReceive == null) {
            contactReceive = new ArrayList<>();
        }
        int x = 0;
        int sizeSend = contactReceive.size();
        if (sizeRoot != 0 && sizeSend != 0) {
            for (int i = 0; i < sizeRoot; i++) {
                for (int j = 0; j < sizeSend; j++) {
                    if (mContacts.get(i).getPhone().equals(contactReceive.get(j).getPhone())) {
                        mContacts.get(i).setSelected(true);
                        isHasContactDefault = true;
                        x++;
                    }
                }
            }
        }
        if (x == sizeRoot) {
            isSelectedStyle = 0;
            mBinding.btnOption.setImageResource(R.drawable.ic_check_tick_white);
        }
        if (0 < x && x < sizeRoot) {
            isSelectedStyle = 1;
            mBinding.btnOption.setImageResource(R.drawable.ic_check_remove);
        } else {
            isSelectedStyle = 2;
            mBinding.btnOption.setImageResource(R.drawable.ic_check_no_white);
        }

        mSize = mContacts.size();
        mSharedPrefs.putListContact(mContacts, SharedPrefsKey.KEY_LIST_CONTACT_HOLDER);
        mAdapter = new ContactAdapter(this, mContacts);
        mAdapter.setOnItemClickListener(this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleView.setAdapter(mAdapter);
        Ads.f(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (checkHasSelected()) {
                    showDialogConfirm();
                } else {
                    finish();
                }
                break;
            case R.id.btn_option:
                switch (isSelectedStyle) {
                    case 0:
                    case 1:
                        isSelectedStyle = 2;
                        mBinding.btnOption.setImageResource(R.drawable.ic_check_no_white);
                        mAdapter.removeSelectedAll();
                        break;
                    case 2:
                        mAdapter.setSelectedAll();
                        isSelectedStyle = 0;
                        mBinding.btnOption.setImageResource(R.drawable.ic_check_tick_white);
                        break;
                }

                break;
            case R.id.btn_cancel:
                if (checkHasSelected()) {
                    showDialogConfirm();
                } else {
                    finish();
                }
                break;
            case R.id.btn_done:
                mContactSelected = mAdapter.getContactSelected();
                int size = mContactSelected.size();
                if (size > 0) {
                    Intent intent = new Intent();
                    intent.setClass(this, AddMsgActivity.class);
                    intent.putParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT, (ArrayList<? extends Parcelable>) mContactSelected);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    mNavigator.showToast(R.string.plz_select_phone_number);
                }
                break;
        }

    }

    private void showDialogConfirm() {
        ConfirmCancelDialog f = ConfirmCancelDialog.getInstance();
        getSupportFragmentManager().beginTransaction().add(f, CANCEL_DIALOG).commit();
    }

    @Override
    public void onItemClick(int pos) {
    }

    @Override
    public void onBackPressed() {
        if (checkHasSelected()) {
            showDialogConfirm();
        } else {
            super.onBackPressed();
        }
    }

    private boolean checkHasSelected() {
        mContactSelected = mAdapter.getContacts();
        if (mContactSelected.size() > 0) {
            mIsHasContactSelected = true;
        } else {
            if (isHasContactDefault) {
                mIsHasContactSelected = true;
            }
        }

        return mIsHasContactSelected;
    }

    public void setCheckedImage(int style) {
        isSelectedStyle = style;
        switch (style) {
            case 0:
                mBinding.btnOption.setImageResource(R.drawable.ic_check_tick_white);
                break;
            case 1:
                mBinding.btnOption.setImageResource(R.drawable.ic_check_remove);
                break;
            case 2:
                mBinding.btnOption.setImageResource(R.drawable.ic_check_no_white);
                break;
        }
    }
}
