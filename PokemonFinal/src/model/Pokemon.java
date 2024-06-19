package model;

public class Pokemon {
    private int id;
    private String pokemon;
    private String tipo;

    public Pokemon(int id, String pokemon, String tipo) {
        this.id = id;
        this.pokemon = pokemon;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getPokemon() {
        return pokemon;
    }

    public String getTipo() {
        return tipo;
    }
}
