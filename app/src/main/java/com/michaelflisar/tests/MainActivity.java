package com.michaelflisar.tests;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.tests.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        int activities = createSubActivityButtons(binding);
        binding.tvInfo.setText(String.format("Existing MCVE Tests: %d", activities));
    }

    private int createSubActivityButtons(MainActivityBinding binding) {
        int activities = 0;
        try {
            ActivityInfo[] list = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities;
            activities = list.length - 1;
            for (ActivityInfo info : list) {
                if (!info.name.equals(MainActivity.class.getName())) {
                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.main_activity_list_item, binding.llActivities, false);
                    tv.setTag(info);
                    tv.setText(info.loadLabel(getPackageManager()));
                    tv.setOnClickListener(this);
                    binding.llActivities.addView(tv);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return activities;
    }

    @Override
    public void onClick(View v) {
        // no security checks, we know what we get
        ActivityInfo info = (ActivityInfo) v.getTag();
        ComponentName name = new ComponentName(info.packageName, info.name);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(name);
        startActivity(intent);
    }
}