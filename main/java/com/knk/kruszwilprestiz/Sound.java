package com.knk.kruszwilprestiz;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.Switch;
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

    public enum Type {
        NOTIFICATION, ALARM, RINGTONE
    }

    public Sound(int soundId, Button button, String caption, MediaPlayer mp) {
        this.soundId = soundId;
        this.button = button;
        this.caption = caption;
        this.mp = mp;
    }

    public void play(Context context) {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(context, this.soundId);
        mp.start();
    }

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

    public File download(Context context, File dir, String name) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            file = new File(dir, name + ".mp3");
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

    public void send(Activity activity, Context context, File dir) {
        File file;
        try {
            file = download(context, dir);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.knk.kruszwilprestiz.provider", file);
            ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(uri, "audio/mpeg").build();
            MessengerUtils.shareToMessenger(activity, 321, shareToMessengerParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAs(Activity activity, Context context, File dir, Type type) {

        File file = null;
        try {
           switch (type) {
                case ALARM:
                    file = download(context, dir, Type.ALARM);
                    break;

                case RINGTONE:
                    file = download(context, dir, Type.RINGTONE);
                    break;

                case NOTIFICATION:
                    file = download(context, dir, Type.NOTIFICATION);
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }
}
