package com.thelocalmarketplace.software.gui.view.partials.customer;

import javax.swing.*;
import java.awt.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

public class MainPaymentPanel extends JPanel {
	
	private static final long serialVersionUID = 1949875737521523233L;
	
	public JLabel paymentOwedLabel;
	public JLabel selectOneLabel;
	
	public JButton backToCheckoutButton;
	public JButton debitButton;
	public JButton creditButton;
	public JButton cashButton;
  
//PANEL FOR CustomerView------------------------------------------------
	/**
	 * Constructs a new ThankYouPanel.
	 */
    public MainPaymentPanel() {	
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Change Label
        this.paymentOwedLabel = new JLabel();
        this.setTotal(BigDecimal.ZERO);
        
        paymentOwedLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(paymentOwedLabel, gbc);

        this.selectOneLabel = new JLabel("Please Select Payment Method:");
        selectOneLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(selectOneLabel, gbc);

        this.creditButton = new JButton("Credit");
        this.debitButton = new JButton("Debit");
        this.cashButton = new JButton("Cash");
        
        gbc.gridx = 0; gbc.gridy = 2;
        this.creditButton.setPreferredSize(new Dimension(150,150));
        //Uncomment this if you want to include the Penny
        add(creditButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        this.debitButton.setPreferredSize(new Dimension(150,150));
        //Uncomment this if you want to include the Penny
        add(debitButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        this.cashButton.setPreferredSize(new Dimension(150,150));
        //Uncomment this if you want to include the Penny
        add(cashButton, gbc);
        
        this.backToCheckoutButton = new JButton("Return to Checkout");
        gbc.gridx = 0; gbc.gridy = 5;
        backToCheckoutButton.setPreferredSize(new Dimension(150,50));
        add(backToCheckoutButton, gbc);    
    }
    
    public void setTotal(BigDecimal total) {
		this.paymentOwedLabel.setText("Total Owed: $" + total.setScale(2, RoundingMode.HALF_DOWN).toString());
	}
}
