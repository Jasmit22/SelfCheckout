package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tdc.banknote.Banknote;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.controllers.pay.cash.BanknoteDispenserController;

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
public class BanknoteDispenserControllerTests {
	private BanknoteDispenserController inputBronze;
	private BanknoteDispenserController inputSilver;
	private BanknoteDispenserController inputGold;

	private SelfCheckoutStationSoftware logicBronze;
	private SelfCheckoutStationSoftware logicSilver;
	private SelfCheckoutStationSoftware logicGold;
	
	private BigDecimal tenDollar;
	private BigDecimal fiveDollar;
	private BigDecimal twoDollar;
	private BigDecimal dollar;
	private BigDecimal quarter;
	private BigDecimal halfDollar;
	private BigDecimal[] coinList;
	private BigDecimal[] noteList;
	private SelfCheckoutStationBronze stationBronze;
	private SelfCheckoutStationSilver stationSilver;
	private SelfCheckoutStationGold stationGold;
	private Currency cad;

	private Banknote twoNote;
	private Banknote fiveNote;
	private Banknote tenNote;
	private Banknote[] noteArray;
	private IBanknoteDispenserStub stub;
	
	@Before
	public void setup() {
		dollar = new BigDecimal(1);
		halfDollar = new BigDecimal(0.50);
		quarter = new BigDecimal(0.25);
		twoDollar = new BigDecimal(2);
		fiveDollar = new BigDecimal(5);
		tenDollar = new BigDecimal(10);
		coinList = new BigDecimal[] {quarter, halfDollar, dollar};
		noteList = new BigDecimal[] {twoDollar, fiveDollar, tenDollar};
		
		cad = Currency.getInstance("CAD"); 
		
		twoNote = new Banknote(cad, twoDollar);
		fiveNote = new Banknote(cad, fiveDollar);
		tenNote = new Banknote(cad, tenDollar);
		noteArray = new Banknote[] {twoNote, fiveNote, tenNote};
		
		stub = new IBanknoteDispenserStub();
		
		SelfCheckoutStationBronze.configureCurrency(cad);
		SelfCheckoutStationSilver.configureCurrency(cad);
		SelfCheckoutStationGold.configureCurrency(cad);
		
		SelfCheckoutStationBronze.configureCoinDenominations(coinList);
		SelfCheckoutStationSilver.configureCoinDenominations(coinList);
		SelfCheckoutStationGold.configureCoinDenominations(coinList);
		
		SelfCheckoutStationBronze.configureBanknoteDenominations(noteList);
		SelfCheckoutStationSilver.configureBanknoteDenominations(noteList);
		SelfCheckoutStationGold.configureBanknoteDenominations(noteList);
		
		SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(10);
		SelfCheckoutStationSilver.configureBanknoteStorageUnitCapacity(10);
		SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(10);
		
		SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(10);
		SelfCheckoutStationSilver.configureCoinStorageUnitCapacity(10);
		SelfCheckoutStationGold.configureCoinStorageUnitCapacity(10);
		
		SelfCheckoutStationBronze.configureCoinTrayCapacity(5);
		SelfCheckoutStationSilver.configureCoinTrayCapacity(5);
		SelfCheckoutStationGold.configureCoinTrayCapacity(5);
		
		SelfCheckoutStationBronze.configureCoinDispenserCapacity(5);
		SelfCheckoutStationSilver.configureCoinDispenserCapacity(5);
		SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
		
		stationBronze = new SelfCheckoutStationBronze();
		stationSilver = new SelfCheckoutStationSilver();
		stationGold = new SelfCheckoutStationGold();
		logicBronze = new SelfCheckoutStationSoftware(stationBronze);
		logicSilver = new SelfCheckoutStationSoftware(stationSilver);
		logicGold = new SelfCheckoutStationSoftware(stationGold);
		inputBronze = new BanknoteDispenserController(logicBronze, twoDollar);
		inputSilver = new BanknoteDispenserController(logicSilver, twoDollar);
		inputGold = new BanknoteDispenserController(logicGold, twoDollar);
	}
	
	@Test (expected = NullPointerException.class)
	public void failedSetup() {
		new BanknoteDispenserController(logicBronze,null);
	}
	
	@Test
	public void getBanknotesTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	@Test
	public void banknoteAddedTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		expected.add(twoNote);
		
		inputBronze.banknoteAdded(stub, twoNote);
		inputSilver.banknoteAdded(stub, twoNote);
		inputGold.banknoteAdded(stub, twoNote);
		
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	
	@Test
	public void banknotesEmptyTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		
		inputBronze.banknoteAdded(stub, twoNote);
		inputSilver.banknoteAdded(stub, twoNote);
		inputGold.banknoteAdded(stub, twoNote);
		
		inputBronze.banknotesEmpty(stub);
		inputSilver.banknotesEmpty(stub);
		inputGold.banknotesEmpty(stub);
		
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	@Test
	public void banknoteRemovedTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		
		inputBronze.banknoteAdded(stub, twoNote);
		inputSilver.banknoteAdded(stub, twoNote);
		inputGold.banknoteAdded(stub, twoNote);
		
		inputBronze.banknoteRemoved(stub, twoNote);
		inputSilver.banknoteRemoved(stub, twoNote);
		inputGold.banknoteRemoved(stub, twoNote);
		
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	@Test
	public void banknotesLoadedTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		expected.add(twoNote);
		expected.add(fiveNote);
		expected.add(tenNote);
		
		inputBronze.banknotesLoaded(stub, noteArray);
		inputSilver.banknotesLoaded(stub, noteArray);
		inputGold.banknotesLoaded(stub, noteArray);
		
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	@Test
	public void banknotesUnloadedTest() {
		List<Banknote> expected = new ArrayList<Banknote>();
		expected.add(twoNote);
		
		inputBronze.banknotesLoaded(stub, noteArray);
		inputSilver.banknotesLoaded(stub, noteArray);
		inputGold.banknotesLoaded(stub, noteArray);
		
		inputBronze.banknotesUnloaded(stub, fiveNote, tenNote);
		inputSilver.banknotesUnloaded(stub, fiveNote, tenNote);
		inputGold.banknotesUnloaded(stub, fiveNote, tenNote);
		
		List<Banknote> actual1 = inputBronze.getAvailableBanknotes();
		List<Banknote> actual2 = inputSilver.getAvailableBanknotes();
		List<Banknote> actual3 = inputGold.getAvailableBanknotes();
		
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
		assertEquals(expected, actual3);
	}
	
	//Note: not sure what exactly to test here. Am I to use the IBanknoteDispenser somehow?
	@Test
	public void moneyFullTest() {
		inputBronze.moneyFull(stub);
		inputSilver.moneyFull(stub);
		inputGold.moneyFull(stub);
	}
}
