package main;

import dao.PokemonDAO;
import model.Pokemon;
import model.PokemonStats;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PokemonTester {
    public static void main(String[] args) {
        PokemonDAO dao = new PokemonDAO();

        try {
            // Q1: Inserir dados na tabela principal tb_pokemon
            System.out.println("Iniciando inserção de pokemons na tabela principal...");
            List<Pokemon> pokemons = dao.retrievePokemons();
            System.out.println("Pokemons inseridos: ");
            pokemons.forEach(pokemon -> System.out.println(pokemon.getId() + ", " + pokemon.getPokemon() + ", " + pokemon.getTipo()));

            // Q2 e Q3: Inserir nas tabelas específicas por tipo sem duplicação
            System.out.println("\nInserindo pokemons nas tabelas específicas por tipo...");
            PokemonManager.insertIntoTypeTables(dao, pokemons);

            // Q4: Criar totalizador de pokemons por tipo
            System.out.println("\nCriando totalizador de pokemons por tipo...");
            PokemonManager.createTotalizer(dao, pokemons);

            // Q5: Deletar duplicados da tabela principal e inserir na tabela de deletados
            System.out.println("\nDeletando duplicados da tabela principal e inserindo na tabela de deletados...");
            dao.deleteDuplicates();

            // Q6: Validar dados extraídos e totalizadores
            System.out.println("\nValidando dados extraídos...");
            Map<String, PokemonStats> stats = dao.retrieveStats();
            PokemonValidator.validateStats(stats);

            System.out.println("\nTeste completo!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
