package com.example.iplay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Mysong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }
    private TextView textView;
  ImageView play,prev,next;
  ArrayList<File> songs;
  MediaPlayer mediaPlayer;
  String tc;
  SeekBar sbar;
  Thread updateseek;
int position;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysong);
        textView = findViewById(R.id.textView);
        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        sbar = findViewById(R.id.seekbar);
        Intent intent = getIntent();
         Bundle bundle = intent.getExtras();
         songs = (ArrayList) bundle.getParcelableArrayList("songlist");
         tc = intent.getStringExtra("currsong");
         textView.setText(tc);
         position = intent.getIntExtra("position",0);
         Uri uri = Uri.parse(songs.get(position).toString());
         mediaPlayer = MediaPlayer.create(this,uri);
         mediaPlayer.start();
         sbar.setMax(mediaPlayer.getDuration());
         play.setImageResource(R.drawable.pause);
         textView.setSelected(true);



         sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                 mediaPlayer.seekTo(seekBar.getProgress());

             }
         });
         updateseek = new Thread()
        {
            @Override
            public void run ()
            {
                int currpos=0;
                try {
                    while (currpos<mediaPlayer.getDuration())
                    {
                        currpos = mediaPlayer.getCurrentPosition();
                        sbar.setProgress(currpos);
                        sleep(800);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };
         updateseek.start();



        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           mediaPlayer.stop();
           mediaPlayer.release();
           if (position!=0)
           {
               position = position-1;
           }
           else
           {
               position = songs.size()-1;
           }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                sbar.setMax(mediaPlayer.getDuration());
                textView.setText(songs.get(position).getName().toString());
                play.setImageResource(R.drawable.pause);
                textView.setSelected(true);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size()-1)
                {
                    position = position+1;
                }
                else
                {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                sbar.setMax(mediaPlayer.getDuration());
               textView.setText(songs.get(position).getName().toString());
                play.setImageResource(R.drawable.pause);
                textView.setSelected(true);
            }
        });

         play.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (mediaPlayer.isPlaying())
                 {
                     play.setImageResource(R.drawable.playb);
                     mediaPlayer.pause();
                 }
                 else
                 {
                     play.setImageResource(R.drawable.pause);
                     mediaPlayer.start();
                     textView.setSelected(true);

                 }
             }
         });
    }
}