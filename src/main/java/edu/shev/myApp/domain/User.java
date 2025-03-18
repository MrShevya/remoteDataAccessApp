package edu.shev.myApp.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private boolean active;

    @ManyToMany(mappedBy = "recievers")
    private List<FileSystem> recievedFiles = new ArrayList<>();

    //Ролевая система
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER) // формирует доп таблицу для хранения enum, fetch - подгрузка(в данном случае жадная, есть еще ленивая)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id")) // аннотация говорит, что поле будет храниться в отдельной таблице, для которой мы не описывали маппинг ранее + связь таблицы с текущей таблицы
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; // роли пользователя


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public List<FileSystem> getFiles(){
        return recievedFiles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
