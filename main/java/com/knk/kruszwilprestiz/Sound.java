package com.knk.kruszwilprestiz;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

public class Sound {
    private int soundId;
    private View button;
    private String caption;
    private MediaPlayer mp;

    public Sound(int soundId, View button, String caption, MediaPlayer mp) {
        this.soundId = soundId;
        this.button = button;
        this.caption = caption;
        this.mp = mp;
    }

    public void play(Context context){
        if(mp != null) {
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(context,this.soundId);
        mp.start();
    }
}
