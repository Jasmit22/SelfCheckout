package com.thelocalmarketplace.software.test.gui.customer;

import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.partials.customer.EndSessionPanel;

import powerutility.PowerGrid;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.swing.JLabel;

import org.junit.Before;

/*
 * Testing all buttons that cause panels to switch (with no to minimal logic)
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
public class CustomerViewSwitchPanelsTests {

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	CustomerView customerView;

	public EndSessionPanel endSessionPanel;
	public JLabel custTotalLabel;
	public JLabel memberLabel;
	public JLabel weightLabel;

	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();

		AbstractSelfCheckoutStation.resetConfigurationToDefaults();

		// d1 = new dummyProductDatabaseWithOneItem();
		// d2 = new dummyProductDatabaseWithNoItemsInInventory();

		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		logic = new SelfCheckoutStationSoftware(station);
		logic.setEnabled(true);

		customerView = new CustomerView(logic);
		customerView.startSessionPanel.startSessionButton.doClick();
	}

	@Test
	public void testmarklastPanel() {
		customerView.switchCardLayoutView("Test");
		customerView.markLastPanel();
		assertEquals("Test", customerView.getCurrentCardLayoutView());
	}

	@Test
	public void testGoToLastPanel() {
		customerView.switchCardLayoutView("Test");
		customerView.markLastPanel();
		customerView.gotoLastPanel();
		assertEquals("Test", customerView.getCurrentCardLayoutView());
	}


	// Line 211
	@Test
	public void testPurchaseBagsButton() {
		customerView.PurchaseBagsButton.doClick();
		assertEquals("numpad_PurchaseBags_Panel", customerView.getCurrentCardLayoutView());
	}

	// Line 220
	@Test
	public void testNumpad_PurchaseBags_Panel_returnCheckout() {
		customerView.numpad_PurchaseBags_Panel.returnCheckout.doClick();
		assertEquals("checkoutPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 228
	@Test
	public void testAddOwnBagsButton() {
		customerView.AddOwnBagsButton.doClick();
		assertEquals(logic.getStateLogic().getState(), States.ADDBAGS);
	}

	// Line 228
	@Test
	public void testDontBagItemButton() {
		customerView.AddOwnBagsButton.doClick();
		// INCOMPLETE TEST
	}

	// Line 242
	@Test
	public void tesTypeInMemberNoButton() {
		customerView.TypeInMemberNoButton.doClick();
		assertEquals("numpad_MembershipType_Panel", customerView.getCurrentCardLayoutView());
		assertEquals(logic.getStateLogic().getState(), States.ADDMEMBERSHIP);
	}

	// Line 252
	@Test
	public void testnumpad_MembershipType_Panel() {
		customerView.numpad_MembershipType_Panel.returnCheckout.doClick();
		assertEquals("checkoutPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 262
	@Test
	public void testAddItem_PLUButton() {
		customerView.AddItem_PLUButton.doClick();
		assertEquals("numpad_PLUItem_Panel", customerView.getCurrentCardLayoutView());
	}

	// Line 270
	@Test
	public void testReturnCheckoutButton() {
		// Act: Simulate button click
		customerView.numpad_PLUItem_Panel.returnCheckout.doClick();
		// Assert: Verify the outcome
		assertEquals("checkoutPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 280
	@Test
	public void testAddItem_VisualButton() {
		customerView.AddItem_VisualButton.doClick();
		assertEquals("visualCatalog", customerView.getCurrentCardLayoutView());
	}

	// Line 289
	@Test
	public void testPayForOrderButton() {
		customerView.PayForOrderButton.doClick();
		assertEquals("selectPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 313
	@Test
	public void testselectPaymentPanel_debitButton() {
		customerView.selectPaymentPanel.debitButton.doClick();
		assertEquals("debitPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 321
	@Test
	public void testselectPaymentPanel_creditButton() {
		customerView.selectPaymentPanel.creditButton.doClick();
		assertEquals("creditPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 329
	@Test
	public void testselectPaymentPanel_cashButton() {
		customerView.selectPaymentPanel.cashButton.doClick();
		assertEquals("cashPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 337
	@Test
	public void testselectPaymentPanel_backToCheckoutButton() {
		customerView.selectPaymentPanel.backToCheckoutButton.doClick();
		assertEquals("checkoutPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 348
	@Test
	public void testcashPaymentPanel_backToPaymentButton() {
		customerView.cashPaymentPanel.backToPaymentButton.doClick();
		assertEquals("selectPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 360
	@Test
	public void testdebitPaymentPanel_backToPaymentButton() {
		customerView.debitPaymentPanel.backToPaymentButton.doClick();
		assertEquals("selectPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 369
	@Test
	public void testdebitPaymentPanel_PINButton() {
		customerView.debitPaymentPanel.PINButton.doClick();
		assertEquals("numpad_PINDebit", customerView.getCurrentCardLayoutView());
	}

	// Line 376
	@Test
	public void testnumpad_PINDebit_returnCheckout() {
		customerView.numpad_PINDebit.returnCheckout.doClick();
		assertEquals("debitPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 386
	@Test
	public void testcreditPaymentPanel_backToPaymentButton() {
		customerView.creditPaymentPanel.backToPaymentButton.doClick();
		assertEquals("selectPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 395
	@Test
	public void testcreditPaymentPanel_PINButton() {
		customerView.creditPaymentPanel.PINButton.doClick();
		assertEquals("numpad_PINCredit", customerView.getCurrentCardLayoutView());
	}

	// Line 395
	@Test
	public void testnumpad_PINCredit_returnCheckout() {
		customerView.numpad_PINCredit.returnCheckout.doClick();
		assertEquals("creditPaymentPanel", customerView.getCurrentCardLayoutView());
	}

	// Line 416
	@Test
	public void testStart() {
		customerView.start();
		assertEquals("mainMenu", customerView.getCurrentCardLayoutView());
	}

	// Line 532
	@Test
	public void testGetANDUpdateDisplayWeight() {
		Mass testWeight = new Mass(2);
		customerView.checkoutPanel();
		customerView.updateDisplayWeight(testWeight);
		assertEquals(testWeight, customerView.getDisplayWeight());
	}

	// Line 532
	@Test
	public void testGetANDUpdateDisplayTotal() {
		BigDecimal testTotal = new BigDecimal("2");
		customerView.checkoutPanel();
		customerView.updateDisplayTotal(testTotal);
		System.out.println(customerView.custTotalLabel.toString());
		assertEquals("Total is: $2.00", customerView.custTotalLabel.getText());
	}

	// Line 933
	@Test
	public void testEnableButtonsBlocked() {

	}

}
