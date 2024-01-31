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

public class DiscrepencyOverlayPanel extends JPanel {
	
	private static final long serialVersionUID = -5943542460346146313L;
	
	JLabel textLabel;
	JButton dontBag;
	CustomerView view;
	
	public DiscrepencyOverlayPanel(String lable, CustomerView cust) {
		
		view = cust;
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        
		this.setPreferredSize(new Dimension(getWidth(), getHeight()));
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0, 200));
        //Label 
        this.textLabel = new JLabel();
        //lable = String.format("<html><div WIDTH=%d>%s</div></html>", getWidth()-5, lable);
        this.textLabel.setText(lable);
        this.textLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.textLabel.setHorizontalAlignment(JLabel.CENTER);
        this.textLabel.setBackground(Color.BLACK);
        this.textLabel.setForeground(Color.WHITE);
        this.textLabel.setOpaque(true);
        this.textLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridy = 0;
        add( this.textLabel, gbc);
        
        
        this.dontBag = new JButton("Don't Bag Item");
        this.dontBag.setForeground(Color.WHITE);
        this.dontBag.setBackground(Color.BLACK);
        Border line = new LineBorder(Color.WHITE);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        this.dontBag.setBorder(compound);
        
        this.dontBag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dontBagItemHandler();
				
			}
        });
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 2;
        add(this.dontBag, gbc);
        this.dontBag.setVisible(false);
        
        this.addMouseListener( (MouseListener) new MouseAdapter() {} );// stops buttons under this layer from being pressable
	}
	public void setLabel(String newLabel) {
		this.textLabel.setText(newLabel);
	}
	
	public void resetView() {
		this.dontBag.setVisible(false);
	}
	
	public void allowDontBag() {
		this.dontBag.setVisible(true);
	}
	
	public void dontBagItemHandler() {
		this.view.dontBagItemPushed();
	}
	
	@Override
	protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
