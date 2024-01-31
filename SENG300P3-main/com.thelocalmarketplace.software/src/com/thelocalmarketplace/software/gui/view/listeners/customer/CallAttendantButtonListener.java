package com.thelocalmarketplace.software.gui.view.listeners.customer;

import java.awt.Color;
import java.awt.event.ActionEvent;

import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;
import com.thelocalmarketplace.software.gui.view.CustomerView;

/**
 * Causes appropriate events to occur when the "CallAttendant" button is pressed on the customer station
 * 
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

public class CallAttendantButtonListener extends AbstractLogicActionListener{

	private boolean status;
	private CustomerView View;
	
	
	/**
	 * Constructor, sets the initial status to false
	 * @param logic - the SelfCheckoutStation to which the button belongs
	 */
	public CallAttendantButtonListener(ISelfCheckoutStationSoftware logic) {
		super(logic);
		status = false;
	}
	
	/**
	 * Directs what must be done upon Call Attendant button press 
	 * What it does: 
	 * 1. Toggles the color of the call attendant button in Customer View (to indicate if attendant is notified or not)
	 * 2. Calls "updateonCustomerCallsAttendant" method on the LocalViewController to which this station belongs 
	 * 3. Passes the name of the calling station, the status of the CallAttendant button, and the logic object for the calling station
	 */
	public void actionPerformed(ActionEvent e) {		
		
		// Get the CustomerView to which the button belongs, so that the button
		View = logic.getViewController().getCustomerView();
		
		// "Calling" attendant
		if (status == false){
			//Button.setForeground(Color.GREEN);
			status = true;
			View.CallAttendantButton.setBackground(Color.GREEN);
			logic.getViewController().updateOnCustomerCallsAttendant(logic.getName(), status, logic);
		}
		
		// "Uncalling" attendant
		else{
			status = false;
			View.CallAttendantButton.setBackground(Color.BLACK);
			logic.getViewController().updateOnCustomerCallsAttendant(logic.getName(), status, logic);
		}				
	}
	
	/**
	 * Returns the current status of this button
	 * @return
	 */
	public boolean checkStatus() {
		return status;
	}
	
	/**
	 * Set the status of this button (used by the attendant station when the popUp is resolved)
	 * @param bol - the boolean to which the status should be set
	 */
	public void setStatus(boolean bol) {
		status = bol;
	}
}
