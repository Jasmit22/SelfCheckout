package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.ReusableBag;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.logic.BuyBagsLogic;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.SimulationException;
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

public class BuyBagsTests {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;

	ReusableBag bag1;
	ReusableBag bag2;
	ReusableBag bag3;
	ReusableBag bag4;
	ReusableBag bag5;
	
	@Before public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		bag1 = new ReusableBag();
		bag2 = new ReusableBag();
		bag3 = new ReusableBag();
		bag4 = new ReusableBag();
		bag5 = new ReusableBag();
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
	}
	@Test (expected = SimulationException.class)
	public void startBuyBagsNoStartOfSession() {
		session.getBuyBagsLogic().startBuyBags();
	}
	@Test (expected  = SimulationException.class) public void startBuyBagstwice() {
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().startBuyBags();
		
	}
	@Test (expected  = SimulationException.class) public void endBuyBagsNoSessionstarted() {
		//session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().endBuyBags();

	}@Test public void startBuyBagsState() {
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		assertTrue("did not enter buy bags state", session.getStateLogic().inState(States.BUYBAGS));
	}@Test public void endBuyBagsState() {
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().endBuyBags();
		assertTrue("state is not normal after not adding any bags", session.getStateLogic().inState(States.NORMAL));
	}@Test public void testAddBagsInDiscrepency() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3,bag4,bag5};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(5);
		session.getHardware().getBaggingArea().addAnItem(bag1);
		session.getHardware().getBaggingArea().addAnItem(bag2);
		session.getHardware().getBaggingArea().addAnItem(bag3);
		
		assertTrue("state is not blocked after adding not enough bags to bagging area", session.getStateLogic().inState(States.BLOCKED));
	}@Test public void testAddBagsNoDiscrepency() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3,bag4,bag5};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(5);
		session.getHardware().getBaggingArea().addAnItem(bag1);
		session.getHardware().getBaggingArea().addAnItem(bag2);
		session.getHardware().getBaggingArea().addAnItem(bag3);
		session.getHardware().getBaggingArea().addAnItem(bag4);
		session.getHardware().getBaggingArea().addAnItem(bag5);
		
		assertTrue("after adding bags to bagging area, state is not normal", session.getStateLogic().inState(States.NORMAL));
	}@Test public void testAddBagsDispenserEmpty() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(5);
		
		assertTrue("bags were dispensed when shouldnt be", session.getBagDispenserController().getNumberReusableBagsDispensed()==3);
	}@Test public void testAddBagsDispenserCostCartUpdates() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(5);
		
		assertTrue("bags were dispensed when shouldnt be", session.getProductLogic().getBalanceOwed().equals(new BigDecimal(3)));
	}@Test public void testAddBagsMultiple() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3,bag4,bag5};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(5);
		session.getHardware().getBaggingArea().addAnItem(bag1);
		session.getHardware().getBaggingArea().addAnItem(bag2);
		session.getBuyBagsLogic().dispenseBags(3);
		session.getHardware().getBaggingArea().addAnItem(bag3);
		session.getHardware().getBaggingArea().addAnItem(bag4);
		session.getHardware().getBaggingArea().addAnItem(bag5);
		assertTrue("after adding bags to bagging area multiple times, state is not normal", session.getStateLogic().inState(States.NORMAL));
	}@Test public void testAddBagsMultipleCost() throws OverloadedDevice, EmptyDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1,bag2,bag3,bag4,bag5};
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(2);
		session.getHardware().getBaggingArea().addAnItem(bag1);
		session.getHardware().getBaggingArea().addAnItem(bag2);
		session.getBuyBagsLogic().dispenseBags(3);
		session.getHardware().getBaggingArea().addAnItem(bag3);
		session.getHardware().getBaggingArea().addAnItem(bag4);
		session.getHardware().getBaggingArea().addAnItem(bag5);
		assertTrue("after adding bags to bagging area multiple times, cost of cart is not correct", session.getProductLogic().getBalanceOwed().equals(BuyBagsLogic.BAG_PRICE.multiply(new BigDecimal(5))));

	}
	
	@Test public void testRemoveBag() throws OverloadedDevice, EmptyDevice {
	ReusableBag[] bags = new ReusableBag[] {bag1};
	station.getReusableBagDispenser().load(bags);
	session.startSession();
	session.getBuyBagsLogic().startBuyBags();
	session.getBuyBagsLogic().dispenseBags(1);
	session.getHardware().getBaggingArea().addAnItem(bag1);;
	session.getBuyBagsLogic().removeBag();
	assertTrue("after removing bag, station is blocked", session.getStateLogic().inState(States.BLOCKED));
	}
	

}
