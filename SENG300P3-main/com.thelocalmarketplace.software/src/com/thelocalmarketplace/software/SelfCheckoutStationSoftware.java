package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.software.controllers.BagDispenserController;
import com.thelocalmarketplace.software.controllers.CardReaderController;
import com.thelocalmarketplace.software.controllers.ReceiptPrintingController;
import com.thelocalmarketplace.software.controllers.WeightDiscrepancyController;
import com.thelocalmarketplace.software.controllers.item.AddBarcodedItemController;
import com.thelocalmarketplace.software.controllers.pay.cash.BanknoteDispenserController;
import com.thelocalmarketplace.software.controllers.pay.cash.CashPaymentController;
import com.thelocalmarketplace.software.controllers.pay.cash.CoinDispenserController;
import com.thelocalmarketplace.software.controllers.pay.cash.CoinPaymentController;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.LocalViewController;
import com.thelocalmarketplace.software.logic.AddItemByTextLogic;
import com.thelocalmarketplace.software.logic.BuyBagsLogic;
import com.thelocalmarketplace.software.logic.CardPaymentLogic;
import com.thelocalmarketplace.software.logic.CurrencyPaymentLogic;
import com.thelocalmarketplace.software.logic.MassLogic;
import com.thelocalmarketplace.software.logic.MembershipLogic;
import com.thelocalmarketplace.software.logic.ProductLogic;
import com.thelocalmarketplace.software.logic.StateLogic;
import com.thelocalmarketplace.software.state.listeners.*;

import ca.ucalgary.seng300.simulation.InvalidStateSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

