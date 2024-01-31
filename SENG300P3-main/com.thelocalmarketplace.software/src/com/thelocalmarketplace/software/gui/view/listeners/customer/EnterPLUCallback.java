package com.thelocalmarketplace.software.gui.view.listeners.customer;

import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.listeners.INumpadCallback;

import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Listens when customer inputs a PLU code to add item to cart
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

public class EnterPLUCallback implements INumpadCallback {

	private ISelfCheckoutStationSoftware logic;
	
	
    public EnterPLUCallback(ISelfCheckoutStationSoftware logic) {
        this.logic = logic;
    }

    @Override
    public void onEnterPressed(String sequence) {
    	if (!sequence.isEmpty() && sequence != null) {
    		try {    			
    			this.logic.getProductLogic().startAddPLUProductToCart(new PriceLookUpCode(sequence));
    		}
    		catch (SimulationException e) {
    			if (this.logic.getViewController().isReady()) {
    				this.logic.getViewController().getCustomerView().notify("Failed to add PLU coded product");
    			}
    		}
    	}
    }
    
}