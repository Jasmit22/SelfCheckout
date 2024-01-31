package com.thelocalmarketplace.software.controllers;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.bag.ReusableBagDispenserListener;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;

/**
 * Bag Dispenser Controller
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
public class BagDispenserController extends AbstractLogicDependant implements ReusableBagDispenserListener{

	/**
	 * Instance of buy bags logic
	 */
	private int numberBagsDispensed;

	
	/**
	 * Base constructor
	 */
    public BagDispenserController(SelfCheckoutStationSoftware logic) throws NullPointerException {
    	super(logic);
    	
    	this.logic.getHardware().getReusableBagDispenser().register(this);
    	this.numberBagsDispensed = 0;
    }
    
    /**
	 * Dispenses a bag
     * @throws EmptyDevice 
	 */
    public ReusableBag dispenseBag() throws EmptyDevice {
    	ReusableBag r = this.logic.getHardware().getReusableBagDispenser().dispense();  	
    	
    	return r;
    }
    
    /**
   	 * returns number of reusable bags dispensed
   	 */
    public int getNumberReusableBagsDispensed() {
    	return this.numberBagsDispensed;
    }
    
    /**
	 * Sets number of bags dispensed back to 0
	 */
    public void clearNumberReusableBags() {
    	this.numberBagsDispensed = 0;
    }
    
	@Override
	public void aBagHasBeenDispensedByTheDispenser() {
		this.numberBagsDispensed++;
	
		
		try {
			this.logic.getViewController().updateOnBagPurchased();

		}catch (Exception e) {
			// only when testing will this be entered (no gui)
		}
	};
	
	@Override
	public void theDispenserIsOutOfBags() {};
	
	@Override
	public void bagsHaveBeenLoadedIntoTheDispenser(int count) {
		
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	};
}
