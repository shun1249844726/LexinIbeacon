package com.lexinsmart.xushun.lexinibeacon;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.lexinsmart.xushun.lexinibeacon.ui.fragment.ApplicationScenariosFragment;
import com.lexinsmart.xushun.lexinibeacon.ui.fragment.DeviceFragment;
import com.lexinsmart.xushun.lexinibeacon.ui.adapter.ViewPagerAdapter;
import com.lexinsmart.xushun.lexinibeacon.ui.fragment.ScanListFragment;
import com.lexinsmart.xushun.lexinibeacon.utils.file.FileUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MenuItem menuItem;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);


                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);


                    return true;
            }
            return false;
        }

    };
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (menuItem != null) {
                menuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }
            menuItem = navigation.getMenu().getItem(position);
            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Title
        toolbar.setTitle("");

        setSupportActionBar(toolbar);


        mViewPager = (ViewPager) findViewById(R.id.vp_main_content);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        setupViewPager(mViewPager);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                    } else {

                    }
                });

        String filePath = Environment.getExternalStorageDirectory() + "/bluetoothdata.txt";
        String filePathKalman = Environment.getExternalStorageDirectory() + "/bluetoothdataKalman.txt";
        String filePathAve = Environment.getExternalStorageDirectory() + "/bluetoothdataAve.txt";
        FileUtils.createIfNotExist(filePath);
        FileUtils.writeString(filePath, "", "utf-8");
        FileUtils.createIfNotExist(filePathKalman);
        FileUtils.writeString(filePathKalman, "", "utf-8");

        FileUtils.createIfNotExist(filePathAve);
        FileUtils.writeString(filePathAve, "", "utf-8");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ScanListFragment.newInstance("1"));
        adapter.addFragment(DeviceFragment.newInstance("2"));
        adapter.addFragment(ApplicationScenariosFragment.newInstance("3r"));

        viewPager.setAdapter(adapter);
    }
}
