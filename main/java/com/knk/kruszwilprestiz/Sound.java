package com.knk.kruszwilprestiz;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
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


    public static final int TYPE_NOTIFICATION = 0;
    public static final int TYPE_RINGTONE = 1;
    public static final int TYPE_ALARM = 2;


    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 13;

    protected int soundId;
    protected boolean isActive;
    protected String content;
    protected Button soundButton;
    protected Uri contentIdRingtone;
    protected Uri contentIdNotification;




    public Sound(int inSoundId){
        soundId = inSoundId;

    }
    public void setContent(String cont){
        content = cont;
    }

    public void activate(){
        isActive = true;
    }

    public void setIsActive(boolean active){
        isActive = active;
    }
    public void setButton(Button btn){
        soundButton = btn;
    }

    public File soundDownload(Context context, File dir, Activity activity, Boolean isNotification) {
        InputStream fileInputStream;
        FileOutputStream fileOutputStream;

        try {

            fileInputStream = context.getResources().openRawResource(soundId);
            File f;
            if(!isNotification) {
                f = new File(dir, (String) soundButton.getText() + ".mp3");
            }else{
                f = new File(dir, (String) soundButton.getText() + " powiadomienie.mp3");
            }

            if(!f.exists() && !f.isDirectory()) {
                f.createNewFile();
            }

            fileOutputStream = new FileOutputStream(f);

            byte[] buffer = new byte[1024];
            int len = fileInputStream.read(buffer);
            while (len != -1) {
                fileOutputStream.write(buffer, 0, len);
                len = fileInputStream.read(buffer);
            }

            fileInputStream.close();
            fileOutputStream.close();
            Toast.makeText(context.getApplicationContext(),"Utworzono plik: \n" + f.getAbsolutePath(),Toast.LENGTH_LONG).show();

            return f;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("tager",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("tager",e.getMessage());
        } catch (Exception e){
            Log.i("tager",e.getMessage());
        }

        return null;
    }


    public void setAs(Context context, File dir, int which, Activity activity){
        
        File f;
        if(which == TYPE_NOTIFICATION) {
            f = soundDownload(context, dir, activity, true);
        }else{
            f = soundDownload(context, dir, activity, false);
        }

        ContentValues values = new ContentValues();

        values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, f.length());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, f.getName());
        values.put(MediaStore.MediaColumns.TITLE,f.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "Kruszwil");
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_RINGTONE, (which==TYPE_RINGTONE)? true: false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, (which==TYPE_NOTIFICATION)? true: false);
        //values.put(MediaStore.Audio.Media.IS_ALARM, (which==TYPE_ALARM)? true: false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(f.getAbsolutePath());

        Uri newUri = context.getContentResolver().insert(uri, values);

        if(newUri == null){
          context.getContentResolver().delete(uri,null,null);
          newUri = context.getContentResolver().insert(uri, values);
        }

        switch (which){
            case TYPE_RINGTONE:
                RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE,newUri);
                Toast.makeText(context,"Ustawiono dzwonek",Toast.LENGTH_LONG).show();
                break;
            case TYPE_NOTIFICATION:
                RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_NOTIFICATION,newUri);
                Toast.makeText(context,"Ustawiono powiadomienie",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void sendSound(Context context, File dir, Activity activity) {

        File file = soundDownload(context, dir, activity,false);
        ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(
                        Uri.fromFile(file),"audio/mpeg"
        ).build();

        MessengerUtils.shareToMessenger(activity, REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
    }
}


