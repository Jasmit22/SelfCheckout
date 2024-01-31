package com.thelocalmarketplace.software.general;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.thelocalmarketplace.software.general.Enumerations.CardTypes;

/**
 * Class for utilities
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
public class Utilities {

	/**
	 * Set to true to have dark theme on for the GUI
	 */
	private static boolean ENABLE_DARK_THEME = true;
	
	/**
	 * Used for incrementing/decrementing a counting map
	 * @param <T> The type that is being counted
	 * @param map Is a reference to a map object of type: Map<T, Integer>
	 * @param item Is the item to increment/decrement
	 * @param amount Is the amount to increment/decrement by
	 */
	public static <T> void modifyCountMapping(Map<T, Integer> map, T item, int amount) {
		if (map.containsKey(item)) {
			map.put(item, map.get(item) + amount);
		}
		else {
			map.put(item, amount);
		}
		
		if (map.get(item) <= 0) {
			map.remove(item);
		}
	}
	
	/**
	 * Swaps an entry between two maps
	 * @param <T> Type of key
	 * @param <U> Type of value associated with key
	 * @param key Key associated with entry getting swapped
	 * @param takeFrom The source map the entry is taken from
	 * @param putIn Destination map for the entry
	 */
	public static <T, U> void swapMapEntry(T key, Map<T, U> takeFrom, Map<T, U> putIn) {
		U value = takeFrom.get(key);

		putIn.put(key, value);
		takeFrom.remove(key);
	}
	
	/**
	 * Sets colour scheme for the GUI
	 * 
	 * @param component The GUI component getting a colour set
	 */
	public static void setComponentColour(Component component) {
		if (ENABLE_DARK_THEME) {			
			component.setBackground(Color.BLACK);
			component.setForeground(Color.WHITE);
			
			if (component instanceof Container) {
				for (Component child : ((Container) component).getComponents()) {
					setComponentColour(child);
				}
			}
		}
    }
	
	/**
     * Gets the payment type of a card
     * @param type The string that represents the card type
     * @return Either DEBIT or CREDIT
     */
    public static CardTypes getCardType(String type) {
    	String t = type.toLowerCase();
    	
    	switch (t) {
	    	case "debit":
	    		return CardTypes.DEBIT;
	    	case "credit":
	    		return CardTypes.CREDIT;
	    	case "membership":
	    		return CardTypes.MEMBERSHIP;
    	}
    	
    	return CardTypes.NONE;
    }
}