package dao;

import model.Pokemon;
import model.PokemonStats;

import java.sql.*;
import java.util.*;

public class PokemonDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pokemons?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "152603";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public List<Pokemon> retrievePokemons() throws SQLException {
        List<Pokemon> pokemons = new ArrayList<>();
        String query = "SELECT id, pokemon, tipo FROM tb_pokemon";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                pokemons.add(new Pokemon(rs.getInt("id"), rs.getString("pokemon"), rs.getString("tipo")));
            }
        }
        return pokemons;
    }

    public void insertIntoTypeTable(String tableName, Pokemon pokemon) throws SQLException {
        String insertQuery = "INSERT INTO " + tableName + " (id, pokemon, tipo) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, pokemon.getId());
            pstmt.setString(2, pokemon.getPokemon());
            pstmt.setString(3, pokemon.getTipo());
            pstmt.executeUpdate();
        }
    }

    public void insertTotalizer(Map<String, Integer> totalizador) throws SQLException {
        String insertQuery = "INSERT INTO tb_pokemon_totalizador (tipo, quantidade) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            for (Map.Entry<String, Integer> entry : totalizador.entrySet()) {
                pstmt.setString(1, entry.getKey());
                pstmt.setInt(2, entry.getValue());
                pstmt.executeUpdate();
            }
        }
    }

    public void deleteDuplicates() throws SQLException {
        String tempTable = "temp_duplicate_ids";
        String createTempTableQuery = "CREATE TEMPORARY TABLE " + tempTable + " AS " +
                                      "SELECT MIN(id) AS id FROM tb_pokemon GROUP BY pokemon, tipo";

        String deleteQuery = "DELETE FROM tb_pokemon WHERE id NOT IN (SELECT id FROM " + tempTable + ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTempTableQuery);
            stmt.executeUpdate(deleteQuery);
        }
    }

    public Map<String, PokemonStats> retrieveStats() throws SQLException {
        Map<String, PokemonStats> stats = new HashMap<>();
        String query = "SELECT tipo, COUNT(*) as quantidade, GROUP_CONCAT(id) as ids, GROUP_CONCAT(pokemon) as pokemons FROM tb_pokemon GROUP BY tipo " +
                       "UNION ALL " +
                       "SELECT 'eletrico' as tipo, COUNT(*) as quantidade, GROUP_CONCAT(id) as ids, GROUP_CONCAT(pokemon) as pokemons FROM tb_pokemon_eletrico " +
                       "UNION ALL " +
                       "SELECT 'fogo' as tipo, COUNT(*) as quantidade, GROUP_CONCAT(id) as ids, GROUP_CONCAT(pokemon) as pokemons FROM tb_pokemon_fogo " +
                       "UNION ALL " +
                       "SELECT 'voador' as tipo, COUNT(*) as quantidade, GROUP_CONCAT(id) as ids, GROUP_CONCAT(pokemon) as pokemons FROM tb_pokemon_voador";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                int quantidade = rs.getInt("quantidade");
                String ids = rs.getString("ids");
                String pokemons = rs.getString("pokemons");

                stats.put(tipo, new PokemonStats(quantidade, ids, pokemons));
            }
        }
        return stats;
    }
}
