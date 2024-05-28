package com.yasminm.model;

import javax.persistence.*;

@Entity
@Table(name = "UserCollection")
public class UserCollection {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "UserId")
    private Long userid;

    @Column(name = "musicid")
    private Long musicid;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getMusicid() {
        return musicid;
    }

    public void setMusicid(Long musicid) {
        this.musicid = musicid;
    }

    
}
