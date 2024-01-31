package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
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
public class AddOwnBagsTests {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;

	Mass bag1mass;
	Mass bag2mass;
	Mass invalidBagMass;
	Mass m_product;
	
	public BarcodedProduct product;
	public Barcode barcode;
	public Numeral digits;
	public Numeral[] barcode_numeral;
	
	// Although bags arent barcoded this will allow me to test logic
	Bag bag1;
	Bag bag2;
	Bag bag3;
	BarcodedItem b;
	
	@Before public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		//d1 = new dummyProductDatabaseWithOneItem();
		//d2 = new dummyProductDatabaseWithNoItemsInInventory();
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		session = new SelfCheckoutStationSoftware(station);
		
		bag1mass = new Mass((double)8);
		bag2mass = new Mass((double)10);
		invalidBagMass = new Mass((double)30);
		
		barcode_numeral = new Numeral[]{Numeral.one,Numeral.two, Numeral.three};
		barcode = new Barcode(barcode_numeral);
		
		product = new BarcodedProduct(barcode, "some item",5,(double)300.0);
		m_product = new Mass((double)300.0);
		b =  new BarcodedItem(barcode, m_product);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);
		
		
		
		bag1 = new Bag(bag1mass);
		bag2 = new Bag(bag2mass);
		bag3 = new Bag(invalidBagMass);
		
		session.setEnabled(true);
		
	}
	
	
	
	
	@Test public void addValidBagsTestBagMass() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag1);
		session.getMassLogic().endAddBags();
		assertTrue("bag mass did not update correctly", session.getMassLogic().getExpectedMass().equals(bag1mass));
		
	}@Test(expected = SimulationException.class) public void addBagsWhenSessionNotStarted() throws Exception {
		session.getMassLogic().startAddBags();
		
	}@Test(expected = SimulationException.class) public void endBagsWhenSessionNotStarted() throws Exception {
		session.getMassLogic().endAddBags();
		
	}@Test(expected = SimulationException.class) public void addBagsWhenNotInState() throws Exception {
		session.startSession();
		//session.getMassLogic().startAddBags();
		session.getMassLogic().endAddBags();
	}
	
	@Test public void addValidBagsTestNoDiscrepency() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag1);
		session.getMassLogic().endAddBags();
		assertTrue("bag mass did not update correctly", !session.getMassLogic().getBaggingDiscrepency());
		
	}
	
	@Test public void addMultipleValidBagsTestExpectedMass() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag1);
		station.getBaggingArea().addAnItem(bag2);
		session.getMassLogic().endAddBags();
		Mass expected = bag1mass.sum(bag2mass);
		assertTrue("bag mass did not update correctly",session.getMassLogic().getExpectedMass().equals(expected));
		
	}@Test public void addMultipleValidBagsTestNoDiscrepency() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag1);
		station.getBaggingArea().addAnItem(bag2);
		session.getMassLogic().endAddBags();
		assertTrue("bag mass did not update correctly", !session.getMassLogic().getBaggingDiscrepency());
		
	}
	
	@Test public void addInValidBagsTest() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag3);
		session.getMassLogic().endAddBags();
		//station.getBaggingArea().addAnItem(bag2);
		//Mass expected = bag1mass.sum(bag2mass);
		assertTrue("bag mass did not update correctly", session.getMassLogic().getBaggingDiscrepency());
		
	}@Test public void addValidBagsExitBaggingCheckBlockedTest() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag1);
		session.getMassLogic().endAddBags();
		//station.getBaggingArea().addAnItem(bag2);
		//Mass expected = bag1mass.sum(bag2mass);
		assertTrue("bag mass did not update correctly", !this.session.getStateLogic().inState(States.BLOCKED));
		
	}@Test public void addInvalidBagsFixByApproving() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag3);
		session.getMassLogic().endAddBags();
		session.getMassLogic().approveBaggingArea();
		assertTrue("bag mass did not update correctly", this.session.getStateLogic().inState(States.NORMAL));
		
	}@Test public void addInvalidBagsFixByApprovingCheckExpectedMass() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag3);
		session.getMassLogic().endAddBags();
		session.getMassLogic().approveBaggingArea();
		assertTrue("bag mass did not update correctly",session.getMassLogic().getExpectedMass().equals(invalidBagMass));
		
	}
	@Test public void addInvalidBagsFixByRemoving() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag3);
		session.getMassLogic().endAddBags();
		station.getBaggingArea().removeAnItem(bag3);
		session.getMassLogic().endAddBags();
		
		assertTrue("bag mass did not update correctly", !this.session.getStateLogic().inState(States.BLOCKED));
		
	}@Test public void addInvalidBagsFixByapprovingAddMoreBags() throws Exception {
		session.startSession();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag3);
		session.getMassLogic().endAddBags();
		session.getMassLogic().approveBaggingArea();
		session.getMassLogic().startAddBags();
		station.getBaggingArea().addAnItem(bag2);
		session.getMassLogic().endAddBags();

		assertTrue("bag mass did not update correctly", !this.session.getMassLogic().getBaggingDiscrepency());
		
	}
	
	public static class Bag extends Item{
		public Bag(Mass m) {
			super(m);
		}
	}
}