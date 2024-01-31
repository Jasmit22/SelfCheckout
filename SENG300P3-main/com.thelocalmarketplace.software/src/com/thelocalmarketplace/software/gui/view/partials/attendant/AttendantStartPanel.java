package com.thelocalmarketplace.software.gui.view.partials.attendant;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.SetStationEnabledButtonListener;
import com.thelocalmarketplace.software.gui.view.partials.MultilineButton;
import com.thelocalmarketplace.software.gui.view.partials.StatusLabel;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;

/**
 * Default visible panel for the attendant
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

public class AttendantStartPanel extends JPanel {
	
	private static final long serialVersionUID = 5258597041505147210L;
	
	public JButton enableStationButton;
	public JButton disableStationButton;
	
	public MultilineButton switchToMaintainButton;
	public JButton overrideDiscrepancyButton;
	public JButton addItemByTextSearchButton;
	public JButton printReceiptButton;
	public JButton switchToStationSelectButton;
	
	public StatusLabel selectedStationLabel;
	
	public StatusLabel stationEnabledLabel;
	public StatusLabel stationStateLabel;
	public StatusLabel sessionStartedLabel;
	
	public StatusLabel inkLabel;
	public StatusLabel paperLabel;
	
	public Map<BigDecimal, StatusLabel> coinLabels;
	public Map<BigDecimal, StatusLabel> banknoteLabels;
	
	
	/**
	 * Base constructor
	 */
	public AttendantStartPanel(SelfCheckoutStationSoftware logic) {
		this.coinLabels = new TreeMap<>();
		this.banknoteLabels = new TreeMap<>();
		
		this.enableStationButton = new JButton("Enable Station");
		this.disableStationButton = new JButton("Disable Station");
		this.switchToMaintainButton = new MultilineButton(new String[] {"Maintain", "(Simulated)"});
		this.overrideDiscrepancyButton = new JButton("Override Discrepancy");
		this.addItemByTextSearchButton = new JButton("Add Item By Text Search");
		this.printReceiptButton = new JButton("Print Duplicate Reciept");
		this.switchToStationSelectButton = new JButton("Select Station");
		
		this.selectedStationLabel = new StatusLabel("Selected Station", "", "");
		
		this.stationEnabledLabel = new StatusLabel("Enabled", "", "");
		this.stationStateLabel = new StatusLabel("State", "", "");
		this.sessionStartedLabel = new StatusLabel("Session Active", "", "");
		
		this.enableStationButton.addActionListener(new SetStationEnabledButtonListener(logic, true));
		this.disableStationButton.addActionListener(new SetStationEnabledButtonListener(logic, false));
		
		this.overrideDiscrepancyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (logic.getStateLogic().inState(States.ADDBAGS)) {
					logic.getMassLogic().approveBaggingArea();
				} else if(logic.getViewController().isAddingItem){
					logic.getProductLogic().grantApprovalSkipBaggingOfLastProductAdded();
				} else {
					logic.getMassLogic().overrideDiscrepancy();
				}
			}
		});
		
		this.printReceiptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logic.getReceiptPrintingController().printDuplicateReceipt();
			}
		});
		
		this.inkLabel = new StatusLabel("Ink Status", "?", "purple");
		this.paperLabel = new StatusLabel("Paper Status", "?", "purple");

		for (BigDecimal cd : logic.getCoinPaymentLogic().getDenominationsAsList()) {
			coinLabels.put(cd, new StatusLabel("$" + cd.setScale(2, RoundingMode.HALF_DOWN).toString() + " Coin Status", "?", "purple"));
		}
		
		for (BigDecimal bd : logic.getCashPaymentLogic().getDenominationsAsList()) {
			banknoteLabels.put(bd, new StatusLabel("$" + bd.setScale(2, RoundingMode.HALF_DOWN).toString() + " Banknote Status", "?", "purple"));
		}
		
		// Setup
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel body = new JPanel();
		Utilities.setComponentColour(body);

		body.add(this.createStatusPanel());
		body.add(this.createPredictPanel());
		body.add(this.createControlOptionsPanel());
		
		this.add(new TopTitlePanel("Attendant Control Panel"));
		this.add(body);
	}
    
    private JPanel createStatusPanel() {
    	JPanel statusPanel = new JPanel();
    	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
    	statusPanel.setBorder(new EmptyBorder(0, 0, 0, 20)); // Padding
    	
    	statusPanel.add(this.selectedStationLabel);
    	statusPanel.add(this.stationEnabledLabel);
    	statusPanel.add(this.stationStateLabel);
    	statusPanel.add(this.sessionStartedLabel);
    	
    	Utilities.setComponentColour(statusPanel);
    	
    	return statusPanel;
    }
    
    private JPanel createPredictPanel() {
    	JPanel predictStatusPanel = new JPanel();
    	predictStatusPanel.setLayout(new BoxLayout(predictStatusPanel, BoxLayout.Y_AXIS));
    	predictStatusPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Padding
    	
    	predictStatusPanel.add(this.inkLabel);
    	predictStatusPanel.add(this.paperLabel);
    	predictStatusPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Padding

    	for (Entry<BigDecimal, StatusLabel> e : this.coinLabels.entrySet()) {
    		predictStatusPanel.add(e.getValue());
    	}
    	
    	predictStatusPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	
    	for (Entry<BigDecimal, StatusLabel> e : this.banknoteLabels.entrySet()) {
    		predictStatusPanel.add(e.getValue());
    	}
    	
    	Utilities.setComponentColour(predictStatusPanel);
    	
    	return predictStatusPanel;
    }
    
    private JPanel createControlOptionsPanel() {
    	JPanel optionsPanel = new JPanel();
    	optionsPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Padding
    	
    	JPanel inner1 = new JPanel();
    	JPanel inner2 = new JPanel();
    	
    	inner1.setLayout(new BoxLayout(inner1, BoxLayout.Y_AXIS));
    	inner1.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding
    	
    	inner2.setLayout(new BoxLayout(inner2, BoxLayout.Y_AXIS));
    	inner2.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding
    	
    	inner1.add(this.enableStationButton);
    	inner1.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	inner1.add(this.disableStationButton);
    	inner1.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	
    	inner2.add(this.switchToMaintainButton);
    	inner2.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	inner2.add(this.overrideDiscrepancyButton);
    	inner2.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	inner2.add(this.addItemByTextSearchButton);
    	inner2.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	inner2.add(this.printReceiptButton);
    	inner2.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
    	inner2.add(this.switchToStationSelectButton);
    	
    	optionsPanel.add(inner1);
    	optionsPanel.add(inner2);
    	
    	Utilities.setComponentColour(optionsPanel);
    	
    	return optionsPanel;
    }
}
