package com.thelocalmarketplace.software.gui.view.partials;

import javax.swing.JLabel;

/**
 * Represents a visible status label
 */

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

public class StatusLabel extends JLabel {
	
	private static final long serialVersionUID = -5714452158302180225L;
	
	private String text;
	private String status;
	private String statusColour;
	
	
	/**
	 * Base constructor
	 * A status label is of the format --> [text]: <font color="[statusColour]">[status]</font>
	 * @param text Corresponds to [text]
	 * @param defaultStatus Corresponds to [status]
	 * @param statusColour Corresponds to [statusColour]
	 */
	public StatusLabel(String text, String defaultStatus, String statusColour) {
		this.text = text;
		this.status = defaultStatus;
		this.statusColour = statusColour;
		
		this.update();
	}
	
	public void updateStatus(String newStatus, String newColour) {
		this.status = newStatus;
		this.statusColour = newColour;
		
		this.update();
	}
	
	private void update() {
		this.setText("<html>" + this.text + ": " + "<font color='" + this.statusColour + "'>" + this.status + "</font></html>");
	}
}
