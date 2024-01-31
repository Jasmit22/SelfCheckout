package com.thelocalmarketplace.software.state;

import com.thelocalmarketplace.software.general.Enumerations.States;

/**
 * @author Connell Reffo		(10186960)
 * @author Tara Strickland 		(10105877)
 * @author Julian Fan			(30235289)
 * @author Samyog Dahal			(30194624)
 * @author Phuong Le			(30175125)
 * @author Daniel Yakimenka		(10185055)
 * @author Merick Parkinson		(30196225)
 * @author Julie Kim			(10123567)
 * @author Ajaypal Sallh		(30023811)
 * @author Nathaniel Dafoe		(30181948)
 * @author Anmol Ratol			(30231177)
 * @author Chantel del Carmen	(30129615)
 * @author Dana Al Bastrami		(30170494)
 * @author Maria Munoz			(30175339)
 * @author Ernest Shukla		(30156303)
 * @author Hillary Nguyen		(30161137)
 * @author Robin Bowering		(30123373)
 * @author Anne Lumumba			(30171346)
 * @author Jasmit Saroya		(30170401)
 * @author Fion Lei				(30134327)
 * @author Royce Knoepfli		(30172598) */
public class StateTransition {

	/**
	 * Initial state
	 */
	private States initial;
	
	/**
	 * Final state
	 */
	private States end;
	
	/**
	 * Constructor for a registerable state transition
	 * @throws NullPointerException If any argument is null
	 */
	public StateTransition(States initial, States end) throws NullPointerException {
		this.setInitialState(initial);
		this.setFinalState(end);
	}

	public States getInitialState() {
		return initial;
	}

	public void setInitialState(States initial) throws NullPointerException {
		if (initial == null) {
			throw new NullPointerException("Initial");
		}
		
		this.initial = initial;
	}

	public States getFinalState() {
		return end;
	}

	public void setFinalState(States end) throws NullPointerException {
		if (end == null) {
			throw new NullPointerException("End");
		}
		
		this.end = end;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof StateTransition)) {
			return false;
		}
		
		StateTransition ob = (StateTransition) o;
		
		return (this.getInitialState().equals(ob.getInitialState()) && this.getFinalState().equals(ob.getFinalState()));
	}
}
