package com.thelocalmarketplace.software.controllers;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.state.InvalidStateTransitionException;

import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Weight Discrepancy Controller
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
public class WeightDiscrepancyController extends AbstractLogicDependant implements ElectronicScaleListener {
	
	/**
	 * WeightDiscrepancyController Constructor
	 * 
	 * @param logic A reference to the logic instance
	 * @throws NullPointerException If logic is null
	 */
	public WeightDiscrepancyController(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
		
		// Register self to bagging area scale
		this.logic.getHardware().getBaggingArea().register(this);
	}
	
	/**
	 * Triggered when mass on bagging area scale is changed
	 * If a weight discrepancy is detected, system is updated accordingly
	 * 
	 * @param scale The electronic scale responsible for weighing items in the bagging area
	 * @param mass The new mass measured by the scale
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		final Mass diff = mass.difference(this.logic.getMassLogic().getActualMass()).abs();

		// Update actual weight of the scale
		this.logic.getMassLogic().updateActualMass(mass);
		
		// Update view
		if (this.logic.getViewController().isReady()) {			
			this.logic.getViewController().updateWeightOnCustomerView(mass);
		}	
		
		// Weight discrepancies are ignored when in ADDBAGS and ADDITEM states
		if (this.logic.getStateLogic().inState(States.ADDBAGS)){
			
			// The actual mass now is whatever was on the scale before this change
			if (mass.compareTo(this.logic.getMassLogic().getActualMass()) > 0) {
				
				// Add the bag to the bag mass
				this.logic.getMassLogic().updateTotalBagMass(this.logic.getMassLogic().getTotalBagMass().sum(diff));

			} else {
				
				// Remove the bag from the bag mass
				this.logic.getMassLogic().updateTotalBagMass(this.logic.getMassLogic().getTotalBagMass().difference(diff).abs());
			}
			if (this.logic.getMassLogic().inBaggingDiscrepency) {
				
				// In case the item causing issues was removed
				this.logic.getMassLogic().handleWeightDiscrepancy();
			}
		}
		else if (this.logic.getStateLogic().inState(States.ADDITEM)) {
			this.logic.getProductLogic().onItemMassUpdated(diff);
		}
		else {
			try {				
				this.logic.getMassLogic().handleWeightDiscrepancy();
			}
			catch (SimulationException e1) {
				if (this.logic.getViewController().isReady() && this.logic.isSessionActive() && !this.logic.isEnabled()) {
					this.logic.getViewController().getCustomerView().notify("Station not ready for that action");
				}
			}
			catch (InvalidStateTransitionException e2) {
				System.out.println("Unable to transition");
			}
			
			if (this.logic.getStateLogic().inState(States.BUYBAGS)) {
				this.logic.getBuyBagsLogic().endBuyBags();
			}
		}
	}
	
	// - - - - Notify methods here are a good place to trigger UI listeners - - - -
	
	/**
	 * Triggered when actual weight is over expected weight
	 */
	public void notifyOverload() {
		System.out.println("Weight discrepancy detected. Please remove item(s)");
		
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateDiscrepencyLabel("Weight In Bagging Area Is Larger Than Expected, Please Remove Item");
		}
		
	}
	
	/**
	 * Triggered when actual weight is under expected weight
	 */
	public void notifyUnderload() {
		System.out.println("Weight discrepancy detected. Please add item(s)");
		
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateDiscrepencyLabel("Weight In Bagging Area Is Lower Than Expected, Please Add Item");
		}
	}

	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {}
	
	// ---- Unused -----

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
		
	}
}	