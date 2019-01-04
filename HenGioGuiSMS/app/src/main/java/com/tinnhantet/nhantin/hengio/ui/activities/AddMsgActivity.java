package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.PhoneNumberAdapter;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.databinding.ActivityAddMsgBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ContactOptionDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.DateTimeUtil;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMsgActivity extends AppCompatActivity implements View.OnClickListener, OnDataClickListener<Contact> {

    private static final String CONTACT_OPTION_DIALOG = "CONTACT_OPTION_DIALOG";
    private ActivityAddMsgBinding mBinding;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Navigator mNavigator;
    private List<Contact> mContacts;
    private List<Contact> mContactSelected;
    private PhoneNumberAdapter mAdapter;
    private int mPosSelected;
    private SharedPrefsImpl mSharedPrefs;
    private List<Integer> mPosSelectedPhone;
    private Calendar mMyCalendar;
    private int mHour, mMinute, mYear, mMonth, mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initAction();
    }

    private void initAction() {
        mBinding.txtDone.setOnClickListener(this);
        mBinding.time.setOnClickListener(this);
        mBinding.date.setOnClickListener(this);
        mBinding.time.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnContact.setOnClickListener(this);
        mBinding.edtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBinding.edtPhoneNumber.setText("");
                    mBinding.rvNumbers.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_msg);
        mBinding.txtDone.setText(Html.fromHtml(getString(R.string.done)));
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        mContactSelected = new ArrayList<>();
        mMyCalendar = Calendar.getInstance();
        mBinding.edtContent.setMovementMethod(new ScrollingMovementMethod());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                mBinding.date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
            }

        };
        new DatePickerDialog(this, mOnDateSetListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        mBinding.time.setText(hourOfDay + "h : " + minute + "p");

                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time:
                showTimePicker();
                break;
            case R.id.date:
                showDatePicker();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_contact:
                showContactChooser();
                break;
            case R.id.txt_done:
                hideSoftKeyboard();
                switch (invalidData()) {
                    case 0:
                        MessageDatabaseHelper helper = MessageDatabaseHelper.getInstance(this);
                        helper.addMsg(getMessage(mContactSelected, mBinding.edtContent.getText().toString(), mMyCalendar));
                        finish();
                        break;
                    case 1:
                        mNavigator.showToast(R.string.contact_empty);
                        mBinding.edtPhoneNumber.requestFocus();
                        break;
                    case 2:
                        mNavigator.showToast(R.string.invalid_date);
                        break;
                    case 3:
                        mNavigator.showToast(R.string.content_empty);
                        mBinding.edtContent.requestFocus();
                        break;

                }

                break;
        }
    }

    private int invalidData() {
        if (mContactSelected.size() == 0) {
            return 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay, mHour, mMinute);
        long timeSet = calendar.getTimeInMillis();
        long timeNow = Calendar.getInstance().getTimeInMillis();
        String s = DateTimeUtil.convertTimeToString(Calendar.getInstance().getTimeInMillis());
        if ((timeSet - timeNow) < (60 * 1000)) {
            return 2;
        }else {
            mMyCalendar = calendar;        }
        if (TextUtils.isEmpty(mBinding.edtContent.getText().toString())) {
            return 3;
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CONTACT && resultCode == Activity.RESULT_OK) {
            mContactSelected.clear();
            mContacts = data.getParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT);
            int size = mContacts.size();
            String s = "";
            for (int i = 0; i < size; i++) {
                if (mContacts.get(i).isSelected()) {
                    mContactSelected.add(mContacts.get(i));
                }
            }

            mAdapter = new PhoneNumberAdapter(this, mContactSelected);
            mAdapter.setOnContactListener(this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL);
            mBinding.rvNumbers.setLayoutManager(layoutManager);
            mBinding.rvNumbers.setAdapter(mAdapter);
            mBinding.rvNumbers.setVisibility(View.GONE);
            for (int i = 0; i < mContactSelected.size(); i++) {
                Contact contact = mContactSelected.get(i);
                String name = contact.getName();
                if (name.equals("")) {
                    name = contact.getPhone();
                }
                s += name + ",";
            }
            s = s.substring(0, s.length() - 1);
            mBinding.edtPhoneNumber.setText(s);
            mBinding.edtPhoneNumber.clearFocus();

            //getResources().getDimension(R.dimen._1sdp)
        }
    }

    private void showContactChooser() {
        Intent intent = new Intent();
        intent.setClass(this, ContactActivity.class);
        intent.putParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT, (ArrayList<? extends Parcelable>) mContactSelected);
        mNavigator.startActivityForResult(intent, Constant.REQUEST_CODE_CONTACT);
    }

    public void removeContact() {
        mContactSelected.remove(mPosSelected);
        mAdapter.notifyItemRemoved(mPosSelected);
    }

    @Override
    public void onItemClick(Contact contact, int pos) {
        mPosSelected = pos;
        ContactOptionDialog f = ContactOptionDialog.getInstance(contact);
        getSupportFragmentManager().beginTransaction().add(f, CONTACT_OPTION_DIALOG).commit();
    }

    private Message getMessage(List<Contact> contacts, String content, Calendar c) {
        String listContact = mSharedPrefs.listContactString(contacts);
        Message message = new Message();
        message.setListContact(listContact);
        message.setSend(false);
        message.setTime(String.valueOf(c.getTimeInMillis()));
        message.setContent(content);
        return message;
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {

        }
    }
}
