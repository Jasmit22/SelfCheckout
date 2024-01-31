package com.thelocalmarketplace.software.controllers;

import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.CardTypes;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Membership;
import com.thelocalmarketplace.software.general.Utilities;

/**
 * Card Reader Controller
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
public class CardReaderController extends AbstractLogicDependant implements CardReaderListener{
    
	/**
     * Base constructor
     * @param logic Reference to the central station logic
     * @throws NullPointerException If logic is null
     */
    public CardReaderController(SelfCheckoutStationSoftware logic) throws NullPointerException {
        super(logic);

        this.logic.getHardware().getCardReader().register(this);
    }

    @Override
    public void theDataFromACardHasBeenRead(CardData data) {
    	CardTypes t = Utilities.getCardType(data.getType());
    	String message = "";
    	
    	// Don't attempt to charge a membership card
    	if (!t.equals(CardTypes.MEMBERSHIP)) {
    		if (!this.logic.isSessionActive()) {
    			message = "Session not started";
    		}
    		else if (!this.logic.getStateLogic().inState(States.CHECKOUT)) {
    			message = "Not ready for checkout";
    		}
    		else if (!this.logic.getSelectedPaymentMethod().toString().equals(t.toString())) {
    			message = "Pay by " + t.toString() + " not selected";
    		}
    		
    		if (message.length() > 0) {
    			if (this.logic.getViewController().isReady()) {    				
    				this.logic.getViewController().getCustomerView().notify(message);
    			}
    			
    			return;
    		}
    		
    		// Check if transaction successful
    		if(this.logic.getCardPaymentLogic().approveTransaction(data.getNumber(),this.logic.getProductLogic().getBalanceOwed().doubleValue())){
    			
    			// If successful reduce amount owed by customer otherwise do nothing
    			this.logic.getProductLogic().modifyBalance(logic.getProductLogic().getBalanceOwed().negate());
    			
    			// Print receipt
				this.logic.getReceiptPrintingController().handlePrintReceipt(BigDecimal.ZERO);
    		}
    		else {
    			// Notify customer that transaction failed
    			if (this.logic.getViewController().isReady()) {    				
    				this.logic.getViewController().getCustomerView().notify("Transaction failed");
    			}
    		}
    		
    		System.out.println("Total owed: " + this.logic.getProductLogic().getBalanceOwed());
    	}
    	else { // Else card is a membership card
    		if (!this.logic.isSessionActive()) {
    			message = "Session not started";
    		}
    		else if (!this.logic.getStateLogic().inState(States.ADDMEMBERSHIP)) {
    			message = "Not ready for membership card";
    		}
    		
    		if (message.length() > 0) {
    			if (this.logic.getViewController().isReady()) {    				
    				this.logic.getViewController().getCustomerView().notify(message);
    			}
    			
    			return;
    		}
    		
    		// Card is confirmed to be a Membership Card 
    		// Check if Membership Number is Valid 			
			String membershipNumber = data.getNumber();
			
			// If membership is valid, boolean is set to true
			boolean isMembershipNumberValid = this.logic.getMembershipLogic().checkIfMembershipNumberValid(membershipNumber);
	 		
			if(isMembershipNumberValid) {
				
				// Goto normal state
				this.logic.getStateLogic().gotoState(States.NORMAL);
				
				// If Valid Membership
				this.logic.getMembershipLogic().setSelectedMembership(membershipNumber);
				
				Membership selectedMembership = this.logic.getMembershipLogic().getSelectedMembership();
				
				// Add membership number to the receipt controller 
				this.logic.getReceiptPrintingController().setMembershipNumberOnReceipt(membershipNumber);
				
				// Inside this method, update the customer GUI Display
				this.logic.getViewController().updateOnValidMembershipCardRead(selectedMembership);
				
				// Go back to main panel
	   			this.logic.getViewController().getCustomerView().switchCardLayoutView("checkoutPanel");
			} else {

				// Notify customer
				if (this.logic.getViewController().isReady()) {
					this.logic.getViewController().getCustomerView().notify("Invalid membership card detected");
				}
   			}
    	}
    }
    
    // ---- Unused ----

    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {

    }

    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {

    }

    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {

    }

    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {

    }


	@Override
	public void aCardHasBeenInserted() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void theCardHasBeenRemoved() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void aCardHasBeenTapped() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void aCardHasBeenSwiped() {
    
    }
}
