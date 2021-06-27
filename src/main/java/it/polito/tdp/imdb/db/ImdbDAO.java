package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void  listAllActors(Map <Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(actor.getId(), actor);
				
				
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllGenres(){
		String sql = "SELECT DISTINCT g.genre AS gen "
				+ "FROM movies_genres g "
				+ "ORDER BY gen ASC ";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String genere=res.getString("gen");
				result.add(genere);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(String genere, Map <Integer, Actor> idMap){
		String sql = "SELECT DISTINCT r.actor_id AS id "
				+ "FROM roles r, movies m, movies_genres mg "
				+ "WHERE r.movie_id=m.id AND m.id=mg.movie_id AND mg.genre=? "
				+ "ORDER BY r.actor_id ASC ";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Actor a= idMap.get(res.getInt("id"));
				if(a!=null)
					result.add(a);
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Adiacenza> getAdiacenze(String genere, Map <Integer, Actor> idMap){
		String sql = "SELECT DISTINCT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(*) AS peso "
				+ "FROM roles r1, roles r2, movies_genres mg "
				+ "WHERE r1.movie_id=mg.movie_id AND r2.movie_id= mg.movie_id AND mg.genre=? "
				+ "AND r1.actor_id> r2.actor_id "
				+ "GROUP BY r1.actor_id, r2.actor_id ";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Actor a1= idMap.get(res.getInt("id1"));
				Actor a2= idMap.get(res.getInt("id2"));
				Double peso= res.getDouble("peso");
				Adiacenza a = new Adiacenza(a1,a2,peso);
				result.add(a);
				
					
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
