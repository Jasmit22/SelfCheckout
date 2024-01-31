package com.thelocalmarketplace.software.gui.view.listeners.attendant;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.swing.JTextField;

import com.tdc.CashOverloadException;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.AbstractLogicActionListener;


/**
 * This class is responsible for maintaining coins by the attendant
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

public class MaintainCoinsButtonListener extends AbstractLogicActionListener {
	
	private JTextField field;
	private BigDecimal denomination;
	
	
    public MaintainCoinsButtonListener(SelfCheckoutStationSoftware logic, JTextField field, BigDecimal denomination) {
        super(logic);
        
        this.field = field;
        this.denomination = denomination;
    }

    /***
     * This method allows for attendant to refill or unload the coins in a dispenser
     * @param amount The number of coins to be loaded or unloaded
     * @param denomination Denomination of the coins to be loaded or unloaded
     */
    private void maintainCoins(int amount, BigDecimal denomination) {
    	
        // Iterate through each coin denomination in the station
        for (BigDecimal currentDenomination : this.logic.getHardware().getCoinDenominations()) {
        	
            // Check if the denomination matches the desired denomination
            if (currentDenomination.equals(denomination)) {
                ICoinDispenser dispenser = this.logic.getHardware().getCoinDispensers().get(denomination);

                if (amount > 0) {

                    // Load new coins into the dispenser
                    for (int j = 0; j < amount; j++) {
                        try {
                            dispenser.load(new Coin(Currency.getInstance("CAD"), currentDenomination));
                        }
                        catch (CashOverloadException e) {
                        	return;
                        }
                    }
                }
                else if (amount < 0) {
                	
                    //  Attendant Unload coins
                    List<Coin> unloadedCoins = dispenser.unload();

                    // Trim the list to the desired count
                    unloadedCoins = unloadedCoins.subList(0, Math.max((unloadedCoins.size() + amount), 0));
                    
                    // Reload the trimmed coins back to the dispenser
                    for (Coin coin : unloadedCoins) {
                        try {
                            dispenser.load(coin);
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
			this.maintainCoins(Integer.parseInt(this.field.getText()), this.denomination);
		}
		catch (NumberFormatException e1) {}
	}
}