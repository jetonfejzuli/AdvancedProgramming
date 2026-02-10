package Lab8.Task1;

import java.util.ArrayList;
import java.util.List;

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde

class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}", title, artist);
    }
}

class MP3Player {
    private List<Song> songs;
    private int currentSong;
    private PlayerState state;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.currentSong = 0;
        this.state = PlayerState.STOPPED;
    }

    public void pressPlay() {
        if (state == PlayerState.PLAYING) {
            System.out.println("Song is already playing");
        } else {
            state = PlayerState.PLAYING;
            System.out.printf("Song %d is playing\n", currentSong);
        }
    }

    public void pressStop() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
            System.out.printf("Song %d is paused\n", currentSong);
        } else if (state == PlayerState.PAUSED) {
            state = PlayerState.STOPPED;
            currentSong = 0;
            System.out.println("Songs are stopped");
        } else {
            System.out.println("Songs are already stopped");
        }
    }

    public void pressFWD() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
        }
        currentSong = (currentSong + 1) % songs.size();
        System.out.println("Forward...");
    }

    public void pressREW() {
        if (state == PlayerState.PLAYING) {
            state = PlayerState.PAUSED;
        }
        currentSong = (currentSong - 1 + songs.size()) % songs.size();
        System.out.println("Reward...");
    }

    public void printCurrentSong() {
        System.out.println(songs.get(currentSong));
    }

    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = %s}",
                currentSong, songs);
    }
}

enum PlayerState {
    PLAYING, PAUSED, STOPPED
}