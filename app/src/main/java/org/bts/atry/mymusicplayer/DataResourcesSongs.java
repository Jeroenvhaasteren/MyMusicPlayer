package org.bts.atry.mymusicplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jeroen on 06/04/2017.
 */

class DataResourcesSong {
    private final static List<SongObj> PLAYLIST = new ArrayList<>(Arrays.asList(
            new SongObj("Brazil Samba", R.raw.bensoundbrazilsamba, "Brazil", "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola."),
            new SongObj("Country boy", R.raw.bensoundcountryboy, "USA", "Country music is a genre of American popular music that originated in the Southern United States in the 1920s."),
            new SongObj("India", R.raw.bensoundindia, "India", "The music of India includes multiple varieties of folk music, pop, and Indian classical music. India's classical music tradition, including Hindustani music and Carnatic, has a history spanning millennia and developed over several eras."),
            new SongObj("Little planet", R.raw.bensoundlittleplanet, "Iceland", "The music of Iceland includes vibrant folk and pop traditions. Well-known artists from Iceland include medieval music group Voces Thules, alternative rock band The Sugarcubes, singers Björk and Emiliana Torrini, post-rock band Sigur Rós and indie folk/indie pop band Of Monsters and Men."),
            new SongObj("Psychedelic", R.raw.bensoundpsychedelic, "South Korea", "The Music of South Korea has evolved over the course of the decades since the end of the Korean War, and has its roots in the music of the Korean people, who have inhabited the Korean peninsula for over a millennium. Contemporary South Korean music can be divided into three different main categories: Traditional Korean folk music, popular music, or K-pop, and Western-influenced non-popular music."),
            new SongObj("Relaxing", R.raw.bensoundrelaxing, "Indonesia", "The music of Indonesia demonstrates its cultural diversity, the local musical creativity, as well as subsequent foreign musical influences that shaped contemporary music scenes of Indonesia. Nearly thousands of Indonesian islands having its own cultural and artistic history and character."),
            new SongObj("The elevator Bossa Nova", R.raw.bensoundtheelevatorbossanova, "Brazil", "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola.")
    ));

    public DataResourcesSong() {}

    public List<SongObj> getSongObjs(){
        return PLAYLIST;
    }

    public SongObj getFirst(){
        return PLAYLIST.get(0);
    }

    public  SongObj getSong(int index) {
        return PLAYLIST.get(index);
    }

    public List<SongObj> getPlaylist() {
        return PLAYLIST;
    }

    public SongObj getNext(SongObj currentSongObj){
        int currentIndex = PLAYLIST.indexOf(currentSongObj);
        if (currentIndex == PLAYLIST.size() - 1){
            return PLAYLIST.get(0);
        } else {
            return PLAYLIST.get(currentIndex + 1);
        }
    }

    public SongObj getPrev(SongObj currentSongObj){
        int currentIndex = PLAYLIST.indexOf(currentSongObj);
        if (currentIndex == 0){
            return PLAYLIST.get(PLAYLIST.size() - 1);
        } else {
            return PLAYLIST.get(currentIndex - 1);
        }
    }

}
