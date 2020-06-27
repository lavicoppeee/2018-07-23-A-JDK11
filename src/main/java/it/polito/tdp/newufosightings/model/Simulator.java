package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {
	
	private PriorityQueue<Event> queue;
	
	//Parametri di simulazione
	private Integer T1 = 10;
	private Integer alfa = 10;
	
	private LocalDateTime fine;
	
	private Graph<State, DefaultWeightedEdge> graph;
	
	//Modello del mondo
	private Map<String, StateDefcon> defcon;
	
	
	public void init(List<Event> events, Integer T, Integer alfa, Graph<State, DefaultWeightedEdge> graph) {
		this.queue = new PriorityQueue<Event>();
		this.queue.addAll(events);
		System.out.println(this.queue.size());
		
		this.T1 = T;
		this.alfa = alfa;
		this.graph = graph;
		
		this.defcon = new HashMap<String, StateDefcon>();
		for(State state : this.graph.vertexSet()) {
			this.defcon.put(state.getId(), new StateDefcon(state, 5.0));
		}
		
		this.fine = events.get(events.size()-1).getDate();
	}
	
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			if(e.getDate().isAfter(fine))
				return;
			this.processEvent(e);
		}
	}


	private void processEvent(Event e) {
		
		State state = e.getState();
		String id = state.getId();
		
		switch(e.getType()) {
		case ALLERTA:
			if(this.defcon.containsKey(id)) {
				StateDefcon sDefcon = this.defcon.get(id);
				if(sDefcon.getDefcon() >= 2) {
					System.out.println(sDefcon.getDefcon());
					sDefcon.setDefcon(sDefcon.getDefcon() - 1.0);
					System.out.println(sDefcon.getDefcon());
				}
				
				Random r = new Random();
				Double p = r.nextDouble();
				if(p <= this.alfa/100.0) {
					for(State vicino : Graphs.successorListOf(this.graph, state)) {
						if(this.defcon.containsKey(vicino.getId())) {
							StateDefcon vDefcon = this.defcon.get(vicino.getId());
							if(vDefcon.getDefcon() >= 1.5) {
								vDefcon.setDefcon(vDefcon.getDefcon() - 0.5);
							}
							this.queue.add(new Event(EventType.CESSATA_ALLERTA_VICINO, vicino, e.getDate().plusDays(this.T1)));
						}
					}
				}
				this.queue.add(new Event(EventType.CESSATA_ALLERTA, e.getState(), e.getDate().plusDays(this.T1)));
			}
			break;
			
		case CESSATA_ALLERTA:
			if(this.defcon.containsKey(id)) {
				StateDefcon sDefcon = this.defcon.get(id);
				if(sDefcon.getDefcon() <= 4) {
					sDefcon.setDefcon(sDefcon.getDefcon() + 1.0);
				}
			}
			break;
			
		case CESSATA_ALLERTA_VICINO:
			if(this.defcon.containsKey(id)) {
				StateDefcon sDefcon = this.defcon.get(id);
				if(sDefcon.getDefcon() <= 4.5) {
					sDefcon.setDefcon(sDefcon.getDefcon() + 0.5);
				}
			}
			break;
		}
		
	}
	
	
	public List<StateDefcon> getStateDefcon() {
		List<StateDefcon> result = new ArrayList<StateDefcon>(this.defcon.values());
		result.sort(null);
		return result;
	}

}
