package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.adapters.PhoneNumberAdapter;
import com.tinnhantet.nhantin.hengio.database.sharedprf.SharedPrefsImpl;
import com.tinnhantet.nhantin.hengio.database.sqlite.MessageDatabaseHelper;
import com.tinnhantet.nhantin.hengio.databinding.ActivityAddMsgBinding;
import com.tinnhantet.nhantin.hengio.listeners.OnDataClickListener;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.models.Message;
import com.tinnhantet.nhantin.hengio.services.MessageService;
import com.tinnhantet.nhantin.hengio.ui.dialogs.ContactOptionDialog;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.DateTimeUtil;
import com.tinnhantet.nhantin.hengio.utils.Navigator;
import com.tinnhantet.nhantin.hengio.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMsgActivity extends AppCompatActivity implements View.OnClickListener, OnDataClickListener<Contact> {

    private static final String CONTACT_OPTION_DIALOG = "CONTACT_OPTION_DIALOG";
    private ActivityAddMsgBinding mBinding;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Navigator mNavigator;
    private List<Contact> mContactSelected;
    private PhoneNumberAdapter mAdapter;
    private int mPosSelected;
    private SharedPrefsImpl mSharedPrefs;
    private Calendar mMyCalendar;
    private int mHour, mMinute, mYear, mMonth, mDay;
    private PendingIntent pIntent;
    private AlarmManager aManager;
    private boolean mIsEdit;
    private boolean mIsForward;
    private Message mMessageEdit;
    private Message mMessageComeback;
    private MessageDatabaseHelper mHelper;
    private List<Contact> typeContact;

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
                    mAdapter = new PhoneNumberAdapter(AddMsgActivity.this, mContactSelected);
                    mAdapter.setOnContactListener(AddMsgActivity.this);
                    StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL);
                    mBinding.rvNumbers.setLayoutManager(gridLayoutManager);
                    mBinding.rvNumbers.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mBinding.rvNumbers.setVisibility(View.VISIBLE);
                } else {
//                    String number = mBinding.edtPhoneNumber.getText().toString();
//                    if (number.length() == 10) {
//                        if (mContactSelected == null) {
//                            mContactSelected = new ArrayList<>();
//                        }
//                        int size = mContactSelected.size();
//                        boolean check = false;
//                        if (size > 0) {
//                            for (int i = 0; i < size; i++) {
//                                if (mContactSelected.get(i).getPhone().equals(number)) {
//                                    check = true;
//                                    break;
//                                }
//                            }
//                        }
//
//                        if (!check) {
//                            Contact contact = new Contact(0, "", number);
//                            contact.setSelected(true);
//                            mContactSelected.add(contact);
//                            typeContact.add(contact);
//                            mAdapter.notifyItemInserted(mContactSelected.size() - 1);
//                            mBinding.edtPhoneNumber.clearFocus();
//                            mBinding.edtPhoneNumber.setText("");
//                        }
//                    } else {
//                        mNavigator.showToast(R.string.invalid_phone);
//                    }

                }
            }
        });

        mBinding.edtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String num = mBinding.edtPhoneNumber.getText().toString();
                    if (mContactSelected == null) {
                        mContactSelected = new ArrayList<>();
                    }
                    if (num.isEmpty() && mContactSelected.size() == 0) {
                        mNavigator.showToast(R.string.contact_empty);
                        mBinding.edtPhoneNumber.requestFocus();
                    } else if (!num.isEmpty()) {
                        validatePhoneNumber();
                    }

                }
            }
        });
        mBinding.edtPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO) {
                    validatePhoneNumber();
                    return false;
                }
                return false;
            }
        });
    }

    private boolean validatePhoneNumber() {
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        String num = mBinding.edtPhoneNumber.getText().toString();
        if (num.matches(pattern)) {
            boolean ok = true;
            if (mContactSelected == null) {
                mContactSelected = new ArrayList<>();
            }
            int size = mContactSelected.size();
            for (int i = 0; i < size; i++) {
                Contact contact = mContactSelected.get(i);
                String phone = contact.getPhone();
                if (num.equals(phone)) {
                    mNavigator.showToast(contact.getName() + "(" + phone + ")" + "đã có");
                    mBinding.edtPhoneNumber.requestFocus();
                    ok = false;
                    break;
                }
            }
            if (ok) {
                Contact contact = new Contact(0, "", num);
                contact.setSelected(true);
                mContactSelected.add(contact);
                typeContact.add(contact);
                mAdapter.notifyItemInserted(mContactSelected.size() - 1);
                mBinding.edtPhoneNumber.clearFocus();
                mBinding.edtPhoneNumber.setText("");
                if (mIsForward) {
                    mAdapter = new PhoneNumberAdapter(AddMsgActivity.this, mContactSelected);
                    mAdapter.setOnContactListener(AddMsgActivity.this);
                    StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,
                            StaggeredGridLayoutManager.VERTICAL);
                    mBinding.rvNumbers.setLayoutManager(gridLayoutManager);
                    mBinding.rvNumbers.setAdapter(mAdapter);
                    mBinding.rvNumbers.setVisibility(View.VISIBLE);
                }
                hideSoftKeyboard();
            }
            return true;
        } else {
            mNavigator.showToast(R.string.invalid_phone);
            mBinding.edtPhoneNumber.requestFocus();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mBinding.edtPhoneNumber.clearFocus();
        return true;
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_msg);
        mBinding.txtDone.setText(Html.fromHtml(getString(R.string.done)));
        mNavigator = new Navigator(this);
        mSharedPrefs = new SharedPrefsImpl(this);
        mHelper = MessageDatabaseHelper.getInstance(this);
        mMyCalendar = Calendar.getInstance();
        mBinding.edtContent.setMovementMethod(new ScrollingMovementMethod());
        typeContact = new ArrayList<>();
        //set Data to UI when nothing
        mContactSelected = new ArrayList<>();
        mAdapter = new PhoneNumberAdapter(this, mContactSelected);
        mAdapter.setOnContactListener(this);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvNumbers.setLayoutManager(gridLayoutManager);
        mBinding.rvNumbers.setAdapter(mAdapter);
        //getIntent from edit
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(ViewMsgActivity.FORWARD)) {
                    mIsForward = true;
                }
            }

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mMessageEdit = bundle.getParcelable(Constant.EXTRA_MSG);
                if (mMessageEdit != null) {
                    if (!mIsForward) {
                        mIsEdit = true;
                    }
                    initData(mMessageEdit);
                } else {
                    initData(null);
                }
            } else {
                initData(null);
            }

        } else {
            initData(null);
        }
    }

    private void showDatePicker() {
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
        new DatePickerDialog(this, mOnDateSetListener, mYear, mMonth,
                mDay).show();

    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        mBinding.time.setText(mHour + " Giờ : " + mMinute + " Phút");

                    }
                }, mHour, mMinute, true);
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
                if (mBinding.edtPhoneNumber.getText().toString().isEmpty()) {
                    showContactChooser();
                    break;
                }
                if (validatePhoneNumber()) {
                    showContactChooser();
                }

                break;
            case R.id.txt_done:
                hideSoftKeyboard();
                String num = mBinding.edtPhoneNumber.getText().toString();
                if (mContactSelected == null) {
                    mContactSelected = new ArrayList<>();
                }
                if (num.isEmpty() && mContactSelected.size() == 0) {
                    mNavigator.showToast(R.string.contact_empty);
                    mBinding.edtPhoneNumber.requestFocus();
                } else {
                    validatePhoneNumber();
                }
                switch (invalidData()) {
                    case 0:
                        if (mIsEdit) {
                            int id = mMessageEdit.getPendingId();
                            mHelper.deleteMsg(id);
                            Intent i = new Intent(this, MessageService.class);
                            PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), id, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            aManager.cancel(pIntent);
                            doneMsg();
                            Intent intent = new Intent();
                            intent.setClass(this, ViewMsgActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.EXTRA_MSG, mMessageComeback);
                            intent.putExtra(Constant.EXTRA_MSG, bundle);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            break;
                        }
                        if (mIsForward) {
                            doneMsg();
                            ViewMsgActivity.mInstance.finish();
                            mNavigator.startActivity(MainActivity.class);
                            finish();
                            break;
                        }
                        doneMsg();
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


    private void doneMsg() {
        Message message = Message.getMessage(mContactSelected, mBinding.edtContent.getText().toString(), mMyCalendar);
        long pIntentId = mHelper.addMsg(message);
        //add msg to schedule
        message.setPendingId((int) pIntentId);
        sendMsg(message, pIntentId);
        mMessageComeback = message;
    }

    private void sendMsg(Message message, long pId) {
        Intent i = new Intent(this, MessageService.class);
        i.putExtra(Constant.EXTRA_MSG, message);
        pIntent = PendingIntent.getService(getApplicationContext(), (int) pId, i, PendingIntent.FLAG_UPDATE_CURRENT);
        aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        aManager.set(AlarmManager.RTC_WAKEUP, mMyCalendar.getTimeInMillis(), pIntent);
    }

    private int invalidData() {
        if (mContactSelected == null) {
            mContactSelected = new ArrayList<>();
        }
        if (mContactSelected.size() == 0) {
            return 1;
        }
        Calendar calendar = Calendar.getInstance();

        calendar.set(mYear, mMonth, mDay, mHour, mMinute);
        long timeSet = calendar.getTimeInMillis();
        long timeNow = Calendar.getInstance().getTimeInMillis();
        if ((timeSet - timeNow) < (2 * 60 * 1000)) {
            return 2;
        } else {
            mMyCalendar = calendar;
        }
        if (TextUtils.isEmpty(mBinding.edtContent.getText().toString())) {
            return 3;
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CONTACT && resultCode == Activity.RESULT_OK) {
            //ko có sẵn r ->
            if (mContactSelected == null) {
                mContactSelected = new ArrayList<>();
            }
            int size1 = mContactSelected.size();
            if (typeContact.size() == 0) {
                for (int i = 0; i < size1; i++) {
                    Contact contact = mContactSelected.get(i);
                    if (contact.getName().equals("")) {
                        typeContact.add(contact);
                    }
                }
            }

            mContactSelected.clear();
            List<Contact> newContactSelected = new ArrayList<>();
            List<Contact> contacts = data.getParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT);
            int size = contacts.size();
            for (int i = 0; i < size; i++) {
                if (contacts.get(i).isSelected()) {
                    newContactSelected.add(contacts.get(i));
                }
            }

            int sizeOfSelected = typeContact.size();
            int sizeOfNewSelected = newContactSelected.size();
            for (int i = 0; i < sizeOfNewSelected; i++) {
                for (int j = 0; j < sizeOfSelected; j++) {
                    if (newContactSelected.get(i).getPhone().equals(typeContact.get(j).getPhone())) {
                        typeContact.get(j).setSelected(false);
                    }
                }
            }

            //add all none Selected
            for (int i = 0; i < sizeOfSelected; i++) {
                Contact contact = typeContact.get(i);
                if (contact.isSelected()) {
                    newContactSelected.add(contact);
                }
            }
            mContactSelected.addAll(newContactSelected);
            mBinding.edtPhoneNumber.setText(StringUtils.getAllNameContact(mContactSelected));
            mBinding.edtPhoneNumber.clearFocus();
            mAdapter = new PhoneNumberAdapter(this, mContactSelected);
            mAdapter.notifyDataSetChanged();
            mBinding.rvNumbers.setVisibility(View.GONE);
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
        Contact contact = mContactSelected.get(mPosSelected);
        int size = typeContact.size();
        for (int i = 0; i < size; i++) {
            if (typeContact.get(i).getPhone().equals(contact.getPhone())) {
                typeContact.remove(i);
                break;
            }
        }
        mContactSelected.remove(mPosSelected);
        mAdapter.notifyItemRemoved(mPosSelected);
    }

    @Override
    public void onItemClick(Contact contact, int pos) {
        mPosSelected = pos;
        ContactOptionDialog f = ContactOptionDialog.getInstance(contact);
        getSupportFragmentManager().beginTransaction().add(f, CONTACT_OPTION_DIALOG).commit();
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {

        }
    }

    private void initData(Message message) {
        if (message == null) {
            Long time = Calendar.getInstance().getTimeInMillis() + (5 * 60 * 1000);
            String[] dateTimeArr = DateTimeUtil.separateTime(time);
            mHour = Integer.parseInt(dateTimeArr[0]);
            mMinute = Integer.parseInt(dateTimeArr[1]);
            mDay = Integer.parseInt(dateTimeArr[2]);
            mMonth = Integer.parseInt(dateTimeArr[3]) - 1;
            mYear = Integer.parseInt(dateTimeArr[4]);
            mBinding.date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
            mBinding.time.setText(mHour + " Giờ : " + mMinute + " Phút");
            mBinding.edtPhoneNumber.setText("");
            mBinding.edtContent.setText("");
        } else {
            Long time = Long.valueOf(message.getTime());
            String[] dateTimeArr = DateTimeUtil.separateTime(time);
            mHour = Integer.parseInt(dateTimeArr[0]);
            mMinute = Integer.parseInt(dateTimeArr[1]);
            mDay = Integer.parseInt(dateTimeArr[2]);
            mMonth = Integer.parseInt(dateTimeArr[3]) - 1;
            mYear = Integer.parseInt(dateTimeArr[4]);
            mBinding.date.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
            mBinding.time.setText(mHour + " Giờ : " + mMinute + " Phút");
            List<Contact> contacts = StringUtils.getAllContact(message.getListContact());
            mBinding.edtPhoneNumber.setText(StringUtils.getAllNameContact(contacts));
            mBinding.edtContent.setText(message.getContent());
            mContactSelected = contacts;
            mAdapter.notifyDataSetChanged();
            mBinding.rvNumbers.setVisibility(View.VISIBLE);
        }
    }
}
