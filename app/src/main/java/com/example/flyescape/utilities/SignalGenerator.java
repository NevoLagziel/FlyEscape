package com.example.flyescape.utilities;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;



public class SignalGenerator {

    private static SignalGenerator instance;
    private Context context;
    private static Vibrator vibrator;

    private SignalGenerator(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SignalGenerator(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static SignalGenerator getInstance() {
        return instance;
    }

    public void toast(String text, int length) {
        Toast
                .makeText(context, text, length)
                .show();
    }

    public void vibrate(long length) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(length);
        }
    }

    public void sound(int raw) {
        MediaPlayer.create(context, raw).start();
    }

    public MediaPlayer backgroundSound(int raw){
        MediaPlayer mediaPlayer = MediaPlayer.create(context,raw);
        mediaPlayer.setVolume(0.3f,0.3f);
        mediaPlayer.setLooping(true);
        return mediaPlayer;
    }

}

