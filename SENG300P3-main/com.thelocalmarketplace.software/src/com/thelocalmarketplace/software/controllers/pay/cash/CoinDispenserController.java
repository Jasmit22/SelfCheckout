package com.thelocalmarketplace.software.controllers.pay.cash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserObserver;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.Status;

/**
 * Represents an object that will control a coin dispenser of a specific coin denomination
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
public class CoinDispenserController extends AbstractLogicDependant implements CoinDispenserObserver {
	
	private static final int BUFFER = 10;
	
	/**
	 * List of available coins of this denomination
	 */
	private List<Coin> available;
	
	/**
	 * Denomination of coins that this dispenser controller is in charge of
	 */
	private BigDecimal denomination;

	
	/**
	 * Base constructor
	 * @param logic Is the reference to the logic
	 * @param denomination Is the denomination this controller will dispense
	 * @throws NullPointerException If any argument is null
	 */
	public CoinDispenserController(SelfCheckoutStationSoftware logic, BigDecimal denomination) throws NullPointerException {
		super(logic);
		
		if (denomination == null) {
			throw new NullPointerException("Denomination");
		}
		
		this.available = new ArrayList<>();
		this.denomination = denomination;
		
		// Attach self to specific dispenser corresponding to its denomination
		this.logic.getHardware().getCoinDispensers().get(this.denomination).attach(this);
	}
	
	/** 
	 * Singular function that unifies the various check functions for the coins
	 * Updates view accordingly
	 * Coin dispenser may be empty, low, stable, or full
	 * 
	 * @param dispenser The coin dispenser to be checked
	 * @return predicted status of the coins
	 */
	private Status runCheck(ICoinDispenser dispenser) {
		if (this.getAvailableCoins().size() <= 0) {
			this.logic.getViewController().updateOnCoinsEmpty(denomination);
			
			return Status.EMPTY;
		}
		else if (this.getAvailableCoins().size() <= BUFFER) {
			this.logic.getViewController().updateOnCoinsLow(denomination);
			
			return Status.LOW;
		}
		else if (this.getAvailableCoins().size() < dispenser.getCapacity()) {
			this.logic.getViewController().updateOnCoinsStable(denomination);
			
			return Status.STABLE;
		}
		
		this.logic.getViewController().updateOnCoinsFull(denomination);
		
		return Status.FULL;
	}
	
	/**
	 * Gets a list of coins of corresponding denomination that are available as change
	 * @return The list of coins
	 */
	public List<Coin> getAvailableCoins() {
		return this.available;
	}
	
	@Override
	public void coinsEmpty(ICoinDispenser dispenser) {
		this.available.clear();
		
		this.runCheck(dispenser);
	}

	@Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
		this.available.add(coin);
		
		this.runCheck(dispenser);
	}

	@Override
	public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
		this.available.remove(coin);
		
		this.runCheck(dispenser);
	}
	
	@Override
	public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
		for (Coin c : coins) {
			this.available.add(c);
		}

		this.runCheck(dispenser);
	}

	@Override
	public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {
		for (Coin c : coins) {
			this.available.remove(c);
		}
		
		this.runCheck(dispenser);
	}
	
	// ------ Unused -------

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

	@Override
	public void coinsFull(ICoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}
}
