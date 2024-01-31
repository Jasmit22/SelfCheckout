package com.thelocalmarketplace.software.controllers.pay.cash;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import java.util.Map.Entry;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.BanknoteValidator;
import com.tdc.banknote.BanknoteValidatorObserver;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;

/**
 * Controller for payments using cash
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
public class CashPaymentController extends AbstractLogicDependant implements BanknoteValidatorObserver {

	/**
	 * Base Constructor
	 * 
	 * @param logic The self-checkout software the controller belongs to
	 * @throws NullPointerException if any argument is null
	 */
	public CashPaymentController(ISelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
		
		this.logic.getHardware().getBanknoteValidator().attach(this);
	}
	
	/**
	 * Emits a combination of banknotes and coins to full fill change requirements
	 * @param overpay Is the change to give back
	 * @return The amount that failed to be returned
	 */
	public BigDecimal processCashChange(BigDecimal overpay) {
	    BigDecimal missed = BigDecimal.ZERO;
	    BigDecimal sum = BigDecimal.ZERO;
	    
	    if (overpay.compareTo(BigDecimal.ZERO) == 0) {
	        return missed;
	    }
	    
	    // Sanitize over pay
	    overpay = overpay.abs();
	    
		// Calculate required change in banknotes
	    Map<BigDecimal, Integer> changeInBanknotes = this.logic.getCashPaymentLogic().calculateChange(overpay, this.logic.getAvailableBanknotesInDispensers(), false);
		
		// Dispense banknotes
		for (Entry<BigDecimal, Integer> entry : changeInBanknotes.entrySet()) {
			BigDecimal denomination = entry.getKey();
			int count = entry.getValue();

			// Loop for each available banknote
			for (int i = 0; i < count; i++) {
				try {			
					this.logic.getHardware().getBanknoteDispensers().get(denomination).emit();
					
					sum = sum.add(denomination);
				} catch (Exception e) {
					missed = missed.add(denomination);
					
					System.out.println("Failed to emit banknote");
				}
			}
		}
		
		// Dispense what is left as coins
		missed = missed.add(this.logic.getCoinPaymentController().processCoinChange(overpay.subtract(sum)));
	
		
	    return missed;
	}

	
	@Override
	public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
		String message = "";
		
		if (!this.logic.isSessionActive()) {
			message = "Session not started";
		}
		else if (!this.logic.getStateLogic().inState(States.CHECKOUT)) {
			message = "Not ready for checkout";
		}
		else if (!this.logic.getSelectedPaymentMethod().equals(PaymentMethods.CASH)) {
			message = "Payment by cash not selected";
		}
		else if (this.logic.getProductLogic().getBalanceOwed().compareTo(BigDecimal.ZERO) == 0) {
			message = "Nothing to pay for";
		}
		
		if (message.length() > 0) {
			if (this.logic.getViewController().isReady()) {    				
				this.logic.getViewController().getCustomerView().notify(message);
			}

			return;
		}
		
		BigDecimal pay = this.logic.getProductLogic().getBalanceOwed().subtract(denomination);
		pay = pay.setScale(2, RoundingMode.HALF_UP);
		
        this.logic.getProductLogic().modifyBalance(denomination.negate());

        if (pay.compareTo(BigDecimal.ZERO) <= 0) {
        	pay = pay.abs();
        	
            BigDecimal missed = processCashChange(pay);
            
            if (missed.compareTo(BigDecimal.ZERO) > 0) {
            	
            	// Suspend station
				this.logic.getStateLogic().gotoState(States.SUSPENDED);
            	
            	message = "Not enough change available: " + missed +  " is unavailable";
            }
            else {
                message = "Payment complete. Change dispensed successfully";
                
                // Print receipt
				this.logic.getReceiptPrintingController().handlePrintReceipt(pay.subtract(missed));
            }
        }
        else {
            // Round the change to two decimal places
        	
            message = "Payment accepted. Remaining balance: " + pay.setScale(2, RoundingMode.HALF_EVEN);
            
        }
        
        // Notify customer
        if (message.length() > 0 && this.logic.getViewController().isReady()) {    				
			this.logic.getViewController().getCustomerView().notify(message);
		}
	}

	@Override
	public void badBanknote(BanknoteValidator validator) {
		if (this.logic.getViewController().isReady()) {    				
			this.logic.getViewController().getCustomerView().notify("Invalid Banknote Detected. Balance owed: " + this.logic.getProductLogic().getBalanceOwed().setScale(2, RoundingMode.HALF_EVEN));
		}
	}

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}


}
