package com.tinnhantet.nhantin.hengio.ui.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.databinding.ActivityAddMsgBinding;
import com.tinnhantet.nhantin.hengio.models.Contact;
import com.tinnhantet.nhantin.hengio.utils.Constant;
import com.tinnhantet.nhantin.hengio.utils.Navigator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AddMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddMsgBinding mBinding;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Navigator mNavigator;
    private List<Contact> mContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initAction();
    }

    private void initAction() {
        mBinding.time.setOnClickListener(this);
        mBinding.date.setOnClickListener(this);
        mBinding.time.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnContact.setOnClickListener(this);
    }

    private void initUI() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_msg);
        mBinding.txtDone.setText(Html.fromHtml(getString(R.string.done)));
        mNavigator = new Navigator(this);
        mBinding.edtContent.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getDateSelect(Calendar calendar) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        mBinding.date.setText(sdf.format(calendar.getTime()));
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                getDateSelect(calendar);
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
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "Sáng";

                        } else {
                            AM_PM = "Chiều";
                            hourOfDay = hourOfDay - 12;
                        }
                        mBinding.time.setText(hourOfDay + ":" + minute + " " + AM_PM);
                    }
                }, hour, minute, false);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CONTACT && resultCode == Activity.RESULT_OK) {
            mContacts = data.getParcelableArrayListExtra(Constant.EXTRA_LIST_CONTACT);
            int size = mContacts.size();
            String s = "";
            for (int i = 0; i < size; i++) {
                if (mContacts.get(i).isSelected()) {
                    String name = mContacts.get(i).getName();
                    if (name.equals("")) {
                        name = mContacts.get(i).getPhone();
                    }
                    s += name + "\n";
                }
            }
            mNavigator.showToast(s);
        }
    }

    private void showContactChooser() {
        Intent intent = new Intent();
        intent.setClass(this, ContactActivity.class);
        mNavigator.startActivityForResult(intent, Constant.REQUEST_CODE_CONTACT);
    }
}
