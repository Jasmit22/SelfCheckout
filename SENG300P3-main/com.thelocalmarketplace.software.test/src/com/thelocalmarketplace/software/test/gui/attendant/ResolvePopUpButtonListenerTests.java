package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.ResolvePopUpButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.customer.CallAttendantButtonListener;

import powerutility.PowerGrid;

public class ResolvePopUpButtonListenerTests {

	
	private ResolvePopUpButtonListener listener;
	private JDialog dialog;

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
		
		dialog = new JDialog();
		listener = new ResolvePopUpButtonListener(logic, dialog);
		button.addActionListener(listener);
		
		
	}
	private <T> T getComponent(String fieldName, Class<T> fieldType, Object instance) 
            throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldType.cast(field.get(instance));
    }
	@Test 
	public void testResolvePopup() throws NoSuchFieldException, IllegalAccessException {
		//button.addActionListener(listener);
		
		logic.setEnabled(false);
		
		button.doClick();
		
		CallAttendantButtonListener caller = getComponent("caller", CallAttendantButtonListener.class, listener);

		assertTrue("popup not resolved", !caller.checkStatus());
	}
	
}

