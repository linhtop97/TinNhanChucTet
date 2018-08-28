package com.example.nttungg.passwordgenarator.screens.listscreen;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.screens.savescreen.SaveActivity;
import com.example.nttungg.passwordgenarator.screens.services.WindowService;
import com.example.nttungg.passwordgenarator.utils.Constant;
import com.example.nttungg.passwordgenarator.utils.Utils;

import java.util.ArrayList;

/**
 * ListPass Screen.
 */
public class ListPassActivity extends AppCompatActivity implements ListPassContract.View,
        View.OnClickListener,SearchView.OnQueryTextListener {

    public static Intent getListIntent(Context context){
        Intent mIntent = new Intent(context,ListPassActivity.class);
        return mIntent;
    }

    private ListPassContract.Presenter mPresenter;
    private DataUserAdapter mDataUserAdapter;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private Dialog mDialog;
    private SearchView mSearchView;
    private ImageView mImageViewBack;
    private TextView mTextViewEmpty;

    private ArrayList<UserData> mUserData = new ArrayList<>();
    private ArrayList<UserData> mUserDataCat = new ArrayList<>();

    private CheckBox mCheckBoxGreen;
    private CheckBox mCheckBoxRed;
    private CheckBox mCheckBoxBlue;
    private CheckBox mCheckBoxGrey;
    private CheckBox mCheckBoxOrange;
    private CheckBox mCheckBoxPurple;
    private Button mButtonSelect;
    private boolean isCategory;

    private UserData mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpass);
        isCategory  = false;
        mRecyclerView = findViewById(R.id.recycler_list);
        mTextViewEmpty = findViewById(R.id.textView_list_empty);
        mToolbar = findViewById(R.id.toolbar_display);
        mPresenter = new ListPassPresenter(this);
        mPresenter.readData();
        mDataUserAdapter = new DataUserAdapter(mPresenter,this);
        mRecyclerView.setAdapter(mDataUserAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        initToolbar();
    }


    @Override
    protected void onResume() {
        mPresenter.readData();
        super.onResume();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        mImageViewBack = findViewById(R.id.image_back_listpass);
        mImageViewBack.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        mSearchView.setOnQueryTextListener(this);

        View searchplate = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchplate.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.MULTIPLY);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    mDataUserAdapter.replaceData(mUserData);
                    mUserDataCat = new ArrayList<>();
                    isCategory = false;
                    return true; // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    mPresenter.setNewPositon();
                    return true;
                }
            });
        } else {
            mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mDataUserAdapter.replaceData(mUserData);
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_all:
                mPresenter.readData();
                isCategory = false;
                break;
            case R.id.item_category:
                isCategory = true;
                showDialog();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 29) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showPermisionDialog();
                } else {
                    startService(WindowService.getServiceIntent(this,mUser));
                }
            }
        }
    }

    public void showPermisionDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        } else {
            ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.MyDialogTheme);
            builder = new AlertDialog.Builder(ctw);
        }
        builder.setTitle("Permission")
                .setMessage("\n" +
                        "Grant Permission To Use Floating View")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())),29);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(getResources().getDrawable(R.drawable.ic_warning))
                .show();
    }

    public void showWindow(UserData userData) {
        mUser = userData;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())),29);
            } else {
                startService(WindowService.getServiceIntent(this,userData));
            }
        }else{
            startService(WindowService.getServiceIntent(this,userData));
        }
    }

    private void initDialog(){
       mCheckBoxBlue = mDialog.findViewById(R.id.checkBox_blue);
       mCheckBoxGreen = mDialog.findViewById(R.id.checkBox_green);
       mCheckBoxRed = mDialog.findViewById(R.id.checkBox_red);
       mCheckBoxGrey = mDialog.findViewById(R.id.checkBox_grey);
       mCheckBoxOrange = mDialog.findViewById(R.id.checkBox_orange);
       mCheckBoxPurple = mDialog.findViewById(R.id.checkBox_purple);
       mButtonSelect = mDialog.findViewById(R.id.button_select);
       mButtonSelect.setOnClickListener(this);
    }

    public void showDialog(){
        mDialog = new Dialog(ListPassActivity.this);
        mDialog.setContentView(R.layout.layout_dialog_menu);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
        initDialog();
    }
    public void checkBoxOption(){
        isCategory = true;
        mPresenter.setNewPositon();
        if (mCheckBoxBlue.isChecked()){
            mPresenter.searchCategory(mUserDataCat,Constant.color_blue);
        }
        if(mCheckBoxPurple.isChecked()) {
            mPresenter.searchCategory(mUserDataCat,Constant.color_purple);
        }
        if(mCheckBoxOrange.isChecked()){
            mPresenter.searchCategory(mUserDataCat,Constant.color_orange);
        }
        if(mCheckBoxGreen.isChecked()){
            mPresenter.searchCategory(mUserDataCat, Constant.color_green);
        }
        if(mCheckBoxRed.isChecked()){
            mPresenter.searchCategory(mUserDataCat, Constant.color_red);
        }
        if(mCheckBoxGrey.isChecked()){
            mPresenter.searchCategory(mUserDataCat, Constant.color_grey);
        }
        mDataUserAdapter.replaceData(mUserDataCat);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void getSuccess(ArrayList<UserData> userData) {
        mUserData = new ArrayList<>();
        mUserData.addAll(userData);
        mDataUserAdapter.replaceData(userData);
    }

    @Override
    public void getFailed() {

    }

    @Override
    public void deleteSuccess(ArrayList<UserData> userData) {
        mUserData = new ArrayList<>();
        mUserData.addAll(userData);
        Toast.makeText(this,"Delete Successed",Toast.LENGTH_SHORT).show();
        if (isCategory){
           mDataUserAdapter.replaceData(mUserDataCat);
           mPresenter.initPosition(mUserDataCat);
        }else{
            mDataUserAdapter.replaceData(mUserData);
        }
        dataEmpty(mPresenter.getPositionList());
    }

    @Override
    public boolean getIsCategory() {
        return isCategory;
    }

    @Override
    public void deleteCategoryData(int i) {
        if (mUserDataCat.size() > 0){
            mUserDataCat.remove(i);
        }
    }

    @Override
    public void showFloatingWindow(UserData userData) {
        showWindow(userData);
    }

    @Override
    public void dataEmpty(ArrayList<Integer> position) {
        if (position.size() > 0){
            mTextViewEmpty.setVisibility(View.INVISIBLE);
        }else{
            mTextViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_select:
                mUserDataCat = new ArrayList<>();
                checkBoxOption();
                mDialog.dismiss();
                break;
            case R.id.image_back_listpass:
                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constant.is_show = false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        isCategory = true;
        mUserDataCat = new ArrayList<>();
        mPresenter.setNewPositon();
        mPresenter.searchTitle(mUserDataCat,newText);
        mDataUserAdapter.replaceData(mUserDataCat);
        return false;
    }
}
