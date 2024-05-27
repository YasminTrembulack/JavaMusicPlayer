package com.yasminm.model;

import javax.persistence.*;

@Entity
@Table(name = "MusicData")
public class MusicData {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "Title")
    private String title;

    @Column(name = "Artist")
    private String artist;

    @Column(name = "Album")
    private String album;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
