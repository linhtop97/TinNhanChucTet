package com.vdt.trieuvu310198.goldtime.customprompt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.vdt.trieuvu310198.goldtime.R;
import com.vdt.trieuvu310198.goldtime.adapter.DialogBackgroundPromptAdapter;
import com.vdt.trieuvu310198.goldtime.adapter.DialogIconPromptAdapter;
import com.vdt.trieuvu310198.goldtime.adapter.LVDialogMusicAdapter;
import com.vdt.trieuvu310198.goldtime.customview.TimePickerCustom;
import com.vdt.trieuvu310198.goldtime.data.DataPrompt;
import com.vdt.trieuvu310198.goldtime.model.ModelLVDiaLogAmBao;
import com.vdt.trieuvu310198.goldtime.model.PromptModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomPromptActivity extends AppCompatActivity implements View.OnClickListener {
    private CalendarView calendarView;
    private EditText edNotePrompt;

    private TextView tvcalenDar;
    private TextView tvTimepicker;
    private TextView tvRingtore;

    private Button btSave;
    private Button btBack;

    private ImageView imgIcon;

    private CalendarView viewCalendar;
    private TimePickerCustom timePickerCustom;

    private LinearLayout llISelectIcon;
    private LinearLayout llSeclectBackground;
    private LinearLayout llBackround;
    private LinearLayout llRingtore;

    private List<Integer> listIcon;
    private List<String> listBackground;
    private List<ModelLVDiaLogAmBao> listModelDialogRingtore;
    private List<PromptModel> listPrompt;

    private RecyclerView RVIcon;
    private RecyclerView RVBackground;
    private ListView lvRingtore;

    private int positionMusic;
    private int background;
    private int positionIcon;

    private String date;
    private String time;

    private int positionRV;

    private DialogIconPromptAdapter dialogIconPromptAdapter;
    private DialogBackgroundPromptAdapter dialogBackgroundPromptAdapter;
    private LVDialogMusicAdapter adapterLVDialogMusic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_prompt);

        inIt();
        xuli();
        readData();

    }

    private void xuli() {
        listIcon = new ArrayList<>();
        listBackground = new ArrayList<>();
        listModelDialogRingtore = new ArrayList<>();
        listPrompt = new ArrayList<>();

    }

    private void inIt() {
        tvcalenDar = findViewById(R.id.tv_date);
        tvTimepicker = findViewById(R.id.tv_hourandminute);
        tvRingtore = findViewById(R.id.tv_ringtore);
        llISelectIcon = findViewById(R.id.select_ic);
        llSeclectBackground = findViewById(R.id.select_background);
        llBackround = findViewById(R.id.ll_background);
        llRingtore = findViewById(R.id.ll_ringtore);
        edNotePrompt = findViewById(R.id.ed_note);
        imgIcon = findViewById(R.id.img_ic_prompt);
        btSave = findViewById(R.id.bt_luu_prompt);
        btBack = findViewById(R.id.bt_cancel_prompt);


        tvcalenDar.setOnClickListener(this);
        tvTimepicker.setOnClickListener(this);
        llSeclectBackground.setOnClickListener(this);
        llISelectIcon.setOnClickListener(this);
        llRingtore.setOnClickListener(this);
        btSave.setOnClickListener(this);
        btBack.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                showDialogCalendar();
                break;
            case R.id.tv_hourandminute:
                showDialogTimepicker();
                break;
            case R.id.select_ic:
                showDialogIcon();
                break;
            case R.id.select_background:
                showDialogBackground();
                break;

            case R.id.ll_ringtore:
                showDialogRingTore();
                break;

            case R.id.bt_luu_prompt:
                if (date == null) {
                    Toast.makeText(this, "mời chọn ngày", Toast.LENGTH_SHORT).show();
                } else if (time == null) {
                    Toast.makeText(this, "mời chọn giờ", Toast.LENGTH_SHORT).show();
                }
                else {
                    savaData();
                    Intent intent = new Intent("UPDATEPROMPT");
                    sendBroadcast(intent);
                    finish();
                }
                break;

            case R.id.bt_cancel_prompt:
                finish();
                break;
        }
    }

    private void showDialogRingTore() {
        dataRingtore();
        final Dialog dialogRingTore = new Dialog(this);
        dialogRingTore.setContentView(R.layout.lv_dialog_am_bao);
        dialogRingTore.setContentView(R.layout.lv_dialog_am_bao);
        lvRingtore = dialogRingTore.findViewById(R.id.lv_dialog_music);

        //dialog.setCancelable(true);
        dialogRingTore.setCanceledOnTouchOutside(true);

        //bắt sự kiên 2 bt hủy và ok
        dialogRingTore.findViewById(R.id.bt_ok_dialog_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < listModelDialogRingtore.size(); i++) {
                    if (listModelDialogRingtore.get(i).isIscheckedMusic()) {
                        tvRingtore.setText(listModelDialogRingtore.get(i).getNameMusic());
                        positionMusic = i;
                        break;
                    }
                }
                dialogRingTore.dismiss();
            }
        });
        dialogRingTore.findViewById(R.id.bt_huy_dialog_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRingTore.dismiss();
            }
        });

        // set click cho listview

        LVDialogMusicAdapter.OnDialogListClickListener onDialogListClickListener = new LVDialogMusicAdapter.OnDialogListClickListener() {

            @Override
            public void onItemClick(int position) {
                //bắt sự kiện click thì thay đổi radio button
                for (int i = 0; i < listModelDialogRingtore.size(); i++) {
                    if (listModelDialogRingtore.get(i).isIscheckedMusic() == true) {
                        listModelDialogRingtore.get(i).setIscheckedMusic(false);
                        break;
                    }
                }
                listModelDialogRingtore.get(position).setIscheckedMusic(true);
                adapterLVDialogMusic.notifyDataSetChanged();
            }
        };
        adapterLVDialogMusic = new LVDialogMusicAdapter(this, R.layout.item_listview_am_bao, listModelDialogRingtore,
                onDialogListClickListener);
        lvRingtore.setAdapter(adapterLVDialogMusic);
        dialogRingTore.show();
    }

    private void showDialogBackground() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("chọn màu")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //lấy màu
