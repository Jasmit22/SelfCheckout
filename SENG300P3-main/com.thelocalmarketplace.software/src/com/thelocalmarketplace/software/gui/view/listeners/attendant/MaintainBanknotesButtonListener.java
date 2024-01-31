package com.thelocalmarketplace.software.gui.view.listeners.attendant;

import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.swing.JTextField;


/**
 * This class is responsible for maintaining banknotes by the attendant
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

public class MaintainBanknotesButtonListener extends AbstractLogicActionListener {
	
	private JTextField field;
	private BigDecimal denomination;
	
	
    public MaintainBanknotesButtonListener(SelfCheckoutStationSoftware logic, JTextField field, BigDecimal denomination) {
        super(logic);
        
        this.field = field;
        this.denomination = denomination;
    }

    /***
     * This method allows for attendant to refill or unload the banknotes in a dispenser
     * @param amount The number of banknotes to be loaded or unloaded
     * @param denomination Denomination of the banknotes to be loaded or unloaded
     */
    private void maintainBanknotes(int amount, BigDecimal denomination) {
    	
        // Iterate through each banknote denomination in the station
        for (BigDecimal currentDenomination : this.logic.getHardware().getBanknoteDenominations()) {
        	
            // Check if the denomination matches the desired denomination
            if (currentDenomination.equals(denomination)) {
                IBanknoteDispenser dispenser = this.logic.getHardware().getBanknoteDispensers().get(denomination);

                if (amount > 0) {

                    // Load new banknotes into the dispenser
                    for (int j = 0; j < amount; j++) {
                        try {
                            dispenser.load(new Banknote(Currency.getInstance("CAD"), currentDenomination));
                        }
                        catch (CashOverloadException e) {
                        	return;
                        }
                    }
                }
                else if (amount < 0) {
                	
                    //  Attendant Unload banknotes
                    List<Banknote> unloadedBanknotes = dispenser.unload();
                   
                    // Trim the list to the desired count
                    unloadedBanknotes = unloadedBanknotes.subList(0, Math.max((unloadedBanknotes.size() + amount), 0));
                    
                    // Reload the trimmed banknotes back to the dispenser
                    for (Banknote banknote : unloadedBanknotes) {
                        try {
                            dispenser.load(banknote);
                        }
                        catch (CashOverloadException e) {
                        	return;
                        }
                    }
                }
            }
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.maintainBanknotes(Integer.parseInt(this.field.getText()), this.denomination);
		}
		catch (NumberFormatException e1) {}
	}
}