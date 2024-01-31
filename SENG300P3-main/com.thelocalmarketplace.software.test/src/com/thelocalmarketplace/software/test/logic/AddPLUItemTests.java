package com.thelocalmarketplace.software.test.logic;

import org.junit.Before;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.bag.ReusableBag;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
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

public class AddPLUItemTests {

    
    SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;

    // PLU Database Initiation
    public PriceLookUpCode PLUCode1;
    public PriceLookUpCode PLUCode2;
    public PriceLookUpCode PLUCode3;

    public PLUCodedProduct PLUProduct1;
    public PLUCodedProduct PLUProduct2;
    public PLUCodedProduct PLUProduct3;

    public String PLUString1;
    public String PLUString2;
    public String PLUString3;

    public Mass PLUMass1;
    public Mass PLUMass2;
    public Mass PLUMass3;

    public PLUCodedItem PLUItem1;
    public PLUCodedItem PLUItem2;
    public PLUCodedItem PLUItem3;
    
	public ReusableBag bag1;

    
    @Before
    public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		this.station = new SelfCheckoutStationBronze();
        
        PLUString1 = new String("88888");
        PLUString2 = new String("2222");
        PLUString3 = new String("11100");
        
        PLUCode1 = new PriceLookUpCode(PLUString1);
        PLUCode2 = new PriceLookUpCode(PLUString2);
        PLUCode3 = new PriceLookUpCode(PLUString3);

        PLUProduct1 = new PLUCodedProduct(PLUCode1, "Cucumber", (long)8.88);
        PLUProduct2 = new PLUCodedProduct(PLUCode2, "Potato", (long)2.22);
        PLUProduct3 = new PLUCodedProduct(PLUCode3, "Onion", (long)1.11);

        ProductDatabases.PLU_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();

        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCode1, PLUProduct1); 
        ProductDatabases.INVENTORY.put(PLUProduct1, 10);
        
        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCode2, PLUProduct2);
        ProductDatabases.INVENTORY.put(PLUProduct2, 2);
        
        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCode3, PLUProduct3);
        
        PLUMass1 = new Mass((double) 8888);
        PLUMass2 = new Mass((double) 2222);
        PLUMass3 = new Mass((double) 1111); 
        
        PLUItem1 = new PLUCodedItem(PLUCode1, PLUMass1);
        PLUItem2 = new PLUCodedItem(PLUCode2, PLUMass2);
        PLUItem3 = new PLUCodedItem(PLUCode3, PLUMass3);

		bag1 = new ReusableBag();


    }

    /**
     * Tests when the power is off and try to add PLU item to cart
     */
    @Test (expected = NoPowerException.class)
    public void testNoPower() {
		station.plugIn(PowerGrid.instance());
		station.turnOff();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);			
    }
    /**
     * Tests when power is on and try to add PLU item to cart
     */
    @Test 
    public void testPowerOn(){
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);
		assertTrue("Item was successfully added to cart", session.getProductLogic().getCart().size() == 1);
    }

    /**
     * Tests when power is on but session is inactive
     */
    @Test (expected = InvalidStateSimulationException.class)
    public void testInactiveSessionAddPLU() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.stopSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);
    }
    
    /**
     * Tests power on and the correct PLU code is added to the cart
     */
    @Test
    public void testPowerOnCorrectPLU() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);
		assertTrue("Item was successfully added to cart", session.getProductLogic().getCart().containsKey(PLUProduct1));
    }
    
	/**
	 * Tests adding null PLU Code to cart
	 */
    @Test (expected = InvalidStateSimulationException.class) 
    public void testAddNullPLUCodeToCart() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();	
		session.getProductLogic().startAddPLUProductToCart(null);
    }
    /**
     * Tests adding PLU product with null code to cart
     */
    @Test (expected = NullPointerSimulationException.class)
    public void testNullPLUCode1() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		PLUCodedProduct nullCode = new PLUCodedProduct(null,"test",1);
		session.getProductLogic().startAddPLUProductToCart(nullCode.getPLUCode());
    }
    
    /**
     * Tests adding PLU product with null description
     */
    @Test (expected = NullPointerSimulationException.class)
    public void testNullDescriptionOnPLUProduct() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();	
		PriceLookUpCode test = new PriceLookUpCode("1111");
		PLUCodedProduct nullProduct = new PLUCodedProduct(test,null,1);
		session.getProductLogic().startAddPLUProductToCart(nullProduct.getPLUCode());
    }
    
    /**
     * Tests adding an invalid PLU code (> 5 digits) to the cart
     */
    @Test (expected = InvalidArgumentSimulationException.class)
    public void testInvalidPLUCode() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		PriceLookUpCode invalid = new PriceLookUpCode("111000");
		session.getProductLogic().startAddPLUProductToCart(invalid);
		station.getBaggingArea().addAnItem(PLUItem1);
    }
    /**
     * Tests adding an invalid PLU code (< 4 digits) to the cart
     */
    @Test (expected = InvalidArgumentSimulationException.class)
    public void testInvalidPLUCode1() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		PriceLookUpCode invalid = new PriceLookUpCode("111");
		session.getProductLogic().startAddPLUProductToCart(invalid);
		station.getBaggingArea().addAnItem(PLUItem1);
    }
    
    /**
     * Tests for adding any illegal letters
     */
    @Test (expected = InvalidArgumentSimulationException.class)
    public void testIllegalPLUCode() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		PriceLookUpCode invalid = new PriceLookUpCode("111B");
		session.getProductLogic().startAddPLUProductToCart(invalid);
		station.getBaggingArea().addAnItem(PLUItem1);	
    }
    
    
    @Test
    public void testValidPLUCodeBaggingArea() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);
		assertTrue("Valid PLU code added to bagging area", session.getProductLogic().getCart().get(PLUProduct1) == 1);
    }

