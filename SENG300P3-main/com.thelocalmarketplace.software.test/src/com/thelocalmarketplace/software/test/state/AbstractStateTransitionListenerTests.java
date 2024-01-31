package com.thelocalmarketplace.software.test.state;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.logic.StateLogic;
import com.thelocalmarketplace.software.state.AbstractStateTransitionListener;

import powerutility.PowerGrid;

/**
 * Tests AbstractStateTransitionListener
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
public class AbstractStateTransitionListenerTests {

    // Creates listener stub for testing 
    private class StateTransitionListenerStub extends AbstractStateTransitionListener {
        public boolean onTransitionCalled = false;
    	public StateTransitionListenerStub(SelfCheckoutStationSoftware logic) {
            super(logic);
        }

        @Override
        public void onTransition() {
            onTransitionCalled = true;
        }
    }
    
    private StateLogic stateLogic;
    private StateTransitionListenerStub listener;
    
    @Before 
    public void setup() {
    	AbstractSelfCheckoutStation.resetConfigurationToDefaults();
    	SelfCheckoutStationGold station = new SelfCheckoutStationGold();
        SelfCheckoutStationSoftware logic = new SelfCheckoutStationSoftware(station);
        stateLogic = logic.getStateLogic();
        listener = new StateTransitionListenerStub(logic);
        PowerGrid.engageUninterruptiblePowerSource();
        
        station.plugIn(PowerGrid.instance());
        station.turnOn();
        
        stateLogic.registerListener(States.BLOCKED, listener);
    }
    
    @After
    public void teardown() {
    	PowerGrid.engageFaultyPowerSource();
    	AbstractSelfCheckoutStation.resetConfigurationToDefaults();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullLogic() {
        new StateTransitionListenerStub(null);
    }

    @Test
    public void testOnTransitionMethod() throws Exception {
        stateLogic.gotoState(States.BLOCKED);
        assertTrue("onTransition should be called during the state transition", listener.onTransitionCalled);
    }
    
    @Test
    public void testOnTransitionMethodSameState() throws Exception {
    	stateLogic.registerListener(States.NORMAL, listener);
        stateLogic.gotoState(States.NORMAL);
        assertTrue("onTransition should be called during the state transition", listener.onTransitionCalled);
    }
}
