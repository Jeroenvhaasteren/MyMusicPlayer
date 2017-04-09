package org.bts.atry.mymusicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

public class MyMediaPlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private final static String TAG = MyMediaPlayerService.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private List<SongObj> PLAYLIST;
    public static int mSongPlaying = 0;
    private int mResumePosition;

    //FLAGS
    public static final String PLAYER_ACTION = "playerAction";
    public static final String ACTION_SET = "setNewSong";
    public static final int ACTION_PLAY = 0;
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_NEXT = 3;
    public static final int ACTION_PREV = 4;



    public MyMediaPlayerService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        GetSongList();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mSongPlaying = intent.getIntExtra(ACTION_SET, 0);
        switch(intent.getIntExtra(PLAYER_ACTION, 0)) {
            case MyMediaPlayerService.ACTION_PLAY:
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                } else {
                    stopSong();
                    this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                }
                playSong();
                break;
            case MyMediaPlayerService.ACTION_NEXT:
                Log.w(MyMediaPlayerService.TAG,"Play Next");
                stopSong();
                this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                playSong();
                break;
            case MyMediaPlayerService.ACTION_PREV:
                Log.w(MyMediaPlayerService.TAG,"Play Prev");
                stopSong();
                this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                playSong();
                break;
            case MyMediaPlayerService.ACTION_PAUSE:
                Log.w(MyMediaPlayerService.TAG,"Pauze");
                pauseSong();
                break;
            case MyMediaPlayerService.ACTION_RESUME:
                if (this.mediaPlayer == null) {
                    this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                } else {
                    stopSong();
                    this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
                }
                this.mediaPlayer.seekTo(this.mResumePosition);
                playSong();
                break;
            default:
                Log.w(MyMediaPlayerService.TAG,"Unknown Player Action");
                break;
        }
        startMediaPlayer();
        return START_STICKY;
    }

    private void sendMessage() {
        Intent intent = new Intent("songPlaying");
        intent.putExtra("song", this.mSongPlaying);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void startMediaPlayer() {
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

    private void launchNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(PLAYLIST.get(mSongPlaying).getTitle())
                .setContentText(PLAYLIST.get(mSongPlaying).getDescription().substring(0,65)+"...")
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, SongDetails.class);
        notificationIntent.putExtra("song", this.mSongPlaying);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) this .getSystemService(Service.NOTIFICATION_SERVICE);
        //Change Notification id if you want to stack the notifications. I only want one
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSong();
        if (this.mediaPlayer != null) this.mediaPlayer.release();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.w(MyMediaPlayerService.TAG,"Song is completed");
        this.mSongPlaying++;
        if(this.mSongPlaying > (this.PLAYLIST.size() - 1) ) {this.mSongPlaying = 0;}
        stopSong();
        this.mediaPlayer = MediaPlayer.create(this, GetSongSource());
        playSong();
        sendMessage();
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
    }

    private void GetSongList() {
        this.PLAYLIST = new DataResourcesSong().getPlaylist();
    }

    private int GetSongSource() {
        SongObj song = this.PLAYLIST.get(this.mSongPlaying);
        int SongSource = song.getdataFile();
        return SongSource;
    }

    private void playSong() {
        Log.i(MyMediaPlayerService.TAG,"Start Playing");
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        launchNotification();
        sendMessage();
    }

    private void stopSong() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mResumePosition = mediaPlayer.getCurrentPosition();
        }
    }

}

