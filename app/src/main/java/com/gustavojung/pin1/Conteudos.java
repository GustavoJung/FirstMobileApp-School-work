package com.gustavojung.pin1;

public class Conteudos {

        public int id;
        public String nome;
        public int temporadas;
        public String descricao;
        public int rating;
        public boolean favorito;

    public Conteudos(){

    }
    public Conteudos(int id,String nome,int temporadas, String descricao, int rating, boolean favorito) {
        this.id = id;
        this.nome = nome;
        this.temporadas = temporadas;
        this.descricao = descricao;
        this.rating = rating;
        this.favorito = favorito;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
