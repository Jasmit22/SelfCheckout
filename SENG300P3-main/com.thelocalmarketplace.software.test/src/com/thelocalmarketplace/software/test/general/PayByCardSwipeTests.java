package com.thelocalmarketplace.software.test.general;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.general.Utilities;

import org.junit.Before;
import org.junit.Test;
import powerutility.PowerGrid;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

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
public class PayByCardSwipeTests {

    SelfCheckoutStationGold station;
    SelfCheckoutStationSoftware session;

    CardIssuer bank;

    Card card;
    
    
    @Before
    public void setup() {

        PowerGrid.engageUninterruptiblePowerSource();
	    PowerGrid.instance().forcePowerRestore();

        AbstractSelfCheckoutStation.resetConfigurationToDefaults();

        station = new SelfCheckoutStationGold();
        station.plugIn(PowerGrid.instance());
        station.turnOn();


        session = new SelfCheckoutStationSoftware(station);
        session.setEnabled(true);
        session.startSession();

        //set up bank details
        CardIssuer bank = new CardIssuer("Scotia Bank",3);
        session.getCardPaymentLogic().configureCardIssuer(bank);
        this.card = new Card("DEBIT","123456789","John","329", "123", true, true);
        Calendar expiry = Calendar.getInstance();
        expiry.set(2025,Calendar.JANUARY,24);
        bank.addCardData("123456789", "John",expiry,"329",32.00);

        this.session.selectPaymentMethod(PaymentMethods.DEBIT);
    }

    @Test
    public void testInvalidState() throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.getHardware().getCardReader().enable();
		session.getHardware().getCardReader().swipe(this.card);
		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
    }

    @Test
    public void testSessionNotStartedSwipe() throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.stopSession();
		session.getHardware().getCardReader().enable();
		session.getStateLogic().gotoState(States.CHECKOUT);
		session.getHardware().getCardReader().swipe(this.card);
		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
    }

    @Test
    public void testValidTransaction() throws IOException {
    	// loop in case there is a chip malfunction
    	// count iterations in case there IS a problem with the test, it should not create an infinite loop. 
    	int i = 0;
    	do {
    		setup();
    		try {    			
    			session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
    			session.getHardware().getCardReader().enable();
    			session.getStateLogic().gotoState(States.CHECKOUT);
    			session.getHardware().getCardReader().swipe(this.card);
    		}
    		catch (Exception e) {}
    	}
    	while (session.getProductLogic().getBalanceOwed().intValue() != 0 && i++ < 10);
    	
        assertEquals(BigDecimal.valueOf(0.0), session.getProductLogic().getBalanceOwed());
    }

    @Test
    public void testDeclinedTransaction() throws IOException {
    	// eventually there will be a chip corruption error. 
    	do {
    		setup();
    		try {
    			session.getProductLogic().updateBalance(BigDecimal.valueOf(50.00));
    			session.getHardware().getCardReader().enable();
    			session.getStateLogic().gotoState(States.CHECKOUT);
    			session.getHardware().getCardReader().swipe(this.card);    			
    		}
    		catch (Exception e) {}
    	}
    	while (session.getProductLogic().getBalanceOwed().intValue() != 50);
    	
        assertEquals(BigDecimal.valueOf(50.0), session.getProductLogic().getBalanceOwed());
    }
    
    @Test
    public void testWrongSwipeMethodSelected() throws IOException {
		session.selectPaymentMethod(PaymentMethods.CREDIT);
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.getHardware().getCardReader().enable();
		session.getStateLogic().gotoState(States.CHECKOUT);
		session.getHardware().getCardReader().swipe(this.card);
		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
    }

    @Test
    public void testStationBlockedSwipe()throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.getStateLogic().gotoState(States.BLOCKED);
		session.getHardware().getCardReader().enable();
		session.getHardware().getCardReader().swipe(this.card);
		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
    }
    
    @Test
    public void testGetCardPaymentTypeDebit() {
    	Card c = new Card("deBiT","123456789","John","329", "123", true, true);
    	
    	assertEquals(PaymentMethods.DEBIT.name(), Utilities.getCardType(c.kind).name());
    }
    
    @Test
    public void testGetCardPaymentTypeCredit() {
    	Card c = new Card("CreDIt","123456789","John","329", "123", true, true);
    	
    	assertEquals(PaymentMethods.CREDIT.name(), Utilities.getCardType(c.kind).name());
    }
    
    @Test
    public void testGetCardPaymentTypeNone() {
    	Card c = new Card("fdsgds","123456789","John","329", "123", true, true);
    	
    	assertEquals(PaymentMethods.NONE.name(), Utilities.getCardType(c.kind).name());
    }
}
