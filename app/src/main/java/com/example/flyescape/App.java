package com.example.flyescape;

import android.app.Application;
import android.media.MediaPlayer;

import com.example.flyescape.utilities.DataManager;
import com.example.flyescape.utilities.MySPv3;
import com.example.flyescape.utilities.SignalGenerator;
import com.google.gson.Gson;

public class App extends Application {

    public static final String KEY = "FlyEscape";

    public static MediaPlayer menuMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        SignalGenerator.init(this);
        MySPv3.init(this);
        String json = MySPv3.getInstance().getString(KEY,"");
        DataManager dataManager = new Gson().fromJson(json,DataManager.class);
        if(dataManager == null){
            DataManager dataManager1 = new DataManager();
            String newJson = new Gson().toJson(dataManager1);
            MySPv3.getInstance().putString(KEY,newJson);
        }
        menuMediaPlayer = SignalGenerator.getInstance().backgroundSound(R.raw.menu_activity_background);
    }
}
