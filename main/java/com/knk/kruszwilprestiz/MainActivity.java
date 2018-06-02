package com.knk.kruszwilprestiz;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<View, Sound> soundMap =  new HashMap<>();
    private MediaPlayer mp;
    private File fileSaveDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFileSaveDir();
        // Check whether has the write settings permission or not.
        boolean settingsCanWrite = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            settingsCanWrite = Settings.System.canWrite(this);
        }

        if(!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);
        }else {
            // If has permission then show an alert dialog with message.
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Masz uprawnienia do zmiany ustawień!");
            alertDialog.show();
        }

        addSound(R.id.kupujetensyf, R.raw.kupujetensyf, "Kupuję ten syf, żeby Janusze dostali zawału.");
        addSound(R.id.wsadzrolexa, R.raw.wsadzrolexa,  "Wsadź w dupę Rolexa");
        addSound(R.id.bylynajdrozsze, R.raw.bylynajdrozsze,"Były najdroższe, dlatego je wziąłem");
        addSound(R.id.ekskluzywnewidowisko, R.raw.ekskluzywnewidowisko,  "To już czas na ekskluzywne widowisko");

        addSound(R.id.niczymlord, R.raw.niczymlord,  "Niczym lord");
        addSound(R.id.prestizowo, R.raw.prestizowo,  "Prestiżowo");
        addSound(R.id.zakladamRolexa, R.raw.zakladamroleksa,  "Zakładam Rolexa i wyrywam następną");
        addSound(R.id.szacunek, R.raw.szacunek,  "Nie mając szacunku do pieniędzy nie masz szacunku do samego siebie");
        addSound(R.id.parkowacsamochod, R.raw.parkowacsamochod,  "Samochód należy parkować w takim miejscu, aby każdy zobaczył mój prestiż");
        addSound(R.id.jestemmarek, R.raw.jestemmarek,  "Siema, jestem Marek, mam 16 lat");
        addSound(R.id.prostytutka, R.raw.prostytutka,  "Prostytutka");
        addSound(R.id.blachara, R.raw.blachara,  "Blachara");

        addSound(R.id.stosunek, R.raw.stosunek,  "Codziennie odbywam stosunek seksualny z co najmniej jedną dziewczyną");
        addSound(R.id.lustro, R.raw.lustro,  "Lustra używam do oglądania mojego Rolexa z dwóch stron");
        addSound(R.id.zasmiecackonta, R.raw.zasmiecackonta,  "Nie zamierzam nawet zaśmiecać tym sobie konta");
        addSound(R.id.ledwo300, R.raw.ledwo300,  "Trochę mało prestiżowy, ledwo 300 zł kosztuje");
        addSound(R.id.przystepnacena, R.raw.przystepnacena,  "Cena jak na kalkulator jest bardzo przystępna, bo kosztuje zaledwie półtora tysiąca złotych");
        addSound(R.id.wiekliczba, R.raw.wiekliczba,  "Wiek to tylko liczba");
        addSound(R.id.rolexodmierzaczas, R.raw.rolexodmierzaczas,  "Mój Rolex odmierza czas na odpoczynek");
    }

    private void addSound(int buttonId, int soundId, String caption) {
        Button button = findViewById(buttonId);
        Sound sound = new Sound(soundId, button, caption, this.mp);
        soundMap.put(button, sound);

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createMenu(v);
                return true;
            }
        });
    }

    public void buttonOnClick(View view) {
        soundMap.get(view).play(this);
    }

    private void createMenu(final View view) {
        CharSequence[] options = {"POBIERZ", "WYŚLIJ MESSENGEREM", "USTAW JAKO DZWONEK", "USTAW JAKO POWIADOMIENIE", "USTAW JAKO DŹWIĘK ALARMU"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz opcję:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        try {
                            soundMap.get(view).download(getApplicationContext(), fileSaveDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 1:
                        soundMap.get(view).send(MainActivity.this, getApplicationContext(), fileSaveDir);
                        break;

                    case 2:
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.RINGTONE);
                        break;

                    case 3:
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.NOTIFICATION);
                        break;

                    case 4:
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.ALARM);
                        break;
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getFileSaveDir() {
        boolean isK = true;
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };

        for(String s: permissions) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(),s)!= PackageManager.PERMISSION_GRANTED) {
                isK = false;
                break;
            }
        }

        if(isK) {
            getSaveDir();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 123);
        }
    }


    private void getSaveDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Kruszwil Soundboard");
        if(!file.mkdirs()) {
            Log.i("halko", "Directory not created :c");
        }

        fileSaveDir = file;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
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
