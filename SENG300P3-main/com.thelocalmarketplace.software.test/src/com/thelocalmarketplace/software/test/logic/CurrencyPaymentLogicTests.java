package com.thelocalmarketplace.software.test.logic;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.logic.CurrencyPaymentLogic;

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
public class CurrencyPaymentLogicTests {

	/**
	 * Reference to an arbitrary CurrencyLogic instance
	 */
	private CurrencyPaymentLogic logic1;
	
	/**
	 * Reference to an arbitrary CurrencyLogic instance
	 */
	private CurrencyPaymentLogic logic2;
	
	/**
	 * List of available coin denominations to test with
	 * {0.05, 0.10, 0.25, 1.00, 2.00}
	 */
	private List<BigDecimal> denominationsCAD;
	
	/**
	 * List of other coin denominations that are "unconventional"
	 * {0.03, 0.07, 0.12, 0.14, 0.20}
	 */
	private BigDecimal[] denominationsOther;
	
	/**
	 * Mapping of available Canadian coins to be tested
	 */
	private Map<BigDecimal, Integer> availableCAD;
	
	/**
	 * Mapping of available "other" coins to be tested (unrealistic)
	 */
	private Map<BigDecimal, Integer> availableOther;
	
	
	private BigDecimal sum(Map<BigDecimal, Integer> denominationCounts) {
		BigDecimal sum = BigDecimal.ZERO;
		
		for (Entry<BigDecimal, Integer> e : denominationCounts.entrySet()) {
			sum = sum.add(e.getKey().multiply(new BigDecimal(e.getValue())));
		}
		
		return sum;
	}
	
	private void initDenominations() {
		this.denominationsCAD = new ArrayList<>();
		
		// CAD
		double[] cd = new double[] {0.05, 0.10, 0.25, 1.00, 2.00};
		
		for (double d : cd) {
			this.denominationsCAD.add(new BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN));
		}
		
		// Other
		this.denominationsOther = new BigDecimal[] {
			new BigDecimal(0.03),
			new BigDecimal(0.07),
			new BigDecimal(0.12),
			new BigDecimal(0.14),
			new BigDecimal(0.20)
		};
	}
	
	private void initAvailableCoins() {
		this.availableCAD = new HashMap<>();
		this.availableOther = new HashMap<>();
		
		// 2 coins of each denomination
		
		// CAD - $6.80 worth
		for (BigDecimal d : this.denominationsCAD) {
			this.availableCAD.put(d, 2);
		}
		
		// Other - ?1.12 worth
		for (BigDecimal d : this.denominationsOther) {
			this.availableOther.put(d, 2);
		}
	}
	
	@Before
	public void init() {
		this.initDenominations();
		this.initAvailableCoins();
		
		this.logic1 = new CurrencyPaymentLogic(this.denominationsCAD);
		this.logic2 = new CurrencyPaymentLogic(this.denominationsOther);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerExceptionIfArgumentNullInConstructorList() {
		new CurrencyPaymentLogic((List<BigDecimal>) null);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerExceptionIfArgumentNullInConstructorArray() {
		new CurrencyPaymentLogic((BigDecimal[]) null);
	}
	
//	@Test(expected = MissedCashException.class)
//	public void shouldThrowMissedCashExceptionOnEdgeInCalculateChangeWithCAD() throws MissedCashException {
//		final BigDecimal overpay = new BigDecimal(6.83);
//		
//		// Should use all available cash plus attempt another nickel to round up $6.85
//		this.logic1.calculateChange(overpay, availableCAD);
//	}
	
//	@Test(expected = MissedCashException.class)
//	public void shouldThrowMissedCashExceptionOnEdgeInCalculateChangeWithOther() throws MissedCashException {
//		final BigDecimal overpay = new BigDecimal(1.136); // ?1.12 + ?0.016 
//		
//		 // Should use all available cash plus attempt another ?0.03 to round up
//		this.logic2.calculateChange(overpay, availableOther);
//	}
	
	@Test
	public void testCalculateChangeUnderRequiredWithCADAndIncludeUnavailableTrue() {
		final BigDecimal overpay = new BigDecimal(6.82);
		
		Map<BigDecimal, Integer> change = this.logic1.calculateChange(overpay, availableCAD, true);
		
		assertEquals(new BigDecimal(6.85).setScale(5, RoundingMode.HALF_DOWN), this.sum(change).setScale(5, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testCalculateChangeUnderRequiredWithCADAndIncludeUnavailableFalse() {
		final BigDecimal overpay = new BigDecimal(6.82);

		Map<BigDecimal, Integer> change = this.logic1.calculateChange(overpay, availableCAD, false);
		
		assertEquals(new BigDecimal(6.80).setScale(5, RoundingMode.HALF_DOWN), this.sum(change).setScale(5, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testCalculateChangeUnderRequiredWithOtherAndIncludeUnavailableTrue() {
		final BigDecimal overpay = new BigDecimal(1.136);
		
		Map<BigDecimal, Integer> change = this.logic2.calculateChange(overpay, availableOther, true);
		
		// Should use all available cash plus another ?0.016 to complete
		assertEquals(new BigDecimal(1.15).setScale(5, RoundingMode.HALF_DOWN), this.sum(change).setScale(5, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testCalculateChangeUnderRequiredWithOtherAndIncludeUnavailableFalse() {
		final BigDecimal overpay = new BigDecimal(1.136);

		Map<BigDecimal, Integer> change = this.logic2.calculateChange(overpay, availableOther, false);
		
		// Should use all available cash plus another ?0.016 to complete but cannot
		assertEquals(new BigDecimal(1.12).setScale(5, RoundingMode.HALF_DOWN), this.sum(change).setScale(5, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testCalculateChangeWithAllAvailableCoinsWithCAD() {
		final BigDecimal overpay = new BigDecimal(6.80);
		
		// Should use all available cash exactly to pay $6.80
		assertEquals(this.logic1.calculateChange(overpay, availableCAD, true), availableCAD);
	}
	
	@Test
	public void testCalculateChangeWithAllAvailableCoinsWithOther() {
		final BigDecimal overpay = new BigDecimal(1.12);
		
		// Should use all available cash exactly to pay ?1.12
		assertEquals(this.logic2.calculateChange(overpay, availableOther, true), availableOther);
	}
	
	@Test
	public void testGetDenominationsAsList() {
		assertEquals(this.logic1.getDenominationsAsList(), this.denominationsCAD);
	}
	
	@Test
	public void testCalculateChangeWithUnavailableDenominations() {
		final BigDecimal overpay = new BigDecimal(6.82);
		
		// Clear denominations
		this.availableCAD.clear();

		this.logic1.calculateChange(overpay, availableCAD, true);
	}
}
