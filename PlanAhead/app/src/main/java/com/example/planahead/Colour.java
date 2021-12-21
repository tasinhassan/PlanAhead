package com.example.planahead;


public class Colour
{
    int background;
    int secondary;
    int buttons;
    int title;
    int text;
    int calendarbar;
    public static int type = 1; // 1 for light, 0 for dark

    public Colour()
    {
        lightTheme();
    }


    public  void lightTheme()
    {
        background = R.color.white;
        secondary = R.color.white;
        title = R.color.black;
        text = R.color.grey;
        buttons = R.color.blue;
        calendarbar = R.color.grayer;

        type = 1;
    }

    public void darkTheme()
    {
        background = R.color.darkGray;
        buttons = R.color.white;
        title = R.color.white;
        text = R.color.grey;
        secondary = R.color.otherGray;
        calendarbar = R.color.greenbutton;

        type = 0;
    }

    public void nightshadeTheme()
    {
        background = R.drawable.gradient_10;
        buttons = R.color.white;
        title = R.color.white;
        text = R.color.grey;
        secondary = R.color.navBar;
        calendarbar = R.color.navBar;

        type = 2;
    }

}
