package fr.isen.java2.db.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		//Création d'une liste pour pouvoir la retourner
		List<Genre> listOfGenres = new ArrayList<>();
		
		String sqlQuery = "SELECT * FROM genre";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery(sqlQuery)) {
	                while (results.next()) {
	                	//Récupération des éléments de genre
	                	Integer idgenre= results.getInt("idgenre");
	                	String name= results.getString("name");
	                	
	                	// Création du genre
	                    Genre genre = new Genre(
	                    		idgenre,
	                    		name
	                    );
	                    //Ajout du genre dans la liste
	                    listOfGenres.add(genre);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return listOfGenres;
		
	}

	public Genre getGenre(String name) {
		String sqlQuery = "SELECT * FROM genre WHERE name=?";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        	//Attribue la valeur name à "?"
	        	statement.setString(1, name);
	        	try (ResultSet results = statement.executeQuery()) {
	                if (results.next()) {
	                	// Renvoie le genre
	                    return new Genre(results.getInt("idgenre"),
	                                    results.getString("name"));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
	public void addGenre(String name) {
		String sqlQuery = "INSERT INTO genre(name) VALUES (?)";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        	//Attribue la valeur name à "?"
	        	statement.setString(1,name);
	        	//Met à jour la base de donnée
	            statement.executeUpdate();
	        }
	    }catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
