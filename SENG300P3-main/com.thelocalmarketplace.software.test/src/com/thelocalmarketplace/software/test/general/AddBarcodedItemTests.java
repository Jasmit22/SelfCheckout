package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
//import com.jjjwelectronics.scanner.BarcodeScanner;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.NoPowerException;
import powerutility.PowerGrid;

/**
 * Tests for adding a barcoded item to the cart
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
public class AddBarcodedItemTests {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	
	//stuff for database
	
	public Barcode barcode;
	public Barcode barcode2;
	public Numeral digits;
	
	public BarcodedItem bitem;
	public Mass itemMass;
	
	public BarcodedItem bitem2;
	public Mass itemMass2;
	public BarcodedItem bitem3;
	public Mass itemMass3;
	
	public BarcodedItem bitem4;
	public Mass itemMass4;
	
	public BarcodedItem bitem5;
	public Mass itemMass5;
	public Numeral[] barcode_numeral;
	public Numeral[] barcode_numeral2;
	public Numeral[] barcode_numeral3;
	public Barcode b_test;
	public BarcodedProduct product;
	public BarcodedProduct product2;
	public BarcodedProduct product3;
	
	@Before public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		//d1 = new dummyProductDatabaseWithOneItem();
		//d2 = new dummyProductDatabaseWithNoItemsInInventory();
		this.station = new SelfCheckoutStationBronze();
		
		//initialize database
		barcode_numeral = new Numeral[] {Numeral.one, Numeral.two, Numeral.three};
		barcode_numeral2 = new Numeral[] {Numeral.three, Numeral.two, Numeral.three};
		barcode_numeral3 = new Numeral[] {Numeral.three, Numeral.three, Numeral.three};
		barcode = new Barcode(barcode_numeral);
		barcode2 = new Barcode(barcode_numeral2);
		b_test = new Barcode(barcode_numeral3);
		product = new BarcodedProduct(barcode, "some item",(long)5.99,(double)300.0);
		product2 = new BarcodedProduct(barcode2, "some item 2",(long)1.00,(double)300.0);
		product3 = new BarcodedProduct(b_test, "some item 3",(long)1.00,(double)300.0);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
		ProductDatabases.INVENTORY.put(product2, 1);	

		//initialize barcoded item
		itemMass = new Mass((long) 1000000);
		bitem = new BarcodedItem(barcode2, itemMass);
		itemMass2 = new Mass((double) 300.0);//300.0 grams
		bitem2 = new BarcodedItem(barcode, itemMass2);
		itemMass3 = new Mass((double) 300.0);//3.0 grams
		bitem3 = new BarcodedItem(barcode, itemMass3);
		bitem4 = new BarcodedItem(b_test, itemMass3);
		itemMass5 = new Mass((double) 300.0);
		bitem5 = new BarcodedItem(barcode2,itemMass5);
	}
	
	
	//the following function was taken mainly from Angelina's tests for bulkyitems
	public void scanUntilAdded(Product p, BarcodedItem b) {
		int cnt = 0;
		
		while(cnt < 1000 && !session.getProductLogic().getCart().containsKey(p)) {
			station.getHandheldScanner().scan(b);
			cnt++;
		}
	}
	
	//Test for when there is no power for the barcode to be scanned
	@Test (expected = NoPowerException.class) public void testNoPower(){
		station.plugIn(PowerGrid.instance());
		station.turnOff();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product, bitem2);
	}@Test public void testPowerOn(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product, bitem2);
		
		assertTrue("item was not successfully added to cart", session.getProductLogic().getCart().size() ==1);
	}@Test public void testPowerOnRightItem(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product, bitem2);
		//long s = session.cart.getLastItem().getPrice();
		
		assertTrue("item was not successfully added to cart", session.getProductLogic().getCart().containsKey(product));
	}
	
	@Test
	public void testPowerOntwoScansNoBaggingAreaUpdates(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product, bitem2);
		this.scanUntilAdded(product2, bitem);
		
		assertTrue("item was not successfully added to cart", session.getProductLogic().getCart().size() ==1);
	}@Test public void testPowerOntwoScansWithBaggingAreaUpdatesRightItem(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product2, bitem5);
		station.getBaggingArea().addAnItem(bitem5);
		this.scanUntilAdded(product, bitem3);
		//long s = session.cart.getLastItem().getPrice();
		Integer cart = session.getProductLogic().getCart().get(product);
		
		assertTrue("item was not successfully added to cart", cart != null && cart.equals(1));
	}@Test(expected = SimulationException.class) 
	public void testPowerOnScanItemNoInventory(){
		station.plugIn(PowerGrid.instance());
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b_test, product3);
		ProductDatabases.INVENTORY.put(product3, 0);
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product3, bitem4);
		
		//assertTrue("item was not successfully added to cart", s == (long) 1.00);
	}@Test(expected = SimulationException.class) 
	public void testPowerOnScanItemNotInDatabase(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		this.scanUntilAdded(product3, bitem4);
		
		//assertTrue("item was not successfully added to cart", s == (long) 1.00);
	}
	
}