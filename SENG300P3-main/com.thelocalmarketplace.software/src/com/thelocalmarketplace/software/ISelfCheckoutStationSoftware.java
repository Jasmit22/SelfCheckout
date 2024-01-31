package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.Map;
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
import com.thelocalmarketplace.software.gui.LocalViewController;
import com.thelocalmarketplace.software.logic.AddItemByTextLogic;
import com.thelocalmarketplace.software.logic.BuyBagsLogic;
import com.thelocalmarketplace.software.logic.CardPaymentLogic;
import com.thelocalmarketplace.software.logic.CurrencyPaymentLogic;
import com.thelocalmarketplace.software.logic.MassLogic;
import com.thelocalmarketplace.software.logic.MembershipLogic;
import com.thelocalmarketplace.software.logic.ProductLogic;
import com.thelocalmarketplace.software.logic.StateLogic;

/**
 * Specifies the basis of self checkout station software
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
public interface ISelfCheckoutStationSoftware extends IStationSoftware<ISelfCheckoutStation> {

	/**
	 * Sets if this station is enabled or not
	 * @param enabled Is if the station should be enabled or disabled
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * Checks if this station is enabled
	 * @return True if enabled, false otherwise
	 */
	public boolean isEnabled();
	
	/**
	 * Marks the current self checkout session as active
	 */
	public void startSession();
	
	/**
	 * Marks the current self checkout session as inactive
	 */
	public void stopSession();
	
	/**
	 * Checks if the session is active
	 * @return True if started, false otherwise
	 */
	public boolean isSessionActive();
	
	/**
	 * Resets this station so its ready for a new customer
	 */
	public void reset();
	
	/**
	 * Getter for the station name
	 * @return This station's name
	 */
	public String getName();
	
	/**
	 * Getter for the station ID
	 * @return This station's ID
	 */
	public UUID getID();
	
	public Map<BigDecimal, Integer> getAvailableCoinsInDispensers();
	
	public Map<BigDecimal, Integer> getAvailableBanknotesInDispensers();
	
	public PaymentMethods getSelectedPaymentMethod();
	
	public void selectPaymentMethod(PaymentMethods method);
	
	public CurrencyPaymentLogic getCoinPaymentLogic();
	
	public CurrencyPaymentLogic getCashPaymentLogic();
	
	public MassLogic getMassLogic();
	
	public BuyBagsLogic getBuyBagsLogic();
	
	public ProductLogic getProductLogic();
	
	public CardPaymentLogic getCardPaymentLogic();
	
	public AddItemByTextLogic getAddItemByTextLogic();
	
	public StateLogic getStateLogic();
	
	public MembershipLogic getMembershipLogic();
	
	public CoinPaymentController getCoinPaymentController();
	
	public CashPaymentController getCashPaymentController();
	
	public AddBarcodedItemController getAddBarcodedItemController();
	
	public BagDispenserController getBagDispenserController();
	
	public WeightDiscrepancyController getWeightDiscrepancyController();
	
	public CardReaderController getCardReaderController();
	
	public ReceiptPrintingController getReceiptPrintingController();
	
	public LocalViewController getViewController();
	
	public Map<BigDecimal, CoinDispenserController> getCoinDispenserControllers();
	
	public Map<BigDecimal, BanknoteDispenserController> getBanknoteDispenserControllers();
}
