package com.thelocalmarketplace.software.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.bag.ReusableBag;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.tdc.CashOverloadException;
import com.tdc.banknote.Banknote;
import com.tdc.banknote.IBanknoteDispenser;
import com.tdc.coin.Coin;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.ISelfCheckoutStation;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/**
 * Class containing static variables that will be used to setup the demonstration
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
public class DemoSetup {

	public static final Currency CURRENCY = Currency.getInstance("CAD");
	
	public static final CardIssuer BANK = new CardIssuer("CIBC", 100);
	
	public static final Map<Card, String> DEMO_BANK_CARDS = new HashMap<>();
	public static final Map<Card, String> DEMO_MEMBERSHIP_CARDS = new HashMap<>();
	public static final Map<Item, String> DEMO_ITEMS = new HashMap<>();
	public static final Map<ReusableBag, String> DEMO_BAGS = new HashMap<>();
	public static final Map<BigDecimal, String> DEMO_COIN_DENOMS = new HashMap<>();
	public static final Map<BigDecimal, String> DEMO_BANKNOTE_DENOMS = new HashMap<>();
	
	public static final BigDecimal[] COIN_DENOMS_CAD = new BigDecimal[] {
			new BigDecimal(0.05),
			new BigDecimal(0.10),
			new BigDecimal(0.25),
			new BigDecimal(1.00),
			new BigDecimal(2.00)
	};
	
	public static final BigDecimal[] BANKNOTE_DENOMS_CAD = new BigDecimal[] {
			new BigDecimal(5),
			new BigDecimal(10),
			new BigDecimal(20),
			new BigDecimal(50),
			new BigDecimal(100)
	};
	
	
	public static List<String> productsToAdd;
	
	public static List<Mass> massesOfProducts;
	
	public static List<Double> doubleMasses;
	
	public static List<Long> pricesOfProducts;
	
	public static List<String> pluLookupCodes;
	
	private static List<Numeral> numerals;
	
	
	public static void setup() {
		
		// Add 3 bags
		for (byte i = 0; i < 3; i++) {			
			DEMO_BAGS.put(new ReusableBag(), "Bag " + (i + 1));
		}
		
		Card card1 = new Card("credit", "23894932", "Mr. Customer", "083", "1234", false, true);
		Card card2 = new Card("credit", "72894332", "Mr. Customer", "233", "1234", true, true);
		Card card3 = new Card("debit", "65546766", "Mr. Customer", "645", "1234", false, true);
		Card card4 = new Card("debit", "76867324", "Mr. Customer", "432", "1234", true, true);
		
		// Add bank cards used for tap/swipe
		DEMO_BANK_CARDS.put(card1, "Credit Card (No Tap)");
		DEMO_BANK_CARDS.put(card2, "Credit Card (With Tap)");
		DEMO_BANK_CARDS.put(card3, "Debit Card (No Tap)");
		DEMO_BANK_CARDS.put(card4, "Debit Card (With Tap)");

		final Calendar expiry = Calendar.getInstance();
	    expiry.set(2025, Calendar.JANUARY, 24);
		
		BANK.addCardData("23894932", "Mr. Customer", expiry, "083", 100);
		BANK.addCardData("72894332", "Mr. Customer", expiry, "233", 100);
		BANK.addCardData("65546766", "Mr. Customer", expiry, "645", 100);
		BANK.addCardData("76867324", "Mr. Customer", expiry, "432", 100);
		
		// Add membership card(s)
		DEMO_MEMBERSHIP_CARDS.put(new Card("membership", "23784", "Mr. Customer", "000", "0000", false, false), "Mr. Customer's Invalid Membership");
		DEMO_MEMBERSHIP_CARDS.put(new Card("membership", "23788", "Mr. Customer", "000", "0000", false, false), "Mr. Customer's Valid Membership");
		
		// Add coin denominations
		for (BigDecimal d : COIN_DENOMS_CAD) {
			DEMO_COIN_DENOMS.put(d, "$" + d.setScale(2, RoundingMode.HALF_DOWN).toString() + " Coin");
		}
		
		// Add banknote denominations
		for (BigDecimal d : BANKNOTE_DENOMS_CAD) {
			DEMO_BANKNOTE_DENOMS.put(d, "$" + d.setScale(2, RoundingMode.HALF_DOWN).toString() + " Banknote");
		}
		
		// ==== ITEMS AND PRODUCTS ====
		intializeDatabase();		
	}
	
	/**
	 * The following function was taken from https://www.baeldung.com/java-generating-random-numbers-in-range
	 * @return random int between min and max
	 */
	
	public static int getRandomInt(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);

	}

	/**
	 *Initializes a random number of products in the DATABASE that will be used in the demo.
	 *Half of these are barcoded and the other half is PLU.
	 *Each Product has random components such as weight, cost, barcode, number in inventory etc
	 */
	public static void intializeDatabase() {
		
		//there will be one of each item in the database
		productsToAdd = Arrays.asList("Apple", "Asparagus", "Banana", "Kale", "Orange", "Potatoe","Strawberries","Red Onions","Lettucce","Mushroom", "Garlic", "Beet", "Bok Choy", "Brussel Sprouts", "Chocolate Milk", "Cheese");
		massesOfProducts = Arrays.asList(new Mass(103.0), new Mass(230.0), new Mass(50.0), new Mass(73.0), new Mass(100.0), new Mass(421.0), new Mass(253.0), new Mass(670.0), new Mass(420.0), new Mass(350.0), new Mass(82.0), new Mass(99.0), new Mass(153.0), new Mass(79.0), new Mass(900.0), new Mass(182.0));
		doubleMasses = Arrays.asList(103.0, 230.0, 50.0, 73.0, 10.0, 421.0, 253.0, 67.0, 420.0, 350.0, 82.0, 99.0, 153.0, 79.0, 900.0, 182.0);
		pricesOfProducts = Arrays.asList(2L,1L,5L,6L,9L,8L,6L,5L,10L,50L,43L,32L,14L,16L,14L,3L);// the first half of the products will be plu
		pluLookupCodes = Arrays.asList("0001","0002","0003","0004","0005","0006","0007","0008");
		numerals = Arrays.asList(Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five, Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine);
		
		for (int i = 0; i < productsToAdd.size() - 1; i++) {
			if (i < 8) {// PLU products
				PriceLookUpCode PLUlookup = new PriceLookUpCode(pluLookupCodes.get(i));
				PLUCodedProduct p = new PLUCodedProduct(PLUlookup, productsToAdd.get(i), pricesOfProducts.get(i));
				
				ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUlookup,p);

				ProductDatabases.INVENTORY.put(p, 10);

				for (int cnt = 0; cnt < 2; cnt++) {
					DEMO_ITEMS.put(new PLUCodedItem(PLUlookup, massesOfProducts.get(i)), productsToAdd.get(i));
				}
				
			}else {// Barcoded products
				int randomBarcodeLength = getRandomInt(7, 12);
				
				Numeral[] barcode_numeral = new Numeral[randomBarcodeLength];
				
				for (int num = 0; num < randomBarcodeLength; num ++) {
					int randomNumeral = getRandomInt(0,9);
					barcode_numeral[num] = numerals.get(randomNumeral);
				}
				
				Barcode bc = new Barcode(barcode_numeral);
				BarcodedProduct bp = new BarcodedProduct(bc,productsToAdd.get(i), pricesOfProducts.get(i),doubleMasses.get(i).doubleValue());
				//System.out.println(doubleMasses.get(i)+ " "+ massesOfProducts.get(i));
				ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bc, bp);
				ProductDatabases.INVENTORY.put(bp, 10);
				for (int cnt = 0; cnt < 2; cnt++) {
					DEMO_ITEMS.put(new BarcodedItem(bc, massesOfProducts.get(i)), productsToAdd.get(i));
				}
			}
			
		}
	}
	
	/**
	 * Preloads dispensable items, making sure paper, ink, coins, and banknotes are full
	 * 
	 * @param station The self-checkout station to preload
	 */
	public static void preloadStationDispensables(ISelfCheckoutStation station) {
		
		// Top off paper
		try {			
			station.getPrinter().addPaper(100);
		}
		catch (Exception e) {}
		
		// Top off ink
		try {			
			station.getPrinter().addInk(1 << 20);
		}
		catch (Exception e) {}
		
		// Top off coins
		for (Entry<BigDecimal, ICoinDispenser> e : station.getCoinDispensers().entrySet()) {
			try {
				ICoinDispenser c = e.getValue();
				
				for (int i = 0; i < c.getCapacity(); i++) {					
					c.load(new Coin(CURRENCY, e.getKey()));
				}
			} catch (CashOverloadException e1) {}
		}
		
		// Top off banknotes
		for (Entry<BigDecimal, IBanknoteDispenser> e : station.getBanknoteDispensers().entrySet()) {
			try {
				IBanknoteDispenser d = e.getValue();
				
				for (int i = 0; i < d.getCapacity(); i++) {					
					d.load(new Banknote(CURRENCY, e.getKey()));
				}
			} catch (CashOverloadException e1) {}
		}
	}
}