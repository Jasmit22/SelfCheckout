package com.thelocalmarketplace.software.test.gui.customer;

import static org.junit.Assert.*;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterPLUCallback;

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

public class AddPLUItemButtonListenerTest {
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	
    public PriceLookUpCode PLUCode1;
    public PLUCodedProduct PLUProduct1;
    public String PLUString1;
    public Mass PLUMass1;
    public PLUCodedItem PLUItem1;

    public EnterPLUCallback PLUCallback;
    
    public JButton PLUButton;
	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		session = new SelfCheckoutStationSoftware(station);
		PLUCallback = new EnterPLUCallback(session);
		
//		PLUButton = getComponent("AddItem_PLUButton", JButton.class, session.view.getCustomerView());
//		PLUButton.addActionListener(PLUCallback);
		
		PLUString1 = new String("88888");
        PLUCode1 = new PriceLookUpCode(PLUString1);
        PLUProduct1 = new PLUCodedProduct(PLUCode1, "Cucumber", (long)8.88);

        ProductDatabases.PLU_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		
        ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCode1, PLUProduct1); 
        ProductDatabases.INVENTORY.put(PLUProduct1, 10);
        
        PLUMass1 = new Mass((double) 8888);
        PLUItem1 = new PLUCodedItem(PLUCode1, PLUMass1);
		
        session.setEnabled(true);
        session.startSession();
	}
	
	/**
	 * Tests Entering correct PLU code and placing in bagging area and checking if cart updates item
	 */
	@Test 
	public void testEnterPLUCallback() {
		PLUCallback.onEnterPressed(PLUString1);
		station.getBaggingArea().addAnItem(PLUItem1);
		assertTrue(session.getProductLogic().getCart().containsKey(PLUProduct1));
	}
	
	/**
	 * Tests entering invalid sequence/code, test should fail
	 */
    @Test 
	public void testEnterEmptySequencePLUCallback() {
		PLUCallback.onEnterPressed("1");
		
	}

    
    
	
    
}
