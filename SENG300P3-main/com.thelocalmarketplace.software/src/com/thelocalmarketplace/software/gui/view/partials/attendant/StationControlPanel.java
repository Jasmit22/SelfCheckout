package com.thelocalmarketplace.software.gui.view.partials.attendant;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

/**
 * Panel that represents what the attendant can see when managing an individual station
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

public class StationControlPanel extends JPanel {

	private static final long serialVersionUID = -3141514302051980391L;
	
	private SelfCheckoutStationSoftware logic;
	
	private CardLayout cardLayout;
	private JPanel cardPanel;

	public AttendantStartPanel startPanel;
	public AttendantMaintainPanel maintainPanel;
	public AttendantAddItemViaSearchPanel addItemPanel;
	
	public StationControlPanel(SelfCheckoutStationSoftware logic) {
		this.logic = logic;
		
		this.cardLayout = new CardLayout();
		this.cardPanel = new JPanel(this.cardLayout);
		
		this.startPanel = new AttendantStartPanel(this.logic);
		this.maintainPanel = new AttendantMaintainPanel(this.logic);
		this.addItemPanel = new AttendantAddItemViaSearchPanel(this.logic);
		
		this.startPanel.selectedStationLabel.updateStatus(logic.getName(), "#45bbd9");
		
		// Setup button listeners
		this.maintainPanel.switchToStartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { switchCardLayoutView("start"); }
		});
		
		this.addItemPanel.switchToStartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) { switchCardLayoutView("start"); }
		});
		
		this.startPanel.switchToMaintainButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { switchCardLayoutView("maintain"); }
		});
		
		this.startPanel.addItemByTextSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { switchCardLayoutView("additem"); }
		});
		
		this.cardPanel.add(this.startPanel, "start");
    	this.cardPanel.add(this.maintainPanel, "maintain");
    	this.cardPanel.add(this.addItemPanel, "additem");
    	
    	this.add(this.cardPanel);
    	
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	this.switchCardLayoutView("start");
	}
	
	/**
	 * Facade for switching the attendant view between various states
	 * @param panel Is the name of the panel to switch view to
	 */
	private void switchCardLayoutView(String panel) {
		this.cardLayout.show(cardPanel, panel);
	}
	
	
}
