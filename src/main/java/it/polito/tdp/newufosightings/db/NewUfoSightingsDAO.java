package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Arco;
import it.polito.tdp.newufosightings.model.Event;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.Event.EventType;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting ";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates(Map<String, State> idMap, int anno, String forma) {
		
		String sql="SELECT st.`Area`,st.`Capital`,st.`id`, st.`Lat`, st.`Lng`, st.`Name`, st.`Neighbors`, st.`Population` " + 
				"FROM sighting as s, state as st " + 
				"WHERE YEAR(s.`datetime`)=? and s.shape=? and s.state=st.`id` " + 
				"GROUP BY st.id " + 
				"ORDER BY st.id ASC ";
		List<State> result=new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, forma);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getString("id"))) {
					
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				
				idMap.put(state.getId(), state);
				result.add(state);
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getShape(int x){
		String sql="SELECT s.`shape` as shape " + 
				"FROM sighting as s " + 
				"WHERE YEAR(s.`datetime`)= ? " + 
				"GROUP BY shape " + 
				"ORDER BY shape ASC  ";
		List<String> result= new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				result.add(rs.getString("shape"));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	

	public List<Arco> getArchi(Map<String, State> idMap, int x, String forma){
		String sql="SELECT st1.id as st1, st2.id as st2, COUNT(DISTINCT s1.`shape`) as peso " + 
				"FROM sighting as s1, sighting as s2, state as st1, state as st2, `neighbor`as n1 " + 
				"WHERE st1.id>st2.id " + 
				"and  s1.state=st1.id and s2.state=st2.id " + 
				"and s1.id<>s2.id " + 
				"and st1.`id`=n1.`state1` and st2.`id`=n1.`state2` " + 
				"and YEAR(s1.`datetime`)= ? " + 
				"and YEAR (s2.`datetime`)= ? " + 
				"and s1.`shape`=? " + 
				"and s2.`shape`=? " + 
				"GROUP BY st1, st2 ";
		
		List<Arco> result= new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, x);
			st.setInt(2, x);
			st.setString(3,forma);
			st.setString(4,forma);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(idMap.containsKey(rs.getString("st1")) && idMap.containsKey(rs.getString("st2"))) {
					
					Arco a= new Arco(idMap.get(rs.getString("st1")), idMap.get(rs.getString("st2")), rs.getInt("peso"));
					result.add(a);
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
	}

	public List<Event> getAllEvent(int year, String shape, Map<String,State> idMap){
		String sql="SELECT DATETIME, state " + 
				"FROM sighting " + 
				"WHERE YEAR(DATETIME) = ? AND shape = ? " +
				"ORDER BY DATETIME";
			
	    List<Event> result=new ArrayList<>();
	    try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(idMap.containsKey(res.getString("state").toUpperCase())) {
					State state = idMap.get(res.getString("state").toUpperCase());
					result.add(new Event(EventType.ALLERTA, state, res.getTimestamp("datetime").toLocalDateTime()));
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return result;
	}
	
	
	
	
	
}
