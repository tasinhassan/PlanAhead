package com.example.planahead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ThemesActivity extends AppCompatActivity implements ThemeManager {

    ConstraintLayout parentLayout;
    TextView title;
    Button defaultTheme;
    Button darkTheme;
    Button gradientOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        parentLayout = findViewById(R.id.parentLayout);
        title = findViewById(R.id.title);
        defaultTheme = findViewById(R.id.defaultTheme);
        darkTheme = findViewById(R.id.darkTheme);
        gradientOne = findViewById(R.id.gradientOne);

        changeThemeColour(HomeActivity.colour);

        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        defaultTheme.setOnClickListener(v -> {
            HomeActivity.colour.lightTheme();
            HomeActivity.bottomNav.setBackground(getResources().getDrawable(R.drawable.white_grey_border_top));
            ColorStateList navMenuIconList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[] {
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.grey),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.grey),
                            getResources().getColor(R.color.black)
                    }
            );
            HomeActivity.bottomNav.setItemIconTintList(navMenuIconList);
            databaseHandler.deleteTheme();
            databaseHandler.insertTheme("Light");
            changeThemeColour(HomeActivity.colour);
        });

        darkTheme.setOnClickListener(v -> {
            HomeActivity.colour.darkTheme();
            HomeActivity.bottomNav.setBackground(getResources().getDrawable(R.drawable.black_grey_border_top));
            ColorStateList navMenuIconList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[] {
                            getResources().getColor(R.color.greenbutton),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.greenbutton),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.greenbutton)
                    }
            );
            HomeActivity.bottomNav.setItemIconTintList(navMenuIconList);
            databaseHandler.deleteTheme();
            databaseHandler.insertTheme("Dark");
            changeThemeColour(HomeActivity.colour);
        });

        gradientOne.setOnClickListener(v -> {
            HomeActivity.colour.nightshadeTheme();
            HomeActivity.bottomNav.setBackground(getResources().getDrawable(R.drawable.nightshade_nav_border));
            ColorStateList navMenuIconList = new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_checked},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_focused},
                            new int[]{android.R.attr.state_pressed}
                    },
                    new int[] {
                            getResources().getColor(R.color.white),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.white),
                            getResources().getColor(R.color.black),
                            getResources().getColor(R.color.white)
                    }
            );
            HomeActivity.bottomNav.setItemIconTintList(navMenuIconList);
            databaseHandler.deleteTheme();
            databaseHandler.insertTheme("Nightshade");
            changeThemeColour(HomeActivity.colour);
        });
    }

    @Override
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            parentLayout.setBackground(getResources().getDrawable(colour.background));
            SettingsFragment.fr.setBackground(getResources().getDrawable(colour.background));
        }
        else {
            parentLayout.setBackgroundColor(getResources().getColor(colour.background));
            SettingsFragment.fr.setBackgroundColor(getResources().getColor(colour.background));
        }

        title.setTextColor(getResources().getColor(colour.title));
        defaultTheme.setTextColor(getResources().getColor(colour.title));
        darkTheme.setTextColor(getResources().getColor(colour.title));
        gradientOne.setTextColor(getResources().getColor(colour.title));


        SettingsFragment.name.setTextColor(getResources().getColor(colour.title));
        SettingsFragment.notificationsButton.setTextColor(getResources().getColor(colour.title));
        SettingsFragment.signOutBtn.setTextColor(getResources().getColor(colour.title));
        SettingsFragment.theme.setTextColor(getResources().getColor(colour.title));


        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        String[] theme = databaseHandler.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                defaultTheme.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                darkTheme.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                gradientOne.setBackground(this.getResources().getDrawable(R.drawable.settings_button));

                SettingsFragment.notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                SettingsFragment.signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                SettingsFragment.theme.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
            } else if (theme[0].equals("Dark")){
                defaultTheme.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                darkTheme.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                gradientOne.setBackground(this.getResources().getDrawable(R.drawable.button_green));

                SettingsFragment.notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                SettingsFragment.signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                SettingsFragment.theme.setBackground(this.getResources().getDrawable(R.drawable.button_green));
            }
            else if (theme[0].equals("Nightshade")) {
                defaultTheme.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                darkTheme.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                gradientOne.setBackground(this.getResources().getDrawable(R.drawable.button_white));

                SettingsFragment.notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                SettingsFragment.signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                SettingsFragment.theme.setBackground(this.getResources().getDrawable(R.drawable.button_white));
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
