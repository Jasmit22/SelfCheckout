package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.assertFalse;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.ResolvePopUpButtonListener;

import powerutility.PowerGrid;

/**

@author Connell Reffo        (10186960)
@author Tara Strickland         (10105877)
@author Julian Fan            (30235289)
@author Samyog Dahal            (30194624)
@author Phuong Le            (30175125)
@author Daniel Yakimenka        (10185055)
@author Merick Parkinson        (30196225)
@author Julie Kim            (10123567)
@author Ajaypal Sallh        (30023811)
@author Nathaniel Dafoe        (30181948)
@author Anmol Ratol            (30231177)
@author Chantal del Carmen    (30129615)
@author Dana Al Bastrami        (30170494)
@author Maria Munoz            (30175339)
@author Ernest Shukla        (30156303)
@author Hillary Nguyen        (30161137)
@author Robin Bowering        (30123373)
@author Anne Lumumba            (30171346)
@author Jasmit Saroya        (30170401)
@author Fion Lei                (30134327)
@author Royce Knoepfli        (30172598)
*/
public class ResolvePopUpButtonTests {

	  private SelfCheckoutStationSoftware logic;
	  private SelfCheckoutStationGold station;
	  private JDialog pane;
	  private CustomerView view;
	  private ResolvePopUpButtonListener listener;

	  
	  @Before
	  public void setUp() {
		  	PowerGrid.engageUninterruptiblePowerSource();
	        SelfCheckoutStationGold.resetConfigurationToDefaults();
	    	station = new SelfCheckoutStationGold();
	        logic = new SelfCheckoutStationSoftware(station);
	        pane = new JDialog();
	        view = new CustomerView(logic); // Replace with actual constructor if needed

	        // Set callAttendantButton in CustomerView
	        view.CallAttendantButton = new JButton();

	        // Initialize JDialog
	        pane = new JDialog();

	        // Initialize and set listener
	        listener = new ResolvePopUpButtonListener(logic, pane);

	        station.plugIn(PowerGrid.instance());
	        station.turnOn();
	        logic.setEnabled(true);
	        logic.startSession();  
	} 
	  @Test
	  public void testResolvePopUpAction() {
	      // Simulate the Resolve button being pressed
	      ActionEvent event = new ActionEvent(pane, ActionEvent.ACTION_PERFORMED, "");
	      listener.actionPerformed(event);

	      // Assertions to check expected behavior
	      // Check if the dialog is disposed
	      assertFalse("Pane should be disposed", pane.isDisplayable());
	  }
}
