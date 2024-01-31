package com.thelocalmarketplace.software.gui.view.partials.simulation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.gui.view.listeners.ITypeSelectCallback;

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

public class SimulationSelectPanel<T> extends JPanel {

	private static final long serialVersionUID = -6871419448485534627L;
	
	public JButton switchBackToMainButton;
	private JPanel optionsPanel;
	
	private Map<T, JButton> optionButtons;
	
	
	public SimulationSelectPanel(Map<T, String> allOptions, Map<T, String> initialOptions, ITypeSelectCallback<T> callback) {
		this.optionButtons = new HashMap<>();
		
		this.optionsPanel = new JPanel();
		this.switchBackToMainButton = new JButton("Back");
		
		for (Entry<T, String> o : allOptions.entrySet()) {
			JButton b = new JButton(o.getValue());
			b.setEnabled(false);
			
			b.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					callback.onSelection(o.getKey());
				}
			});
			
			this.optionButtons.put(o.getKey(), b);
			this.optionsPanel.add(b);
		}
		
		this.configureAllowableOptions(initialOptions);
		
		JPanel body = new JPanel();
		
		body.setLayout(new BoxLayout(body, BoxLayout.X_AXIS));
		body.setBorder(new EmptyBorder(15, 0, 0, 0));
		
		this.optionsPanel.setLayout(new GridLayout(5, 5, 5, 5));
		this.optionsPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

		body.add(this.optionsPanel);
		body.add(switchBackToMainButton);
		
		this.add(body);
		
		Utilities.setComponentColour(this);
	}

	public void configureAllowableOptions(Map<T, String> allowableOptions) {
		for (Entry<T, JButton> b : this.optionButtons.entrySet()) {
			if (allowableOptions.containsKey(b.getKey())) {
				b.getValue().setEnabled(true);
			}
			else {
				b.getValue().setEnabled(false);
			}
		}
	}
}
