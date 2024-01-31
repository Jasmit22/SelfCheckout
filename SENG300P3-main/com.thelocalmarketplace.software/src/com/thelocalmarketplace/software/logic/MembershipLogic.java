package com.thelocalmarketplace.software.logic;

import java.util.HashMap;
import java.util.Map;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.software.general.Membership;

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
 * @author Chantal del Carmen	(30129615)
 * @author Dana Al Bastrami		(30170494)
 * @author Maria Munoz			(30175339)
 * @author Ernest Shukla		(30156303)
 * @author Hillary Nguyen		(30161137)
 * @author Robin Bowering		(30123373)
 * @author Anne Lumumba			(30171346)
 * @author Jasmit Saroya		(30170401)
 * @author Fion Lei				(30134327)
 * @author Royce Knoepfli		(30172598) */
public class MembershipLogic {
	
	/**
	 * Local database of memberships
	 * Ideally, in a future iteration, this will be a store wide database or larger
	 */
	private Map<Barcode, Membership> membershipDatabase;
	
	private Map<Character, Numeral> charNumeralMap;
	
	private Membership selectedMembership = null;
	
	
	/**
	 * Base constructor
	 */
	public MembershipLogic() {
		membershipDatabase = new HashMap<Barcode, Membership>();
		
		this.initCharNumeralMap();
	}
	
	/**
	 * Creates a mapping of character/numeral pairs 0-9
	 */
	private void initCharNumeralMap() {
		charNumeralMap = new HashMap<Character, Numeral>();
		
		// Add key, value pairs to map
		charNumeralMap.put(Character.valueOf('0'), Numeral.zero);
		charNumeralMap.put(Character.valueOf('1'), Numeral.one);
		charNumeralMap.put(Character.valueOf('2'), Numeral.two);
		charNumeralMap.put(Character.valueOf('3'), Numeral.three);
		charNumeralMap.put(Character.valueOf('4'), Numeral.four);
		charNumeralMap.put(Character.valueOf('5'), Numeral.five);
		charNumeralMap.put(Character.valueOf('6'), Numeral.six);
		charNumeralMap.put(Character.valueOf('7'), Numeral.seven);
		charNumeralMap.put(Character.valueOf('8'), Numeral.eight);
		charNumeralMap.put(Character.valueOf('9'), Numeral.nine);		
	}
	
	/**
	 * Unselects membership so station is ready for new customer
	 */
	public void clearSelectedMembership() {
		this.selectedMembership = null;
	}

	/**
	 * Adds given membership to the database by converting the membershipNumber from
	 * String to Barcode, then adding this barcode with associated Membership to map
	 * 
	 * @param membershipNumber of the membership card
	 */
	public void registerMembershipToLocalDatabase(String membershipNumber) {
		Barcode membershipBarcode = convertStringToBarcode(membershipNumber);
		Membership membership = new Membership(membershipNumber);
		
		membershipDatabase.put(membershipBarcode, membership);
	}
	
	/**
	 * Checks a membershipNumber if it is a valid membership number against database
	 * 
	 * @param membershipNumber String
	 * @return isMembershipNumberValid boolean - true if valid; false otherwise
	 */
	public boolean checkIfMembershipNumberValid(String membershipNumber) {
		boolean isMembershipNumberValid = false;
		
		// If membershipNumber empty, throw null pointer exception
		if (membershipNumber == "" || membershipNumber == null) {
			throw new NullPointerException("Membership number should not be empty");
		}
		
		// Convert membershipNumber from String to Barcode
		Barcode membershipNumberBarcode = convertStringToBarcode(membershipNumber);
		
		// If database contains the barcode number, set isMembershipNumberValid to true
		if (membershipDatabase.containsKey(membershipNumberBarcode)) {
			isMembershipNumberValid = true;
		}		
		
		// Return isMembershipNumberValid:
		// - True, if valid
		// - False, if not in database
		return isMembershipNumberValid;
	}

	/**
	 * Sets the selectedMembership to the membershipNumber that is passed in by first
	 * converting the membership number to a barcode, and then calling findMembership() 
	 * to get the membership associated with the membership number
	 * 
	 * @param membershipNumber
	 * @throws NullPointerException if membershipNumber is empty or null
	 */
	public void setSelectedMembership(String membershipNumber) {
		
		// If membershipNumber empty, throw null pointer exception
		if(membershipNumber == "" || membershipNumber == null) {
			throw new NullPointerException("Membership number should not be empty");
		}
		
		// Convert membershipNumber from String to Barcode
		Barcode membershipNumberBarcode = convertStringToBarcode(membershipNumber);
		
		// Set selected membership to the membership found to be associated with the barcode 
		// in the database
		selectedMembership = findMembership(membershipNumberBarcode);
	}
	
	/**
	 * Getter for the selectedMembership
	 * 
	 * @return selectedMembership
	 */
	public Membership getSelectedMembership() {
		return selectedMembership;
	}
	
	/**
	 * Takes a Barcode membership number and checks the membership database for that barcode number
	 * and gives back the membership it is associated with
	 * 
	 * @param membershipNumberBarcode Barcode
	 * @return foundMembership from the database
	 */
	public Membership findMembership(Barcode membershipNumberBarcode) {
		Membership foundMembership;
		
		// If database contains the barcode number, set selected membership to that membership
		if (membershipDatabase.containsKey(membershipNumberBarcode)) {
			foundMembership = membershipDatabase.get(membershipNumberBarcode);
		} else { 
			// Else database does not contain barcode, throw null pointer exception
			throw new NullPointerException("Membership not found in database");
		}
		
		return foundMembership;		
	}

	/**
	 * Takes a membershipNumber String and converts it to a Barcode object and returns it
	 * 
	 * @param membershipNumber
	 * @return barcodeNumber
	 */
	public Barcode convertStringToBarcode(String membershipNumber) {
		
		// Create an array to put membership number into
		Numeral[] membershipNumberArray = new Numeral[membershipNumber.length()];
		
		// Add membership numerals to array
		for(int i = 0; i < membershipNumber.length(); i++) {
			membershipNumberArray[i] = charNumeralMap.get(membershipNumber.charAt(i));
		}
		
		// Create barcode number using the membershipNumberArray and return it
		Barcode barcodeNumber = new Barcode(membershipNumberArray);
		
		return barcodeNumber;
	}
}