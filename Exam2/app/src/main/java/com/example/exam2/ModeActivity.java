package com.example.exam2;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;

import com.example.exam2.databinding.ActivityModeBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ModeActivity extends AppCompatActivity {

    private ActivityModeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Enable Up Navigation (Ancestral Navigation) in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up ViewPager2 with 3 swipeable tabs
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        String[] tabTitles = new String[]{
                getString(R.string.tab_first),
                getString(R.string.tab_second),
                getString(R.string.tab_third)
        };

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        // PopupMenu for Light / Night / System mode
        binding.buttonSetMode.setOnClickListener(this::showModePopupMenu);

        // Custom Back-stack Behavior for Tabs
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.viewPager.getCurrentItem() != 0) {
                    // On Second or Third tab -> go back to First tab
                    binding.viewPager.setCurrentItem(0, true);
                } else {
                    // On First tab -> return to MainActivity
                    finish();
                }
            }
        });
    }

    private void showModePopupMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_mode, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.mode_light) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            } else if (itemId == R.id.mode_night) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return true;
            } else if (itemId == R.id.mode_system) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
