package com.thelocalmarketplace.software.controllers.pay.cash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.BanknoteDispenserObserver;
import com.tdc.banknote.IBanknoteDispenser;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.Status;

/**
 * Banknote Dispensing Controller
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
public class BanknoteDispenserController extends AbstractLogicDependant implements BanknoteDispenserObserver {
	
	private static final int BUFFER = 10;
	
	/**
	 * List of all available banknotes of the denomination of this controller
	 */
	private List<Banknote> available;

	/**
	 * Denomination of banknotes that this dispenser controller is in charge of
	 */
	private BigDecimal denomination;
	
	/**
	 * Creates a controller to manage a banknote dispenser
	 * 
	 * @param logic The self-checkout software the controller belongs to
	 * @param denomination The denomination of the banknotes
	 * @throws NullPointerException if the denomination is null
	 */
	public BanknoteDispenserController(SelfCheckoutStationSoftware logic, BigDecimal denomination) throws NullPointerException {
		super(logic);
		
		if (denomination == null) {
			throw new NullPointerException("Denomination");
		}
		
		this.available = new ArrayList<>();
		this.denomination = denomination;
		
		// Attach self to specific dispenser corresponding to its denomination
		this.logic.getHardware().getBanknoteDispensers().get(this.denomination).attach(this);
	}
	
	/**
	 * Used in other methods to verify the that the number of banknotes is/isn't low or stable.
	 * Banknote dispenser may be empty, low, stable, or full
	 * 
	 * @param dispenser The banknote dispenser that's being checked
	 * @return predicted status of banknotes 
	 */
	private Status predictBanknotesInDispenser(IBanknoteDispenser dispenser) {
		if (this.getAvailableBanknotes().size() <= 0) {
			this.logic.getViewController().updateOnBanknotesEmpty(denomination);
			
			return Status.EMPTY;
		}
		else if (this.getAvailableBanknotes().size() <= BUFFER) {
			this.logic.getViewController().updateOnBanknotesLow(denomination);
			
			return Status.LOW;
		}
		else if (this.getAvailableBanknotes().size() < dispenser.getCapacity()) {
			this.logic.getViewController().updateOnBanknotesStable(denomination);
			
			return Status.STABLE;
		}
		
		this.logic.getViewController().updateOnBanknotesFull(denomination);
		
		return Status.FULL;
	}
	
	/**
	 * Gets the list of available banknotes
	 * 
	 * @return A list of available banknotes in the dispenser
	 */
	public List<Banknote> getAvailableBanknotes() {
        return this.available;
    }
	
	@Override
	public void banknotesEmpty(IBanknoteDispenser dispenser) {
		this.available.clear();
		
		this.predictBanknotesInDispenser(dispenser);
	}
	
	@Override
	public void banknoteAdded(IBanknoteDispenser dispenser, Banknote banknote) {
		this.available.add(banknote);
		
		this.predictBanknotesInDispenser(dispenser);
	}
	
	@Override
	public void banknoteRemoved(IBanknoteDispenser dispenser, Banknote banknote) {
		this.available.remove(banknote);
		
		this.predictBanknotesInDispenser(dispenser);
	}
	
	@Override
	public void banknotesLoaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		for (Banknote b : banknotes) {
            this.available.add(b);
        }
		
		this.predictBanknotesInDispenser(dispenser);
	}
	
	@Override
	public void banknotesUnloaded(IBanknoteDispenser dispenser, Banknote... banknotes) {
		 for (Banknote b : banknotes) {
			 this.available.remove(b);
	     }
		 
		 this.predictBanknotesInDispenser(dispenser);
	}
	
	@Override
	public void moneyFull(IBanknoteDispenser dispenser) {
		System.out.println("Banknote Dispenser is full: " + dispenser);		
		
		this.logic.getViewController().updateOnBanknotesFull(this.denomination);
	}
	
	// ---- Unused ----
	
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
