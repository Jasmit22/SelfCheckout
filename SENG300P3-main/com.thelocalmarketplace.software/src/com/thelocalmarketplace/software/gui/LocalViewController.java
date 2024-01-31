package com.thelocalmarketplace.software.gui;

import java.math.BigDecimal;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.software.ISelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Membership;
import com.thelocalmarketplace.software.gui.view.CustomerSimulationView;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.partials.attendant.AttendantCustomerPopUp;
import com.thelocalmarketplace.software.gui.view.partials.attendant.StationControlPanel;
import com.thelocalmarketplace.software.logic.BuyBagsLogic;

/**
 * Class for controlling the view relative to the checkout station this is on
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

public class LocalViewController extends AbstractLogicDependant implements IViewable {
	
	/**
	 * The control panel specific to this local machine
	 */
	private StationControlPanel localAttendantView;
	
	/**
	 * View class that represents what the customer can see
	 */
	private CustomerView customerView;
	
	/**
	 * Control panel to simulate customer actions
	 */
	private CustomerSimulationView simulationView;
	
	/**
	 * Tracks if the view controller is ready
	 * (Basically is just false so GUI is not started when automated testing)
	 */
	private boolean ready;
	
	/**
	 * Declare class for controlling the popUp dialogs on the attendant frame
	 * @author Daniel
	 */
	private AttendantCustomerPopUp popUp;
	
	/**
	 * Controls the timer thread that monitors the bagging area scale
	 */
	private DiscrepencyThreadController discrepencyThread;
	
	/**
	 * Controls weather the discrepency overlay contains a dont bag items button or not
	 */
	public boolean isAddingItem = false;
	
	
	/**
	 * Base constructor
	 */
	public LocalViewController(ISelfCheckoutStationSoftware logic) {
		super(logic);
		
		this.customerView = new CustomerView(this.logic);
		this.simulationView = new CustomerSimulationView(this.logic);
		this.ready = false;

		this.discrepencyThread = new DiscrepencyThreadController(logic);
	}

	public boolean isReady() {
		return this.ready;
	}
	
	/**
	 * Establishes a connection between the local view controller and attendant view
	 * Allows for local stations to update information on the attendant screen
	 * @param panel The view to connect to
	 */
	public void connectToAttendantView(StationControlPanel panel, JFrame attendantViewFrame) {
		this.localAttendantView = panel;
		
		// Initialize popUp class and connect to attendant frame
		this.popUp = new AttendantCustomerPopUp(attendantViewFrame);
	}
	
	/**
	 * Facade for tapping into the attendant's view from this local station
	 * @return The instance of the attendant view object
	 */
	public StationControlPanel getLocalAttendantView() {
		return this.localAttendantView;
	}
	
	/**
	 * Gets the the customer view
	 * @return The instance
	 */
	public CustomerView getCustomerView() {
		return this.customerView;
	}
	
	/**
	 * Gets the the simulation view
	 * @return The instance
	 */
	public CustomerSimulationView getCustomerSimulationView() {
		return this.simulationView;
	}
	
	/**
	 * Starts the LocalViewController.
	 * Initializes views and updates GUI components based on the logic state.
	 */
	@Override
	public void start() {
		if (this.localAttendantView == null) {
			throw new NullPointerException("Connection to Attendant View not established");
		}
		
		this.ready = true;
		
		// Initialization
    	if (this.logic.isEnabled()) this.updateOnStationEnabled();
    	else this.updateOnStationDisabled();
    	
    	if (this.logic.isSessionActive()) this.updateOnSessionStarted();
    	else this.updateOnSessionStopped();
    	
    	if (this.logic.getStateLogic().inState(States.NORMAL)) {    		
    		this.updateOnNormalStateEntered();
    	}
    	else if (this.logic.getStateLogic().inState(States.SUSPENDED)) {
    		this.updateOnStationSuspended();
    	}
		
    	// Start views
		SwingUtilities.invokeLater(() -> {
			this.customerView.start();
			this.simulationView.start();
        });
	}
	
	/**
	 * Shows the views associated with the LocalViewController.
	 */
	@Override
	public void show() {
		this.customerView.show();
		this.simulationView.show();
	}

	/**
	 * Shows the views associated with the LocalViewController.
	 */
	@Override
	public void hide() {
		this.customerView.hide();
		this.simulationView.hide();
	}
	
	/**
	 * Updates the GUI when the station is enabled.
	 */
	public void updateOnStationEnabled() {
		if (this.isReady()) {			
			this.localAttendantView.startPanel.stationEnabledLabel.updateStatus("TRUE", "green");
			
			this.localAttendantView.startPanel.enableStationButton.setEnabled(false);
			this.localAttendantView.startPanel.switchToMaintainButton.setEnabled(false);
			this.localAttendantView.startPanel.disableStationButton.setEnabled(true);
			
			if (!this.logic.isSessionActive()) {				
				this.customerView.switchCardLayoutView("startSessionPanel");
			}
			else {
				this.customerView.gotoLastPanel();
			}
		}
 	}
 	
 	/**
 	 * Updates the GUI when the station is disabled.
 	 */
 	public void updateOnStationDisabled() {
 		if (this.isReady()) {
 			this.localAttendantView.startPanel.stationEnabledLabel.updateStatus("FALSE", "red");

 			this.localAttendantView.startPanel.enableStationButton.setEnabled(true);
 			this.localAttendantView.startPanel.switchToMaintainButton.setEnabled(true);
 			this.localAttendantView.startPanel.disableStationButton.setEnabled(false);
 			this.localAttendantView.startPanel.overrideDiscrepancyButton.setEnabled(false);
 			this.localAttendantView.startPanel.printReceiptButton.setEnabled(false);
 
 			this.customerView.markLastPanel();
 			this.customerView.switchCardLayoutView("disabledStationPanel");
 		}
 	}
 	
 	public  void updateOnNormalStateEntered() {
 		this.isAddingItem = false;
 		if (this.isReady()) { 			
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("NORMAL", "green");
 			this.localAttendantView.startPanel.printReceiptButton.setEnabled(false);

 			this.customerView.enableButtonsBlocked();
 		}
 	}
 	
 	public  void updateOnStationBlocked() {
 		if (this.isReady()) { 			
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("BLOCKED", "orange");
 			
 			this.customerView.disableButtonsBlocked();
 		}
 	}
 	
 	public void updateOnStationSuspended() {
 		if (this.isReady()) {
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("SUSPENDED", "orange"); 			
 		}
 	}
 	
 	public void updateOnOtherStateEntered(String stateName) {
 		if (this.isReady()) { 			
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus(stateName, "yellow");
 		}
 	}
 	
 	public void updateOnAddItemStateEntered() {
 		if (this.isReady()) { 			
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("WAITING FOR ITEM", "purple");
 		}
	}
 	
 	public void updateOnAddMembershipStateEntered() {
 		if (this.isReady()) {
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("WAITING FOR MEMBERSHIP", "purple");
 		}
	}
 	
 	public void updateOnAddBagsStateEntered() {
 		if (this.isReady()) { 			
 			this.localAttendantView.startPanel.stationStateLabel.updateStatus("WAITING FOR BAGS", "purple");
 		}
	}
 	
 	/**
 	 * Updates the GUI when a session is started.
 	 * If the LocalViewController is ready, it updates the sessionStartedLabel on the attendant's start panel to "TRUE" in green color.
 	 */
 	public void updateOnSessionStarted() {
 		if (this.isReady()) {
 			this.localAttendantView.startPanel.sessionStartedLabel.updateStatus("TRUE", "green");
 			this.localAttendantView.startPanel.addItemByTextSearchButton.setEnabled(true);
 			
 			this.customerView.switchCardLayoutView("checkoutPanel");
 		}
 	}
 	
 	/**
 	 * Updates the GUI when a session is stopped.
 	 * If the LocalViewController is ready, it updates the sessionStartedLabel on the attendant's start panel to "FALSE" in red color.
 	 * It also switches the customer view to the "CheckOutPanel".
 	 */
 	public void updateOnSessionStopped() {
 		if (this.isReady()) {
 			this.localAttendantView.startPanel.sessionStartedLabel.updateStatus("FALSE", "red");
 			this.localAttendantView.startPanel.addItemByTextSearchButton.setEnabled(false);
 			
 			try { 				
 				this.customerView.itemsDisplay.removeAll();
 				this.customerView.itemsDisplay.revalidate();
 				this.customerView.itemsDisplay.repaint();
 				
 				this.updateOnValidMembershipCardRead(null);
 			}
 			catch (Exception e) {}
 			
 			this.customerView.switchCardLayoutView("endSessionPanel");
 		}
 	}
 	
 	/**
 	 * Updates the GUI when ink is low.
 	 * If the ink is low, it updates the inkLabel on the attendant's start panel to "Low" in orange color.
 	 */
   	public void updateOnInkLow() {
   		this.localAttendantView.startPanel.inkLabel.updateStatus("LOW", "orange");
   	}
   	
   	/**
 	 * Updates the GUI when ink is empty.
 	 * If the ink is empty, it updates the inkLabel on the attendant's start panel to "Empty" in red color.
 	 */
   	public void updateOnInkEmpty() {
   		this.localAttendantView.startPanel.inkLabel.updateStatus("EMPTY", "red");
   	}
   	
   	/**
   	 * Updates the GUI when ink is full.
   	 * If the ink is full, it updates the inkLabel on the attendant's start panel to "Full" in green color.
   	 */
   	public void updateOnInkFull() {
   		this.localAttendantView.startPanel.inkLabel.updateStatus("FULL", "green");
   	}
   	
   	/**
   	 * Updates the GUI when ink is stable.
   	 * If the ink is full, it updates the inkLabel on the attendant's start panel to "Full" in green color.
   	 */
   	public void updateOnInkStable() {
   		this.localAttendantView.startPanel.inkLabel.updateStatus("STABLE", "green");
   	}
   	
   	/**
   	 * Updates the GUI when paper is low.
   	 * If the paper is low, it updates the paperLabel on the attendant's start panel to "Low" in red color.
   	 */
   	public void updateOnPaperLow() {
   		this.localAttendantView.startPanel.paperLabel.updateStatus("LOW", "red");
   	}
   	
   	public void updateOnPaperEmpty() {
   		this.localAttendantView.startPanel.paperLabel.updateStatus("EMPTY", "red");
	}
   	
   	/**
   	 * Triggered when coin dispenser hardware observer detects low coins of a specific denomination.
   	 * Updates the GUI to indicate low status for the specified coin denomination in red color.
   	 * @param denomination The denomination that is low
   	 */
   	public void updateOnPaperFull() {
   		if (this.isReady())
   			this.localAttendantView.startPanel.paperLabel.updateStatus("FULL", "green");
   	}
   	
	public void updateOnPaperStable() {
		if (this.isReady())
			this.localAttendantView.startPanel.paperLabel.updateStatus("STABLE", "green");
   	}
   	
   	/**
   	 * Triggered when coin dispenser hardware observer detects low coins of a specific denomination
   	 * @param denomination The denomination that is low
   	 */
   	public void updateOnCoinsLow(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.coinLabels.get(denomination).updateStatus("LOW", "orange");
   	}
   	
   	public void updateOnCoinsEmpty(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.coinLabels.get(denomination).updateStatus("EMPTY", "red");
   	}
   	
   	/**
   	 * Triggered when coin dispenser hardware observer detects full coins of a specific denomination
   	 * @param denomination The denomination that is full
   	 */
   	public void updateOnCoinsFull(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.coinLabels.get(denomination).updateStatus("FULL", "yellow");
   	}
   	
   	public void updateOnCoinsStable(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.coinLabels.get(denomination).updateStatus("STABLE", "green");
	}
   	
   	public void updateOnBanknotesEmpty(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.banknoteLabels.get(denomination).updateStatus("EMPTY", "red");
   	}
   	
   	/**
   	 * Triggered when banknote dispenser hardware observer detects low banknotes of a specific denomination
   	 * @param denomination The denomination that is low
   	 */
   	public void updateOnBanknotesLow(BigDecimal denomination) {
   		if (this.isReady())
   			this.localAttendantView.startPanel.banknoteLabels.get(denomination).updateStatus("LOW", "orange");
   	}
   	
   	/**
   	 * Triggered when banknote dispenser hardware observer detects full banknotes of a specific denomination
   	 * @param denomination The denomination that is full
   	 */
   	public void updateOnBanknotesFull(BigDecimal denomination) {
   		if (this.isReady())	
   			this.localAttendantView.startPanel.banknoteLabels.get(denomination).updateStatus("FULL", "yellow");
   	}
   	
   	/**
   	 * Triggered when banknote dispenser hardware observer detects stable banknotes of a specific denomination
   	 * @param denomination The denomination that is stable
   	 */
   	public void updateOnBanknotesStable(BigDecimal denomination) {
   		if (this.isReady())			
   			this.localAttendantView.startPanel.banknoteLabels.get(denomination).updateStatus("STABLE", "green");
   	}

