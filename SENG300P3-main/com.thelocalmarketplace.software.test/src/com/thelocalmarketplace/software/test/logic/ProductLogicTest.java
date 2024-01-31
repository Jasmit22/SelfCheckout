package com.thelocalmarketplace.software.test.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;

import ca.ucalgary.seng300.simulation.SimulationException;
import powerutility.PowerGrid;

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
public class ProductLogicTest {
	
	SelfCheckoutStationBronze station;
	SelfCheckoutStationSoftware logic;
	
	public Barcode barcode;
	public Barcode barcode2;
	public Numeral digits;
	
	public BarcodedItem bitem;

	public Numeral[] barcode_numeral;
	public Numeral[] barcode_numeral2;
	public Numeral[] barcode_numeral3;
	public Barcode b_test;
	public BarcodedProduct product;
	public BarcodedProduct product2;
	public BarcodedProduct product3;

	
	private PriceLookUpCode code1;
	
	private PLUCodedProduct product1;

	public Mass itemMass;
	
	public BarcodedItem bitem2;
	public Mass itemMass2;
	public BarcodedItem bitem3;
	public Mass itemMass3;
	
	public BarcodedItem bitem4;
	public Mass itemMass4;
	
	public BarcodedItem bitem5;
	public Mass itemMass5;

	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
		
		AbstractSelfCheckoutStation.resetConfigurationToDefaults();
		
		barcode_numeral = new Numeral[]{Numeral.one,Numeral.two, Numeral.three};
		barcode_numeral2 = new Numeral[]{Numeral.three,Numeral.two, Numeral.three};
		barcode_numeral3 = new Numeral[]{Numeral.three,Numeral.three, Numeral.three};
		barcode = new Barcode(barcode_numeral);
		barcode2 = new Barcode(barcode_numeral2);
		b_test = new Barcode(barcode_numeral3);
		product = new BarcodedProduct(barcode, "some item",(long)17.00,(double)300.0);
		product2 = new BarcodedProduct(barcode2, "some item 2",(long)1.00,(double)300.0);
		product3 = new BarcodedProduct(b_test, "some item 3",(long)1.00,(double)300.0);
		
		code1 = new PriceLookUpCode("21321");
		product1 = new PLUCodedProduct(code1, "test", 1);
		
