package com.example.planahead;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment implements ThemeManager{

    FirebaseUser user;
    static FrameLayout fr;
    static TextView name;
    static Button notificationsButton;
    static Button theme;
    static Button signOutBtn;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        name = v.findViewById(R.id.heyName);
        user = HomeActivity.mAuth.getCurrentUser();
        fr = v.findViewById(R.id.backid);
        theme = v.findViewById(R.id.theme);

        String[] n = user.getDisplayName().split(" ");
        name.setText("Hey " + n[0] + "!");

        notificationsButton = v.findViewById(R.id.notificationsButton);
        signOutBtn = v.findViewById(R.id.signOutButton);

        changeThemeColour(HomeActivity.colour);

        notificationsButton.setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), NotificationsActivity.class);
            startActivity(intent);
        });

        signOutBtn.setOnClickListener(c -> signOut());

        theme.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), ThemesActivity.class);
            startActivity(intent);
        });

        return v;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }

    @Override
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            fr.setBackground(getContext().getResources().getDrawable(colour.background));
        }
        else {
            fr.setBackgroundColor(getContext().getResources().getColor(colour.background));
        }


        name.setTextColor(getContext().getResources().getColor(colour.title));
        notificationsButton.setTextColor(getContext().getResources().getColor(colour.title));
        signOutBtn.setTextColor(getContext().getResources().getColor(colour.title));
        theme.setTextColor(getContext().getResources().getColor(colour.title));

        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        String[] t = databaseHandler.getTheme("theme").toArray(new String[0]);
        if(t.length != 0) {
            if (t[0].equals("Light")) {
                notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                theme.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
            } else if (t[0].equals("Dark")){
                notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                theme.setBackground(this.getResources().getDrawable(R.drawable.button_green));
            }
            else if (t[0].equals("Nightshade")){
                notificationsButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                signOutBtn.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                theme.setBackground(this.getResources().getDrawable(R.drawable.button_white));
            }
        }
    }
}