// CUSTOMER
   	
   	/**
   	 * @author Daniel Yakimenka (10185055)
   	 * @param name - the name of station requesting attendant assistance
   	 * @param status - indicates weather the call attendant button was toggled on or off
   	 * @param logic - the SelfCheckoutStation at which the button was pressed
   	 */
   	public void updateOnCustomerCallsAttendant(String name, boolean status, ISelfCheckoutStationSoftware logic) {
   		// Connell's comments: 
   		// create popup where attendant will go see Customer
   		// please see customer --> Pop Up! 
   		// create popup on Customer where they cant do anything until attendant approves <-- **not part of use case description**
   		// please wait for attendant to exit out of frame --> then it will release this 
   		//
   		
   		// Check if the popUp class was initialized
   		if (popUp == null) {
   			throw new NullPointerException("Attendant view not connected");
   		}
   		
   		// Check if popUp for this specific station has been created, if not, create one
   		else if (status == true && popUp.PopUpMap.get(name) == null) {
   	   		popUp.createPopUp(name, logic);
   		}
   		// If a popUp dialog has already been created, make it visible if its not, otherwise do nothing
   		else if (status == true && popUp.PopUpMap.get(name) != null) {
   			
   			JDialog pane = popUp.PopUpMap.get(name);
   			
   			if (!pane.isVisible()) {
   				popUp.toggle(name);
   			}
   		}
   		
   		// If a popUp exists and the button was toggled off, delete the popUp 
   		else if (status == false && popUp.PopUpMap.get(name) != null){
   			
   			JDialog pane = popUp.PopUpMap.get(name);
   			
   			if (pane.isVisible()) {
   				pane.dispose();
   			}
   		}
   		
   		// Depreciated statement: I have disabled the ability to close the popUp via "X" button which would cause condition:
   		// status == false && popUp.PopUpMap.get(name) == null
   		else {
   			popUp.deletePopUp(name);	
   		}		
   	}
   	

   	public void requestApprovalDontBag() {
   		this.localAttendantView.startPanel.overrideDiscrepancyButton.setEnabled(true);
   	}
   	
   	public void requestApprovedDontBag() {
   		this.localAttendantView.startPanel.overrideDiscrepancyButton.setEnabled(false);
   	}
   	
	/**
   	 * Updates the GUI when a customer requests to add their own bags.
   	 */
   	public void updateOnStartAddOwnBags() {
   		// Customer wishes to add own bag --> add on scale 
   		this.customerView.displayAddOwnBags();
   	}
   	
   	/**
   	 * Updates the GUI when a customer selects finished adding bags and there is no weight discrepency
   	 */
   	public void updateOnSuccessfullAddOwnBags() {
   		this.customerView.removeAddOwnBags();
   		this.localAttendantView.startPanel.overrideDiscrepancyButton.setEnabled(false);
   	}
   	
   	/**
   	 * Updates the GUI when a customer selects finished adding bags and there is weight discrepency
   	 */
   	public void updateOnUnsuccessfulAddOwnBags() {
   		this.customerView.addingOwnBagsPanel.unsuccessfullAdd();
   		this.localAttendantView.startPanel.overrideDiscrepancyButton.setEnabled(true);
   		
   	}
   	
   	

   	/**
   	 * Triggered when a product is removed from a customers cart
   	 */
   	
   	public void updateRemoveItemFromCart() {
   		if (this.isReady()) {
   			System.out.println("update remove");
   			this.customerView.showReminder("removeItem");
   			//this.startDiscrepencyThread();
   			this.logic.getMassLogic().handleWeightDiscrepancy();
   		}
   	}


   	
   	/**
   	 * Triggered when a product is added to a customers cart
   	 */
   	public void updateOnProductAddedToCart(String text, long price, Product product) {
   		if (this.isReady()) {   			
   			this.customerView.addNewItemToCheckoutDisplay(text, price, product);
   			this.isAddingItem = true;
   			this.customerView.enableDontBagItemButton();
   			if (product instanceof BarcodedProduct) {
   				this.startDiscrepencyThread();
   				
   			}
   			this.customerView.showReminder("addItem");
   		
   		}
   	}
   	
   	public void updateOnPluCodedProductPutOnScale() {
   		this.customerView.unshowReminder();
   	}
   	
   	/**
   	 * Triggered when a customer purchases a store bag.
   	 * Updates the GUI to reflect the purchase of a grocery bag with the specified value.
   	 */
   	public void updateOnBagPurchased() {
   		this.customerView.addNewItemToCheckoutDisplay("Grocery Bag",BuyBagsLogic.BAG_PRICE.longValue(), null);
   		this.startDiscrepencyThread();
   		this.customerView.showReminder("addBag");
   		
   	}
   	
   	
	/**
   	 * Triggered when a customer removes a store bag.
   	 * Updates the GUI to reflect the purchase of a grocery bag with the specified value.
   	 */
   	public void updateReusableBagRemoved() {
   		this.customerView.showReminder("removeItem");
   		this.startDiscrepencyThread();
   	}
   	
   	/**
   	 * Changes the string in the discrepency panel triggered on norify overload or underload
   	 */
   	public void updateDiscrepencyLabel(String newLabel) {
   		this.customerView.discrepencyPanelAddItem.setLabel(newLabel);
   	}

	/**
   	 * Starts discrepency thread when a barcoded item has been added
   	 */
   	public void startDiscrepencyThread() {
   		if (this.isReady()) {   			
   			this.discrepencyThread.start();
   		}
   	}
   	
   	/**
   	 * Stops discrepency thread when no weight discrepency 
   	 */
   	public void stopDiscrepencyThread() {
   		if (this.isReady()) {   			
   			this.discrepencyThread.stop();
   		}
   	}
  
	/**
   	 * Updates the GUI weight on customer view
   	 * @param item The name of the PLU item.
   	 * @param price The price of the PLU item.
   	 */
   	public void updateWeightOnCustomerView(Mass weight) {
   		if (this.isReady()) {
   			this.customerView.updateDisplayWeight(weight); //update weight   
   			//if (this.logic.getStateLogic().inState(States.ADDBAGS)) {
   			//	this.logic.getMassLogic().handleWeightDiscrepancy();
   			//}
   		}
   	}
   	
	/**
	 * Updates the price in customer view when the product logic updates price
   	 */
   	public void updatePriceOnCustomerView() {
   		if (this.isReady()) {
   			this.customerView.updateDisplayTotal(this.logic.getProductLogic().getBalanceOwed()); //update total
   		}
   	}
   	
   	/**
	 * Displays discrepency screen in customer view
   	 */
   	
   	public void addDiscrepencyCustomerView() {
   		System.out.println("adding discrepency");
   		if(this.isAddingItem) {
   			this.customerView.discrepencyPanelAddItem.allowDontBag();
   		}
   		this.customerView.displayDiscrepency();
   	}
   	
   	/**
	 * removes discrepency screen in customer view
   	 */
   	
   	public void removeDiscrepencyCustomerView() {
   		System.out.println(logic.getStateLogic().getState().toString());
   		
   		this.customerView.removeDiscrepency();
   		this.stopDiscrepencyThread();
   		this.isAddingItem = false;
   		this.customerView.unshowReminder();
   		this.customerView.disableDontBagItemButton();
   		this.customerView.discrepencyPanelAddItem.resetView();
   		
   		if (this.logic.getStateLogic().inState(States.BUYBAGS)) {
   			this.logic.getBuyBagsLogic().endBuyBags();
   		}if (this.logic.getStateLogic().inState(States.ADDBAGS)) {
   			this.logic.getMassLogic().endAddBags();
   			this.customerView.removeAddOwnBags();
   		}
   	}
   	
   	/**
   	 * Updates the GUI when a customer swipes a valid membership card.
   	 * Updates the membership number on the customer display and the end session panel.
   	 * @param memberNo The barcode representing the membership number.
   	 */
   	public void updateOnValidMembershipCardRead(Membership memberNo) {
   		if (this.isReady()) {
   			if (memberNo == null) {
   				this.customerView.updateDisplayMember("?");
   				this.customerView.endSessionPanel.updateMemberNo("?");
   			}
   			else {   				
   				this.customerView.updateDisplayMember(memberNo.getMembershipNumber()); // Update Membership Number
   				this.customerView.endSessionPanel.updateMemberNo(memberNo.getMembershipNumber()); // Update Membership Number on Receipt
   				
   				// Notify customer
   				this.logic.getViewController().getCustomerView().notify("Valid membership recognized");
   			}
   		}
    }
   	
   	public void updateOnReceiptPrintingFail() {
   		if (this.isReady()) {
   			this.localAttendantView.startPanel.printReceiptButton.setEnabled(true);
   		}
   	}
   	
   	public void openMembershipNumPad() {
   		this.customerView.switchCardLayoutView("numpad_MembershipType_Panel");
   	}
}
