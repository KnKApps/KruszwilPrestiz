package com.knk.kruszwilprestiz;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.Toast;


import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private Map<View, Sound> soundMap = new HashMap<View, Sound>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private boolean isFirstLaunch;
    private Boolean gotPermission = false;
    protected File fileSaveDir;

    private final int DOWNLOAD_PERMISSIONS_REQUEST_CODE = 123;
    private final int SET_AS_PERMISSIONS_REQUEST_CODE = 124;
    private final int SURE_DIALOG_SET_RINGTONE = 0;
    private final int SURE_DIALOG_SET_NOTIFIFCATION = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getPreferences(getApplicationContext().MODE_PRIVATE);
        editor = prefs.edit();
        isFirstLaunch = prefs.getBoolean("isFirstLaunch",true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(isFirstLaunch){
            makeEmptyDialog(R.string.thankYouDialogMessage);
            editor.putBoolean("isFirstLaunch",false);
            editor.commit();

        }

        getFileSaveDir();
        //getSetAsPersmisisons();

        addSound(R.id.kupujetensyf, R.raw.kupujetensyf, true, "Kupuję ten syf, żeby Janusze dostali zawału");
        addSound(R.id.wsadzrolexa, R.raw.wsadzrolexa, true, "Wsadź w dupę Rolexa");
        addSound(R.id.bylynajdrozsze, R.raw.bylynajdrozsze,true,"Były najdroższe, dlatego je wziąłem");
        addSound(R.id.ekskluzywnewidowisko, R.raw.ekskluzywnewidowisko, true, "To już czas na ekskluzywne widowisko");

        addSound(R.id.niczymlord, R.raw.niczymlord, true, "Niczym lord");
        addSound(R.id.prestizowo, R.raw.prestizowo, true, "Prestiżowo");
        addSound(R.id.zakladamRolexa, R.raw.zakladamroleksa, true, "Zakładam Rolexa i wyrywam następną");
        addSound(R.id.szacunek, R.raw.szacunek, true, "Nie mając szacunku do pieniędzy nie masz szacunku do samego siebie");
        addSound(R.id.parkowacsamochod, R.raw.parkowacsamochod, true, "Samochód należy parkować w takim miejscu, aby każdy zobaczył mój prestiż");
        addSound(R.id.jestemmarek, R.raw.jestemmarek, true, "Siema, jestem Marek, mam 16 lat");
        addSound(R.id.prostytutka, R.raw.prostytutka, true, "Prostytutka");
        addSound(R.id.blachara, R.raw.blachara, true, "Blachara");

        addSound(R.id.stosunek, R.raw.stosunek, true, "Codziennie odbywam stosunek seksualny z co najmniej jedną dziewczyną");
        addSound(R.id.lustro, R.raw.lustro, true, "Lustra używam do oglądania mojego Rolexa z dwóch stron");
        addSound(R.id.zasmiecackonta, R.raw.zasmiecackonta, true, "Nie zamierzam nawet zaśmiecać tym sobie konta");
        addSound(R.id.ledwo300, R.raw.ledwo300, true, "Trochę mało prestiżowy, ledwo 300 zł kosztuje");
        addSound(R.id.przystepnacena, R.raw.przystepnacena, true, "Cena jak na kalkulator jest bardzo przystępna, bo kosztuje zaledwie półtora tysiąca złotych");
        addSound(R.id.wiekliczba, R.raw.wiekliczba, true, "Wiek to tylko liczba");
        addSound(R.id.rolexodmierzaczas, R.raw.rolexodmierzaczas, true, "Mój Rolex odmierza czas na odpoczynek");







    }
    protected void buttonOnClick(View view){
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this,soundMap.get(view).soundId);
        mediaPlayer.start();
    }


    private void addSound(int buttonId, int soundId, boolean isActive, String cont){
        Button button = (Button) findViewById(buttonId);
        final Sound sound = new Sound(soundId);
        soundMap.put(button,sound);

        sound.setContent(cont);
        boolean active = prefs.getBoolean(getString(sound.soundId),isActive);
        sound.setIsActive(active);

        sound.setButton((Button) button);

        sound.soundButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                makeLongClickMenu(view);
                return true;
            }
        });

    }

    private void makeEmptyDialog(int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sureDialog(String message, final int dialogCode, final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                switch (dialogCode){
                    case SURE_DIALOG_SET_NOTIFIFCATION:
                        soundMap.get(view).setAs(getApplicationContext(),fileSaveDir, Sound.TYPE_NOTIFICATION,MainActivity.this);
                        break;
                    case SURE_DIALOG_SET_RINGTONE:
                        soundMap.get(view).setAs(getApplicationContext(),fileSaveDir, Sound.TYPE_RINGTONE,MainActivity.this);
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void makeLongClickMenu(final View view){
        CharSequence menuOptions[] = new CharSequence[]{"POBIERZ","WYŚLIJ","USTAW JAKO POWIADOMIENIE", "USTAW JAKO DZWONEK"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("WYBIERZ OPCJE");
        builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        soundMap.get(view).soundDownload(getApplicationContext(),fileSaveDir,MainActivity.this, false);
                        break;
                    case 1:
                        soundMap.get(view).sendSound(getApplicationContext(),fileSaveDir,MainActivity.this);
                        break;
                    case 2:
                        sureDialog("Czy na pewno chcesz ustawić dźwięk " + soundMap.get(view).soundButton.getText() + " jako powiadomienie?",
                               SURE_DIALOG_SET_NOTIFIFCATION,view);
                       // soundMap.get(view).setAs(getApplicationContext(),fileSaveDir, Sound.TYPE_NOTIFICATION,MainActivity.this);
                        break;
                    case 3:
                        sureDialog("Czy na pewno chcesz ustawić dźwięk " + soundMap.get(view).soundButton.getText() + " jako dzwonek?",
                                SURE_DIALOG_SET_RINGTONE,view);
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void getSetAsPersmisisons(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_CONFIGURATION)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_SETTINGS, Manifest.permission.CHANGE_CONFIGURATION, Manifest.permission.MODIFY_AUDIO_SETTINGS},SET_AS_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getFileSaveDir() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS, Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.MODIFY_AUDIO_SETTINGS};

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(MainActivity.this,
                        permissions,DOWNLOAD_PERMISSIONS_REQUEST_CODE);
          //  }
        }else{
            getSaveDir();
        }

    }

    public void getSaveDir(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Kruszwil Soundboard");
        if (!file.mkdirs()) {
            Log.i("tagozaur", "Directory not created");
        }
       fileSaveDir = file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case DOWNLOAD_PERMISSIONS_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSaveDir();
                } else {
                    Toast.makeText(this,
                            "Aby móc pobierać dźwięki, wysyłać je, oraz ustawiać jako dźwięk powiadomień lub dzwonka, musisz udzielić aplikacji odpowiednich uprawnień",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
