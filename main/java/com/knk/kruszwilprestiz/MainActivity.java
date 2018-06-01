package com.knk.kruszwilprestiz;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<View, Sound> soundMap =  new HashMap<>();
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                return false;
            }
        });
    }

    public void buttonOnClick(View view) {
        soundMap.get(view).play(this);
    }


}
