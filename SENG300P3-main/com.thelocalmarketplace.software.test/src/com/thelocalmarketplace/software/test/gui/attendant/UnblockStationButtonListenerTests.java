package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertTrue;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.UnblockStationButtonListener;

import powerutility.PowerGrid;

public class UnblockStationButtonListenerTests {

	
	private UnblockStationButtonListener listener;
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
		listener = new UnblockStationButtonListener(logic);
		button.addActionListener(listener);
		
	}
	@Test 
	public void testStateChangeOnClick() {
		logic.getStateLogic().gotoState(States.BLOCKED);
		button.doClick();
		assertTrue("station did not enter normal state", logic.getStateLogic().inState(States.NORMAL));
	}
	
}
