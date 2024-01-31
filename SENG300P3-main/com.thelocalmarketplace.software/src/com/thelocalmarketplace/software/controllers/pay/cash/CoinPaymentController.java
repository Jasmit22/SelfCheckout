package com.thelocalmarketplace.software.controllers.pay.cash;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Controller for payment through coins
 * 
 * Uses CurrencyLogic to calculate necessary change requirements
 * Then interacts with hardware to carry out change operations
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
public class CoinPaymentController extends AbstractLogicDependant implements CoinValidatorObserver {
	
	/**
	 * Base constructor
	 * @param logic reference to SelfCheckoutStationLogic the controller belongs to
	 * @throws NullPointerException If logic is null
	 */
	public CoinPaymentController(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
	
		this.logic.getHardware().getCoinValidator().attach(this);
	}
	
	/**
	 * Interacts with dispenser hardware to dispense coins as change to customer.
	 * 
	 * Precalculates the required change from what is available using logic classes,
	 * then attempts to interact with dispenser hardware to dispense what was calculated
	 * 
	 * @param overpay Is the amount to give back (will be converted to a positive value)
	 * @return The total value that the customer did not receive back due to failures (should be 0)
	 */
	public BigDecimal processCoinChange(BigDecimal overpay) {
		BigDecimal missed = BigDecimal.ZERO;
		
		if (overpay.compareTo(BigDecimal.ZERO) == 0) {
			
			// No over pay so return 0
			return missed;
		}
		
		// Sanitize argument
		overpay = overpay.abs();
		
		// Calculate change mapping
		Map<BigDecimal, Integer> change = this.logic.getCoinPaymentLogic().calculateChange(overpay, this.logic.getAvailableCoinsInDispensers(), true);
		
		System.out.println(this.logic.getAvailableCoinsInDispensers().toString());
		
		// Dispense coins
		for (Entry<BigDecimal, Integer> c : change.entrySet()) {
			final BigDecimal denomination = c.getKey();

			// Loop for each available coin
			for (int i = 0; i < c.getValue(); i++) {
				try {
					
					// Attempt to emit a coin from specific coin dispenser				
					this.logic.getHardware().getCoinDispensers().get(denomination).emit();
				} catch (Exception e) {
					missed = missed.add(denomination);
					
					System.out.println("Failed to emit coin");
				}
			}
		}
		
		return missed;
	}
	
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) throws SimulationException {
		String message = "";
		
		if (!this.logic.isSessionActive()) {
			message = "Session not started";
		}
		else if (!this.logic.getStateLogic().inState(States.CHECKOUT)) {
			message = "Not ready for checkout";
		}
		else if (!this.logic.getSelectedPaymentMethod().equals(PaymentMethods.CASH)) {
			message = "Payment by coin not selected";
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
		
		BigDecimal pay = this.logic.getProductLogic().getBalanceOwed().subtract(value);
		pay = pay.setScale(2, RoundingMode.HALF_UP);
		// Make Payment to transaction.
		this.logic.getProductLogic().modifyBalance(value.negate());
		
		// Check if balance has been paid in full
		if (pay.compareTo(BigDecimal.ZERO) <= 0) {
			pay = pay.abs();
			
			// Process change
			BigDecimal missed = this.processCoinChange(pay);
			
			// Check if some change failed to dispense
			if (missed.compareTo(BigDecimal.ZERO) > 0) {
				
				// Suspend station
				this.logic.getStateLogic().gotoState(States.SUSPENDED);
				
				message = "Not enough coin change is available: " + missed +  " is unavailable";
			}
			else {
				message = "Payment complete. Change dispensed successfully";
				
				// Print receipt
				this.logic.getReceiptPrintingController().handlePrintReceipt(pay.subtract(missed));
			}
		}
		else {
		
			message = "Balance owed: " + this.logic.getProductLogic().getBalanceOwed().setScale(2, RoundingMode.HALF_EVEN);
		}
		
		// Notify customer
        if (message.length() > 0 && this.logic.getViewController().isReady()) {    				
			this.logic.getViewController().getCustomerView().notify(message);
		}
	}
	
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		if (this.logic.getViewController().isReady()) {    				
			this.logic.getViewController().getCustomerView().notify("Invalid Coin Detected. Balance owed: " + this.logic.getProductLogic().getBalanceOwed().setScale(2, RoundingMode.HALF_EVEN));
		}
	}
	
	// ---- Unused ----
	
	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
	}
	
	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
	}
	
	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
	}
	
	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
	}
}
