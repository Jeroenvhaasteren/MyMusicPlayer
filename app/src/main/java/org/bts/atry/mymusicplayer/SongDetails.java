package org.bts.atry.mymusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SongDetails extends AppCompatActivity {

    private int mCurrentSong;
    private SongObj mSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentSong = extras.getInt("song", 0);
        }
        this.mSong = new DataResourcesSong().getSong(mCurrentSong);

        TextView tvTitle = (TextView) this.findViewById(R.id.tv_details_title);
        TextView tvCountry = (TextView) this.findViewById(R.id.tv_details_country);
        TextView tvDesc = (TextView) this.findViewById(R.id.tv_details_description);

        tvTitle.setText(this.mSong.getTitle());
        tvCountry.setText(this.mSong.getCountry());
        tvDesc.setText(this.mSong.getDescription());

    }
}
