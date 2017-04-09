package org.bts.atry.mymusicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        //Build the string title array for the listview
        ArrayList<String> mListArray = new ArrayList<>();
        for (SongObj aSong : this.PLAYLIST)
        {
            mListArray.add(aSong.getTitle());
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListArray));

        //Bind context menu to list items
        registerForContextMenu(mListView);
    }

    private void openMainActivity(int position) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("song", position);
        startActivity(i);
    }

    private void openDetailsActivity(int position) {
        Intent i = new Intent(getApplicationContext(), SongDetails.class);
        i.putExtra("song", position);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(PlayList.TAG, "Song: " + position + " is clicked");
        openMainActivity(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Inflate context menu
        MenuInflater mContMenuInflater = this.getMenuInflater();
        mContMenuInflater.inflate(R.menu.context_menu_playlist, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        //Get information from list items long clicked
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;

        //Switch statement What contextmenu items is clicked
        if (item.getItemId() == R.id.cbtn_playlist_play)
        {
            Log.i(PlayList.TAG, "Play btn Clicked of " + listPosition);
            openMainActivity(listPosition);
            return true;
        }
        else if (item.getItemId() == R.id.cbtn_playlist_details)
        {
            Log.i(PlayList.TAG, "Info btn Clicked of " + listPosition);
            openDetailsActivity(listPosition);
            return true;
        }
        else
            return super.onContextItemSelected(item);
    }
}
