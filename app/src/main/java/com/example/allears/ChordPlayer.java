package com.example.allears;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChordPlayer {
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;
    List<Integer> samples = new ArrayList<Integer>();
    AudioManager audioManager;
    Integer numTones;


    ChordPlayer(Context context, Integer... tones) {
        addSamplesToList();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();
        soundPoolMap = new HashMap<Integer, Integer>();
        numTones = tones.length;
        for (int i = 0; i < numTones; i++) {
            soundPoolMap.put(i + 1, soundPool.load(context, samples.get(tones[i]), 1));
        }
        audioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
    }

    private void addSamplesToList() {
        samples.add(R.raw.c3);
        samples.add(R.raw.db3);
        samples.add(R.raw.d3);
        samples.add(R.raw.eb3);
        samples.add(R.raw.e3);
        samples.add(R.raw.f3);
        samples.add(R.raw.gb3);
        samples.add(R.raw.g3);
        samples.add(R.raw.ab3);
        samples.add(R.raw.a3);
        samples.add(R.raw.bb3);
        samples.add(R.raw.b3);
        samples.add(R.raw.c4);
        samples.add(R.raw.db4);
        samples.add(R.raw.d4);
        samples.add(R.raw.eb4);
        samples.add(R.raw.e4);
        samples.add(R.raw.f4);
        samples.add(R.raw.gb4);
        samples.add(R.raw.g4);
        samples.add(R.raw.ab4);
        samples.add(R.raw.a4);
        samples.add(R.raw.bb4);
        samples.add(R.raw.b4);
        samples.add(R.raw.c5);
    }

    public void playChord() {

        float curVolume = audioManager.getStreamVolume(audioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC);
        float leftVolume = curVolume/maxVolume;
        float rightVolume = curVolume/maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        for (int i = 0; i < numTones; i++) {
            soundPool.play(i+1, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
        }
    }
}
