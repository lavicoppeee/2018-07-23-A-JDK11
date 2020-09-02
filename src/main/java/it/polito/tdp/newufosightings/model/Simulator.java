package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;
import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {

	//in 
	int alpha;
	int t1;
	
	//mi serve per prendere tutti gli eventi
	NewUfoSightingsDAO dao;
	
	//mondo
	private Graph<State,DefaultWeightedEdge> graph;
	private PriorityQueue<Event> queue;
	
	//out
	Map<String, Defcon> DEFCON;
	LocalDateTime fine;
	
	public void init(List<Event> events, int a, int t, Graph<State,DefaultWeightedEdge> graph1) {
		
		this.alpha=a;
		this.t1=t;
		this.graph=graph1; 
		
		queue= new PriorityQueue<Event>();
		this.queue.addAll(events);
		
		this.DEFCON= new HashMap<>();
		
		//INIZILIZZO TUTTI I LIVELLI DEGLI STATI A 5
		for(State s: this.graph.vertexSet()) {
			Defcon d= new Defcon(s,5.0);
			this.DEFCON.put(s.getId(),d);
		}
		
		this.fine=events.get(events.size()-1).getTime();
		  
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			if(e.getTime().isAfter(fine))
				return;
			this.processEvent(e);
		}
	}

	private void processEvent(Event e) {

		State state = e.getState();
		String id = state.getId();

		switch (e.getType()) {

		case ALLERTA:
			//lo stato è gia nella mappa decremento il livello 
			if(DEFCON.containsKey(id)) {
				Defcon stateD=this.DEFCON.get(id);
				//non può scendere mai sotto 1
				if(stateD.getLevel()>=2) {
					stateD.setLevel(stateD.getLevel()-1.0);
				}
				
				Random randi=new Random();
				Double p=randi.nextDouble();
				
				if(p<=(alpha/100)) {
					//prendo gli adiacenti allo stato di partenza 
					List<State> vicini=Graphs.successorListOf(graph, state);
					for(State v: vicini) {
						//se la mappa contiene gli stati adiacenti
						
						if(DEFCON.containsKey(v.getId())) {
						Defcon def=this.DEFCON.get(v.getId());
						
						//controlo che posso sottrarre e non andare sotto 1
						if(def.getLevel()>=1.5) {
							def.setLevel(stateD.getLevel()-0.5);
						}
						this.queue.add(new Event(EventType.CESSATA_ALLERTA_VICINO,v,e.getTime().plusDays(t1)));
						
						}
					}
				}
				
				this.queue.add(new Event(EventType.CESSATA_ALLERTA,e.getState(),e.getTime().plusDays(t1)));
				
			}
			break;

		case CESSATA_ALLERTA:
			if(this.DEFCON.containsKey(id)) {
				Defcon sDefcon=this.DEFCON.get(id);
				if(sDefcon.getLevel()<=4) {
					sDefcon.setLevel(sDefcon.getLevel()+1.0);
				}
			}
			break;

		case CESSATA_ALLERTA_VICINO:
			if(this.DEFCON.containsKey(id)) {
				Defcon sDefcon=this.DEFCON.get(id);
				if(sDefcon.getLevel()<=4.5) {
					sDefcon.setLevel(sDefcon.getLevel()+0.5);
				}
			}
			break;
		}

	}

	public List<Defcon> getStateDefcon() {
		List<Defcon> result = new ArrayList<Defcon>(this.DEFCON.values());
		result.sort(null);
		return result;
	}
	
	
	
}
