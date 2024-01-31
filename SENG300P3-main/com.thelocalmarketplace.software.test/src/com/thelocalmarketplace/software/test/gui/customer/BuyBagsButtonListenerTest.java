package com.thelocalmarketplace.software.test.gui.customer;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.ReusableBag;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterBagCountCallback;
import com.thelocalmarketplace.software.state.listeners.AddBagsListener;
import com.thelocalmarketplace.software.state.listeners.BuyBagsListener;

import powerutility.PowerGrid;

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
 * @author Chantal del Carmen	(30129615)
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

public class BuyBagsButtonListenerTest {
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	
	ReusableBag bag1;
	ReusableBag bag2;
	ReusableBag bag3;
	ReusableBag bag4;
	ReusableBag bag5;
	
	AddBagsListener addBagsListener;
	BuyBagsListener	buyBagsListener;
	
	EnterBagCountCallback callback;
	
	@Before
	public void setUp() throws OverloadedDevice {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		session = new SelfCheckoutStationSoftware(station);
		callback = new EnterBagCountCallback(session);
		
		bag1 = new ReusableBag();
		bag2 = new ReusableBag();
		bag3 = new ReusableBag();
		bag4 = new ReusableBag();
		bag5 = new ReusableBag();
		
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3,bag4,bag5};

		station.getReusableBagDispenser().load(bags);
		session.setEnabled(true);

	}
	
	/**
	 * Tests entering correct amount of bags to dispense
	 */
	@Test
	public void testEnterBagCountCallback() {
        session.startSession();
		callback.onEnterPressed("5");	
		session.getBuyBagsLogic().endBuyBags();
	}
	
	/*
	 * Tests adding no bags to dispense
	 */
	@Test
	public void testEnterZeroBagCount() {
        session.startSession();
		callback.onEnterPressed("0");
	}
	
	/*
	 * Tests entering number of bags than allowed/in dispenser
	 */
	@Test 
	public void testEnterExceedBagCount() {
        session.startSession();
		callback.onEnterPressed("6");
	}
	
	/**
	 * Tests entering null bags
	 */
//	@Test
//	public void testEnterNullSequenceBagCount() {
//		callback.onEnterPressed(null);
//	}
	
	@Test
	public void testSimulationExceptionOnEnter() {
        session.startSession();
		session.stopSession();
		callback.onEnterPressed("3");	
	}
}
