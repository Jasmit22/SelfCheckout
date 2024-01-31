package com.thelocalmarketplace.software.gui.view.listeners.attendant;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.listeners.customer.CallAttendantButtonListener;

/**
 * Listens for presses of the "Resolve" button in attendant PopUps and performs appropriate actions
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

public class ResolvePopUpButtonListener extends AbstractLogicActionListener{
	
	private CustomerView View;
	private JDialog pane;
	private ActionListener[] array;
	private CallAttendantButtonListener caller;
	
	/**
	 * Constructor for the listener, tracks the dialog to which it belongs and the customer station logic that triggered the dialog
	 * @param logic - the calling logic (customer station)
	 * @param pane - the Dialog to which the button this class listens to belongs
	 */
	public ResolvePopUpButtonListener(ISelfCheckoutStationSoftware logic, JDialog pane) {
		super(logic);
		
		this.pane = pane;
	}

	/**
	 * Actions:
	 * 1. Retrieves the CustomerView from which the attendant was called
	 * 2. Toggles off the Call Attendant button at the calling customer station
	 * 3. Deletes the dialog window the button belongs to
	 * 4. Sets the status to false in the CallAttendantButtonListener 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// 1.
		View = logic.getViewController().getCustomerView();
		
		// 2.
		View.CallAttendantButton.setBackground(Color.BLACK);
		
		// 3.
		pane.dispose();
		
		// 4. Here there should only be one listener attached to the CallAttendant button 
		array = View.CallAttendantButton.getActionListeners();
		caller = (CallAttendantButtonListener) array[0];		
		caller.setStatus(false);
	}
}
