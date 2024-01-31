package com.thelocalmarketplace.software.logic;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.scanner.Barcode;

import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.state.InvalidStateTransitionException;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Handles all logic operations related to weight for a self checkout station
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
 * @author Chantel del Carmen	(30129615)
 * @author Dana Al Bastrami		(30170494)
 * @author Maria Munoz			(30175339)
 * @author Ernest Shukla		(30156303)
 * @author Hillary Nguyen		(30161137)
 * @author Robin Bowering		(30123373)
 * @author Anne Lumumba			(30171346)
 * @author Jasmit Saroya		(30170401)
 * @author Fion Lei				(30134327)
 * @author Royce Knoepfli		(30172598) */
public class MassLogic extends AbstractLogicDependant {
	
	/**
	 * Mass limit for bags when customer is adding own bags
	 * 20 grams
	 */
	public static final Mass BAG_WEIGHT_LIMIT = new Mass((double) 20);
	
	/**
	 * Expected weight change on software side
	 */
	private Mass expectedMass;
	
	/**
	 * Actual weight on scale
	 */
	private Mass actualWeight;
	
	/**
	 * Tolerance for weight difference before scale blocks due to discrepancy
	 */
	private Mass sensitivity; 
	
	/**
	 * Mass of all bags that have been added
	 */
	private Mass bagMassTotal;
	
	/**
	 * Tracks whether or not a bagging discrepancy has been found
	 */
	public boolean inBaggingDiscrepency;

	/**
	 * Tracks whether or not the attendant has approved the current bagging area
	 */
	private boolean approvedBagging;
	
	
	/**
	 * Base constructor
	 */
	public MassLogic(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
		
		this.logic = logic;
		this.expectedMass = Mass.ZERO;
		this.actualWeight = Mass.ZERO;
		this.sensitivity = this.logic.getHardware().getBaggingArea().getSensitivityLimit();
		this.bagMassTotal = Mass.ZERO;
	}
	
	/**
	 * Adds the expected weight of the product with given barcode to the expectedWeight
	 * @param barcode barcode of the item for which to add the expected weight
	 */
	public void addExpectedMassOfBarcodedProduct(Barcode barcode) {
		if (!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)) {
			throw new InvalidStateSimulationException("Barcode not registered to product database");
		}
		
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		Mass mass = new Mass(product.getExpectedWeight());
		
