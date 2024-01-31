package com.thelocalmarketplace.software.gui.view.partials.attendant;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.MaintainBanknotesButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.MaintainCoinsButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.RefillInkButtonListener;
import com.thelocalmarketplace.software.gui.view.listeners.attendant.RefillPaperButtonListener;
import com.thelocalmarketplace.software.gui.view.partials.TopTitlePanel;

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

public class AttendantMaintainPanel extends JPanel {

	private static final long serialVersionUID = -3351284492475906037L;
	
	private JButton refillInkButton;
	private JButton refillPaperButton;
	
	private JTextField maintainAmountField;

	private Map<BigDecimal, JButton> maintainCoinsButtons;
	private Map<BigDecimal, JButton> maintainBanknotesButtons;
	
	public JButton switchToStartButton;
	
	
	public AttendantMaintainPanel(SelfCheckoutStationSoftware logic) {
		this.refillInkButton = new JButton("Refill Ink to Full");
		this.refillPaperButton = new JButton("Refill Paper to Full");
		
		this.maintainAmountField = new JTextField();
		this.maintainAmountField.setText("0");
		this.maintainAmountField.setColumns(15);
		this.maintainAmountField.setHorizontalAlignment(JTextField.CENTER);
		
		this.switchToStartButton = new JButton("Back");
		
		this.maintainCoinsButtons = new HashMap<>();
		this.maintainBanknotesButtons = new HashMap<>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		for (BigDecimal cd : logic.getCoinPaymentLogic().getDenominationsAsList()) {
			final JButton b = new JButton("<html><font color='green'>Load</font>/<font color='red'>Unload</font> $" + cd.setScale(2, RoundingMode.HALF_DOWN).toString() + " Coins</html>");
			
			b.addActionListener(new MaintainCoinsButtonListener(logic, maintainAmountField, cd));
			
			this.maintainCoinsButtons.put(cd, b);
		}
		
		for (BigDecimal bd : logic.getCashPaymentLogic().getDenominationsAsList()) {
			final JButton b = new JButton("<html><font color='green'>Load</font>/<font color='red'>Unload</font> $" + bd.setScale(2, RoundingMode.HALF_DOWN).toString() + " Banknotes</html>");
			
			b.addActionListener(new MaintainBanknotesButtonListener(logic, this.maintainAmountField, bd));
			
			this.maintainBanknotesButtons.put(bd, b);
		}
		
		this.refillInkButton.addActionListener(new RefillInkButtonListener(logic));
		this.refillPaperButton.addActionListener(new RefillPaperButtonListener(logic));
		
		JPanel body = new JPanel();

		body.add(this.createTopPanel());	
		body.add(this.createMaintainOptionsPanel());
		body.add(this.createRefillOptionsPanel());
		
		Utilities.setComponentColour(body);
		
		this.add(new TopTitlePanel("Attendant Maintain Panel"));
		this.add(body);
	}
	
	private JPanel createTopPanel() {
		JPanel p = new JPanel();
		p.setBorder(new EmptyBorder(0, 100, 0, 100)); // Padding
		
		p.add(this.maintainAmountField);
		p.add(this.switchToStartButton);
		
		return p;
	}
	
	private JPanel createRefillOptionsPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(new EmptyBorder(20, 20, 0, 20)); // Padding
		
		p.add(this.refillInkButton);
		p.add(Box.createRigidArea(new Dimension(0, 5))); // Padding
		p.add(this.refillPaperButton);
		p.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
		
		return p;
	}
	
	private JPanel createMaintainOptionsPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(new EmptyBorder(20, 0, 0, 0)); // Padding
		
		for (Entry<BigDecimal, JButton> e : this.maintainCoinsButtons.entrySet()) {
			p.add(Box.createRigidArea(new Dimension(0, 5))); // Padding
			p.add(e.getValue());
		}
		
		p.add(Box.createRigidArea(new Dimension(0, 10))); // Padding
		
		for (Entry<BigDecimal, JButton> e : this.maintainBanknotesButtons.entrySet()) {
			p.add(Box.createRigidArea(new Dimension(0, 5))); // Padding
			p.add(e.getValue());
		}
		
		return p;
	}
}
