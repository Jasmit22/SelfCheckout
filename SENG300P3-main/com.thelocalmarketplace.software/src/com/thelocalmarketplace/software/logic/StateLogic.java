package com.thelocalmarketplace.software.logic;

import java.util.ArrayList;
import java.util.HashMap;

import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.state.AbstractStateTransitionListener;
import com.thelocalmarketplace.software.state.InvalidStateTransitionException;
import com.thelocalmarketplace.software.state.StateTransition;

/**
 * Handles all state transition logic
 * 
 * Effectively represents a finite automaton that handles transitions between states
 * NOTE: Not all transitions have to be defined
 * 
 * I guess CPSC 351 was useful in the end
 * 
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
public class StateLogic {

	/**
	 * Tracks all registered listeners indexed by a state
	 * Multiple listeners can be mapped to a single state change event
	 */
	private HashMap<States, ArrayList<AbstractStateTransitionListener>> events;
	
	/**
	 * Transition function for the state machine
	 */
	private ArrayList<StateTransition> transitions;
	
	/**
	 * Current state of the machine
	 */
	private States state;
	
	/**
	 * Tracks the last state
	 */
	private States lastState;

	
	/**
	 * Base constructor
	 */
	public StateLogic(States initial, States last) {
		this.state = initial;
		this.lastState = last;
		this.events = new HashMap<>();
		this.transitions = new ArrayList<>();
	}
	
	/**
	 * Registers a new transition to a transition listener
	 * @param initial Is the initial state
	 * @param end Is the final state
	 * @throws NullPointerException If transition is null
	 */
	public void registerTransition(States initial, States end) throws NullPointerException {
		if (initial == null || end == null) {
			throw new NullPointerException();
		}
		
		this.transitions.add(new StateTransition(initial, end));
	}
	
	/**
	 * Registers a new event listener
	 * @param state Is the state to listen for
	 * @param listener The listener to register
	 * @throws NullPointerException If any argument is null
	 */
	public void registerListener(States state, AbstractStateTransitionListener listener) throws NullPointerException {
		if (state == null || listener == null) {
			throw new NullPointerException();
		}
		
		if (this.events.containsKey(state)) {
			this.events.get(state).add(listener);
		}
		else {
			ArrayList<AbstractStateTransitionListener> l = new ArrayList<>();
			l.add(listener);
						
			this.events.put(state, l);
		}
		
	}
	
	/**
	 * Transitions to a different state
	 * If next = current, then stay
	 * @param next The new state to go to
	 * @throws InvalidStateTransitionException if that transition is not allowed
	 */
	public void gotoState(States next) throws InvalidStateTransitionException {
		if (!(this.transitions.contains(new StateTransition(this.getState(), next)) || this.getState().equals(next))) {
			throw new InvalidStateTransitionException();
		}
		
		this.lastState = this.state;
		this.state = next;
		//checks if listeners are registered for current state
		if(this.events.containsKey(this.state)) {
			for (AbstractStateTransitionListener listener : this.events.get(this.state)) {
				listener.onTransition();
			}
		}
	} 
	
	/**
	 * Checks if in a given state
	 * @param state The state to check
	 * @return If in the state or not
	 */
	public boolean inState(States state) {
		return this.getState().equals(state);
	}
	
	/**
	 * Gets the current state
	 * @return the state
	 */
	public States getState() {
		return this.state;
	}
	
	/**
	 * Gets the last state
	 * @return the state
	 */
	public States getLastState() {
		return this.lastState;
	}
}
