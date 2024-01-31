package com.thelocalmarketplace.software.gui.view.partials.customer;

import javax.swing.*;

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


import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.listeners.INumpadCallback;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumpadPanel extends JPanel {
	
	private static final long serialVersionUID = 1336190884741302465L;
	
	private INumpadCallback listener;
	
	private JTextField displayLabel;
    private StringBuilder enteredNumber; // Variable to store the entered number

	public JButton buttonENTER;
	public JButton buttonCLEAR;
	public JButton returnCheckout;

    
    public NumpadPanel(CustomerView customerView, INumpadCallback listener) {
    	this.listener = listener;

        setOpaque(true); // Make the panel transparent
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 400)); // Set the preferred size to 400x400

        // Initialize the displayLabel
        displayLabel = new JTextField();
        enteredNumber = new StringBuilder();
        displayLabel.setFont(new Font("Arial", Font.BOLD, 24));
        displayLabel.setEditable(false); // Make the text field read-only
        displayLabel.setHorizontalAlignment(JTextField.RIGHT); // Right-align text
        displayLabel.setPreferredSize(new Dimension(150, 50));

        GridBagConstraints gbcDisplay = new GridBagConstraints();
        gbcDisplay.gridx = 0;
        gbcDisplay.gridy = 0;
        gbcDisplay.gridwidth = GridBagConstraints.REMAINDER; // Span across three columns
        add(displayLabel, gbcDisplay);

        // Create buttons and add them to the center of the panel
        JButton button1 = new JButton("  1  ");
        JButton button2 = new JButton("  2  ");
        JButton button3 = new JButton("  3  ");
        JButton button4 = new JButton("  4  ");
        JButton button5 = new JButton("  5  ");
        JButton button6 = new JButton("  6  ");
        JButton button7 = new JButton("  7  ");
        JButton button8 = new JButton("  8  ");
        JButton button9 = new JButton("  9  ");
        JButton button0 = new JButton("  0  ");
        
        buttonCLEAR = new JButton("CLEAR");
        buttonENTER = new JButton("ENTER");
        returnCheckout = new JButton("Return");
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        //Placements of Button (indentation for readability)
        gbc.gridy = 1;
            gbc.gridx = 0;
            add(button1, gbc);
            gbc.gridx = 1;
            add(button2, gbc);
            gbc.gridx = 2;
            add(button3, gbc);
        gbc.gridy = 2;    
            gbc.gridx = 0;
            add(button4, gbc);
            gbc.gridx = 1;
            add(button5, gbc);
            gbc.gridx = 2;
            add(button6, gbc);
        gbc.gridy = 3;    
            gbc.gridx = 0;
            add(button7, gbc);
            gbc.gridx = 1;
            add(button8, gbc);
            gbc.gridx = 2;
            add(button9, gbc);
        gbc.gridy = 4;    
            gbc.gridx = 0;
            add(buttonCLEAR, gbc);
            gbc.gridx = 1;
            add(button0, gbc);
            gbc.gridx = 2;
            add(buttonENTER, gbc);
        gbc.gridy = 5;    
        	gbc.gridx = 0;
        	gbc.gridwidth = 3;
        	add(returnCheckout, gbc);

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button1_CustomersAddsToBaggingArea();
                	handleNumberButtonPress("1");
                }
            });
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                	handleNumberButtonPress("2");
                }
            });
            button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleNumberButtonPress("3");
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                }
            });
            button4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button1_CustomersAddsToBaggingArea();
                	handleNumberButtonPress("4");
                }
            });
            button5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                	handleNumberButtonPress("5");
                }
            });
            button6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleNumberButtonPress("6");
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                }
            });
            button7.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button1_CustomersAddsToBaggingArea();
                	handleNumberButtonPress("7");
                }
            });
            button8.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                	handleNumberButtonPress("8");
                }
            });
            button9.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleNumberButtonPress("9");
//                	guiLogicInstance.addItemPopUp_button2_CustomersDOESNOTAddsToBaggingArea();
                }
            });
            buttonCLEAR.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleClearButtonPress();
                }
            });
            button0.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleNumberButtonPress("0");
                }
            });
            buttonENTER.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	handleEnterButtonPress();                   
                }
            });
            
            returnCheckout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	returnToCheckoutPress();                   
                }
            });
    
    }

// LOGIC -------------------------------------------------------------
       /**
        * Handles the action when the Start Session button is clicked.
        */

    

    private void handleEnterButtonPress() {
    	try {
            this.listener.onEnterPressed(enteredNumber.toString());
            enteredNumber.setLength(0);
            displayLabel.setText("");
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer entered");
            // Handle the case where the entered value is not a valid integer
        }
    }
    
    private void handleNumberButtonPress(String number) {
        // Append the pressed number to the displayLabel text
        String currentText = displayLabel.getText();
        enteredNumber.append(number);
        displayLabel.setText(currentText + number);
    }

    private void handleClearButtonPress() {
        // Clear the displayLabel text
        displayLabel.setText("");
        enteredNumber.setLength(0);
    }
    
    private void returnToCheckoutPress() {
        // Clear the displayLabel text
    	enteredNumber.setLength(0);
        displayLabel.setText("");
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