package model;

public class PokemonStats {
    private int quantidade;
    private String ids;
    private String pokemons;

    public PokemonStats(int quantidade, String ids, String pokemons) {
        this.quantidade = quantidade;
        this.ids = ids;
        this.pokemons = pokemons;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getIds() {
        return ids;
    }

    public String getPokemons() {
        return pokemons;
    }
}
