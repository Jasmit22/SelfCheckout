package com.thelocalmarketplace.software.gui.view.partials.attendant;

import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.ResolvePopUpButtonListener;

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

public class AttendantCustomerPopUp {
	
	public JDialog pane;
	public HashMap<String,JDialog> PopUpMap = new HashMap<String,JDialog>();
	private JFrame frame;
	
	
	// Constructor, connects to attendant view frame
	public AttendantCustomerPopUp(JFrame frame){
		this.frame = frame;
	}
	
	/**
	 * creates a PopUp for a specific station upon button press from that station, adds the popUp to the hashmap of popUps to ensure to duplication occurs
	 * @param station - The station name where the call occured
	 * @param logic - The logic of the respective station
	 */
	public void createPopUp(String station, ISelfCheckoutStationSoftware logic) {
        final JFrame parent = this.frame;
        JButton button = new JButton("Resolve");
        
        pane = new JDialog(parent, "Attention!", false);
        
        pane.add(new JLabel("Attendant needed at " + station));
        pane.add(button);
        pane.setSize(250,100);
        pane.setLayout(new FlowLayout());
        pane.setLocationRelativeTo(parent);
        pane.setAlwaysOnTop(true); 
        
        // Esnure that the popUp can only be addressed by pressing the "Resolve" button, not the "close window" / "X" button
        pane.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        PopUpMap.put(station, pane);
        
        // Register listener for the "Resolve" button
        button.addActionListener(new ResolvePopUpButtonListener(logic, pane));
        
        pane.setVisible(true);
}
	
	/**
	 * Deletes the popUp dialog specified by name
	 * DEPRECIATED CLASS
	 * @param name - name of popUp to be deleted
	 */
	public void deletePopUp(String name) {
		PopUpMap.put(name, null);
		
	}
	
	/**
	 * Makes an existing popUp dialog visible or invisible based on current visibility
	 * @param name - name of the popUp to be toggled
	 */
	public void toggle(String name) {
		pane = PopUpMap.get(name);
		
		if (pane.isVisible()) {
			pane.setVisible(false);
		}
		
		else {
			pane.setVisible(true);
		}
	}
}

