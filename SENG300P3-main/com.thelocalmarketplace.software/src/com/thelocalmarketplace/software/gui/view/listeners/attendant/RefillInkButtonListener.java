package com.thelocalmarketplace.software.gui.view.listeners.attendant;

import java.awt.event.ActionEvent;

import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.IReceiptPrinter;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;

/**
 * Contains methods for simulating an attempt at servicing ink level by the attendant,
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
public class RefillInkButtonListener extends AbstractLogicActionListener {
	
	private final int MAX_PRINTER_INK = 1 << 20;
	
	
	/**
	 * Constructor for an InkRefillLogic object.
	 * @param logic
	 * The CentralStationLogic constructing this logic object
	 * @throws NullPointerException
	 */
	public RefillInkButtonListener(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
	}
	
	/**
	 * -Simulates an attendant refilling the ink until it overflows (as overflow is unavoidable for bronze level receipt printer
	 *  and not reliably avoidable for silver)
	 * -Should only result in one call of "notifyInkAdded()" within the printer
	 * -Should handle and never re-throw overload exception (the attendant is used to the hardware doing this and has napkins for when they overfill)
	 * -Code was not placed in printer controller because this logic implements simulation methods rather than hardware-software interface methods.
	 * 
	 * @param printer
	 */
	public void refillInk(IReceiptPrinter printer) {
		
		for (int fillAmount = MAX_PRINTER_INK ; fillAmount > 0 ; fillAmount--) {
			try {
				printer.addInk(fillAmount);
				return;
				
			} catch (OverloadedDevice e) {/** Spilling cannot be prevented due to the nature of bronze & silver ink
			level tracking, thus the attendant pours until it tops off with paper towels/napkins on hand **/}				
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refillInk(logic.getHardware().getPrinter());		
	}
}