//                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                        //item.setBackgroundDrawble(new ColorDrawable(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        llBackround.setBackgroundDrawable(new ColorDrawable(selectedColor));
                        background = selectedColor;
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void showDialogIcon() {
        //dataIcon();
        final Dialog dialogIcon = new Dialog(this);
        dialogIcon.setContentView(R.layout.dialog_icon_prompt);
        RVIcon = dialogIcon.findViewById(R.id.rv_dialog_icon_prompt);
        DialogIconPromptAdapter.OnItemClickListener onItemClickListener = new DialogIconPromptAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                imgIcon.setImageResource(listIcon.get(position).intValue());
                positionIcon = position;
                dialogIcon.dismiss();
            }
        };
        GridLayoutManager manager = new GridLayoutManager(this, 7);
        dialogIconPromptAdapter = new DialogIconPromptAdapter(listIcon, CustomPromptActivity.this, onItemClickListener);
        RVIcon.setLayoutManager(manager);
        RVIcon.setAdapter(dialogIconPromptAdapter);
        dialogIcon.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDialogTimepicker() {
        final Dialog dialogTimepicker = new Dialog(this);
        dialogTimepicker.setContentView(R.layout.dialog_timepicker_prompt);
        timePickerCustom = dialogTimepicker.findViewById(R.id.timepicker_prompt);
        if (positionRV != -1) {
            timePickerCustom.setHour(Integer.parseInt(tvTimepicker.getText().toString().substring(0, 2)));
            timePickerCustom.setMinute(Integer.parseInt(tvTimepicker.getText().toString().substring(3, 5)));
        }
        dialogTimepicker.findViewById(R.id.bt_ok_dialog_timepicker_prompt).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                time = String.format("%02d", timePickerCustom.getHour()) + ":" + String.format("%02d", timePickerCustom.getMinute());
                tvTimepicker.setText(time);
                dialogTimepicker.dismiss();
            }
        });
        dialogTimepicker.findViewById(R.id.bt_huy_dialog_timepicker_prompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTimepicker.dismiss();
            }
        });

        dialogTimepicker.show();
    }

    private void showDialogCalendar() {
        final Dialog dialogCalendar = new Dialog(this);
        dialogCalendar.setContentView(R.layout.dialog_calendarview_prompt);
        viewCalendar = dialogCalendar.findViewById(R.id.calendar_view);
        if (positionRV != -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(tvcalenDar.getText().toString().substring(6, 10)));
            calendar.set(Calendar.MONTH, Integer.parseInt(tvcalenDar.getText().toString().substring(3,5)) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tvcalenDar.getText().toString().substring(0, 2)));
            viewCalendar.setDate(calendar.getTimeInMillis());
        }
        viewCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, final int i, final int i1, final int i2) {
                Log.e("DATE", i + " " + i1 + " " + i2);
                dialogCalendar.findViewById(R.id.bt_ok_dialog_calendar_prompt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        date = String.format("%02d", i2) + "/" +  String.format("%02d", i1+1)
                                + "/" + String.format("%02d", i);
                        Log.e("DATE", date);
                        tvcalenDar.setText(date);
                        dialogCalendar.dismiss();
                    }
                });
            }
        });

        dialogCalendar.findViewById(R.id.bt_huy_dialog_calendar_prompt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCalendar.dismiss();
            }
        });
        dialogCalendar.show();
    }

    private void dataIcon() {
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
        listIcon.add(new Integer(R.drawable.icon_bell_off));
        listIcon.add(new Integer(R.drawable.ic_menu_gallery));
        listIcon.add(new Integer(R.drawable.ic_menu_camera));
        listIcon.add(new Integer(R.drawable.ic_menu_send));
        listIcon.add(new Integer(R.drawable.ic_trash));
        listIcon.add(new Integer(R.drawable.ic_menu_share));
    }


    private void dataRingtore() {
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("music 1 (mặc định)", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("music 2", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("music 3", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("music 4", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("sound 1", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("sound 2", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("bell 1", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("bell 2", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("bell 3", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("bell 4", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("harp 1", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("harp 2", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("alert", false));
        listModelDialogRingtore.add(new ModelLVDiaLogAmBao("xmas", false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void readData() {
        dataIcon();
        dataRingtore();
        DataPrompt dataPrompt = new DataPrompt();
        if (dataPrompt.loadDataPrompt(this) != null) {
            listPrompt = dataPrompt.loadDataPrompt(this);
        }
        positionRV = getIntent().getIntExtra("POSITION", -1);
        if (positionRV != -1) {
            edNotePrompt.setText(listPrompt.get(positionRV).getNote());
            tvcalenDar.setText(listPrompt.get(positionRV).getCalendar());
            //set day month, year cho calendarview
            tvTimepicker.setText(listPrompt.get(positionRV).getTime());
            Log.e("AAA", Integer.parseInt(listPrompt.get(positionRV).getTime().substring(3, 4)) + "");
            imgIcon.setImageResource(listIcon.get(listPrompt.get(positionRV).getIcon()).intValue());
            llBackround.setBackgroundDrawable(new ColorDrawable(listPrompt.get(positionRV).getBackground()));
            //backgroundFullScreen.setBackgroundColor(Color.parseColor(listBackground.get(listPrompt.get(positionRV).getBackground()).toString()));
            tvRingtore.setText(listModelDialogRingtore.get(listPrompt.get(positionRV).getRingTore()).getNameMusic());
            listModelDialogRingtore.get(listPrompt.get(positionRV).getRingTore()).setIscheckedMusic(true);

            date = listPrompt.get(positionRV).getCalendar();
            time = listPrompt.get(positionRV).getTime();
            positionIcon = listPrompt.get(positionRV).getIcon();
            background = listPrompt.get(positionRV).getBackground();
            positionMusic = listPrompt.get(positionRV).getRingTore();
        } else {
            imgIcon.setImageResource(listIcon.get(0).intValue());
            //llBackround.setBackgroundColor(Color.parseColor(listBackground.get(0).toString()));
            listModelDialogRingtore.get(0).setIscheckedMusic(true);
            tvRingtore.setText(listModelDialogRingtore.get(0).getNameMusic());
        }
    }

    private void savaData() {
        if(positionRV == -1) {
            String note = edNotePrompt.getText().toString();
            PromptModel promptModel = new PromptModel(note, date, time, positionIcon, background, positionMusic, R.drawable.icon_popup_menu);
            DataPrompt dataPrompt = new DataPrompt();
            dataPrompt.addPrompt(this, promptModel);
        } else {
            Log.e("DATE", date);
            String note = edNotePrompt.getText().toString();
            listPrompt.get(positionRV).setNote(note);
            listPrompt.get(positionRV).setCalendar(date);
            listPrompt.get(positionRV).setTime(time);
            listPrompt.get(positionRV).setIcon(positionIcon);
            listPrompt.get(positionRV).setBackground(background);
            listPrompt.get(positionRV).setRingTore(positionMusic);
            DataPrompt dataPrompt = new DataPrompt();
            dataPrompt.saveDataPrompt(this, listPrompt);
        }
    }

}

