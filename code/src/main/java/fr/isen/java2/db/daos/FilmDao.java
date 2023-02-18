package fr.isen.java2.db.daos;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {

	public List<Film> listFilms() {
		//Création d'une liste pour pouvoir la retourner
		List<Film> listOfFilms = new ArrayList<>();
		
		String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery(sqlQuery)) {
	                while (results.next()) {
	                	//Récupération des éléments de genre
	                	String name= results.getString("name");
	                	Integer idgenre= results.getInt("idgenre");
	                	
	                	// Création du genre
	                	Genre genre =new Genre (idgenre,name);
	                	
	                	//Récupération des éléments du film
	                	Integer idfilm= results.getInt("idfilm");
	                	String title = results.getString("title");
	                	
	                	//Récupération et conversion de la date du film
	                	Timestamp DateAndTime = results.getTimestamp("release_date");
	                	LocalDateTime date = DateAndTime.toLocalDateTime();
	                	LocalDate date1= date.toLocalDate();
	                	
	                	Integer duration=results.getInt("duration");
	                	String summary=results.getString("summary");
	                	String director= results.getString("director");
	                	
	                	// Création du film
	                	Film film= new Film(idfilm,title,date1,genre,duration,director,summary);
	                    
	                	//Ajout du film dans la liste
	                    listOfFilms.add(film);
	                }
	                
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
		return listOfFilms;
	}

	
	public List<Film> listFilmsByGenre(String genreName) {
		//Création d'une liste pour pouvoir la retourner
		List<Film> listOfFilms = new ArrayList<>();
		
		String sqlQuery = "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name =?";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        	statement.setString(1, genreName);
	        	try (ResultSet results = statement.executeQuery()) {
	        		while (results.next()) {
	        			//Récupération des éléments de genre
	                	String name= results.getString("name");
	                	Integer idgenre= results.getInt("idgenre");
	                	
	                	// Création du genre
	                	Genre genre =new Genre (idgenre,name);
	                	
	                	//Récupération des éléments du film
	                	Integer idfilm= results.getInt("idfilm");
	                	String title = results.getString("title");
	                	
	                	//Récupération et conversion de la date du film
	                	Timestamp DateAndTime = results.getTimestamp("release_date");
	                	LocalDateTime date = DateAndTime.toLocalDateTime();
	                	LocalDate date1= date.toLocalDate();
	                	
	                	Integer duration=results.getInt("duration");
	                	String summary=results.getString("summary");
	                	String director= results.getString("director");
	                	
	                	// Création du film
	                	Film film= new Film(idfilm,title,date1,genre,duration,director,summary);
	                    
	                	//Ajout du film dans la liste
	                    listOfFilms.add(film); 
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return listOfFilms;
	}

	public void addFilm(Film film) {
		String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
		
		//Connection à la base de donnée
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
	        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
	        	//Attribue des différentes valeurs de film au différent "?"
	        	statement.setString(1,film.getTitle());
	        	statement.setDate(2,Date.valueOf(film.getReleaseDate()));
	        	statement.setInt(3,film.getGenre().getId());
	        	statement.setInt(4,film.getDuration());
	        	statement.setString(5,film.getDirector());
	        	statement.setString(6,film.getSummary());
	        	//Met à jour la base de donnée
	            statement.executeUpdate();
	        }
	    }catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
