package com.thelocalmarketplace.software.test.logic;

import static org.junit.Assert.assertTrue;

import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;


import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.Banknote;
import com.tdc.coin.Coin;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

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
 * @author Royce Knoepfli		(30172598)
 */
public class CentralStationLogicTests {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware session;
	Currency currency;

	private Coin fiveCentCoin;
	private Coin twentyFiveCentCoin;
	
	private Banknote fiveDollarBill;
	private Banknote tenDollarBill;
	private Banknote twentyDollarBill;
	Banknote[] banknoteList;
	
	private BigDecimal[] denominationsCoinCAD = new BigDecimal[] {
			new BigDecimal(0.05),
			new BigDecimal(0.25),
			new BigDecimal(1.00)
	};
	private BigDecimal[] denominationsBanknotesCAD = new BigDecimal[] {
			new BigDecimal(5.00),
			new BigDecimal(10.00),
			new BigDecimal(20.00)
	};
	@Before
	public void init() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		
		this.currency = Currency.getInstance("CAD");
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		AbstractSelfCheckoutStation.configureCoinDenominations(denominationsCoinCAD);
		AbstractSelfCheckoutStation.configureCoinDispenserCapacity(10);
		AbstractSelfCheckoutStation.configureCoinStorageUnitCapacity(10);
		AbstractSelfCheckoutStation.configureCoinTrayCapacity(10);
		AbstractSelfCheckoutStation.configureCurrency(currency);
		AbstractSelfCheckoutStation.configureBanknoteDenominations(denominationsBanknotesCAD);
		AbstractSelfCheckoutStation.configureBanknoteStorageUnitCapacity(10);
		
		station = new SelfCheckoutStationBronze();
		
		currency = Currency.getInstance("CAD");
		fiveCentCoin = new Coin(currency,new BigDecimal(0.05));
		twentyFiveCentCoin = new Coin(currency,new BigDecimal(0.25));
		
		fiveDollarBill = new Banknote(currency, new BigDecimal(5.0));
		tenDollarBill = new Banknote(currency, new BigDecimal(10.0));
		twentyDollarBill = new Banknote(currency, new BigDecimal(20.0));
		
		
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		session = new SelfCheckoutStationSoftware(station);
		session.setEnabled(true);
	}
	
	@Test public void startSessionStateNormalTest() {
		session.startSession();
		assertTrue("station did not start in noraml state",session.getStateLogic().inState(States.NORMAL));
	}
	@Test public void startSessionPaymentMethodTest() {
		session.selectPaymentMethod(PaymentMethods.CASH);
		session.startSession();
		assertTrue("station did not set payment methods correctly", session.getSelectedPaymentMethod()==PaymentMethods.CASH);
	}@Test public void hasStartedTest() {
		session.startSession();
		
		assertTrue("session did not start", session.isSessionActive());
	}@Test public void hasStoppedTest() {
		session.startSession();
		session.stopSession();
		assertTrue("session did not stop", !session.isSessionActive());
	}@Test(expected = SimulationException.class) public void canStartWhenStarted() {
		session.startSession();
		session.startSession();
		
	}@Test(expected = NullPointerException.class) public void testNullHardware() {
		session = new SelfCheckoutStationSoftware(null);
	}@Test public void getAllCoinsInDispenserTest() throws CashOverloadException, DisabledException {
		session.startSession();
		session.getHardware().getCoinDispensers().get(new BigDecimal(0.05)).receive(fiveCentCoin);
		session.getHardware().getCoinDispensers().get(new BigDecimal(0.05)).receive(fiveCentCoin);
		session.getHardware().getCoinDispensers().get(new BigDecimal(0.25)).receive(twentyFiveCentCoin);
		Map<BigDecimal, Integer> result = session.getAvailableCoinsInDispensers();
		assertTrue("did not get all coins in dispenser", result.get(new BigDecimal(0.05)) == 2 && result.get(new BigDecimal(0.25))==1);
	}@Test public void  getAllBanknotesInDispenserTest() throws CashOverloadException, DisabledException {
		session.startSession();
		banknoteList = new Banknote[]{tenDollarBill,tenDollarBill};
		
		Banknote[] fiveList = new Banknote[] {fiveDollarBill};
		Banknote[] twentyList = new Banknote[] {twentyDollarBill, twentyDollarBill, twentyDollarBill};
		
		session.getHardware().getBanknoteDispensers().get(new BigDecimal(10.00)).load(banknoteList);
		session.getHardware().getBanknoteDispensers().get(new BigDecimal(5.00)).load(fiveList);
		session.getHardware().getBanknoteDispensers().get(new BigDecimal(20.00)).load(twentyList);
		Map<BigDecimal, Integer> result = session.getAvailableBanknotesInDispensers();
		assertTrue("did not get all coins in dispenser", result.get(new BigDecimal(5.0)) == 1 && result.get(new BigDecimal(10.0))==2 && result.get(new BigDecimal(20.0))==3);
	}
	
}
