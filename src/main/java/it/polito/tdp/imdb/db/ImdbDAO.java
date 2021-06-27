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
	public List <String> getGeneri(){
		String sql="SELECT DISTINCT mg.genre AS id "
				+ "FROM movies_genres mg "
				+ "ORDER BY mg.genre ASC";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				result.add(res.getString("id"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(Map<Integer, Actor> idMap, String genere){
		String sql = "SELECT DISTINCT r.actor_id AS id "
				+ "FROM movies_genres mg, movies m, roles r "
				+ "WHERE r.movie_id= m.id AND m.id= mg.movie_id AND mg.genre=? "
				+"ORDER BY r.actor_id ASC ";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor a= idMap.get(res.getInt("id"));
				if(a!=null) {
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	
	
	public List<Adiacenza> getAdiacenza(Map<Integer, Actor> idMap, String genere){
		String sql = "SELECT r1.actor_id AS aa1, r2.actor_id AS aa2, COUNT(*) AS peso "
				+ "FROM movies_genres mg, movies m, roles r1, roles r2 "
				+ "WHERE r1.actor_id> r2.actor_id AND r1.movie_id= r2.movie_id AND "
				+ "r1.movie_id= m.id AND r2.movie_id= m.id AND m.id= mg.movie_id AND mg.genre=? "
				+ "GROUP BY r1.actor_id, r2.actor_id "
				+ "HAVING peso>0";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor a1= idMap.get(res.getInt("aa1"));
				Actor a2= idMap.get(res.getInt("aa2"));
				if(a1!=null && a2!=null) {
					Adiacenza a= new Adiacenza(a1,a2, res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	
	
	
	
	
}
