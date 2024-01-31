package com.thelocalmarketplace.software.gui.view;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.thelocalmarketplace.software.AttendantStationSoftware;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.IViewable;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;
import com.thelocalmarketplace.software.gui.view.partials.attendant.StationControlPanel;


/**
 * Represents what the attendant can see on their screen
 * 
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

public class AttendantView implements IViewable {
	
	private JFrame frame;
	
	private CardLayout cardLayout;
	private JPanel cardPanel;
	
	private AttendantStationSoftware controller;
	
	/**
	 * Control panels for the attendant to manage stations. Each control panel is indexed by the station it controls 
	 */
	private Map<SelfCheckoutStationSoftware, StationControlPanel> controlPanels;
	
	private boolean started;
	
	
	/**
	 * Base constructor
	 */
	public AttendantView(AttendantStationSoftware controller) throws NullPointerException {
		this.controller = controller;
		
		this.controlPanels = new HashMap<>();
		
		this.cardLayout = new CardLayout();
		this.cardPanel = new JPanel(this.cardLayout);
		
		this.frame = new JFrame("Attendant Screen");
		this.show();
	}
	
    @Override
	public void start() {
    	this.started = true;
    	
    	// Create window
    	Container pane = this.frame.getContentPane();
    	
    	this.cardPanel.add(this.createStationOptionsPanel(), "select");
    
    	pane.add(this.cardPanel);
    	
    	this.showStationOptionsPanel();
    	
        // Add the components to the main frame
    	this.frame.setSize(480, 600);
    	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.frame.setLocation(1100, 0); // Adjust the coordinates as needed
	}
    
    @Override
    public void show() {
    	this.frame.setVisible(true);
    }
    
    @Override
    public void hide() {
    	this.frame.setVisible(false);
    }
    
    public boolean isStarted() {
    	return this.started;
    }
    
    /**
     * Gets the station control panel corresponding to an instance of station software
     * @param station The station to get the control panel for
     * @return The corresponding control panel
     */
    public StationControlPanel getCorrespondingControlPanel(SelfCheckoutStationSoftware station) {
    	return this.controlPanels.get(station);
    }
    
    /**
     * Adds an instance of station software as a manageable station for the attendant
     * @param station The station to add
     */
    public void addStationOption(SelfCheckoutStationSoftware station) {
    	StationControlPanel panel = new StationControlPanel(station);
    	
    	panel.startPanel.switchToStationSelectButton.addActionListener(new ActionListener() {
    		
			@Override
			public void actionPerformed(ActionEvent e) { showStationOptionsPanel(); }
		});
    	
    	this.controlPanels.put(station, panel);
    	this.cardPanel.add(panel, station.getID().toString());
    	
    	station.getViewController().connectToAttendantView(panel, this.frame);
    }
    
    private JPanel createStationOptionsPanel() {
    	JPanel p = new JPanel();
    	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    	
    	JPanel body = new JPanel();
    	Utilities.setComponentColour(body);
    	
    	for (Entry<SelfCheckoutStationSoftware, StationControlPanel> en : this.controlPanels.entrySet()) {
    		final String name = en.getKey().getName();
    		final UUID id = en.getKey().getID();
    		
    		JButton b = new JButton(name);
    		
    		b.addActionListener(new ActionListener() {
			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				
    				// Select station in the controller
    				controller.setSupervised(id);
    				
    				// Update attendant view to show correct station
    				showPanel(id.toString());
    			}
    		});
    		
    		Utilities.setComponentColour(b);
    		body.add(b);
    	}
    	
    	p.add(new TopTitlePanel("Select Station"));
    	p.add(body);
    	
    	return p;
    }
    
    private void showPanel(String panel) {
    	this.cardLayout.show(cardPanel, panel);
    }
    
    private void showStationOptionsPanel() {
    	this.showPanel("select");
    }
    
    /**
     * @author Daniel Yakimenka (10185055)
     * Get the correct frame to which call attendant popUps can be attached 
     * @return
     */
    public JFrame getFrame() {
    	return frame;
    }
}