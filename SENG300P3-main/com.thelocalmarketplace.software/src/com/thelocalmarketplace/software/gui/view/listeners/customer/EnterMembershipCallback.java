package com.thelocalmarketplace.software.gui.view.listeners.customer;

import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Membership;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.view.listeners.INumpadCallback;

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

public class EnterMembershipCallback implements INumpadCallback {

	private ISelfCheckoutStationSoftware logic;

	
    public EnterMembershipCallback(ISelfCheckoutStationSoftware logic) {
        this.logic = logic;
    }

    @Override
    public void onEnterPressed(String sequence) {
    	System.out.println("Entered membership number: " + sequence);

    	if (!(sequence.isEmpty() || sequence == null)) {
    		if (this.logic.getStateLogic().inState(States.ADDMEMBERSHIP)) {    			
    			if (this.logic.getMembershipLogic().checkIfMembershipNumberValid(sequence)) {

    				// If valid Membership
    				this.logic.getMembershipLogic().setSelectedMembership(sequence);
    				
    				Membership selectedMembership = this.logic.getMembershipLogic().getSelectedMembership();
    				
    				// Add membership number to the receipt controller 
    				this.logic.getReceiptPrintingController().setMembershipNumberOnReceipt(sequence);
    				
    				// Inside this method, update the customer GUI Display
    				this.logic.getViewController().updateOnValidMembershipCardRead(selectedMembership);
    			}
    			else {
    				if (this.logic.getViewController().isReady()) {    					
    					this.logic.getViewController().getCustomerView().notify("Invalid membership number entered");
    				}
    			}
    		}
    		else {
    			if (this.logic.getViewController().isReady()) {    				
    				this.logic.getViewController().getCustomerView().notify("Not ready for membership number");
    			}
    			
    			return;
    		}
    		
    		// Go back to normal state
			this.logic.getStateLogic().gotoState(States.NORMAL);
    	}
    }
}
