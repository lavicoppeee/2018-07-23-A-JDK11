package it.polito.tdp.newufosightings.model;

public class StateDefcon implements Comparable<StateDefcon> {
	
	private State state;
	private Double defcon;
	
	public StateDefcon(State state, Double defcon) {
		super();
		this.state = state;
		this.defcon = defcon;
	}

	public Double getDefcon() {
		return defcon;
	}

	public void setDefcon(Double defcon) {
		this.defcon = defcon;
	}

	public State getState() {
		return state;
	}

	@Override
	public int compareTo(StateDefcon o) {
		return this.state.compareTo(o.state);
	}
	
	public String toString() {
		return this.state.toString()+" | "+defcon;
	}

}