		bitem = new BarcodedItem(barcode, new Mass((double)3.0));
		ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
		ProductDatabases.INVENTORY.clear();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.INVENTORY.put(product, 1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
		ProductDatabases.INVENTORY.put(product2, 1);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(code1, product1);
		ProductDatabases.INVENTORY.put(product1, 1);
		
		//initialize barcoded item
		itemMass = new Mass((long) 1000000);
		bitem = new BarcodedItem(barcode2, itemMass);
		itemMass2 = new Mass((double) 300.0);//300.0 grams
		bitem2 = new BarcodedItem(barcode, itemMass2);
		itemMass3 = new Mass((double) 300.0);//3.0 grams
		bitem3 = new BarcodedItem(barcode, itemMass3);
		bitem4 = new BarcodedItem(b_test, itemMass3);
		itemMass5 = new Mass((double) 300.0);
		bitem5 = new BarcodedItem(barcode2,itemMass5);
		
		station = new SelfCheckoutStationBronze();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		
		logic = new SelfCheckoutStationSoftware(station);
		logic.setEnabled(true);
		logic.startSession();
	}
	@Test public void updatePriceOfCartTest() {
		BigDecimal price1 = new BigDecimal(50.0);
		logic.getProductLogic().updateBalance(price1);
		assertTrue("price of cart was not updated correctly when adding to it", logic.getProductLogic().getBalanceOwed().equals(price1));
	}@Test public void updateNegativePriceOfCartTest() {
		BigDecimal price1 = new BigDecimal(-50.0);
		logic.getProductLogic().updateBalance(price1);
		assertTrue("price of cart was not updated correctly when subtracting from it", logic.getProductLogic().getBalanceOwed().equals(price1));
	}@Test public void addProductToCartTestCheckPrice() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		assertTrue("price of cart was not updated correctly after adding to cart", logic.getProductLogic().getBalanceOwed().equals(new BigDecimal(17)));
	}@Test public void addMultipleProductToCartTestCheckPrice() {
		BigDecimal price1 = new BigDecimal(17);
		BigDecimal price2 = new BigDecimal((long)1.00);
		BigDecimal expected = price1.add(price2);
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().addBarcodedProductToCart(barcode2);
		assertEquals(expected, logic.getProductLogic().getBalanceOwed());
	}
	
	@Test
	public void testRemoveProductFromCart() {
	
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getHardware().getBaggingArea().addAnItem(bitem);
		//assertEquals(1, logic.getProductLogic().getCart().size());
		logic.getProductLogic().removeProduct(product);
		assertEquals(0, logic.getProductLogic().getCart().size());
	}
	
	@Test(expected = SimulationException.class)
	public void testRemoveNonExistentProductFromCart() {
		logic.getProductLogic().removeProduct(product);
	}
	
	@Test(expected = SimulationException.class)
	public void testAddBarcodeNotInDatabase() {
		Barcode b = new Barcode(new Numeral[] {Numeral.one});
		
		logic.getProductLogic().addBarcodedProductToCart(b);
	}
	
	@Test(expected = SimulationException.class)
	public void testAddBarcodeNotInInventory2() {
		Barcode b = new Barcode(new Numeral[] {Numeral.two});
		BarcodedProduct p = new BarcodedProduct(b, "some item", 5, 4.0);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b, p);
		
		logic.getProductLogic().addBarcodedProductToCart(b);
	}
	
	@Test
	public void testModifyBalanceAdd() {
		logic.getProductLogic().modifyBalance(new BigDecimal(5));
		logic.getProductLogic().modifyBalance(new BigDecimal(3));
		
		assertEquals(new BigDecimal(8).setScale(5, RoundingMode.HALF_DOWN), logic.getProductLogic().getBalanceOwed().setScale(5, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testModifyBalanceSubtract() {
		logic.getProductLogic().modifyBalance(new BigDecimal(3));
		logic.getProductLogic().modifyBalance(new BigDecimal(-5));
		
		assertEquals(new BigDecimal(0).setScale(5, RoundingMode.HALF_DOWN), logic.getProductLogic().getBalanceOwed().setScale(5, RoundingMode.HALF_DOWN));
	}
	
	
	

	public void scanUntilAdded(Product p, BarcodedItem b) {
		while (!logic.getProductLogic().getCart().containsKey(p)) {
			station.getHandheldScanner().scan(b);
		}
	}
	
	@Test(expected = SimulationException.class)
	public void testStartAddItemWhenSessionNotStarted() {
		logic.stopSession();
		logic.getProductLogic().startAddItem(product);

	}@Test public void testStartAddItemStateChange() {
		
		logic.getProductLogic().startAddItem(product);
		assertTrue("state was not changed to additem properly", logic.getStateLogic().inState(States.ADDITEM));
	}@Test public void testStartAddItemPendingProduct() {
		
		logic.getProductLogic().startAddItem(product);
		assertTrue("pending product not set properly", logic.getProductLogic().getPendingProduct()==product);
	}@Test(expected = SimulationException.class) public void testEndAddItemWhenSessionNotStarted() {
		logic.getProductLogic().endAddItem();
		
	}@Test(expected = SimulationException.class) public void testEndAddItemWhenSessionStartedNotInState() {
		logic.getProductLogic().endAddItem();
		
	}
	@Test public void testEndAddItemStateChange() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().endAddItem();
		assertTrue("state was not changed out of additem properly", logic.getStateLogic().inState(States.NORMAL));
	}@Test (expected = SimulationException.class) public void testAddItemToScaleNotInStartAddItemStateChange() {
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		
	}@Test (expected = SimulationException.class) public void testAddItemToScaleInStartAddItemPendingProductNull() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().setPendingProduct(null);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		
	}@Test public void testAddItemMassWasAdded() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		assertTrue("product mass was not recorded properly", logic.getProductLogic().getMassProduct(product)==itemMass2);
		
	}@Test public void testAddItemCartBalanceUpdated() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		assertTrue("balance of cart was not updated correctly", logic.getProductLogic().getBalanceOwed().equals(new BigDecimal((long) 17.0)));
		
	}@Test public void testAddItemCartUpdated() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		assertTrue("product was not added to cart correctly", logic.getProductLogic().getCart().get(product)==1);
		
	}@Test public void testAddItemPendingSetToNull() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		assertTrue("pending product not set back to null", logic.getProductLogic().getPendingProduct()==null);
		
	}@Test public void testAddItemPendingStateChangedToNormal() {
		logic.getProductLogic().startAddItem(product);
		logic.getProductLogic().onItemMassUpdated(itemMass2);
		assertTrue("not in normal state after mass update", logic.getStateLogic().inState(States.NORMAL));
		
	}@Test public void testCalculateCostProductPerUnit() {
		logic.getProductLogic().startAddItem(product);
		BigDecimal result = logic.getProductLogic().calculateCostOfProduct(product, itemMass2);
		assertTrue("cost of product not calculated correctly per unit", result.equals(new BigDecimal((long) 17.0)));
		
	}@Test public void testCalculateCostProductNotPerUnit() {
		logic.getProductLogic().startAddItem(product1);
		BigDecimal result = logic.getProductLogic().calculateCostOfProduct(product1, new Mass((double) 10000.0));
		//expected = 1 dollar * 10000 grams =  1 dollar * 10 kg = 10
		BigDecimal expected = new BigDecimal(10);
		assertTrue("cost of product not calculated correctly per kg", result.equals(expected));
		
	}@Test public void testCalculateCostProductNotPerUnitBig() {
		
		logic.getProductLogic().startAddItem(product1);
		BigDecimal result = logic.getProductLogic().calculateCostOfProduct(product1, new Mass((double) 15030.0));
		//expected = 1 dollar * 10530 grams =  1 dollar * 10.53 kg = 10
		BigDecimal expected = new BigDecimal("15.03");
		assertTrue("cost of product not calculated correctly per kg", result.equals(expected));
		
	}@Test (expected = SimulationException.class) public void testRemoveItemSessionNotStarted() {
		logic.getProductLogic().removeProduct(product);
	}
	
	@Test (expected = SimulationException.class) public void testRemoveProductNotInCart() {
		logic.getProductLogic().removeProduct(product);
	}@Test public void testRemoveBarcodedProductCartBalance() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().removeProduct(product);
		BigDecimal expected = new BigDecimal(0.0);
		assertTrue("cart balance did not update after removing item from cart", logic.getProductLogic().getBalanceOwed().equals(expected));
	}@Test public void testRemoveBarcodedProductExpectedMass() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().removeProduct(product);
		assertTrue("expected mass did not update after removing item from cart", logic.getMassLogic().getExpectedMass().equals(Mass.ZERO));
	}@Test public void testRemoveBarcodedRemovedFromCart() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().removeProduct(product);
		assertTrue("cart did not update after removing item from cart", !logic.getProductLogic().getCart().containsKey(product));
	}@Test public void testRemoveBarcodedRemovedFromCartMoreThanOne() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().removeProduct(product);
		assertTrue("cart did not update after removing item from cart", logic.getProductLogic().getCart().get(product)==1);
	}@Test public void testRemoveBarcodedRemovedRecordedMassesChanged() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		logic.getProductLogic().removeProduct(product);
		assertTrue("item logic recorded mass did not remove product", logic.getProductLogic().getMassProduct(product)==null);
	}@Test public void testRemoveBarcodedRemovedNotBlockedState() {
		this.scanUntilAdded(product, bitem2);
		logic.getProductLogic().removeProduct(product);
		assertTrue("state not normal after removing added product", logic.getStateLogic().inState(States.NORMAL));
	}@Test public void testRemoveBarcodedRemovedBlockedState() {
		this.scanUntilAdded(product, bitem2);
		logic.getHardware().getBaggingArea().addAnItem(bitem2);
		logic.getProductLogic().removeProduct(product);
		assertTrue("state not plocked after not removing item from bagging area", logic.getStateLogic().inState(States.BLOCKED));
	}@Test(expected = SimulationException.class) public void testAddBarcodedItemNotInDatabase() {
		logic.getProductLogic().addBarcodedProductToCart(b_test);
	}@Test(expected = SimulationException.class) public void testAddBarcodedItemNotInInventory() {
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b_test, product3);
		//ProductDatabases.INVENTORY.put(product, 1);
		logic.getProductLogic().addBarcodedProductToCart(b_test);
	}@Test(expected = SimulationException.class) public void testAddBarcodedItemNoneInInventory() {
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(b_test, product3);
		ProductDatabases.INVENTORY.put(product3, 0);
		logic.getProductLogic().addBarcodedProductToCart(b_test);
	}@Test public void testAddBarcodedItemToCart() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		assertTrue("product was not successfully added to cart", logic.getProductLogic().getCart().containsKey(product));
	}@Test public void testAddBarcodedItemToCartNumberCorrect() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		assertTrue("product was not successfully added to cart", logic.getProductLogic().getCart().get(product)==1);
	}@Test public void testAddBarcodedItemToCartBalance() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		assertTrue("cart balance was not updated correctly", logic.getProductLogic().getBalanceOwed().equals(new BigDecimal(17)));
	}@Test public void testAddBarcodedItemExpectedWeight() {
		logic.getProductLogic().addBarcodedProductToCart(barcode);
		assertTrue("expected mass was not updated correctly", logic.getMassLogic().getExpectedMass().equals(new Mass((double) 300.0)));
	}

	// TODO: Test Adding PLU product to cart and removing
	@Test
	public void testAddPLUItemToCart(){
		logic.getProductLogic().startAddPLUProductToCart(code1);
	}
	
	@Test
	public void testGetCostOfProduct() {
		PLUCodedItem pitem = new PLUCodedItem(code1, itemMass);
		
		logic.getProductLogic().startAddPLUProductToCart(code1);
		logic.getHardware().getBaggingArea().addAnItem(pitem);
		
		assertEquals(logic.getProductLogic().calculateCostOfProduct(product, itemMass), logic.getProductLogic().getCostOfProduct(product));
	}
	
	@Test(expected = SimulationException.class)
	public void testStartAddPLUProductWithInvalidCode() {
		logic.getProductLogic().startAddPLUProductToCart(new PriceLookUpCode("76238"));
	}
	
	@Test(expected = SimulationException.class)
	public void testStartAddPLUProductWithItemNotInInventory() {
		PriceLookUpCode c = new PriceLookUpCode("2323");
		
		ProductDatabases.PLU_PRODUCT_DATABASE.put(c, new PLUCodedProduct(c, "2sdfio", 1));
		
		logic.getProductLogic().startAddPLUProductToCart(c);
	}

	@Test
	public void testRemoveProductInAddItemState() {
		PLUCodedItem pitem = new PLUCodedItem(code1, itemMass);
		
		logic.getProductLogic().startAddPLUProductToCart(code1);
		logic.getHardware().getBaggingArea().addAnItem(pitem);
		
		logic.getProductLogic().removeProduct(product1);
		
		assertTrue(logic.getStateLogic().inState(States.NORMAL));
	}
	
	@Test(expected = SimulationException.class)
	public void testEndAddItemSessionNotActive() {
		logic.stopSession();
		logic.getProductLogic().endAddItem();
	}
	
	@Test
	public void testProductSelectedToAddBarcoded() {
		logic.getProductLogic().productSelectedToAdd(product);
	}
	
	@Test
	public void testProductSelectedToAddPLUCoded() {
		logic.getProductLogic().productSelectedToAdd(product1);
	}
	
	private class ProductStub extends Product {
		protected ProductStub(long price, boolean isPerUnit) {
			super(price, isPerUnit);
		}
	}
	
	@Test
	public void testProductSelectedToAddOther() {
		logic.getProductLogic().productSelectedToAdd(new ProductStub(10, false));
	}
	
	@Test
	public void testGrantApprovalToSkipBagging() {
		logic.getProductLogic().productSelectedToAdd(product);
		logic.getProductLogic().grantApprovalSkipBaggingOfLastProductAdded();
		
		assertEquals(logic.getMassLogic().getExpectedMass(), logic.getMassLogic().getActualMass());
	}
}