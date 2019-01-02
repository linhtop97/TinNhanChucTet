package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.ContactAdapter;
import com.tinnhantet.nhantin.hengio.databinding.ActivityContactBinding;
import com.tinnhantet.nhantin.hengio.listeners.DataCallBack;
import com.tinnhantet.nhantin.hengio.listeners.OnItemClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ConfirmCancelDialog;
import com.tinnhantet.nhantin.hengio.ui.dialogs.LoadingDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemClickListener, DataCallBack<List<Contact>> {
    private static final String CANCEL_DIALOG = "CANCEL_DIALOG";
    private static final String LOADING_DIALOG = "LOADING_DIALOG";
    private ActivityContactBinding mBinding;
    private List<Contact> mContacts;
    private List<Contact> mContactSelected;
    private boolean mIsHasContactSelected;
    private ContactAdapter mAdapter;
    private LoadingDialog mLoadingDialog;
    private int mSize;
    private Navigator mNavigator;

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
    }

    private void initUI() {
        mNavigator = new Navigator(this);
        mContactSelected = new ArrayList<>();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact);
        mBinding.btnDone.setText(Html.fromHtml(getString(R.string.done)));
        showDialogLoading();
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
                //show option menu
                PopupMenu actionMenu = new PopupMenu(this, mBinding.btnOption, Gravity.END | Gravity.BOTTOM);
                actionMenu.inflate(R.menu.options_menu_select);
                actionMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_select_all) {
                            //select all
                            mAdapter.setSelectedAll();
                        } else if (item.getItemId() == R.id.action_unselect_all) {
                            //unselect all
                            mAdapter.removeSelectedAll();
                        }
                        return true;
                    }
                });
                actionMenu.show();
                break;
            case R.id.btn_cancel:
                if (checkHasSelected()) {
                    showDialogConfirm();
                } else {
                    finish();
                }
                break;
            case R.id.btn_done:
                mContactSelected = mAdapter.getContacts();
                Intent intent = new Intent();
                intent.setClass(this, AddMsgActivity.class);
                intent.putParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT, (ArrayList<? extends Parcelable>) mContactSelected);
                setResult(Activity.RESULT_OK, intent);
                finish();
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
        for (int i = 0; i < mSize; i++) {
            if (mContactSelected.get(i).isSelected()) {
                mIsHasContactSelected = true;
                break;
            }
        }
        return mIsHasContactSelected;
    }

    private void showDialogLoading() {
        mLoadingDialog = LoadingDialog.getInstance();
        getSupportFragmentManager().beginTransaction().add(mLoadingDialog, LOADING_DIALOG).commit();
        new Contact().getAllContact(this, this);

    }

    @Override
    public void onDataSuccess(List<Contact> data) {
        mContacts = data;
        mSize = mContacts.size();
        mAdapter = new ContactAdapter(this, mContacts);
        mAdapter.setOnItemClickListener(this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycleView.setAdapter(mAdapter);
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDataFailed(String msg) {

    }
}
