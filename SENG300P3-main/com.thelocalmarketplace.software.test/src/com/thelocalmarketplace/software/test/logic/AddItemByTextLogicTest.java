package com.thelocalmarketplace.software.test.logic;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import powerutility.PowerGrid;
import java.util.List;
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

public class AddItemByTextLogicTest {
	
		private SelfCheckoutStationSoftware checkout;
	    private SelfCheckoutStationGold station;
	    private BarcodedProduct product;
	    private BarcodedProduct product2;
	    private Barcode barcode;
	    private Barcode barcode2;
	    private Barcode barcode3;
	    private BarcodedProduct product3;
	    private PLUCodedProduct pluProduct1;
	    private PLUCodedProduct pluProduct2;
	    private PLUCodedProduct pluProduct3;
	    private PriceLookUpCode pluCode1;
	    private PriceLookUpCode pluCode2;
	    private PriceLookUpCode pluCode3;
	    private Product p;
	    


	    @Before
	    public void setUp() {
	        PowerGrid.engageUninterruptiblePowerSource();
	        SelfCheckoutStationGold.resetConfigurationToDefaults();
	        station = new SelfCheckoutStationGold();
	        checkout = new SelfCheckoutStationSoftware(station);
	        station.plugIn(PowerGrid.instance());
			station.turnOn();
			checkout.setEnabled(true);
			checkout.startSession();
	        Numeral[] testBarcode = new Numeral[4];
	        testBarcode[0] = Numeral.nine;
	        testBarcode[1] = Numeral.five;
	        testBarcode[2] = Numeral.eight;
	        testBarcode[3] = Numeral.eight;
	        barcode = new Barcode(testBarcode);
	        product = new BarcodedProduct(barcode, "test", 5, 100);

	        Numeral[] testBarcode2 = new Numeral[2];
	        testBarcode2[0] = Numeral.zero;
	        testBarcode2[1] = Numeral.one;
	        barcode2 = new Barcode(testBarcode2);
	        product2 = new BarcodedProduct(barcode2, "test2", 100, 10);
	        Numeral[] testBarcode3 = new Numeral[2];
	        testBarcode3[0] = Numeral.nine;
	        testBarcode3[1] = Numeral.one;
	        barcode3 = new Barcode(testBarcode3);
	        product3 = new BarcodedProduct(barcode3, "test3", 100, 22);
	        
	        pluCode1 = new PriceLookUpCode("1234");
	        pluCode2 = new PriceLookUpCode("5789");
	        pluCode3 = new PriceLookUpCode("5133");
	        pluProduct1 = new PLUCodedProduct(pluCode1, "test", 2);
	        pluProduct2 = new PLUCodedProduct(pluCode2, "test2", 3);
	        pluProduct3 = new PLUCodedProduct(pluCode3, "test3", 5);
	        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode1, pluProduct1);
	        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode2, pluProduct2);
	        ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode3, pluProduct3);
	        
	        p = product;
	        ProductDatabases.INVENTORY.put(p, 5);
	        
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, product3);
	        
	        p =  pluProduct1;
	        ProductDatabases.INVENTORY.put(p, 5);
	        
	        
	    }
	    @Test
	    public void testFindProductByTextDescription() {
	        List<Product> foundProducts = checkout.getAddItemByTextLogic().findProductByTextDescription("test");
	        assertFalse("Expected non-empty product list", foundProducts.isEmpty());

	        boolean containsDescription = foundProducts.stream().anyMatch(p -> {
	         if (p instanceof BarcodedProduct) {
	         return ((BarcodedProduct) p).getDescription().toLowerCase().contains("test");
	         } else if (p instanceof PLUCodedProduct) {
	          return ((PLUCodedProduct) p).getDescription().toLowerCase().contains("test");
	          }
	          return false;
	});

	        assertTrue("Expected to find product with description 'test'", containsDescription);
	    }

	    @Test(expected = IllegalArgumentException.class)
	    public void testFindProductByTextDescriptionWithEmptyString() {
	    	checkout.getAddItemByTextLogic().findProductByTextDescription("");
	    }

	    @Test(expected = IllegalArgumentException.class)
	    public void testFindProductByTextDescriptionWithNull() {
	    	checkout.getAddItemByTextLogic().findProductByTextDescription(null);
	    }
	    
	    @After
	    public void tearDown() {
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
	        ProductDatabases.PLU_PRODUCT_DATABASE.clear();
	        ProductDatabases.INVENTORY.clear();
	    }
	}
	   