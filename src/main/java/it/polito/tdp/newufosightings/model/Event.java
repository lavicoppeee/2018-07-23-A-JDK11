package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		ALLERTA, CESSATA_ALLERTA, CESSATA_ALLERTA_VICINO
	}
	
	private EventType type;
	private State state;
	private LocalDateTime date;
	
	public Event(EventType type, State state, LocalDateTime date) {
		super();
		this.type = type;
		this.state = state;
		this.date = date;
	}
	
	public EventType getType() {
		return this.type;
	}

	public State getState() {
		return state;
	}

	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public int compareTo(Event o) {
		return this.date.compareTo(o.date);
	}
	
	
	
}