//    @Test
//    public void testInvalidPLUCodeBaggingArea() {
//		station.plugIn(PowerGrid.instance());
//		station.turnOn();
//		session = new SelfCheckoutStationSoftware(station);
//		session.setEnabled(true);
//		session.startSession();
//		
//		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
//		session.getProductLogic().startAddPLUProductToCart(PLUCode2);
//
////		assertTrue("item was not successfully added to cart", session.getProductLogic().getCart().size() ==2);
//
//    }
    /**
     * Tests adding multiple PLU items to cart and putting them in the bagging area
     */
    @Test
    public void testAddingMultiplePLUtoCart() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		station.getBaggingArea().addAnItem(PLUItem1);
		session.getProductLogic().startAddPLUProductToCart(PLUCode2);
		station.getBaggingArea().addAnItem(PLUItem2);
		assertTrue("Multiple items correctly added to cart", session.getProductLogic().getCart().size()==2);
    }

    /**
     * Tests adding multiple PLU items to cart without adding to bagging area, session should be blocked
     */
    @Test
    public void testAddingMultipleItemsNoBagging() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		session.getProductLogic().startAddPLUProductToCart(PLUCode2);
		station.getBaggingArea().addAnItem(PLUItem1);
		station.getBaggingArea().addAnItem(PLUItem2);
		assertTrue("Weight Discrepancy Detected", session.getStateLogic().getState() == States.BLOCKED);
    }
    
    /**
     * Tests to make sure customer can't add PLU items when in bagging state
     * @throws OverloadedDevice 
     */
//    @Test
//    public void testAddingBagsInProgressAndAddPLU() throws OverloadedDevice {
//		ReusableBag[] bags = new ReusableBag[] {bag1};
//		station.plugIn(PowerGrid.instance());
//		station.turnOn();
//		session = new SelfCheckoutStationSoftware(station);
//		session.setEnabled(true);
//		station.getReusableBagDispenser().load(bags);
//		session.startSession();
//		session.getBuyBagsLogic().startBuyBags();
//		session.getBuyBagsLogic().dispenseBags(1);
//		station.getBaggingArea().addAnItem(bag1);
//		System.out.println(session.getStateLogic().getState());
//		session.getProductLogic().startAddPLUProductToCart(PLUCode3);
//    }
    
    /**
     *  Test adding bags then inputting PLU items
     * @throws OverloadedDevice 
     */
    @Test
    public void testAddBagsAndPLU() throws OverloadedDevice {
		ReusableBag[] bags = new ReusableBag[] {bag1};
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		station.getReusableBagDispenser().load(bags);
		session.startSession();
		session.getBuyBagsLogic().startBuyBags();
		session.getBuyBagsLogic().dispenseBags(1);
		station.getBaggingArea().addAnItem(bag1);
		session.getBuyBagsLogic().endBuyBags();
		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
		assertTrue("State should be ADDINGITEMS", session.getStateLogic().getState() == States.ADDITEM);
    }
    
	/**
	 * Tests adding a valid PLU product with no stock/inventory
	 */
    @Test (expected = InvalidStateSimulationException.class)
    public void testNoItemInventory() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		session.getProductLogic().startAddPLUProductToCart(PLUCode3);
		station.getBaggingArea().addAnItem(PLUItem3);
    }


    /**
     * Tests if PLU Code is not in Database
     */
    @Test (expected = InvalidStateSimulationException.class)
    public void testPLUNotInDatabase() {
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
		session.startSession();
		PriceLookUpCode invalid = new PriceLookUpCode("1111");
		session.getProductLogic().startAddPLUProductToCart(invalid);
    }
    
//    /**
//     * Tests placing the PLU item in the bagging area before inputting the code
//     */
//    @Test
//    public void testAddPLUItemInBaggingAreaBeforeInputtingCode() {
//		station.plugIn(PowerGrid.instance());
//		station.turnOn();
//		session = new SelfCheckoutStationSoftware(station);
//		session.setEnabled(true);
//		session.startSession();
//		station.getBaggingArea().addAnItem(PLUItem1);
//		session.getProductLogic().startAddPLUProductToCart(PLUCode1);
//    }

    @After
    public void tearDown() {
		PowerGrid.engageUninterruptiblePowerSource();
    }
}
