package main;

import dao.PokemonDAO;
import model.PokemonStats;

import java.sql.SQLException;
import java.util.Map;

public class PokemonValidator {
    public static void main(String[] args) {
        PokemonDAO dao = new PokemonDAO();

        try {
            Map<String, PokemonStats> stats = dao.retrieveStats();
            validateStats(stats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void validateStats(Map<String, PokemonStats> stats) {
        // Exemplo de validação: Verificar se a quantidade de cada tipo corresponde ao esperado
        for (Map.Entry<String, PokemonStats> entry : stats.entrySet()) {
            System.out.println("Tipo: " + entry.getKey() + ", Quantidade: " + entry.getValue().getQuantidade());
            System.out.println("IDs: " + entry.getValue().getIds());
            System.out.println("Pokemons: " + entry.getValue().getPokemons());
        }
    }
}
