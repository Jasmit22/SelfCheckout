package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.NoPowerException;
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
public class WeightDiscrepencyTests {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	
	//stuff for database
	public ProductDatabases database_one_item;
	public Barcode barcode;
	public Numeral digits;
	
	public BarcodedItem bitem;
	public Mass itemMass;
	
	public BarcodedItem bitem2;
	public Mass itemMass2;
	public BarcodedItem bitem3;
	public Mass itemMass3;
	public Numeral[] barcode_numeral;
	public BarcodedProduct product;
	
	
	//the following function was taken mainly from Angelina's tests for bulkyitems
	public void scanUntilAdded(Product p, BarcodedItem b) {
		while(!session.getProductLogic().getCart().containsKey(p)) {
			station.getHandheldScanner().scan(b);
			System.out.println("Scanned: " + b);
		}
	}
	
	@Before public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		
		//initialize database
		barcode_numeral = new Numeral[]{Numeral.one,Numeral.two, Numeral.three};
		barcode = new Barcode(barcode_numeral);
		product = new BarcodedProduct(barcode, "some item",5,(double)300.0);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);


		//initialize barcoded item
		itemMass = new Mass((long) 1000000);
		bitem = new BarcodedItem(barcode, itemMass);
		itemMass2 = new Mass((double) 300.0);//300.0 grams
		bitem2 = new BarcodedItem(barcode, itemMass2);
		itemMass3 = new Mass((double) 300.0);//3.0 grams
		bitem3 = new BarcodedItem(barcode, itemMass3);
	}

	
	/** Ensures failures do not occur from scanner failing to scan item, thus isolating test cases */
	public void scanUntilAdded(BarcodedItem item) {
		do {
			station.getHandheldScanner().scan(item);
		} while (!session.getProductLogic().getCart().containsKey(product));
	}
	
	
	@Test (expected = NoPowerException.class) 
	public void testWithNoPower() {
		station.turnOff();
		this.scanUntilAdded(product, bitem);
		station.getBaggingArea().addAnItem(bitem);
	}
	
	@Test 
	public void testNoDiscrepency() {
		this.scanUntilAdded(product, bitem3);
		station.getBaggingArea().addAnItem(bitem3);
		assertTrue("weight discrepency detected", !this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test 
	public void testHasDiscrepencyDifferentThanItem() {
		this.scanUntilAdded(product, bitem2);
		station.getBaggingArea().addAnItem(bitem);
		assertTrue("weight discrepancy not detected", session.getMassLogic().checkWeightDiscrepancy());
		assertTrue("station not blocked", this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test 
	public void testHasDiscrepencyNoItemScanned() {
		//station.scanner.scan(bitem2);
		station.getBaggingArea().addAnItem(bitem2);
		assertTrue("weight discrepency not detected", this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test 
	public void testNoDiscrepencyWeightDifferenceLessThanSensitivity() {
		Mass sensativity = station.getBaggingArea().getSensitivityLimit();
		Mass m = sensativity.sum(itemMass2);
		BarcodedItem i = new BarcodedItem(barcode, m);
		this.scanUntilAdded(product, bitem2);
		station.getBaggingArea().addAnItem(i);
		assertTrue("weight discrepency detected", !this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test
	public void testHasDiscrepency() {
		this.scanUntilAdded(product, bitem2);
		station.getBaggingArea().addAnItem(bitem);
		assertTrue("weight discrepancy detected", this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test 
	public void testNoDiscrepencyItemNotScannedPlacedThenRemoved() {
		station.getBaggingArea().addAnItem(bitem);
		station.getBaggingArea().removeAnItem(bitem);
		assertTrue("weight discrepency tedected", !this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test 
	public void testNoDiscrepencyPlacedWrongWeightThenCorrected() {
		this.scanUntilAdded(product, bitem3);
		station.getBaggingArea().addAnItem(bitem);
		station.getBaggingArea().removeAnItem(bitem);
		station.getBaggingArea().addAnItem(bitem3);
		assertFalse("weight discrepancy detected when shouldn't be", this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test public void testHasDiscrepencyRescan() {
		this.scanUntilAdded(product, bitem);
		this.scanUntilAdded(product, bitem);
		assertEquals(1, session.getProductLogic().getCart().size());
	}
	
	@Test(expected = SimulationException.class)
	public void testAddExpectedWeightNonExistentBarcode() {
		this.session.getMassLogic().addExpectedMassOfBarcodedProduct(new Barcode(new Numeral[] {Numeral.one}));
	}
	
	@Test(expected = SimulationException.class)
	public void testRemoveExpectedWeightNonExistentBarcode() {
		this.session.getMassLogic().removeExpectedMassOfBarcodedProduct(new Barcode(new Numeral[] {Numeral.one}));
	}
}