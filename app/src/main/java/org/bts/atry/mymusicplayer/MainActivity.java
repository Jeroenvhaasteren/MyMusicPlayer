package org.bts.atry.mymusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyMediaPlayerService musicSrv;
    private boolean mMusicBound = false;
    private boolean mPaused = false;
    private int mSongPlaying;
    private static List<SongObj> PLAYLIST;

    private TextView tvTitle;
    private TextView tvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView
        tvTitle = (TextView) this.findViewById(R.id.tv_main_title);
        tvCountry = (TextView) this.findViewById(R.id.tv_main_country);

        //Buttons
        final ImageButton btnPrev = (ImageButton) this.findViewById(R.id.btn_main_prev);
        final ImageButton btnPlay = (ImageButton) this.findViewById(R.id.btn_main_play);
        final ImageButton btnPause = (ImageButton) this.findViewById(R.id.btn_main_pause);
        final ImageButton btnNext = (ImageButton) this.findViewById(R.id.btn_main_next);
        final Button btnInfo = (Button) this.findViewById(R.id.btn_main_info);
        final Button btnPlaylist = (Button) this.findViewById(R.id.btn_main_playlist);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);

        //PlayList
        getPlaylist();

        //If start with a song from the playlist
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSongPlaying = extras.getInt("song", 0);
            setSongInfo();
            if(this.mPaused) {
                PlayerController(MyMediaPlayerService.ACTION_RESUME, true);
            } else {
                PlayerController(MyMediaPlayerService.ACTION_PLAY, true);
            }
        }

        //Set up local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("songPlaying"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            mSongPlaying = intent.getIntExtra("song",0);
            setSongInfo();
        }
    };

    private void PlayerController(int mediaPlayerAction, Boolean setNewSong) {
        Intent intent = new Intent(this, MyMediaPlayerService.class);
        intent.putExtra(MyMediaPlayerService.PLAYER_ACTION, mediaPlayerAction);
        if(setNewSong) intent.putExtra(MyMediaPlayerService.ACTION_SET, this.mSongPlaying);
        startService(intent);
        this.mMusicBound = true;
    }

    private void getPlaylist() {
        this.PLAYLIST = new DataResourcesSong().getPlaylist();
    }

    private void setSongInfo() {
        tvTitle.setText(PLAYLIST.get(mSongPlaying).getTitle());
        tvCountry.setText(PLAYLIST.get(mSongPlaying).getCountry());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_info:
                Log.i(MainActivity.TAG,"info btn clicked");
                Intent i = new Intent(getApplicationContext(), SongDetails.class);
                i.putExtra("song", this.mSongPlaying);
                startActivity(i);
                break;
            case R.id.btn_main_prev:
                Log.i(MainActivity.TAG,"prev btn clicked");
                this.mSongPlaying--;
                if(this.mSongPlaying < 0) {this.mSongPlaying = 0;}
                setSongInfo();
                PlayerController(MyMediaPlayerService.ACTION_PREV, true);
                break;
            case R.id.btn_main_play:
                Log.i(MainActivity.TAG,"play btn clicked");
                setSongInfo();
                if(this.mPaused) {
                    PlayerController(MyMediaPlayerService.ACTION_RESUME, true);
                } else {
                    PlayerController(MyMediaPlayerService.ACTION_PLAY, true);
                }
                break;
            case R.id.btn_main_pause:
                Log.i(MainActivity.TAG,"pause btn clicked");
                setSongInfo();
                PlayerController(MyMediaPlayerService.ACTION_PAUSE, true);
                this.mPaused = true;
                break;
            case R.id.btn_main_next:
                Log.i(MainActivity.TAG,"next btn clicked");
                this.mSongPlaying++;
                if(this.mSongPlaying > (this.PLAYLIST.size() - 1) ) {this.mSongPlaying = 0;}
                setSongInfo();
                PlayerController(MyMediaPlayerService.ACTION_NEXT, true);
                break;
            case R.id.btn_main_playlist:
                Log.i(MainActivity.TAG,"playlist btn clicked");
                Intent a = new Intent(getApplicationContext(), PlayList.class);
                startActivity(a);
            default:
                Log.w(MainActivity.TAG,"btn action not bound");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        if(isFinishing()) {
            Intent intent = new Intent(this, MyMediaPlayerService.class);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
            stopService(intent);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        this.mSongPlaying = MyMediaPlayerService.mSongPlaying;
        setSongInfo();
        super.onResume();
    }
}