		this.expectedMass = this.expectedMass.sum(mass);
	}

	
	/**
	 * Removes the weight of the product given from expectedWeight
	 * @param barcode Is the barcode of item to remove weight of
	 */
	public void removeExpectedMassOfBarcodedProduct(Barcode barcode) {
		if (!ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)) {
			throw new InvalidStateSimulationException("Barcode not registered to product database");
		}
		
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		Mass mass = new Mass(product.getExpectedWeight());
		MassDifference difference = this.expectedMass.difference(mass);
		
		if (difference.compareTo(Mass.ZERO) < 0) throw new InvalidStateSimulationException("Expected weight cannot be negative");
		this.expectedMass = difference.abs();
	}
	
	/**
	 * Updates actual weight to the mass passed
	 * @param mass Is the mass to change the actual weight to
	 */
	public void updateActualMass(Mass mass) {
		this.actualWeight = mass;
	}
	
	/**
	 * Updates expected weight to the mass passed
	 * @param mass Is the mass to change the expected weight to
	 */
	public void updateExpectedMass(Mass mass) {
		this.expectedMass = mass;
	}
	
	/**
	 * Checks if there is a weight discrepancy 
	 * @return True if there is a discrepancy; False otherwise
	 */
	public boolean checkWeightDiscrepancy() {
		
		// Checks for discrepancy and calls notifier if needed 
		if (actualWeight.difference(expectedMass).abs().compareTo(this.sensitivity) <= 0 ) {
			if (this.logic.getViewController().isReady()) {
				this.logic.getViewController().removeDiscrepencyCustomerView();
			}
	
			return false;
		}
	
		if (actualWeight.compareTo(expectedMass) > 0) this.logic.getWeightDiscrepancyController().notifyOverload();
		else this.logic.getWeightDiscrepancyController().notifyUnderload();
		
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().addDiscrepencyCustomerView();
		}
		
		return true;
	}
	
	/**
	 * If there is a weight discrepancy, enters blocking state; otherwise, goes back to normal
	 * @throws SimulationException If session not started
	 * @throws SimulationException If the scale is not operational
	 */
	public void handleWeightDiscrepancy() {
		
		// Throw exceptions
		if (!this.logic.isSessionActive()) throw new InvalidStateSimulationException("Session not started");
		
		if (this.logic.getMassLogic().checkWeightDiscrepancy()) {
			if (!this.logic.getStateLogic().inState(States.BLOCKED)) {
				this.logic.getStateLogic().gotoState(States.BLOCKED);
			}
		}
		else if (this.logic.getStateLogic().inState(States.BLOCKED)){
			if (this.logic.getStateLogic().getLastState().equals(States.BLOCKED)) {
				this.logic.getStateLogic().gotoState(States.NORMAL);
			}
			else {				
				this.logic.getStateLogic().gotoState(this.logic.getStateLogic().getLastState());
			}
		}
	}
	
	/**
	 * Sets expected weight to actual weight
	 */
	public void overrideDiscrepancy() {
		this.expectedMass = this.actualWeight;
	}
	
	/**
	 * Handles start of bagging state
	 * Notifies customer to place bags on scale
	 * Enables bagging area and disables scanners 
	 * 
	 * @throws InvalidStateSimulationException if called when CentralStationLogic is not in ADDINGBAGS state 
	 * @throws InvalidStateSimulationException if session is not started
	 */
	public void startAddBags() {
		if (!logic.isSessionActive()) throw new InvalidStateSimulationException("Session has not started");
		
		this.logic.getStateLogic().gotoState(States.ADDBAGS);
		
		System.out.println("Please place bags on the scale");
		if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateOnStartAddOwnBags();
		}
	}
	
	/**
	 * When user has finished adding their bags to the scale
	 * Requires attendant verification if bags are too heavy (indicated by this.approvedBagging = true)
	 * Sets expected weight to the current weight of the bags on the scale 
	 * 
	 * @throws InvalidStateSimulationException if called when CentralStationLogic is not in ADDINGBAGS state 
	 * @throws InvalidStateSimulationException if session is not started
	 */
	public void endAddBags() {
		if (!this.logic.isSessionActive()) throw new InvalidStateSimulationException("Session has not started");
		if (!this.logic.getStateLogic().inState(States.ADDBAGS)) throw new InvalidStateSimulationException("Cannot end ADDBAGS state when not in ADDBAGS state");
		
		if (this.getTotalBagMass().compareTo(MassLogic.BAG_WEIGHT_LIMIT) < 0 || this.approvedBagging) {
			
			// If bag weight is under the allowed weight
			this.overrideDiscrepancy();
			this.approvedBagging = false;
			this.setBaggingDiscrepency(false);
			
			this.logic.getStateLogic().gotoState(States.NORMAL);
			
			if (this.logic.getViewController().isReady()) {
				this.logic.getViewController().updateOnSuccessfullAddOwnBags();
			}
		} else {
			if (this.logic.getViewController().isReady()) {
				this.logic.getViewController().updateOnUnsuccessfulAddOwnBags();
			}

			this.baggingDiscrepencyDetected();
		}
	}
	
	/**
	 * Used for resetting
	 */
	public void reset() {
		this.bagMassTotal = Mass.ZERO;
		this.setBaggingDiscrepency(false);
		
		this.updateActualMass(Mass.ZERO);
		this.updateExpectedMass(Mass.ZERO);
		this.handleWeightDiscrepancy();
	}
	
	/**
	 * Simulates attendant signifying they approve the bagging area 
	 * @throws InvalidStateTransitionException if the add bags state cannot be exited
	 */
	public void approveBaggingArea() throws InvalidStateTransitionException {
		this.overrideDiscrepancy();
		this.approvedBagging = true;
		this.bagMassTotal = Mass.ZERO;
		this.endAddBags();
	}
	
	/**
	 * Simulates attendant being notified that a bagging discrepancy has occurred 
	 * The only way for customer to transition out of ADDBAGS state is for attendant to call
	 * approveBaggingArea()
	 */
	public void baggingDiscrepencyDetected() {
		this.inBaggingDiscrepency = true;
	}
	
	/**
	 * Setter for in baggingDiscrepency
	 */
	public void setBaggingDiscrepency(boolean b) {
		this.inBaggingDiscrepency = b;
	}
	
	/**
	 * Getter for in baggingDiscrepency for testing
	 */
	public boolean getBaggingDiscrepency() {
		return this.inBaggingDiscrepency;
	}
	
	/**
	 * Gets the actual mass on the scale
	 * @return The actual mass
	 */
	public Mass getActualMass() {
		return this.actualWeight;
	}
	
	/**
	 * Sets total mass of current bags
	 * @param m new mass at last event
	 */
	public void updateTotalBagMass(Mass m) {
		this.bagMassTotal = m;
		
	}
	/**
	 * Adds mass bag to current bag mass
	 * @param bag new mass at last event
	 */
	public void addTotalBagMass(Mass bag) {
		this.bagMassTotal = this.bagMassTotal.sum(bag);
	}
	
	/**
	 * Gets total mass of current bags
	 * @param m new mass at last event
	 */
	public Mass getTotalBagMass() {
		return this.bagMassTotal;
	}
	
	/**
	 * Getter for expected mass
	 */
	public Mass getExpectedMass(){
		return this.expectedMass;
	}
}
