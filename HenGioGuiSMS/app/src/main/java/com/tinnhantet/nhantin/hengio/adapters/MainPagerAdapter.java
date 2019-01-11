package com.tinnhantet.nhantin.hengio.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinnhantet.nhantin.hengio.MyApplication;
import com.tinnhantet.nhantin.hengio.R;
import com.tinnhantet.nhantin.hengio.ui.fragments.PendingFragment;
import com.tinnhantet.nhantin.hengio.ui.fragments.SentFragment;
import com.tinnhantet.nhantin.hengio.utils.TabType;


public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_COUNT = 2;
    private int[] tabIcon = new int[]{R.drawable.ic_loading_click, R.drawable.ic_send};
    private int[] tabTitle = new int[]{R.string.pending, R.string.sent};

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TabType.PENDING:
                return PendingFragment.newInstance();
            case TabType.SENT:
                return SentFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.item_tab, null);
        TextView tv = v.findViewById(R.id.tabContent);
        tv.setText(tabTitle[position]);
        if (position == TabType.SENT) {
            tv.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.colorWitheOpa));
        }
        ImageView img = v.findViewById(R.id.img_icon_tab);
        img.setImageResource(tabIcon[position]);
        return v;
    }

}
