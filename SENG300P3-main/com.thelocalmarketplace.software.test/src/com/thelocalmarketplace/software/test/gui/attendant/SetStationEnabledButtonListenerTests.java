package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertTrue;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.SetStationEnabledButtonListener;

import powerutility.PowerGrid;

public class SetStationEnabledButtonListenerTests {

	
	private SetStationEnabledButtonListener listenerTrue;
	private SetStationEnabledButtonListener listenerFalse;
	private JButton button;

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();

		AbstractSelfCheckoutStation.resetConfigurationToDefaults();

		// d1 = new dummyProductDatabaseWithOneItem();
		// d2 = new dummyProductDatabaseWithNoItemsInInventory();

		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		logic = new SelfCheckoutStationSoftware(station);
		button = new JButton();
		listenerTrue = new SetStationEnabledButtonListener(logic, true);
		listenerFalse = new SetStationEnabledButtonListener(logic, false);
		
		
	}
	@Test 
	public void testGoToEnabled() {
		button.addActionListener(listenerTrue);
		
		logic.setEnabled(false);
		
		button.doClick();
		assertTrue("station did not enter normal state", logic.isEnabled());
	}@Test 
	public void testGoToDisabled() {
		button.addActionListener(listenerFalse);
		
		logic.setEnabled(true);
		
		button.doClick();
		assertTrue("station did not enter normal state", !logic.isEnabled());
	}
	
}
