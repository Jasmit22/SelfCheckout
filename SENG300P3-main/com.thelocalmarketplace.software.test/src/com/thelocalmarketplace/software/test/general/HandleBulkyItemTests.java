package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;

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
public class HandleBulkyItemTests {
	
	private SelfCheckoutStationGold station;
	private SelfCheckoutStationSoftware session;
	private BarcodedItem barcodedItem;
	private BarcodedProduct product;
	
	
	/** Ensures failures do not occur from scanner failing to scan item, thus isolating test cases */
	public void scanUntilAdded() {
		do {
			station.getHandheldScanner().scan(barcodedItem);
		} while (!session.getProductLogic().getCart().containsKey(product));
	}
	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		station = new SelfCheckoutStationGold();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		
		Barcode barcode = new Barcode(new Numeral[] {Numeral.one});
		barcodedItem = new BarcodedItem(barcode, Mass.ONE_GRAM);
		product = new BarcodedProduct(barcode, "item", 5, barcodedItem.getMass().inGrams().doubleValue());
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);
	}
	
	@After 
	public void tearDown() {
		PowerGrid.engageFaultyPowerSource();
	}
	
	@Test 
	public void testSkipBaggingNotifiesAttendant() {
		//session.productLogic = attendantLogic;
		scanUntilAdded();
		//session.getMassLogic().skipBaggingRequest(barcodedItem.getBarcode());
		//assertTrue(attendantLogic.requestApprovalCalled);
		session.getMassLogic().baggingDiscrepencyDetected();
		assertTrue(session.getMassLogic().inBaggingDiscrepency);
	} 
	
	@Test
	public void testSkipBaggingBlocksStation() {
		scanUntilAdded();
		//session.getMassLogic().skipBaggingRequest(barcodedItem.getBarcode());
		session.getMassLogic().baggingDiscrepencyDetected();
		assertTrue(this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	// Expected reaction not clear; we have therefore assumed unblocking when weight discrepancy removed is expected
	@Test 
	public void testSkipBaggingAddsAnyways() {
		scanUntilAdded();
		//session.getMassLogic().skipBaggingRequest(barcodedItem.getBarcode());
		station.getBaggingArea().addAnItem(barcodedItem);
		assertFalse(this.session.getStateLogic().inState(States.BLOCKED));
	}
	
	@Test
	public void testAttendantApprovalReducesExceptedWeight() {
		scanUntilAdded();
		//session.getMassLogic().skipBaggingRequest(barcodedItem.getBarcode());
		//session.getMassLogic().grantApprovalSkipBagging(barcodedItem.getBarcode());
		session.getProductLogic().grantApprovalSkipBaggingOfLastProductAdded();
		assertFalse(session.getMassLogic().checkWeightDiscrepancy());
	}
	
	@Test 
	public void testAttendantApprovalUnblocksStation() {
		scanUntilAdded();
		//session.getMassLogic().skipBaggingRequest(barcodedItem.getBarcode());
		//session.getMassLogic().grantApprovalSkipBagging(barcodedItem.getBarcode());
		session.getProductLogic().grantApprovalSkipBaggingOfLastProductAdded();
		assertFalse(this.session.getStateLogic().inState(States.BLOCKED)); // Ensures no longer blocked
	}
}
