package edu.shev.myApp.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class FileObj {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String filename;


    private UUID file_id;

    private String fileStorageName;


    @ManyToOne(fetch = FetchType.EAGER) // связь: одному пользователю пренадлежат множество сообщений // eager: каждый раз, когда получаем файл, должны получить информацию об авторе
    @JoinColumn(name = "user_id")
    private User owner;

    public FileObj() {
    }
    public FileObj(String filename, User user) {
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

    public UUID getFile_id() {
        return file_id;
    }

    public void setFile_id(UUID file_id) {
        this.file_id = file_id;
    }

    public String getFileStorageName() {
        return fileStorageName;
    }

    public void setFileStorageName(String fileStorageName) {
        this.fileStorageName = fileStorageName;
    }

}
