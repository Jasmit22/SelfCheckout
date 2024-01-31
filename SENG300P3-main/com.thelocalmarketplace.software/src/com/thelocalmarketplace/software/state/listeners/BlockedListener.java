package com.thelocalmarketplace.software.state.listeners;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.state.AbstractStateTransitionListener;

/**
 * Blocked Session
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
public class BlockedListener extends AbstractStateTransitionListener {
	
	public BlockedListener(SelfCheckoutStationSoftware logic) throws NullPointerException {
		super(logic);
	}

	@Override
	public void onTransition() {
		
		this.logic.getHardware().getBaggingArea().disable();
		
		this.logic.getHardware().getMainScanner().disable();
		this.logic.getHardware().getHandheldScanner().disable();
		
		this.logic.getHardware().getCoinSlot().disable();
		this.logic.getHardware().getCoinValidator().disable();
		
		this.logic.getHardware().getBanknoteInput().disable();
		this.logic.getHardware().getBanknoteValidator().disable();
		//if (this.logic.getViewController().isReady()) {
			this.logic.getViewController().updateOnStationBlocked();
		//}
		
		
	}
}
