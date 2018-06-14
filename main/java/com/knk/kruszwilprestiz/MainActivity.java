package com.knk.kruszwilprestiz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Map associating buttons and sounds, core of app
    private Map<View, Sound> soundMap =  new HashMap<>();
    private MediaPlayer mp;
    //Directory for app sounds
    private File fileSaveDir;
    //SharedPreferences to save state of an app
    public static SharedPreferences sharedPreferences;
    //Editor for SharedPreferences
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //Navigation bar for Lollipop-and-above users
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorNavBar));

        }

        //Create directory for app sounds
        getFileSaveDir();

        //Delete messenger files
        if(fileSaveDir != null) Sound.deleteFiles(fileSaveDir, Sound.Type.MESSENGER);

        //Associate buttons with sounds
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
        addSound(R.id.sluzacychodz, R.raw.sluzacychodz, "Służacy, chodź");
        addSound(R.id.zapomnialesoczyms, R.raw.zapomnialesoczyms, "Zapomniałeś o czymś czy nie !?");

        addSound(R.id.stosunek, R.raw.stosunek,  "Codziennie odbywam stosunek seksualny z co najmniej jedną dziewczyną");
        addSound(R.id.lustro, R.raw.lustro,  "Lustra używam do oglądania mojego Rolexa z dwóch stron");
        addSound(R.id.zasmiecackonta, R.raw.zasmiecackonta,  "Nie zamierzam nawet zaśmiecać tym sobie konta");
        addSound(R.id.ledwo300, R.raw.ledwo300,  "Trochę mało prestiżowy, ledwo 300 zł kosztuje");
        addSound(R.id.przystepnacena, R.raw.przystepnacena,  "Cena jak na kalkulator jest bardzo przystępna, bo kosztuje zaledwie półtora tysiąca złotych");
        addSound(R.id.wiekliczba, R.raw.wiekliczba,  "Wiek to tylko liczba");
        addSound(R.id.rolexodmierzaczas, R.raw.rolexodmierzaczas,  "Mój Rolex odmierza czas na odpoczynek");

        addSound(R.id.cotyzrobiles, R.raw.cotyzrobiles, "Co Ty zrobiłeś ?!");
        addSound(R.id.donperignon, R.raw.donperignon1, "Don Perignon");
        addSound(R.id.jezykiobce, R.raw.jezykiobce, "Jeżeli nie znacie języków obcych, to wasz problem");
        addSound(R.id.mocneslowa, R.raw.mocneslowa, "Muszę przyznać, że to dosyć mocne słowa jak na dwunastoletnią dziewczynkę");
        addSound(R.id.ocochodzi, R.raw.ocochodzi, "Ej o co Ci chodzi ej ziom");
        addSound(R.id.tanutabuja, R.raw.tanutabuja, "Ta nuta mną buja");
        addSound(R.id.wartoscczlowieka, R.raw.wartoscczlowieka, "Wartość człowieka liczy się w dolarach amerykańskich");
        addSound(R.id.obrzydliwe, R.raw.obrzydliwe, "Jest to obrzydliwe i na samą myśl robi mi się niedobrze");
        addSound(R.id.fekalia, R.raw.fekalia, "Moje fekalia są warte około 200zł");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Delete messenger files]
        if(fileSaveDir != null) Sound.deleteFiles(fileSaveDir, Sound.Type.MESSENGER);
    }

    //Associates button with sound
    private void addSound(int buttonId, int soundId, String caption) {
        Button button = findViewById(buttonId);
        Sound sound = new Sound(soundId, button, caption, this.mp);
        soundMap.put(button, sound);
        boolean isFavourite = sharedPreferences.getBoolean(Integer.toString(buttonId), false);


        if (isFavourite) {
            sound.addFavourite(MainActivity.this);
            Log.i("halko", Integer.toString(buttonId)+" get");
        }


        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createMenu(v);
                return true;
            }
        });
    }

    //Plays a sound, duh
    public void buttonOnClick(View view) {
        soundMap.get(view).play(this);
    }

    //Creates a menu on long click
    private void createMenu(final View view) {
        CharSequence[] options = {"ULUBIONE - DODAJ/USUŃ", "POBIERZ", "WYŚLIJ MESSENGEREM", "USTAW JAKO DZWONEK", "USTAW JAKO POWIADOMIENIE", "USTAW JAKO DŹWIĘK ALARMU"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz opcję:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        soundMap.get(view).toggleFavourite(MainActivity.this);
                        break;

                    case 1:
                        try {
                            soundMap.get(view).download(getApplicationContext(), fileSaveDir);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        soundMap.get(view).send(MainActivity.this, getApplicationContext(), fileSaveDir);
                        break;

                    case 3:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            // Check whether has the write settings permission or not.
                            boolean settingsCanWrite = Settings.System.canWrite(getApplicationContext());
                            //If doesn't have them, reload activity
                            if(!settingsCanWrite) {
                                MainActivity.this.finish();
                                startActivity(getIntent());
                                break;
                            }
                        }
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.RINGTONE);
                        break;

                    case 4:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            // Check whether has the write settings permission or not.
                            boolean settingsCanWrite = Settings.System.canWrite(getApplicationContext());
                            //If doesn't have them, reload activity
                            if(!settingsCanWrite) {
                                MainActivity.this.finish();
                                startActivity(getIntent());
                                break;
                            }
                        }
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.NOTIFICATION);
                        break;

                    case 5:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            // Check whether has the write settings permission or not.
                            boolean settingsCanWrite = Settings.System.canWrite(getApplicationContext());
                            //If doesn't have them, reload activity
                            if(!settingsCanWrite) {
                                MainActivity.this.finish();
                                startActivity(getIntent());
                                break;
                            }
                        }
                        soundMap.get(view).setAs(MainActivity.this, getApplicationContext(), fileSaveDir, Sound.Type.ALARM);
                        break;
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Gets permissions to write and read files and creates a directory for app sounds
    private void getFileSaveDir() {
        //A flag to check whether permission is granted
        boolean isK = true;
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_CONFIGURATION,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        };

        //If permission not granted, change flag to false and break
        for(String s: permissions) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(),s)!= PackageManager.PERMISSION_GRANTED) {
                isK = false;
                break;
            }
        }

        //If any permission not granted, request them
        if(isK) {
            getSaveDir();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 123);
        }
    }


    //Creates directory for app sounds
    private void getSaveDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Kruszwil Soundboard");
        if(!file.mkdirs()) {
            Log.i("halko", "Directory not created :c");
        }

        fileSaveDir = file;
    }


    //Creates directory on permission grant
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSaveDir();
                    //Check whether app has required permissions
                    checkPermissions(this);


                } else {
                    Toast.makeText(this,
                            "Aby móc pobierać dźwięki, wysyłać je, oraz ustawiać jako dźwięk powiadomień lub dzwonka, musisz udzielić aplikacji odpowiednich uprawnień",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    //Checks necessary permissions
    private void checkPermissions(Context context) {


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Check whether has the write settings permission or not.
            boolean settingsCanWrite = Settings.System.canWrite(context);

            if(!settingsCanWrite) {
                // If do not have write settings permission then open the Can modify system settings panel.
                createPermissionsDialog(context);

            }

            //If first launch, show the dialog
            createFirstLaunchDialog(context);

        }
    }

    //Create a dialog to enter settings
    private void createPermissionsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Uprawnienia");
        builder.setMessage("Nadaj aplikacji niezbędne uprawnienia, aby móc korzystać z jej wszystkich funkcji.");
        builder.setPositiveButton("PRZEJDŹ DO USTAWIEŃ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                startActivity(intent);
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Shows only on first launch
    private void createFirstLaunchDialog(Context context){
        boolean isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Witaj w Kruszwil Prestiż!");
            builder.setMessage("Dziękujemy za zakup! Wciśnij przycisk by odtworzyć dźwięk," +
                    " przytrzymaj by skorzystać z dodatkowych funkcji. Miłej zabawy! :)");

            
            builder.setPositiveButton("OKEJ!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            isFirstLaunch = false;
            editor.putBoolean("isFirstLaunch", isFirstLaunch);
            editor.commit();
        }


    }





    public void createAppInfoDialog(View view) {
        PackageInfo packageInfo = null;

        try {
             packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Informacje o aplikacji");
        builder.setMessage("Wersja: "+packageInfo.versionName+"\nKliknij DODAJ DŹWIĘK, aby przejść do formularza i dodać własną propozycję nowego, prestiżowego dźwięku!");
        builder.setPositiveButton("ZAMKNIJ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("DODAJ DŹWIĘK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://kruszwil.000webhostapp.com/")));
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
