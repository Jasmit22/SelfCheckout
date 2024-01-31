package com.thelocalmarketplace.software.gui.view.listeners.attendant;

import java.awt.event.ActionEvent;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;

/**
 * Contains methods for simulating an attempt at servicing paper level by the attendant,
 * triggered by an ActionEvent
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
 * @author Royce Knoeplfi		(30172598)
 */
public class RefillPaperButtonListener extends AbstractLogicActionListener {
	
	private final int MAX_PRINTER_PAPER = 1 << 10;
	
	
	/**
	 * Constructor for an InkRefillLogic object.
	 * @param logic
	 * The CentralStationLogic constructing this logic object
	 * @throws NullPointerException
	 */
	public RefillPaperButtonListener(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
	}
	
	/**
	 * -Simulates an attendant replacing the paper until it's full (as the attendant can't tell how much paper is in the printer, 
	 * they put paper in until it seems like it will jam
	 * -Should only result in one call of "notifyPaperAdded()" within the printer
	 * -Should handle and never re-throw overload exception (the attendant is used to the hardware doing this and 
	 * has pliers ready to pull out excess paper)
	 * -Code was not placed in printer controller because this listener implements simulation methods rather than hardware-software interface methods.
	 * 
	 * @param printer
	 */
	public void refillPaper(IReceiptPrinter printer) {
		
		for (int fillAmount = MAX_PRINTER_PAPER ; fillAmount > 0 ; fillAmount--) {
			try {
				printer.addPaper(fillAmount);
				return;
				
			} catch (OverloadedDevice e) {}				
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refillPaper(logic.getHardware().getPrinter());		
	}
}