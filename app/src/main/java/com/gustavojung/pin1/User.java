package com.gustavojung.pin1;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String Id;
    private List<String> favoritos = new ArrayList<>();

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<String> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<String> favoritos) {
        this.favoritos = favoritos;
    }
}
