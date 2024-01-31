package com.thelocalmarketplace.software.test.gui.customer.listeners.customer;

import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.demo.DemoSetup;
import com.thelocalmarketplace.software.gui.view.CustomerSimulationView;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.partials.customer.EndSessionPanel;
import powerutility.PowerGrid;

import static org.junit.Assert.assertEquals;

import javax.swing.JLabel;

import org.junit.Before;

/*
 * Testing all buttons that cause panels to switch (with no to minimal logic)
 */
public class SimulationViewTests {

	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	CustomerView customerView;
	CustomerSimulationView simulationView;

	public EndSessionPanel endSessionPanel;
	public JLabel custTotalLabel;
	public JLabel memberLabel;
	public JLabel weightLabel;
	
	private BarcodedProduct product;
    private Barcode barcode;

	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		//DemoSetup.intializeDatabase();
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();

		// d1 = new dummyProductDatabaseWithOneItem();
		// d2 = new dummyProductDatabaseWithNoItemsInInventory();

		
		Numeral[] testBarcode = new Numeral[4];
        testBarcode[0] = Numeral.nine;
        testBarcode[1] = Numeral.five;
        testBarcode[2] = Numeral.eight;
        testBarcode[3] = Numeral.eight;
        barcode = new Barcode(testBarcode);
        product = new BarcodedProduct(barcode, "test2", 5, 100);
        
	    ProductDatabases.INVENTORY.put(product, 1);
	    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
	    
		DemoSetup.DEMO_ITEMS.put(new BarcodedItem(barcode, new Mass(100)), "some item");

	        
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();

		logic = new SelfCheckoutStationSoftware(station);
		logic.setEnabled(true);

		customerView = new CustomerView(logic);
		simulationView = new CustomerSimulationView(logic);
		simulationView.configureCustomerSimulation(DemoSetup.CURRENCY, DemoSetup.DEMO_BANK_CARDS, DemoSetup.DEMO_BANK_CARDS, DemoSetup.DEMO_ITEMS, DemoSetup.DEMO_BAGS, DemoSetup.DEMO_COIN_DENOMS, DemoSetup.DEMO_BANKNOTE_DENOMS);

		logic.startSession();
		
	}

	// DELETE MY COMMENTS BEFORE SUBMISSION

	@Test
	public void testAddItemToScaleButton() {
		simulationView.switchToPlaceItemOnScaleViewButton.doClick();
		assertEquals("additemtoscale", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testRemoveItemFromScaleButton() {
		simulationView.switchToRemoveItemFromScaleViewButton.doClick();
		assertEquals("removeitemfromscale", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testScanItemHandHeldButton() {
		simulationView.switchToScanBarcodedItemHandheldScannerViewButton.doClick();
		assertEquals("scanitemhandheld", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testScanItemMainButton() {
		simulationView.switchToScanBarcodedItemMainScannerViewButton.doClick();
		assertEquals("scanitemmain", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testAddBagToScaleButton() {
		simulationView.switchToPlaceBagOnScaleViewButton.doClick();
		assertEquals("addbagtoscale", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testSwipeBankCardButton() {
		simulationView.switchToSwipeBankCardViewButton.doClick();
		assertEquals("swipebankcard", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testTapBankCardButton() {
		simulationView.switchToTapBankCardViewButton.doClick();
		assertEquals("tapbankcard", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testInsertBankCardButton() {
		simulationView.switchToInsertBankCardViewButton.doClick();
		assertEquals("insertbankcard", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testSwipeMembershipCardButton() {
		simulationView.switchToSwipeMembershipCardViewButton.doClick();
		assertEquals("swipemembershipcard", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testSelectCoinButton() {
		simulationView.switchToSelectCoinViewButton.doClick();
		assertEquals("selectcoin", simulationView.getCurrentCardLayoutView());
	}

	@Test
	public void testSelectBanknoteButton() {
		simulationView.switchToSelectBanknoteViewButton.doClick();
		assertEquals("selectbanknote", simulationView.getCurrentCardLayoutView());
	}
}