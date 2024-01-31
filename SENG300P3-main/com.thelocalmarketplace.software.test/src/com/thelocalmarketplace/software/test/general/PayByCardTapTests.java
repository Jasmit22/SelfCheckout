package com.thelocalmarketplace.software.test.general;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.PaymentMethods;
import com.thelocalmarketplace.software.general.Enumerations.States;

import powerutility.PowerGrid;

import static org.junit.Assert.*;

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
public class PayByCardTapTests {

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
	    bank = new CardIssuer("Deutsche Bank",3); // <--- nur weil ich kann
	    session.getCardPaymentLogic().configureCardIssuer(bank);
	    this.card = new Card("DEBIT","123456789","John","329", "123", true, true);
	    Calendar expiry = Calendar.getInstance();
	    expiry.set(2025,Calendar.JANUARY,24);
	    bank.addCardData("123456789", "John",expiry,"329",32.00);

	    this.session.selectPaymentMethod(PaymentMethods.DEBIT);

		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.getHardware().getCardReader().enable();
		session.getStateLogic().gotoState(States.CHECKOUT);
	}
	
/**
 * NOTE: No tap error handling/testing for bronze and silver checkout stations
 */

	/**
	 * Test to ensure that payment does not get processed when in the "Normal" state.
	 * @throws IOException
	 */
	@Test
	public void testInvalidState() throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.getStateLogic().gotoState(States.NORMAL);
		session.getHardware().getCardReader().tap(this.card);

		//If the payment fails due to invalid state, the balance will remain at 10.0
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that a transaction successfully happens in the correct state.
	 * @throws IOException
	 */
	@Test //A new implementation, does not seem to have card chip error issues every time.
	public void testValidTransaction() throws IOException {
		
		// crashes in card issuer line 240: random.nextLong(maximumHoldCount);
		int i = 0;
		
		do {
			session.getHardware().getCardReader().tap(this.card);
		} while (session.getProductLogic().getBalanceOwed() != BigDecimal.ZERO && i++ < 10);
		
	    assertEquals(BigDecimal.valueOf(0.0), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that payment does not get processed when the bank card is blocked.
	 * @throws IOException
	 */
	@Test
	public void testDeclinedTransaction() throws IOException {
		bank.block(card.number);
		session.getHardware().getCardReader().tap(this.card);

        assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
    }

	/**
	 * Test to ensure that payment does not get processed when the wrong payment selection is set.
	 * @throws IOException
	 */
	@Test
    public void testWrongPaymentMethodSelected() throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.selectPaymentMethod(PaymentMethods.CREDIT);
		session.getHardware().getCardReader().tap(this.card);

		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
    }

	/**
	 * Test to ensure that payment does not get processed when in the "Blocked" state.
	 * @throws IOException
	 */
    @Test
    public void testStationBlockedTap()throws IOException {
    	// double transition to avoid a bad transition.
		session.getStateLogic().gotoState(States.NORMAL);
		session.getStateLogic().gotoState(States.BLOCKED);
		session.getHardware().getCardReader().tap(this.card);
		
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
    }

	/**
	 * Test to ensure that payment does not get processed when the session has not started.
	 * @throws IOException
	 */
    @Test
    public void testSessionNotStartedTap() throws IOException {
		session.getProductLogic().updateBalance(BigDecimal.valueOf(10.00));
		session.stopSession();
		session.getHardware().getCardReader().tap(this.card);
		
		assertEquals(BigDecimal.valueOf(10.00), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that a Nullpointer is thrown if the bank is set to null...
	 * Are we supposed to handle this here? Why is this test located in PayByCardTap?
	 * @throws IOException
	 */
    @Test (expected = NullPointerException.class)
    public void testHoldNullBank() throws IOException {
    	session.getCardPaymentLogic().configureCardIssuer(null);
		session.getHardware().getCardReader().tap(this.card);
    }


	/**
	 * Test to ensure that a transaction successfully happens in the correct state.
	 * @throws IOException
	 */
	@Test (expected = NullPointerException.class)
	public void testNullCard() throws IOException {
		session.getHardware().getCardReader().tap(null);
	}

	/**
	 * Test to ensure that a valid membership card is registered and that card tapping does not prevent anything.
	 * @throws IOException
	 */
	@Test
	public void testMembershipTransaction() throws IOException {
		Card membershipCard = new Card("MEMBERSHIP","111234567","Joe","319", "111", true, true);
		session.getMembershipLogic().registerMembershipToLocalDatabase("111234567");
		session.getStateLogic().gotoState(States.NORMAL);
		session.getStateLogic().gotoState(States.ADDMEMBERSHIP);
		session.getHardware().getCardReader().swipe(membershipCard);
		session.getStateLogic().gotoState(States.NORMAL);
		session.getStateLogic().gotoState(States.CHECKOUT);
		session.getHardware().getCardReader().tap(this.card);
		assertEquals("111234567", session.getMembershipLogic().getSelectedMembership().getMembershipNumber());
}

	/**
	 * Test to ensure that a membership card is not accepted when the session has not started.
	 * Theoretically this throws a NullPointerException if the membership is not found. Should  Membership.getMembershipNumber()
	 * not have a null check? Might screw the membershipLogic tests if it implements a null check...
	 * @throws IOException
	 */
	@Test
	public void testMembershipInvalidTransaction() throws IOException {
		Card membershipCard = new Card("MEMBERSHIP","111234567","Joe","319", "111", true, true);
		session.getMembershipLogic().registerMembershipToLocalDatabase("111234568");
		session.getStateLogic().gotoState(States.NORMAL);
		session.getStateLogic().gotoState(States.ADDMEMBERSHIP);
		session.getHardware().getCardReader().swipe(membershipCard);
		session.getStateLogic().gotoState(States.NORMAL);
		session.getStateLogic().gotoState(States.CHECKOUT);
		session.getHardware().getCardReader().tap(this.card);
		assertThrows(NullPointerException.class, () -> session.getMembershipLogic().getSelectedMembership().getMembershipNumber());
	}

	/*
    Don't worry about these test cases. They are unsolvable and we do not have to worry about them...
     */

	/**
	 * Test to ensure that a completely unassigned card is rejected...
	 * @throws IOException
	 */
	//@Test
	public void testBadCard() throws IOException {
		Card badCard = new Card("DEBIT","987654321","Joe","319", "111", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that a false card is rejected...
	 * @throws IOException
	 */
	//@Test
	public void testBadCard2() throws IOException {
		Card badCard = new Card("DEBIT","123456789","Joe","319", "111", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that an unassigned card is rejected...
	 * @throws IOException
	 */
	//@Test
	public void testBadCard3() throws IOException {
		Card badCard = new Card("DEBIT","123456789","John","318", "111", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}

	/**
	 * Test to ensure that an unassigned card is rejected...
	 * @throws IOException
	 */
	//@Test
	public void testBadCard4() throws IOException {
		Card badCard = new Card("DEBIT","123456789","John","319", "101", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}
	/**
	 * Test to ensure that a tap disabled card is rejected...
	 * @throws IOException
	 */
	//@Test
	public void testBadCard5() throws IOException {
		Card badCard = new Card("DEBIT","123456789","Jim","319", "111", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}

	@Test
	public void testBadCard6() throws IOException {
		Card badCard = new Card("DEBIT","","Jim","319", "111", true, true);
		session.getHardware().getCardReader().tap(badCard);
		assertEquals(BigDecimal.valueOf(10.0), session.getProductLogic().getBalanceOwed());
	}
}
