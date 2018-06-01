package com.knk.kruszwilprestiz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Sound {
    private int soundId;
    private Button button;
    private String caption;
    private MediaPlayer mp;

    public Sound(int soundId, Button button, String caption, MediaPlayer mp) {
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

    public File download(Context context, File dir) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            file = new File(dir, (String) button.getText()+".mp3");
            if (!file.exists() && !file.isDirectory()) {
                file.createNewFile();
            }

            is = context.getResources().openRawResource(this.soundId);
            fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int count;

            while((count = is.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, count);
            }

            Toast.makeText(context,"Utworzono plik \n"+file.getAbsolutePath(), Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }

            return file;
        }
    }

    public void send(Activity activity, Context context, File dir) {
        File file;
        try {
            file = download(context, dir);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.knk.kruszwilprestiz.provider", file);
            ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(uri, "audio/mpeg").build();
            MessengerUtils.shareToMessenger(activity, 321,shareToMessengerParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
