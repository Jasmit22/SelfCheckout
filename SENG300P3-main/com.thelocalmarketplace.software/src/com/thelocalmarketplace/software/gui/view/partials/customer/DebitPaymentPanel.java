package com.thelocalmarketplace.software.gui.view.partials.customer;

import javax.swing.*;
import java.awt.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A JPanel that represents the start session panel for the SENG 300 Checkout.
 * It displays a welcome message and a "Start Session" button.
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

public class DebitPaymentPanel extends JPanel {
	
	private static final long serialVersionUID = 1949875737521523233L;
	
	public JLabel paymentOwedLabel;
	public JLabel selectOneLabel;
	public JButton backToPaymentButton;
	public BigDecimal total;
	public JButton TapButton;
	public JButton InsertButton;
	public JButton SwipeButton;
	public JButton PINButton;
	
	public void setTotal(BigDecimal total) {
		this.paymentOwedLabel.setText("Total Owed: $" + total.setScale(2, RoundingMode.HALF_DOWN).toString());
	}
	
	    
//PANEL FOR CustomerView------------------------------------------------
	/**
	 * Constructs a new ThankYouPanel.
	 */
    public DebitPaymentPanel() {
    	
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        this.selectOneLabel = new JLabel("Please Tap, Swipe or Insert Debit Card");
        this.selectOneLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(selectOneLabel, gbc);
           
        this.paymentOwedLabel = new JLabel();
        this.setTotal(BigDecimal.ZERO);
        
        paymentOwedLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(paymentOwedLabel, gbc);
        
        this.PINButton = new JButton("Enter PIN");
        gbc.gridx = 0; gbc.gridy = 4;
        PINButton.setPreferredSize(new Dimension(150,50));
        add(PINButton, gbc);  
        
        this.backToPaymentButton = new JButton("Back");
        gbc.gridx = 0; gbc.gridy = 5;
        backToPaymentButton.setPreferredSize(new Dimension(150,50));
        add(backToPaymentButton, gbc);  
    }
}
    

