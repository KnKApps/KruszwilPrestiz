package com.knk.kruszwilprestiz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class Sound {
    //ID of the mp3 file
    private int soundId;
    private Button button;
    private String caption;
    private MediaPlayer mp;
    private boolean isFavourite = false;


    //Types for RingtoneManager
    public enum Type {
        NOTIFICATION, ALARM, RINGTONE, MESSENGER
    }

    public Sound(int soundId, Button button, String caption, MediaPlayer mp) {
        this.soundId = soundId;
        this.button = button;
        this.caption = caption;
        this.mp = mp;
    }

    //Favourites or unfavourites sounds
    public void toggleFavourite(Activity activity) {
        if(this.isFavourite) {
            removeFavourite(activity);
        } else {
            addFavourite(activity);
        }
    }

    //Guess what it does
    public void play(Context context) {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(context, this.soundId);
        mp.start();
    }

    //Saves a mp3 file in a directory created by getSaveDir()
    public File download(Context context, File dir) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            file = new File(dir, (String) button.getText() + ".mp3");
            if (!file.exists() && !file.isDirectory()) {
                file.createNewFile();
            }

            is = context.getResources().openRawResource(this.soundId);
            fos = new FileOutputStream(file);


            byte[] buffer = new byte[1024];
            int count;

            while ((count = is.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, count);
            }

            Toast.makeText(context, "Utworzono plik \n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();


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


    //Overloaded download() method for setAs()
    public File download(Context context, File dir, Type type) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            switch(type){
                case NOTIFICATION:
                    file = new File(dir, button.getText() + " notification.mp3");
                    break;
                case RINGTONE:
                    file = new File(dir, button.getText() + " ringtone.mp3");
                    break;
                case ALARM:
                    file = new File(dir, button.getText() + " alarm.mp3");
                    break;
                case MESSENGER:
                    file = new File(dir, button.getText() + " messenger.mp3");
                    break;
            }

            if (!file.exists() && !file.isDirectory()) {
                file.createNewFile();
            }

            is = context.getResources().openRawResource(this.soundId);
            fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int count;

            while ((count = is.read(buffer, 0, 1024)) != -1) {
                fos.write(buffer, 0, count);
            }




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

    //Sends a sound message via Messenger
    public void send(Activity activity, Context context, File dir) {
        File file;
        try {
            file = download(context, dir, Type.MESSENGER);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.knk.kruszwilprestiz.provider", file);
            ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(uri, "audio/mpeg").build();
            MessengerUtils.shareToMessenger(activity, 321, shareToMessengerParams);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sets a sound as default ringtone, notification or alarm sounds
    public void setAs(Activity activity, Context context, File dir, Type type) {

        File file = null;
        try {
            //Prevents directory cluttering, deletes unused sounds and saves new ones
           switch (type) {
                case ALARM:
                    deleteFiles(dir, type);
                    file = download(context, dir, Type.ALARM);
                    break;

                case RINGTONE:
                    deleteFiles(dir, type);
                    file = download(context, dir, Type.RINGTONE);
                    break;

                case NOTIFICATION:
                    deleteFiles(dir, type);
                    file = download(context, dir, Type.NOTIFICATION);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Creates a map of values necessary for sound files to be set
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
        values.put(MediaStore.MediaColumns.TITLE,file.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "Kruszwil");
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, (type==Type.RINGTONE)? true: false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, (type==Type.NOTIFICATION)? true: false);
        values.put(MediaStore.Audio.Media.IS_ALARM, (type==Type.ALARM)? true: false);



        //Sets sound as selected, double URIs for preventing errors
        switch (type) {
            case RINGTONE:
                Uri ringtoneUri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());

                Uri newRingtoneUri = context.getContentResolver().insert(ringtoneUri, values);

                if(newRingtoneUri == null){
                    context.getContentResolver().delete(ringtoneUri,null,null);
                    newRingtoneUri = context.getContentResolver().insert(ringtoneUri, values);
                }
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newRingtoneUri);
                Toast.makeText(context,"Ustawiono dzwonek",Toast.LENGTH_LONG).show();
                break;

            case NOTIFICATION:
                Uri notificationUri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());

                Uri newNotificationUri = context.getContentResolver().insert(notificationUri, values);

                if(newNotificationUri == null){
                    context.getContentResolver().delete(notificationUri,null,null);
                    newNotificationUri = context.getContentResolver().insert(notificationUri, values);
                }
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newNotificationUri);
                Toast.makeText(context, "Ustawiono dźwięk powiadomień", Toast.LENGTH_LONG).show();
                break;

            case ALARM:
                Uri alarmUri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());

                Uri newAlarmUri = context.getContentResolver().insert(alarmUri, values);

                if(newAlarmUri == null){
                    context.getContentResolver().delete(alarmUri,null,null);
                    newAlarmUri = context.getContentResolver().insert(alarmUri, values);
                }
                RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_ALARM, newAlarmUri);
                Toast.makeText(context, "Ustawiono dźwięk alarmu", Toast.LENGTH_LONG).show();
                break;
        }

        file.delete();
    }

    //Deletes all files of certain type
    public static void deleteFiles(File dir, Type type) {
        String ending = "";

        switch (type){
            case NOTIFICATION:
                ending = "notification.mp3";
                break;

            case ALARM:
                ending = "alarm.mp3";
                break;

            case RINGTONE:
                ending = "ringtone.mp3";
                break;

            case MESSENGER:
                ending = "messenger.mp3";
                break;
        }

        for (File file : dir.listFiles()) {
            if(file.getName().contains(ending)) {
                file.delete();
            }
        }
    }

    //Moves a sound from main_layout to favourites_layout
    public void addFavourite(Activity activity) {
        LinearLayout main_layout = activity.findViewById(R.id.main_layout);
        LinearLayout favourites_layout = activity.findViewById(R.id.favourites_layout);

        main_layout.removeView(this.button);
        favourites_layout.addView(this.button);
        this.isFavourite = true;
        //Save state of this button
        MainActivity.editor.putBoolean(Integer.toString(this.button.getId()), true);
        MainActivity.editor.commit();
        Log.i("halko", Integer.toString(this.button.getId())+" put");

        this.button.setBackgroundResource(R.drawable.button_bg_fav);
    }

    //Moves a sound from favourites_layout to main_layout
    private void removeFavourite(Activity activity) {
        LinearLayout main_layout = activity.findViewById(R.id.main_layout);
        LinearLayout favourites_layout = activity.findViewById(R.id.favourites_layout);

        favourites_layout.removeView(this.button);
        main_layout.addView(this.button);
        this.isFavourite = false;
        //Save state of this button
        MainActivity.editor.putBoolean(Integer.toString(this.button.getId()), false);
        MainActivity.editor.commit();
        Log.i("halko", Integer.toString(this.button.getId())+" remove");

        this.button.setBackgroundResource(R.drawable.button_bg);


    }
}
