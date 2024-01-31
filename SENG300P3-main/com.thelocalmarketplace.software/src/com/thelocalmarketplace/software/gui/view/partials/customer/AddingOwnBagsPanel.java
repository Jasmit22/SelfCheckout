package com.thelocalmarketplace.software.gui.view.partials.customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.thelocalmarketplace.software.gui.view.CustomerView;

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

public class AddingOwnBagsPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel textLabel1;
	JButton finishedAddingButton;
	CustomerView view;
	String originalText;
	
public AddingOwnBagsPanel(String lable, CustomerView cust) {
		view = cust;
		originalText = lable;
        //GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //
		this.setPreferredSize(new Dimension(getWidth(), getHeight()));
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0, 200));
        //Label 
        this.textLabel1 = new JLabel();
        //lable = String.format("<html><div WIDTH=%d>%s</div></html>", getWidth()-5, lable);
        this.textLabel1.setText(lable);
        this.textLabel1.setFont(new Font("Arial", Font.BOLD, 26));
        this.textLabel1.setHorizontalAlignment(JLabel.CENTER);
        this.textLabel1.setBackground(Color.BLACK);
        this.textLabel1.setForeground(Color.WHITE);
        this.textLabel1.setOpaque(true);
        this.textLabel1.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 0;
        add( this.textLabel1, gbc);
        
        this.finishedAddingButton = new JButton("Done");
        this.finishedAddingButton.setForeground(Color.WHITE);
        this.finishedAddingButton.setBackground(Color.BLACK);
        Border line = new LineBorder(Color.WHITE);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        this.finishedAddingButton.setBorder(compound);
        
        this.finishedAddingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finishedAddingBags();
				
			}
        });
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(this.finishedAddingButton, gbc);
        
        this.addMouseListener( (MouseListener) new MouseAdapter() {} );// stops buttons under this layer from being pressable
	}
	
	@Override
	protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
	
	private void finishedAddingBags() {
		view.doneAddingOwnBags();
	}
	
	public void unsuccessfullAdd() {
		this.textLabel1.setText("The bags youve placed are too heavy an attendant has been notified");
		this.finishedAddingButton.setVisible(false);
	}
	
	public void resetPanel() {
		this.textLabel1.setText(originalText);
		this.finishedAddingButton.setVisible(true);
	}


}
