package com.thelocalmarketplace.software.gui.view.partials;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A title panel that is used as a decoration
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

public class TopTitlePanel extends JPanel {

	private static final long serialVersionUID = 4131119489309940616L;

	
	/**
	 * Base constructor
	 * @param title Is the title of the top panel to be displayed
	 */
	public TopTitlePanel(String title) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    	
    	JLabel t = new JLabel(title);
    	t.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding
    	
    	this.add(t);
	}
}
