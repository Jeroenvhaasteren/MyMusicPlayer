package org.bts.atry.mymusicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlayList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = PlayList.class.getSimpleName();
    private static List<SongObj> PLAYLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        this.PLAYLIST = new DataResourcesSong().getPlaylist();

        final ListView mListView = (ListView) this.findViewById(R.id.lv_playlist);
        mListView.setOnItemClickListener(this);


        ArrayList<String> mListArray = new ArrayList<>();
        // Just populating the 'ArrayList' with the 'String[]'
        for (SongObj aSong : this.PLAYLIST)
        {
            mListArray.add(aSong.getTitle());
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListArray));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(PlayList.TAG, "Element " + position + ", with ID = " + id);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("song", position);
        startActivity(i);
    }
}
