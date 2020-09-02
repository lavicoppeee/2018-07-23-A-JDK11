package it.polito.tdp.newufosightings.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	NewUfoSightingsDAO dao;
	private Graph<State,DefaultWeightedEdge> graph;
	List<Arco> archi;
	List<State> vertici;
	Map<String, State> idMap;
	
	
	Simulator sim;
	
	public Model() {
		dao= new NewUfoSightingsDAO();
	}
	
	public List<String> getShape(int x){
		return dao.getShape(x);
	}
	
	public void creaGrafo(int anno, String forma) {
		this.graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		
		vertici=this.dao.loadAllStates(idMap,anno,forma);
		Graphs.addAllVertices(graph, vertici);
		
		archi=this.dao.getArchi(idMap, anno, forma);
		for(Arco a:archi) {
			if(!this.graph.containsEdge(a.getS1(), a.getS2())) {
				Graphs.addEdgeWithVertices(graph, a.getS1(), a.getS2(),a.getPeso());
				
			}
		}
	
		
	}
	
	//NUMERO VERTICI:

	public int nVertici() {
			return this.graph.vertexSet().size();
		}

	//NUMERO ARCHI:

		public int nArchi() {
			return this.graph.edgeSet().size();
		}
		
		public List<StatePeso> getState(){
			List<StatePeso> result= new ArrayList<>();
			Integer res = 0;
			
			for(State s: vertici) {
				for(DefaultWeightedEdge e : this.graph.outgoingEdgesOf(s)) {
					res += (int) this.graph.getEdgeWeight(e);
				}
				result.add(new StatePeso(s,res));
			}
	
			return result;
			
		}
		
		
		public void simula(int year, String shape, int alpha, int t) {
			List<Event> events=this.dao.getAllEvent(year, shape, idMap);
			sim=new Simulator();
			sim.init(events, alpha, t, graph);
			sim.run();
		}
		
		public List<Defcon> getDefcon(){
			return sim.getStateDefcon();
		}
		
		
	
}
