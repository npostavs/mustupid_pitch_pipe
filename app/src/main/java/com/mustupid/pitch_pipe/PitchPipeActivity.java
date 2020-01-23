package com.mustupid.pitch_pipe;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

public class PitchPipeActivity extends AppCompatActivity {

    private static final int MIN_PITCH = 400;
    private static final int MAX_PITCH = 470;
    private static final int DEFAULT_PITCH = 440;

    private int mPitch = DEFAULT_PITCH;
    private SharedPreferences mPreferences;
    private PitchPipe mPitchPipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch_pipe);

        // Make volume button always control just the media volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Load stored persistent data
        mPreferences = getSharedPreferences("PitchPipe", AppCompatActivity.MODE_PRIVATE);
        mPitch = mPreferences.getInt("pitch", DEFAULT_PITCH);

        mPitchPipe = new PitchPipe();
        setUpNoteButtons();
        setUpPitchPicker();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("pitch", mPitch);
        editor.apply();
    }

    private void setUpNoteButtons() {
        final CompoundButton noteCButton = findViewById(R.id.a_button);
        noteCButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mPitchPipe.play(Note.A.getFrequency() * mPitch);
                else
                    mPitchPipe.stop();
            }
        });
    }

    private void setUpPitchPicker() {
        NumberPicker pitchPicker = findViewById(R.id.pitch_picker);
        pitchPicker.setMinValue(MIN_PITCH);
        pitchPicker.setMaxValue(MAX_PITCH);
        pitchPicker.setValue(mPitch);
        pitchPicker.setWrapSelectorWheel(false);
        pitchPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mPitch = newVal;
                mPitchPipe.setPitch(Note.A.getFrequency() * mPitch);
            }
        });
    }
}
