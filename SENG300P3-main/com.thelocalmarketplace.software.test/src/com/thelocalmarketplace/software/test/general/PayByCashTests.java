package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

/**
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
 * @author Royce Knoepfli		(30134327)
 */
public class PayByCashTests {
	
	private SelfCheckoutStationSoftware logicBronze;
	
	private BigDecimal tenDollar;
	private BigDecimal fiveDollar;
	private BigDecimal twoDollar;
	private BigDecimal dollar;
	private BigDecimal quarter;
	private BigDecimal halfDollar;
	private BigDecimal[] coinList;
	private BigDecimal[] noteList;
	
	private SelfCheckoutStationBronze stationBronze;
	
	private Currency cad;
	private Banknote fiveNote;
	private Banknote tenNote;
	
	@Before
	public void setup() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		dollar = new BigDecimal(1);
		halfDollar = new BigDecimal(0.50);
		quarter = new BigDecimal(0.25);
		twoDollar = new BigDecimal(2);
		fiveDollar = new BigDecimal(5);
		tenDollar = new BigDecimal(10);
		coinList = new BigDecimal[] {quarter, halfDollar, dollar};
		noteList = new BigDecimal[] {twoDollar, fiveDollar, tenDollar};
		
		cad = Currency.getInstance("CAD"); 
		
		fiveNote = new Banknote(cad, fiveDollar);
		tenNote = new Banknote(cad, tenDollar);
		
		AbstractSelfCheckoutStation.configureCurrency(cad);
		AbstractSelfCheckoutStation.configureCoinDenominations(coinList);
		AbstractSelfCheckoutStation.configureBanknoteDenominations(noteList);
		AbstractSelfCheckoutStation.configureBanknoteStorageUnitCapacity(10);
		AbstractSelfCheckoutStation.configureCoinStorageUnitCapacity(10);
		AbstractSelfCheckoutStation.configureCoinTrayCapacity(5);
		AbstractSelfCheckoutStation.configureCoinDispenserCapacity(5);
		
		stationBronze = new SelfCheckoutStationBronze();
		logicBronze = new SelfCheckoutStationSoftware(stationBronze);
		
		stationBronze.plugIn(PowerGrid.instance());
		stationBronze.turnOn();
		
		logicBronze.setEnabled(true);
		
		//Map<BigDecimal, Integer> dispensable = new HashMap<>();
		//dispensable.put(new BigDecimal(0.05), 1); // 1 nickel can be dispensed
		
