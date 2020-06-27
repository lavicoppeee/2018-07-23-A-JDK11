package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private Graph<State, DefaultWeightedEdge> graph;
	private NewUfoSightingsDAO dao;
	private Map<String, State> idMap;
	
	private Simulator sim;
	
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		this.sim = new Simulator();
	}
	
	
	public List<String> getShapeYear(Integer year) {
		return this.dao.getShapeYear(year);
	}
	
	
	public List<State> buildGraph(Integer year, String shape) {
		this.graph = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<String, State>();
		
		List<State> states = this.dao.loadAllStates(idMap);
		Graphs.addAllVertices(this.graph, states);

		for(Adiacenza a : this.dao.getAdiacenze(year, shape, idMap)) {
			if(this.graph.getEdge(a.getState1(), a.getState2()) == null) {
				Graphs.addEdge(this.graph, a.getState1(), a.getState2(), a.getPeso());
			}
		}
		System.out.println(this.graph.vertexSet().size()+" "+this.graph.edgeSet().size());
		states.sort(null);
		return states;
	}
	
	
	public Integer getPesoAdiacenti(State state) {
		Integer res = 0;
		for(DefaultWeightedEdge e : this.graph.outgoingEdgesOf(state)) {
			res += (int) this.graph.getEdgeWeight(e);
		}
		return res;
	}
	
	
	public void simula(Integer year, String shape, Integer T, Integer alfa) {
		List<Event> events = this.dao.getAllEvents(year, shape, idMap);
		this.sim.init(events, T, alfa, graph);
		this.sim.run();
	}
	
	
	public List<StateDefcon> getStateDefcon() {
		return this.sim.getStateDefcon();
	}

}
