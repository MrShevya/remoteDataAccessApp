package edu.shev.myApp.domain;

import jakarta.persistence.*;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Entity
public class FileSystem {
    @Value("${salt}")
    private String salt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String filename;

    private String link;

    @ManyToOne(fetch = FetchType.EAGER) // связь: одному пользователю пренадлежат множество сообщений // eager: каждый раз, когда получаем файл, должны получить информацию об авторе
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_file",
    joinColumns = @JoinColumn(name = "file_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> recievers = new ArrayList<>();



    public FileSystem() {
    }

    public FileSystem(String filename, User user) {
        this.owner = user;
        this.filename = filename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getOwner() {
        return owner;
    }

    public String getOwnerUsername(){
        return owner != null ? owner.getUsername() : "<none>";
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<Long> getRecievers() {
        ArrayList<Long> recieversIds = new ArrayList<>();
        recievers.forEach(User -> recieversIds.add(User.getId()));
        return recieversIds;
    }


    public void addReciever(User reciever){
        this.recievers.add(reciever);
        reciever.getFiles().add(this);
    }

    public void removeReciever(User reciever){          //this.recievers.removeIf(User -> User.equals(reciever));
        this.recievers.remove(reciever);
        reciever.getFiles().remove(this);

    }

    public String getLink(){
        return getId().toString();
    }



}
