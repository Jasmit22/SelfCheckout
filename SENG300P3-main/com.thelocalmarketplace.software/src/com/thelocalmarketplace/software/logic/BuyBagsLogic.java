package com.thelocalmarketplace.software.logic;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.bag.ReusableBag;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;

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

public class BuyBagsLogic extends AbstractLogicDependant {
	
	/**
	 * Price of one reusable bag
	 */
	public static final BigDecimal BAG_PRICE = new BigDecimal(1.0);
	
	/**
	 * Mass of one reusable bag
	 */
	public static final Mass BAG_MASS = new Mass(BigInteger.valueOf(5_000_000));
	
	
	/**
	 * Base constructor for buy bags logic
	 */
	public BuyBagsLogic(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic); 
	}
	
	/**
	 * When a user finishes entering how many reusable bags they wish to purchase the bags are dispensed
	 * @param numberBags Is the number of bags to attempt to dispense
	 * @return The number of bags that failed to dispense
	 */
	public int dispenseBags(int numberBags) {
		int missed = 0;
		
		this.logic.getBagDispenserController().clearNumberReusableBags();
		
		Mass allBoughtBags = Mass.ZERO;
		
		for (int i = 0; i < numberBags; i++) {
			try {
				ReusableBag bagDispensed = this.logic.getBagDispenserController().dispenseBag();
				
				allBoughtBags = allBoughtBags.sum(bagDispensed.getMass());
			} catch (Exception e){
				missed++;
			}
		}
		
		this.logic.getProductLogic().modifyBalance(BAG_PRICE.multiply(new BigDecimal(this.logic.getBagDispenserController().getNumberReusableBagsDispensed())));
		this.logic.getMassLogic().updateExpectedMass(logic.getMassLogic().getExpectedMass().sum(allBoughtBags));
		
		if (missed == numberBags) {
			this.endBuyBags();
		}
		
		return missed;
	}
	
	/**
	 * When a User selects to remove a bag
	 */
	public void removeBag() {
		System.out.println("Removing bag");
		
		this.logic.getProductLogic().modifyBalance(BAG_PRICE.negate());
		this.logic.getMassLogic().updateExpectedMass(logic.getMassLogic().getExpectedMass().difference(BAG_MASS).abs());
		this.logic.getStateLogic().gotoState(States.BLOCKED);
	}

	/**
	 * Simulates when a user signals that they would like to buy bags
	 */
	public void startBuyBags() {
		if (!logic.isSessionActive()) throw new InvalidStateSimulationException("Session has not started");
		if (this.logic.getStateLogic().inState(States.BUYBAGS)) throw new InvalidStateSimulationException("Cannot start BUYBAGS state when in BUYBAGS state");
		
		this.logic.getStateLogic().gotoState(States.BUYBAGS);
	}
	
	/**
	 * Simulates when a user is finished purchasing their own bags
	 */
	
	public void endBuyBags() {
		System.out.println("Ending buy bags");
    
		if (!logic.isSessionActive()) throw new InvalidStateSimulationException("Session has not started");
		
		this.logic.getStateLogic().gotoState(States.NORMAL);
	}
}
