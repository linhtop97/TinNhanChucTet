package com.vdt.trieuvu310198.goldtime;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.vdt.trieuvu310198.goldtime.adapter.ViewpagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewpagerAdapter viewpagerAdapter;
    private String tab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inIt();
        customViewPager();
        seclectTab();
        customToolbar();
    }

    private void inIt() {
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void customViewPager() {
        viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void seclectTab() {
        tab = getIntent().getStringExtra("TABLAYOUT");
        if (tab != null && tab.equals("TIMER")) {
            tabLayout.getTabAt(2).select();
        }
        if (tab != null && tab.equals("ALARM")) {
            tabLayout.getTabAt(0).select();
        }
        if (tab != null && tab.equals("PROMPT")) {
            tabLayout.getTabAt(4).select();
        }
    }
    private void customToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}
