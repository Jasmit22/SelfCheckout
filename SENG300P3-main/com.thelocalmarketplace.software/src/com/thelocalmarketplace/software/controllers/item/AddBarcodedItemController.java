package com.thelocalmarketplace.software.controllers.item;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Represents the software controller for adding barcoded items
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
public class AddBarcodedItemController extends AbstractLogicDependant implements BarcodeScannerListener {    
    
    /**
     * AddBarcodedProductController Constructor
     * 
     * @param logic A reference to the logic instance
     * @throws NullPointerException If logic is null
     */
    public AddBarcodedItemController(SelfCheckoutStationSoftware logic) throws NullPointerException {
    	super(logic);
        
        // Register self to main and hand held barcode scanners
        this.logic.getHardware().getMainScanner().register(this);
        this.logic.getHardware().getHandheldScanner().register(this);
    }
    
    @Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) throws SimulationException, NullPointerException {
    	String message = "";
    	
    	if (!this.logic.isSessionActive()) {
    		message = "The session has not been started";
    	}
    	else if (this.logic.getStateLogic().inState(States.BLOCKED)) {
    		message = "Station is blocked";
    	}
    	
    	if (message.length() > 0) {
			if (this.logic.getViewController().isReady()) {    				
				this.logic.getViewController().getCustomerView().notify(message);
			}

			return;
		}
    	
    	// Update mass and cart in logic
    	this.logic.getProductLogic().addBarcodedProductToCart(barcode);
    	
    	// Block station (will be unblocked by weight discrepancy controller)
		this.logic.getStateLogic().gotoState(States.BLOCKED);
		
		// Tell customer to add bag to cart (good place to trigger UI listeners)
		System.out.println("Item added to cart. Please place scanned item in bagging area");
	}
    
    // ---- Unused ----

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