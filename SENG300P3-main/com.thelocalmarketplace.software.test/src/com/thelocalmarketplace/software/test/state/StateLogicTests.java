package com.thelocalmarketplace.software.test.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.logic.StateLogic;
import com.thelocalmarketplace.software.state.AbstractStateTransitionListener;
import com.thelocalmarketplace.software.state.InvalidStateTransitionException;

import powerutility.PowerGrid;

/**
 * Tests StateLogic
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
public class StateLogicTests {
	
    private StateLogic stateLogic;
    private SelfCheckoutStationSoftware centralLogic;

    @Before
    public void setup() {
    	AbstractSelfCheckoutStation.resetConfigurationToDefaults();
    	SelfCheckoutStationGold station = new SelfCheckoutStationGold();
        centralLogic = new SelfCheckoutStationSoftware(station);
        stateLogic = new StateLogic(States.NORMAL, States.NORMAL);
        
        PowerGrid.engageUninterruptiblePowerSource();
        station.plugIn(PowerGrid.instance());
        station.turnOn();
    }
    
    @After
    public void teardown() {
    	PowerGrid.engageFaultyPowerSource();
    	AbstractSelfCheckoutStation.resetConfigurationToDefaults();
    }
    
    // - - - - - - - - - - Test Constructor - - - - - - - - - -

    @Test
    public void testInitialState() {
        assertEquals("Initial state should be NORMAL", States.NORMAL, stateLogic.getState());
    }
    
    // - - - - - - - - - - Test registerTransition - - - - - - - - - - 

    @Test(expected = NullPointerException.class)
    public void testRegisterTransitionWithNullInitial() {
        stateLogic.registerTransition(null, States.NORMAL);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRegisterTransitionWithNullFinal() {
        stateLogic.registerTransition(States.NORMAL, null);
    }
    
    @Test
    public void testRegisterTransitionSuccessful() {
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
        listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.BLOCKED, listener);
        // verify the transition was added
        stateLogic.gotoState(States.BLOCKED);
        assertEquals("State should change to BLOCKED after registering the transition", States.BLOCKED, stateLogic.getState());
    }
    
    @Test
    public void testRegisterTransitionDuplicate() {
    	//TODO determine expected outcome 
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
    }

    
    //  - - - - - - - - - - Test registerListener - - - - - - - - - -

    @Test(expected = NullPointerException.class)
    public void testRegisterListenerWithNullState() {
        stateLogic.registerListener(null, new listenerStub());
    }
    
    @Test(expected = NullPointerException.class)
    public void testRegisterListenerWithNullListeners() {
        stateLogic.registerListener(States.NORMAL, null);
    }
    
    @Test
    public void testRegisterListenerValid() throws InvalidStateTransitionException {
        listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.NORMAL, listener);

        stateLogic.gotoState(States.NORMAL);
        assertTrue("Listener should be called for NORMAL state", listener.onTransitionCalled);
    }
    
    @Test
    public void testRegisterListenerMultiple() throws InvalidStateTransitionException {
        listenerStub listener1 = new listenerStub();
        listenerStub listener2 = new listenerStub();
        stateLogic.registerListener(States.NORMAL, listener1);
        stateLogic.registerListener(States.NORMAL, listener2);

        stateLogic.gotoState(States.NORMAL);
        assertTrue("First listener should be called for NORMAL state", listener1.onTransitionCalled);
        assertTrue("Second listener should be called for NORMAL state", listener2.onTransitionCalled);
    }
    
    @Test
	public void testExactlyOneStateChangeEventTriggered() {
    	listenerStub listener = new listenerStub();
    	failListenerStub faiListener = new failListenerStub();
    	
    	stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
    	
        stateLogic.registerListener(States.BLOCKED, listener);
        
        for (States s : States.values()) {
        	if (!s.equals(States.BLOCKED)) {
        		stateLogic.registerListener(s, faiListener);
        	}
        }
        
        stateLogic.gotoState(States.BLOCKED);
	}
    
    
    // - - - - - - - - - - Test gotoState - - - - - - - - - -

    @Test(expected = InvalidStateTransitionException.class)
    public void testInvalidTransition() throws InvalidStateTransitionException {
    	listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.BLOCKED, listener);
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
    	stateLogic.gotoState(States.BLOCKED); // Allowed transition
        stateLogic.gotoState(States.SUSPENDED); // not allowed should throw error
        
    }

    @Test
    public void testValidTransition() throws InvalidStateTransitionException {
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
        listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.BLOCKED, listener);
        stateLogic.gotoState(States.BLOCKED);
        assertTrue("State should be BLOCKED after valid transition", stateLogic.inState(States.BLOCKED));
    }

    @Test
    public void testEventListenerTriggeredOnTransition() throws InvalidStateTransitionException {
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
    	listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.BLOCKED, listener);

        stateLogic.gotoState(States.BLOCKED);
        assertTrue("Listener's onTransition should be called during the state transition", listener.onTransitionCalled);
    }
    
    // - - - - - - - - - - Test getLastState - - - - - - - - - -
    @Test
    public void testGetLastState() throws InvalidStateTransitionException {
        stateLogic.registerTransition(States.NORMAL, States.BLOCKED);
        listenerStub listener = new listenerStub();
        stateLogic.registerListener(States.BLOCKED, listener);
        stateLogic.gotoState(States.BLOCKED);
        assertEquals(States.NORMAL, stateLogic.getLastState());
    }
    // - - - - - - - - - - Helper classes for testing - - - - - - - - - -
    
    private class listenerStub extends AbstractStateTransitionListener {
        public boolean onTransitionCalled = false;

        public listenerStub() {
            super(centralLogic);
        }

        @Override
        public void onTransition() {
            onTransitionCalled = true;
        }
    }

    private class failListenerStub extends AbstractStateTransitionListener {
    	
        public failListenerStub() {
            super(centralLogic);
        }

        /**
         * Should not happen
         */
        @Override
        public void onTransition() {
            fail();
        }
    }
}
