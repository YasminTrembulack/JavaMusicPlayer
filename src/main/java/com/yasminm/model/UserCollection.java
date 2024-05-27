package com.yasminm.model;

import javax.persistence.*;

@Entity
@Table(name = "UserCollection")
public class UserCollection {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "UserId")
    private String userid;

    @Column(name = "musicid")
    private String musicid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMusicid() {
        return musicid;
    }

    public void setMusicid(String musicid) {
        this.musicid = musicid;
    }

    
}
