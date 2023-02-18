package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDaoTestCase {
	
	private FilmDao filmDao = new FilmDao();
	
	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS genre (idgenre INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , name VARCHAR(50) NOT NULL);");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS film (\r\n"
				+ "  idfilm INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  title VARCHAR(100) NOT NULL,\r\n"
				+ "  release_date DATETIME NULL,\r\n" + "  genre_id INT NOT NULL,\r\n" + "  duration INT NULL,\r\n"
				+ "  director VARCHAR(100) NOT NULL,\r\n" + "  summary MEDIUMTEXT NULL,\r\n"
				+ "  CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre));");
		stmt.executeUpdate("DELETE FROM film");
		stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26 12:00:00.000', 1, 120, 'director 1', 'summary of the first film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14 12:00:00.000', 2, 114, 'director 2', 'summary of the second film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12 12:00:00.000', 2, 176, 'director 3', 'summary of the third film')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListFilms() {
		// WHEN
		List<Film> films = filmDao.listFilms();
		LocalDate date1=LocalDate.of(2015, 11, 26);
		LocalDate date2=LocalDate.of(2015, 11, 14);
		LocalDate date3=LocalDate.of(2015, 12, 12);
		
		// THEN
		assertThat(films).hasSize(3);
		assertThat(films).extracting("id", "title", "releaseDate", "genre.id", "duration", "director", "summary").containsOnly(
				tuple(1, "Title 1", date1, 1, 120, "director 1", "summary of the first film"),
				tuple(2, "My Title 2", date2, 2, 114, "director 2", "summary of the second film"),
				tuple(3, "Third title", date3, 2, 176, "director 3", "summary of the third film")
		);
	 }
	
	 @Test
	 public void shouldListFilmsByGenre() {
		// WHEN
		List<Film> films = filmDao.listFilmsByGenre("Comedy");
		// THEN
		int i=0;
		while(i<films.size()) {
			assertThat(films.get(i).getGenre().getId()).isEqualTo(2);
			assertThat(films.get(i).getGenre().getName()).isEqualTo("Comedy");
			i++;
		}	
	 }
	
	 @Test
	 public void shouldAddFilm() throws Exception {
		 Connection connection = DataSourceFactory.getDataSource().getConnection();
		 Statement statement = connection.createStatement();
		 statement.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (3,'Western')");
		 
		 // WHEN 
		 Genre genre = new Genre(3,"Western");
		 LocalDate date=LocalDate.of(2016, 10, 10);
		 Film film = new Film(4, "title 4", date, genre, 90, "director 4", "summary of the fourth film");
		 filmDao.addFilm(film);
		 // THEN

		 ResultSet resultSet = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE name='Western'");
		 assertThat(resultSet.next()).isTrue();
		 assertThat(resultSet.getInt("idfilm")).isNotNull();
		 assertThat(resultSet.getString("title")).isEqualTo("title 4");
		 assertThat(resultSet.getInt("genre_id")).isNotNull();
		 assertThat(resultSet.getDate("release_date").toLocalDate()).isEqualTo("2016-10-10");
		 assertThat(resultSet.getString("director")).isEqualTo("director 4");
		 assertThat(resultSet.getInt("duration")).isEqualTo(90);
		 assertThat(resultSet.getString("director")).isEqualTo("director 4");
		 assertThat(resultSet.getString("summary")).isEqualTo("summary of the fourth film");
		 assertThat(resultSet.next()).isFalse();
		 resultSet.close();
		 statement.close();
		 connection.close();
	 }
}
