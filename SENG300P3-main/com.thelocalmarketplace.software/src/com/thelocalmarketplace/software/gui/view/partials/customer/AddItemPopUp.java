package com.thelocalmarketplace.software.gui.view.partials.customer;
import javax.swing.*;

import com.thelocalmarketplace.software.gui.view.CustomerView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class AddItemPopUp extends JPanel {

	private static final long serialVersionUID = 6391969604453373426L;

	private CustomerView customerView;

    
    public AddItemPopUp(CustomerView customerView, JFrame parentFrame) {
    	this.customerView = customerView;
    	addItemPopUp(parentFrame);
    }
   

// LOGIC -------------------------------------------------------------
       /**
        * Handles the action when the Start Session button is clicked.
        */

    private void CustomerAddsItemToBagging() {
    	customerView.switchCardLayoutView("CheckoutPanel");
    }
    
    private void CustomerNOTAddsItemToBagging() {
    	customerView.switchCardLayoutView("CheckoutPanel");
    }
    
    // GUI -------------------------------------------------------------
    /**
     * Initializes the components/GUI of the NumberPad.
     */

    public void addItemPopUp(JFrame parentFrame) {
        setOpaque(true); // Make the panel transparent
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 400)); // Set the preferred size to 400x400

        GridBagConstraints gbc = new GridBagConstraints();
 
        //Label 
        JLabel productLabel = new JLabel("PRODUCT HERE");
        productLabel.setFont(new Font("Arial", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(productLabel, gbc);
        
        //Spacer 
        JLabel spacer = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(spacer, gbc);
        
        //Label 
        JLabel selectLabel = new JLabel("Please select an Action!");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(selectLabel, gbc);
       

        JButton button1 = new JButton("Add Item to Bagging Area");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button1_CustomersAddsToBaggingArea();
            	CustomerAddsItemToBagging();
            }
        });
        JButton button2 = new JButton("Do Not Place Item in Bagging Area");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
            	CustomerNOTAddsItemToBagging();
            }
        });

        gbc.gridy = 3;
        add(button1, gbc);        
        gbc.gridy = 4;
        add(button2, gbc);        
        gbc.gridy = 5;
//        add(button3, gbc);  Uncomment if you want this extra button

    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(128, 128, 128, 128)); // 128, 128, 128 is grey, 128 is the alpha value
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

 
}
