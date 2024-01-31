package com.thelocalmarketplace.software.test.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.state.StateTransition;

/**
 * Tests StateTransition
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
 * @author Royce Knoepfli		(30172598)
 */
public class StateTransitionTests {

	// - - - - - - - - - - Constructor tests - - - - - - - - - -
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullInitial() {
		new StateTransition(null, States.NORMAL);
	}

	
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullFinal() {
		new StateTransition(States.NORMAL, null);
	}

	
	@Test
	public void testValidConstructor() {
		StateTransition transition = new StateTransition(States.NORMAL, States.BLOCKED);
		assertNotNull("The StateTransition object should not be null", transition);
		assertEquals("Initial state should be NORMAL", States.NORMAL, transition.getInitialState());
		assertEquals("Final state should be BLOCKED", States.BLOCKED, transition.getFinalState());
	}

	// - - - - - - - - - - Equals tests - - - - - - - - - -

	@Test
	public void testEqualsWithSameObject() {
		StateTransition transition = new StateTransition(States.NORMAL, States.BLOCKED);
		assertTrue("A StateTransition object should be equal to itself", transition.equals(transition));
	}

	
	@Test
	public void testEqualsWithEqualStates() {
		StateTransition st1 = new StateTransition(States.NORMAL, States.BLOCKED);
		StateTransition st2 = new StateTransition(States.NORMAL, States.BLOCKED);
		assertTrue("Two StateTransition objects with the same states should be equal", st1.equals(st2));
	}

	
	@Test
	public void testEqualsWithDifferentStates() {
		StateTransition st1 = new StateTransition(States.NORMAL, States.BLOCKED);
		StateTransition st3 = new StateTransition(States.BLOCKED, States.NORMAL);
		assertFalse("Two StateTransition objects with different states should not be equal", st1.equals(st3));
	}

	
	@Test
	public void testEqualsWithNonStateTransitionObject() {
		StateTransition st1 = new StateTransition(States.NORMAL, States.BLOCKED);
		Object obj = new Object();
		assertFalse("A StateTransition object should not be equal to a non-StateTransition object", st1.equals(obj));
	}
}