/**
 * Represents the central logic for the control software
 * 
 * By instantiating this class, it is implied that checkout station is now on
 * It effectively acts as the main manager of the entire checkout system (everything links back to this instance)
 * 
 * This class contains a reference to every logic and controller class instance
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
public class SelfCheckoutStationSoftware implements ISelfCheckoutStationSoftware {

	/**
	 * Name of the station
	 */
	private String name;
	
	/**
	 * Unique identifier for the station
	 */
	private UUID id;
	
	/**
	 * Reference to physical hardware this is installed on
	 */
	private ISelfCheckoutStation hardware;
	
	/**
	 * Instance of the currency logic for coin denominations
	 */
	protected CurrencyPaymentLogic coinPaymentLogic;
	
	/**
	 * Instance of the currency logic for banknote denominations
	 */
	protected CurrencyPaymentLogic cashPaymentLogic;
	
	/**
     * Instance of logic for card payment via swipe
     */
	protected CardPaymentLogic cardPaymentLogic;
	
	/**
	 * Instance of logic for adding item to cart via text search
	 */
	protected AddItemByTextLogic addItemByTextLogic;

	/**
	 * Instance of logic for states
	 */
	protected StateLogic stateLogic;
	
	/**
	 * Instance of logic for membership
	 */
	protected MembershipLogic membershipLogic;
	
	/** 
	 * Instance of mass logic 
	 */
	protected MassLogic massLogic;
	
	/**
	 * Instance of buy bags logic
	 */
	protected BuyBagsLogic buyBagsLogic;
	
	/**
	 * Instance of logic that handles products
	 */
	protected ProductLogic productLogic;
	
	/**
	 * Instance of the controller that handles payment with coin
	 */
	protected CoinPaymentController coinPaymentController;
	
	/**
	 * Instance of the controller that handles payment with cash
	 */
	protected CashPaymentController cashPaymentController;
	
	/**
	 * Instance of the controller that handles adding barcoded product
	 */
	protected AddBarcodedItemController addBarcodedProductController;
	
	/**
	 * Instance of the controller that handles dispensing reusable bags
	 */
	protected BagDispenserController buyBagsController;
	
	/**
	 * Instance of the controller that handles weight discrepancy detected
	 */
	protected WeightDiscrepancyController weightDiscrepancyController;
	
	/**
	 * Reference to main view controller
	 */
	protected LocalViewController view;

	/**
	 * Instance of controller that handles swiping a card
	 */
	protected CardReaderController cardReaderController;
	
	/**
	 * Instance of the controller that handles receipt printing
	 */
	protected ReceiptPrintingController receiptPrintingController;
	
	/**
	 * Instances of the controllers that handle coin dispensing indexed by their corresponding coin denomination
	 */
	protected Map<BigDecimal, CoinDispenserController> coinDispenserControllers = new HashMap<>();
	
	/**
	 * Instances of the controllers that handle banknote dispensing indexed by their corresponding banknote denomination
	 */
	protected Map<BigDecimal, BanknoteDispenserController> banknoteDispenserControllers = new HashMap<>();

	/**
	 * Current selected payment method
	 */
	private PaymentMethods paymentMethod;
	
	/**
	 * Tracks if the customer session is active
	 */
	private boolean sessionActive;
	
	/**
	 * Tracks if the station is enabled
	 */
	private boolean enabled;

	
	/**
	 * Most basic constructor for a new CentralStationLogic instance
	 * @throws NullPointerException If hardware is null
	 */
	public SelfCheckoutStationSoftware(ISelfCheckoutStation hardware) throws NullPointerException {
		this(hardware, "Station");
	}
	
	public SelfCheckoutStationSoftware(ISelfCheckoutStation hardware, String name) throws NullPointerException {
		if (hardware == null) {
			throw new NullPointerException("Hardware");
		}
		
		this.sessionActive = false;
		this.enabled = false;

		this.name = name;
		this.id = UUID.randomUUID();
		this.paymentMethod = PaymentMethods.NONE;
		
		this.hardware = hardware;
		
		this.view = new LocalViewController(this);
		
		// Reference to logic objects
		this.massLogic = new MassLogic(this);
		this.stateLogic = new StateLogic(States.SUSPENDED, States.NORMAL);
		this.addItemByTextLogic = new AddItemByTextLogic(this);
		this.buyBagsLogic = new BuyBagsLogic(this);
		this.productLogic = new ProductLogic(this);
		this.cardPaymentLogic = new CardPaymentLogic();
		this.membershipLogic = new MembershipLogic();
		this.coinPaymentLogic = new CurrencyPaymentLogic(this.hardware.getCoinDenominations());
		this.cashPaymentLogic = new CurrencyPaymentLogic(this.hardware.getBanknoteDenominations());	

		// Instantiate each controller
		this.coinPaymentController = new CoinPaymentController(this);
		this.cashPaymentController = new CashPaymentController(this);
		this.addBarcodedProductController = new AddBarcodedItemController(this);
		this.weightDiscrepancyController = new WeightDiscrepancyController(this);
		this.cardReaderController = new CardReaderController(this);
		this.receiptPrintingController = new ReceiptPrintingController(this);
		this.buyBagsController = new BagDispenserController(this);	
		this.configureCoinDispenserControllers(this.getCoinPaymentLogic().getDenominationsAsList());
		this.configureBanknoteDispenserControllers(this.getCashPaymentLogic().getDenominationsAsList());
		
		// Configure states
		this.configureStates();
	}

	/**
	 * Helper method for configuring state transitions and listeners
	 */
	private void configureStates() {
		
		// NORMAL		<==>	[ANY STATE]
		// SUSPENDED	<==>	[ANY STATE]
		for (States s : States.values()) {
			if (!s.equals(States.NORMAL)) {				
				this.getStateLogic().registerTransition(States.NORMAL, s);
				this.getStateLogic().registerTransition(s, States.NORMAL);
			}
			
			if (!s.equals(States.SUSPENDED)) {				
				this.getStateLogic().registerTransition(States.SUSPENDED, s);
				this.getStateLogic().registerTransition(s, States.SUSPENDED);
			}
		}
		
		// Other transitions
		this.getStateLogic().registerTransition(States.BUYBAGS, States.BLOCKED);
		this.getStateLogic().registerTransition(States.BLOCKED, States.BUYBAGS);
		
		// Attach listeners
		this.getStateLogic().registerListener(States.NORMAL, new NormalListener(this));
		this.getStateLogic().registerListener(States.BLOCKED, new BlockedListener(this));
		this.getStateLogic().registerListener(States.SUSPENDED, new SuspendedListener(this));
		this.getStateLogic().registerListener(States.ADDBAGS, new AddBagsListener(this));
		this.getStateLogic().registerListener(States.ADDITEM, new AddItemListener(this));
		this.getStateLogic().registerListener(States.ADDMEMBERSHIP, new AddMembershipListener(this));
		this.getStateLogic().registerListener(States.CHECKOUT, new CheckoutListener(this));
		this.getStateLogic().registerListener(States.BUYBAGS, new BuyBagsListener(this));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if (this.enabled) {
			this.view.updateOnStationEnabled();
			this.getStateLogic().gotoState(this.getStateLogic().getLastState());
		}
		else {
			this.view.updateOnStationDisabled();
			this.getStateLogic().gotoState(States.SUSPENDED);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	/**
	 * Gets the current selected payment method
	 * @return the payment method
	 */
	@Override
	public PaymentMethods getSelectedPaymentMethod() {
		return this.paymentMethod;
	}
	
	/**
	 * Sets the desired payment method for the customer
	 * @param method Is the payment method to use
	 */
	@Override
	public void selectPaymentMethod(PaymentMethods method) {
		this.paymentMethod = method;
	}
	
	/**
	 * Helper method to setup coin dispenser controllers
	 * @param denominations Is the list of coin denominations supported by the hardware
	 */
	private void configureCoinDispenserControllers(List<BigDecimal> denominations) {
		for (BigDecimal d : denominations) {
			this.getCoinDispenserControllers().put(d, new CoinDispenserController(this, d));
		}
	}
	
	/**
	 * Helper method to setup banknote dispenser controllers
	 * @param denominations Is the list of coin denominations supported by the hardware
	 */
	private void configureBanknoteDispenserControllers(List<BigDecimal> denominations) {
		for (BigDecimal d : denominations) {
			this.getBanknoteDispenserControllers().put(d, new BanknoteDispenserController(this, d));
		}
	}
	
	/**
	 * Gets all of the available coins in each coin dispenser
	 * @return A mapping of coin counts indexed by their denomination
	 */
	@Override
	public Map<BigDecimal, Integer> getAvailableCoinsInDispensers() {
		Map<BigDecimal, Integer> available = new HashMap<>();
		
		// Assume all coins in each dispenser are of the same denomination (they should be)
		for (Entry<BigDecimal, CoinDispenserController> e : this.getCoinDispenserControllers().entrySet()) {
			final BigDecimal d = e.getKey();
			final CoinDispenserController c = e.getValue();
			
			available.put(d, c.getAvailableCoins().size());
		}
		
		return available;
	}
	
	/**
	 * Gets all of the available banknotes tracked in each banknote dispenser
	 * @return A mapping of banknote counts indexed by their denomination
	 */
	@Override
	public Map<BigDecimal, Integer> getAvailableBanknotesInDispensers() {
	 	Map<BigDecimal, Integer> available = new HashMap<>();

	    for (Entry<BigDecimal, BanknoteDispenserController> entry : this.getBanknoteDispenserControllers().entrySet()) {
	        final BigDecimal denomination = entry.getKey();
	        final BanknoteDispenserController controller = entry.getValue();
	        
	        available.put(denomination, controller.getAvailableBanknotes().size());
	    }
	    
	    return available;
	}

	/**
	 * Checks if the session is started
	 * @return True if the session is active; false otherwise
	 */
	@Override
	public boolean isSessionActive() {
		return this.sessionActive;
	}
	
	/**
	 * Marks the current self checkout session as active
	 * @throws SimulationException If the session is already active
	 */
	@Override
	public void startSession() throws SimulationException {
		if (!this.isEnabled()) {
			throw new InvalidStateSimulationException("Station not enabled");
		}
		else if (this.isSessionActive()) {
			throw new InvalidStateSimulationException("Session already started");
		}
		
		this.sessionActive = true;
		
		this.reset();
		
		this.getStateLogic().gotoState(States.NORMAL);
		this.view.updateOnSessionStarted();
	}
	
	@Override
	public void stopSession() {
		this.sessionActive = false;
		
		this.view.updateOnSessionStopped();
	}
	
	@Override
	public void reset() {
		this.getProductLogic().reset();
		this.getMassLogic().reset();
		
		this.getMembershipLogic().clearSelectedMembership();
		
		this.getStateLogic().gotoState(States.NORMAL);
		this.view.updateOnSessionStopped();
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public UUID getID() {
		return this.id;
	}

	@Override
	public ISelfCheckoutStation getHardware() {
		return this.hardware;
	}

	@Override
	public CurrencyPaymentLogic getCoinPaymentLogic() {
		return this.coinPaymentLogic;
	}

	@Override
	public CurrencyPaymentLogic getCashPaymentLogic() {
		return this.cashPaymentLogic;
	}

	@Override
	public MassLogic getMassLogic() {
		return this.massLogic;
	}

	@Override
	public BuyBagsLogic getBuyBagsLogic() {
		return this.buyBagsLogic;
	}

	@Override
	public ProductLogic getProductLogic() {
		return this.productLogic;
	}

	@Override
	public CardPaymentLogic getCardPaymentLogic() {
		return this.cardPaymentLogic;
	}

	@Override
	public AddItemByTextLogic getAddItemByTextLogic() {
		return this.addItemByTextLogic;
	}

	@Override
	public StateLogic getStateLogic() {
		return this.stateLogic;
	}

	@Override
	public MembershipLogic getMembershipLogic() {
		return this.membershipLogic;
	}

	@Override
	public CoinPaymentController getCoinPaymentController() {
		return this.coinPaymentController;
	}

	@Override
	public CashPaymentController getCashPaymentController() {
		return this.cashPaymentController;
	}

	@Override
	public AddBarcodedItemController getAddBarcodedItemController() {
		return this.addBarcodedProductController;
	}

	@Override
	public BagDispenserController getBagDispenserController() {
		return this.buyBagsController;
	}

	@Override
	public WeightDiscrepancyController getWeightDiscrepancyController() {
		return this.weightDiscrepancyController;
	}

	@Override
	public CardReaderController getCardReaderController() {
		return this.cardReaderController;
	}

	@Override
	public ReceiptPrintingController getReceiptPrintingController() {
		return this.receiptPrintingController;
	}

	@Override
	public LocalViewController getViewController() {
		return this.view;
	}

	@Override
	public Map<BigDecimal, CoinDispenserController> getCoinDispenserControllers() {
		return this.coinDispenserControllers;
	}

	@Override
	public Map<BigDecimal, BanknoteDispenserController> getBanknoteDispenserControllers() {
		return this.banknoteDispenserControllers;
	}
}
