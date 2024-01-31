package com.thelocalmarketplace.software.gui.view.partials.customer;

import javax.swing.*;
import java.awt.*;

/**
 * When a payment is unsuccessful, the UnsuccessfulPaymentPanel will show, giving the customer the option to
 * try again using another payment method.
 * It displays a message "Payment Has Been Unsuccessful" and has a button with the message
 * "Try Again With Another Payment Method".
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

public class UnsuccessfulPaymentPanel extends JPanel{

	private static final long serialVersionUID = 5425129819137338992L;
	
	private JButton tryAgainButton;
	
    /**
     * Constructs a new UnsuccessfulPaymentPanel
     */
    public UnsuccessfulPaymentPanel(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label
        JLabel unsuccessfulLabel = new JLabel("Payment Unsuccessful");
        unsuccessfulLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(unsuccessfulLabel, gbc);

        // Button
        this.tryAgainButton = new JButton("Try Again With Another Payment Method");

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(tryAgainButton,gbc);
    }
}

