package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.general.Utilities;
import com.thelocalmarketplace.software.general.Enumerations.CardTypes;
 
/**
 * Tests for static utility methods
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
public class UtilitiesTests {
	
	/**
	 * Map that will track the count of any object
	 */
	private Map<Object, Integer> testMap;
	
	private Map<Object, Object> takeFrom;
	private Map<Object, Object> putIn;
	
	private Object o1;
	private Object o2;
	
	
	@Before
	public void init() {
		this.testMap = new HashMap<>();
		
		this.takeFrom = new HashMap<>();
		this.putIn = new HashMap<>();
		
		// Initialize some arbitrary objects
		this.o1 = "str1";
		this.o2 = "str2";
		
		// Initialize their counts arbitrarily in the test map
		this.testMap.put(o1, 1);
		this.testMap.put(o2, 3);
		
		this.takeFrom.put(o1, o2);
	}
	
	@Test
	public void testAddAlreadyExistingInModifyCountMapping() {
		Utilities.modifyCountMapping(this.testMap, o1, 2);
		
		assertEquals(3, testMap.get(o1).intValue());
	}
	
	@Test
	public void testAddNotExistingInModifyCountMapping() {
		Object o = new Object();
		
		Utilities.modifyCountMapping(this.testMap, o, 2);
		
		assertEquals(2, testMap.get(o).intValue());
	}
	
	@Test
	public void testRemoveNotExistingInModifyCountMapping() {
		Object o = new Object();
		
		Utilities.modifyCountMapping(this.testMap, o, -3);
		
		assertFalse(this.testMap.containsKey(o));
	}
	
	@Test
	public void testRemoveAlreadyExistingInModifyCountMapping() {
		Utilities.modifyCountMapping(this.testMap, o2, -1);
		
		assertEquals(2, testMap.get(o2).intValue());
	}
	
	@Test
	public void testRemoveCompletelyAlreadyExistingInModifyCountMapping() {
		Utilities.modifyCountMapping(this.testMap, o1, -1);
		
		assertFalse(this.testMap.containsKey(o1));
	}
	
	@Test
	public void testSwapMapEntry() {
		Utilities.swapMapEntry(o1, takeFrom, putIn);
		
		assertTrue(putIn.containsKey(o1));
	}
	
	@Test
	public void testSetComponentColour() {
		JPanel p = new JPanel();
		JPanel c = new JPanel();
		
		p.add(c);
		
		Utilities.setComponentColour(p);
		
		assertEquals(c.getBackground(), Color.BLACK);
	}
	
	@Test
	public void testGetCardTypeDebit() {
		assertEquals(Utilities.getCardType("DeBIt"), CardTypes.DEBIT);
	}
	
	@Test
	public void testGetCardTypeCredit() {
		assertEquals(Utilities.getCardType("crEdIT"), CardTypes.CREDIT);
	}
	
	@Test
	public void testGetCardTypeMembership() {
		assertEquals(Utilities.getCardType("mEmbERship"), CardTypes.MEMBERSHIP);
	}
	
	@Test
	public void testGetCardTypeNone() {
		assertEquals(Utilities.getCardType("sdfsdf"), CardTypes.NONE);
	}
}
