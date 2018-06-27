package com.example.nttungg.passwordgenarator.screens.listscreen;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
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

import com.example.nttungg.passwordgenarator.R;
import com.example.nttungg.passwordgenarator.models.UserData;
import com.example.nttungg.passwordgenarator.screens.services.WindowService;
import com.example.nttungg.passwordgenarator.utils.Constant;
import com.example.nttungg.passwordgenarator.utils.Utils;

import java.util.ArrayList;

/**
 * ListPass Screen.
 */
public class ListPassActivity extends AppCompatActivity implements ListPassContract.View,DataUserAdapter.ItemClickListener,
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

    private Dialog mPassDialog;
    private ImageView mImageViewClose;
    private Button mButtonEnter;
    private EditText mEditTextPass;
    private TextView mTextViewPassReult;
    private ArrayList<UserData> mUserData = new ArrayList<>();
    private ArrayList<UserData> mUserDataCat = new ArrayList<>();
    public static boolean isResume;

    private CheckBox mCheckBoxGreen;
    private CheckBox mCheckBoxRed;
    private CheckBox mCheckBoxBlue;
    private CheckBox mCheckBoxGrey;
    private CheckBox mCheckBoxOrange;
    private CheckBox mCheckBoxPurple;
    private Button mButtonSelect;
    private boolean isCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpass);
        isCategory  = false;
        mRecyclerView = findViewById(R.id.recycler_list);
        mToolbar = findViewById(R.id.toolbar_display);
        mPresenter = new ListPassPresenter(this);
        mDataUserAdapter = new DataUserAdapter(mPresenter,this,this);
        mRecyclerView.setAdapter(mDataUserAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        initToolbar();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 15) {
            if(resultCode == RESULT_CANCELED){
               isResume = false;
            }
        }
    }

    @Override
    protected void onStop() {
        isResume = true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isResume){
            showPassDialog();
        }
    }

    @Override
    protected void onResume() {
        mPresenter.readData();
        super.onResume();
    }

    public void showPassDialog(){
        mPassDialog = new Dialog(ListPassActivity.this);
        mPassDialog.setContentView(R.layout.layout_dialog_login);
        mPassDialog.show();
        mPassDialog.setCancelable(false);
        mPassDialog.setCanceledOnTouchOutside(false);
        initPassDialog();
    }

    private void initPassDialog() {
        mImageViewClose = mPassDialog.findViewById(R.id.imageView_closedialog);
        mButtonEnter = mPassDialog.findViewById(R.id.button_enter);
        mTextViewPassReult = mPassDialog.findViewById(R.id.textView_resultpass);
        mEditTextPass = mPassDialog.findViewById(R.id.editText_dilogpass);
        mImageViewClose.setVisibility(View.GONE);
        mButtonEnter.setOnClickListener(this);
    }

    public void checkPass(){
        if (!mEditTextPass.getText().toString().equals("")){
            if (mEditTextPass.getText().toString().equals(Utils.getPass(this))){
                mPassDialog.dismiss();
            }else{
                mTextViewPassReult.setText(Constant.wrong_pass);
            }
        }else{
            mTextViewPassReult.setText(Constant.empty_pass);
        }
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        mToolbar.setTitleTextAppearance(this, R.style.MyAppearance);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        mSearchView.setOnQueryTextListener(this);
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
                mDataUserAdapter.replaceData(mUserData);
                isCategory = false;
                break;
            case R.id.item_category:
                isCategory = true;
                showDialog();
                break;
        }
        return true;
    }

    public void showWindow(UserData userData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
            } else {
                startService(WindowService.getServiceIntent(this,userData));
            }
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
        if (isCategory){
           mDataUserAdapter.replaceData(mUserDataCat);
        }else{
            mDataUserAdapter.replaceData(mUserData);
        }
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
    public void onItemClicked(UserData userData) {
        showWindow(userData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_select:
                mUserDataCat = new ArrayList<>();
                checkBoxOption();
                mDialog.dismiss();
                break;
            case R.id.button_enter:
                checkPass();
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
    public boolean onQueryTextChange(String newText) {
        isCategory = true;
        mUserDataCat = new ArrayList<>();
        mPresenter.setNewPositon();
        mPresenter.searchTitle(mUserDataCat,newText);
        mDataUserAdapter.replaceData(mUserDataCat);
        return false;
    }
}
