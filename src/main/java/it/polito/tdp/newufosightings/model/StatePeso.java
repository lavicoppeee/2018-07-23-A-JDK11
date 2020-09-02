package it.polito.tdp.newufosightings.model;

public class StatePeso {

	State state;
	Integer peso;
	
	public StatePeso(State state, Integer peso) {
		super();
		this.state = state;
		this.peso = peso;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return  state.getName() + " | " + peso;
	}
	
	
}
