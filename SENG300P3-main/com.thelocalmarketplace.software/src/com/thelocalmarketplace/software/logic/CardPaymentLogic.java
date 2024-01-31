package com.thelocalmarketplace.software.logic;

import com.thelocalmarketplace.hardware.external.CardIssuer;

/**
 * Logic for membership cards and payment via card
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
 * @author Royce Knoepfli		(30172598) */
public class CardPaymentLogic {
	
	/**
	 * Tracks the PIN value that was entered
	 */
	private String enteredPIN;
	
	/**
	 * The card issuer to communicate with for this card logic
	 */
	private CardIssuer bank;
	
	
    /**
     * Configuration to use for this card logic
     * @param bank Is the bank details to use
     */
    public void configureCardIssuer(CardIssuer bank) {
    	this.bank = bank;
    }
    
    /**
     * Records a PIN value to be remembered
     * @param value The PIN value
     */
    public void recordPIN(String value) {
    	this.enteredPIN = value;
    }
    
    /**
     * Retrieves the PIN value that was entered
     * @return The PIN value
     */
    public String getEnteredPIN() {
    	return this.enteredPIN;
    }

    /**
     * Approves a card transaction
     * @param cardNumber
     * @param chargeAmount
     * @return True if transaction successful, false otherwise
     */
    public boolean approveTransaction(String cardNumber, double chargeAmount) throws NullPointerException {
    	if (this.bank == null) {
    		throw new NullPointerException("No reference to bank");
    	}
    	
        Long holdNumber = this.bank.authorizeHold(cardNumber, chargeAmount);
        
        if (holdNumber != -1) {
            return this.bank.postTransaction(cardNumber, holdNumber, chargeAmount);
        }
        
        return false;
    }
}
