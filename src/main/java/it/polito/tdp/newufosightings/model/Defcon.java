package it.polito.tdp.newufosightings.model;

public class Defcon {

	State s;
	Double level;
	
	public Defcon(State s, Double level) {
		super();
		this.s = s;
		this.level = level;
	}

	public State getS() {
		return s;
	}

	public void setS(State s) {
		this.s = s;
	}

	public Double getLevel() {
		return level;
	}

	public void setLevel(double d) {
		this.level = d;
	}

	@Override
	public String toString() {
		return  s + " | " + level;
	}
	
	
	
}
