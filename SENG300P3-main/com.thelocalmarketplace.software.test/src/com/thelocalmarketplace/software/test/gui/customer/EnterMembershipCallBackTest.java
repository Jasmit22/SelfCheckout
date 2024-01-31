package com.thelocalmarketplace.software.test.gui.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.view.listeners.customer.EnterMembershipCallback;

import powerutility.PowerGrid;

public class EnterMembershipCallBackTest {
	private SelfCheckoutStationSoftware logic;
    private SelfCheckoutStationGold station;
    private EnterMembershipCallback callback;

    @Before
    public void setUp() {
    	PowerGrid.engageUninterruptiblePowerSource();
        SelfCheckoutStationGold.resetConfigurationToDefaults();
    	station = new SelfCheckoutStationGold();
        logic = new SelfCheckoutStationSoftware(station);
        callback = new EnterMembershipCallback(logic);
        station.plugIn(PowerGrid.instance());
        station.turnOn();
        logic.setEnabled(true);
        logic.startSession();  
 
        // Additional setup if necessary
        // For example, set the state to ADDMEMBERSHIP if needed
        logic.getStateLogic().gotoState(States.ADDMEMBERSHIP);
    }
    @Test
    public void testOnEnterPressedWithValidMembership() {
        String validMembership = "12345"; // Assuming this is a valid membership number
        logic.getMembershipLogic().registerMembershipToLocalDatabase(validMembership);
        
        callback.onEnterPressed(validMembership);

        // Assertions to check expected behavior
        // Verify that the membership number is set correctly in the logic
        assertEquals("Membership number should be set",
                     validMembership, logic.getMembershipLogic().getSelectedMembership().getMembershipNumber());
        
        // Additional checks can be performed depending on the logic implementation
    }
    @Test
    public void testOnEnterPressedWithInvalidMembership() {
        String invalidMembership = "1234"; // Invalid membership number
        
        callback.onEnterPressed(invalidMembership);

        // Assertions to check expected behavior
        // Verify that the membership number is not set or an error message is shown
        assertNull("Invalid membership number should not be set",
                   logic.getMembershipLogic().getSelectedMembership());
    }
    @Test
    public void testOnEnterPressedWithEmptyInput() {
        String emptyInput = ""; // Empty input
        callback.onEnterPressed(emptyInput);

        // Assertions to check expected behavior
        // Verify that the system's state or membership number remains unchanged
        assertNull("No membership number should be set for empty input",
                   logic.getMembershipLogic().getSelectedMembership());
    }
   
    @Test
    public void testOnEnterPressedNotInAddMemberShipStates() {
    	logic.getStateLogic().gotoState(States.SUSPENDED);
    	String validMembership = "12345"; // Assuming this is a valid membership number
        logic.getMembershipLogic().registerMembershipToLocalDatabase(validMembership);
        
        callback.onEnterPressed(validMembership);

        assertNull("Invalid membership number should not be set",
                   logic.getMembershipLogic().getSelectedMembership());
    }

}