		//PayByCoinTests.initCoinDispensers(stationBronze, null);
	}
	
	public static void initBanknoteDispensers(AbstractSelfCheckoutStation hardware, Map<BigDecimal, Integer> banknoteAmounts) throws SimulationException, CashOverloadException {
		for (Entry<BigDecimal, Integer> c : banknoteAmounts.entrySet()) {
			for (int j = 0; j < c.getValue(); j++) {	
				hardware.getBanknoteDispensers().get(c.getKey()).load(new Banknote(Currency.getInstance("CAD"), c.getKey()));;
			}
		}
	}
	
	@Test
	public void processZeroChange() {
		BigDecimal expected = new BigDecimal(0);
		BigDecimal actual1 = this.logicBronze.getCashPaymentController().processCashChange(expected);
		
		assertEquals(expected, actual1);
	}
	
	@Test
	public void testInsertBanknoteSessionNotStarted() throws DisabledException, CashOverloadException {
		setup();
		
		logicBronze.stopSession();
		logicBronze.getProductLogic().updateBalance(new BigDecimal(50));
		stationBronze.getBanknoteInput().receive(fiveNote);
	}
	
	@Test
	public void testGoodBanknoteInvalidState() throws DisabledException, CashOverloadException {
		setup();
		
		logicBronze.startSession();
		logicBronze.getStateLogic().gotoState(States.NORMAL);
		logicBronze.getProductLogic().updateBalance(new BigDecimal(50));
		stationBronze.getBanknoteInput().receive(fiveNote);
	}
	
	@Test
	public void testGoodBanknoteInvalidPaymentMethod() throws DisabledException, CashOverloadException {
		setup();
		
		logicBronze.startSession();
		logicBronze.getStateLogic().gotoState(States.CHECKOUT);
		logicBronze.selectPaymentMethod(PaymentMethods.DEBIT);
		logicBronze.getProductLogic().updateBalance(new BigDecimal(50));
		stationBronze.getBanknoteInput().receive(fiveNote);
	}
	
	@Test
	public void testGoodBanknotePayFullAmount() throws DisabledException, CashOverloadException {
		setup();
		
		logicBronze.startSession();
		logicBronze.getStateLogic().gotoState(States.CHECKOUT);
		logicBronze.selectPaymentMethod(PaymentMethods.CASH);
		logicBronze.getProductLogic().updateBalance(new BigDecimal(0));
		stationBronze.getBanknoteInput().receive(fiveNote);
	}
	
	@Test
	public void testInsertGoodBanknoteToPayNotFull() throws DisabledException, CashOverloadException {
		do {
			setup();
			
			logicBronze.startSession();
			logicBronze.getStateLogic().gotoState(States.CHECKOUT);
			logicBronze.selectPaymentMethod(PaymentMethods.CASH);
		    logicBronze.getProductLogic().updateBalance(new BigDecimal(50));
		    
		    stationBronze.getBanknoteInput().receive(fiveNote);
		    stationBronze.getBanknoteInput().receive(fiveNote);
		}
		while (logicBronze.getProductLogic().getBalanceOwed().intValue() != 40);
		
	    assertEquals(40, logicBronze.getProductLogic().getBalanceOwed().intValue());
	}
	
	@Test
	public void testBanknoteEmitSuccess() throws DisabledException, CashOverloadException {
		do {
			setup();
			
			Map<BigDecimal, Integer> dispensable = new HashMap<>();
			dispensable.put(new BigDecimal(5), 1);
			
			initBanknoteDispensers(stationBronze, dispensable);
			
			logicBronze.startSession();
			logicBronze.getStateLogic().gotoState(States.CHECKOUT);
			logicBronze.selectPaymentMethod(PaymentMethods.CASH);
			logicBronze.getProductLogic().updateBalance(new BigDecimal(5));
			
			stationBronze.getBanknoteInput().receive(tenNote);
			
		}
		while (logicBronze.getProductLogic().getBalanceOwed().intValue() != 0);
			
		assertEquals(0, logicBronze.getProductLogic().getBalanceOwed().intValue());
	}
	
	@Test
	public void testBanknoteEmitFail() throws DisabledException, CashOverloadException {
		do {
			setup();
			
			Map<BigDecimal, Integer> dispensable = new HashMap<>();
			dispensable.put(new BigDecimal(2), 1);
			
			initBanknoteDispensers(stationBronze, dispensable);
			
			logicBronze.startSession();
			logicBronze.getStateLogic().gotoState(States.CHECKOUT);
			logicBronze.selectPaymentMethod(PaymentMethods.CASH);
			logicBronze.getProductLogic().updateBalance(new BigDecimal(5));
			
			// Overload banknote output channel
			for (int i = 0; i < 20; i++) {
				stationBronze.getBanknoteOutput().receive(fiveNote);
			}
			
			stationBronze.getBanknoteInput().receive(tenNote);
		}
		while (logicBronze.getProductLogic().getBalanceOwed().intValue() != 0);
	    
	    assertEquals(0, logicBronze.getProductLogic().getBalanceOwed().intValue());
	}
	
	@Test
	public void testGoodBanknoteNoChangeNeeded() throws SimulationException, CashOverloadException, DisabledException {
		do {
			setup();
			
			logicBronze.startSession();
			logicBronze.getStateLogic().gotoState(States.CHECKOUT);
			logicBronze.selectPaymentMethod(PaymentMethods.CASH);
			logicBronze.getProductLogic().updateBalance(new BigDecimal(5));
			stationBronze.getBanknoteInput().receive(fiveNote);
		}
		while (0 != logicBronze.getProductLogic().getBalanceOwed().intValue());
		
		assertEquals(0, logicBronze.getProductLogic().getBalanceOwed().intValue());
	}
	
	@Test
	public void testGoodBanknoteOverpaymentWithInsufficientChange() throws DisabledException, CashOverloadException {
		do {
			setup();
			
			logicBronze.startSession();
			logicBronze.getStateLogic().gotoState(States.CHECKOUT);
			logicBronze.selectPaymentMethod(PaymentMethods.CASH);
			logicBronze.getProductLogic().updateBalance(new BigDecimal(5));
			stationBronze.getBanknoteInput().receive(tenNote);
		}
		while (0 != logicBronze.getProductLogic().getBalanceOwed().intValue());
		
		assertEquals(0, logicBronze.getProductLogic().getBalanceOwed().intValue());
	}

	
	@Test
	public void badBanknoteTest() {
		this.logicBronze.getCashPaymentController().badBanknote(stationBronze.getBanknoteValidator());
	}
}	
