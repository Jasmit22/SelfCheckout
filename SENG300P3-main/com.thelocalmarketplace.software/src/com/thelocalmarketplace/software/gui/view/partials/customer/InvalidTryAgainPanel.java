package com.thelocalmarketplace.software.gui.view.partials.customer;

import javax.swing.*;


import java.awt.*;
import java.math.BigDecimal;

/**
 * A JPanel that represents the "Thank You" panel for the SENG 300 Checkout.
 * It displays a change label, a receipt label, and an "Exit" button.
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

public class InvalidTryAgainPanel extends JPanel {

	private static final long serialVersionUID = 7229070181495105031L;
	
	public JButton GoBackButton;
	public JLabel tryAgainLabel;
	public BigDecimal changeAmount = new BigDecimal(0);
	
	
// PANEL FOR CustomerView------------------------------------------------
	/**
     * Constructs a new ThankYouPanel.
     */
    public InvalidTryAgainPanel() {
//    	
//    	this.changeAmount = changeAmount;
//    	
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Change Label
        this.tryAgainLabel = new JLabel("Invalid. Please Try Again");
        tryAgainLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(tryAgainLabel, gbc);

        // Exit Button
        this.GoBackButton = new JButton("Go Back");
        gbc.gridy = 2;
        add(GoBackButton, gbc);
    }
}
