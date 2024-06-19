package main;

import dao.PokemonDAO;
import model.Pokemon;

import java.sql.SQLException;
import java.util.*;

public class PokemonManager {
    public static void main(String[] args) {
        PokemonDAO dao = new PokemonDAO();

        try {
            List<Pokemon> pokemons = dao.retrievePokemons();
            insertIntoTypeTables(dao, pokemons);
            createTotalizer(dao, pokemons);
            dao.deleteDuplicates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoTypeTables(PokemonDAO dao, List<Pokemon> pokemons) throws SQLException {
        Set<String> eletrico = new HashSet<>();
        Set<String> fogo = new HashSet<>();
        Set<String> voador = new HashSet<>();

        for (Pokemon p : pokemons) {
            switch (p.getTipo()) {
                case "eletrico":
                    if (eletrico.add(p.getPokemon())) {
                        dao.insertIntoTypeTable("tb_pokemon_eletrico", p);
                    }
                    break;
                case "fogo":
                    if (fogo.add(p.getPokemon())) {
                        dao.insertIntoTypeTable("tb_pokemon_fogo", p);
                    }
                    break;
                case "voador":
                    if (voador.add(p.getPokemon())) {
                        dao.insertIntoTypeTable("tb_pokemon_voador", p);
                    }
                    break;
            }
        }
    }

    public static void createTotalizer(PokemonDAO dao, List<Pokemon> pokemons) throws SQLException {
        Map<String, Integer> totalizador = new HashMap<>();
        for (Pokemon p : pokemons) {
            totalizador.put(p.getTipo(), totalizador.getOrDefault(p.getTipo(), 0) + 1);
        }
        dao.insertTotalizer(totalizador);
    }
}
