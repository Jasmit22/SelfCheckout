package com.thelocalmarketplace.software.general;

/**
 * Class of enumerations
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

public class Enumerations {

	/**
	 * Represents a prediction status
	 */
	public static enum Status {
		EMPTY,
		LOW,
		STABLE,
		FULL
	}
	
	/**
	 * Enumeration of possible states
	 */
	public static enum States {
		NORMAL, // Default
		BLOCKED,
		ADDBAGS,
		ADDITEM,
		ADDMEMBERSHIP,
		CHECKOUT,
		SUSPENDED,
		BUYBAGS
	}
	
	/**
	 * Enumeration of card types
	 */
	public static enum CardTypes {
		DEBIT,
		CREDIT,
		MEMBERSHIP,
		NONE
	}
	
	/**
	 * Enumeration of possible payment methods
	 */
	public static enum PaymentMethods {
		NONE, // Default
		CREDIT,
		DEBIT,
		CASH
	}
}
