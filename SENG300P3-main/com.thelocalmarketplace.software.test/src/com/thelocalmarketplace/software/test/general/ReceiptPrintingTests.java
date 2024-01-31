package com.thelocalmarketplace.software.test.general;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;


import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;

import com.jjjwelectronics.scanner.Barcode;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.AttendantStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AttendantStationSoftware;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.Enumerations.States;
import com.thelocalmarketplace.software.gui.view.AttendantView;

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
public class ReceiptPrintingTests {

    private SelfCheckoutStationSoftware logic;
    private SelfCheckoutStationBronze station;
    private AttendantView av;

    @Before
    public void setUp() {
        PowerGrid.engageUninterruptiblePowerSource();
        PowerGrid.instance().forcePowerRestore();
        
        AbstractSelfCheckoutStation.resetConfigurationToDefaults();
        
        ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
        
        station = new SelfCheckoutStationBronze();
        station.plugIn(PowerGrid.instance());
        station.turnOn();
        
        logic = new SelfCheckoutStationSoftware(station);
        logic.setEnabled(true);
        logic.startSession();
        
        this.av = new AttendantView(new AttendantStationSoftware(new AttendantStation()));
        
        av.addStationOption(logic);
        
    }

  
    
    @Test
    public void testHandlePrintReceiptWithoutInk() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        
        logic.getProductLogic().addBarcodedProductToCart(barcode1);  	
        station.getPrinter().addPaper(1000);
        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        assertTrue(this.logic.getStateLogic().inState(States.SUSPENDED));
    }
    
    @Test
    public void testHandlePrintReceiptWithoutPaper() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);      
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        logic.getProductLogic().addBarcodedProductToCart(barcode1);  
        station.getPrinter().addInk(1000);
        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        
        assertTrue(this.logic.getStateLogic().inState(States.SUSPENDED));
    }
    
    @Test
    public void testPrintReceiptWithPaperandInk() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);      
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        logic.getProductLogic().addBarcodedProductToCart(barcode1);  
        station.getPrinter().addInk(1000);
        station.getPrinter().addPaper(1000);
        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        
        assertNotEquals(this.logic.getStateLogic().getState(), States.SUSPENDED);
    }
    
    @Test
    public void testNotifyOutofInk() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);      
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        logic.getProductLogic().addBarcodedProductToCart(barcode1); 
        station.getPrinter().addInk(5);
        station.getPrinter().addPaper(5);
        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        
        assertEquals(this.logic.getStateLogic().getState(), States.SUSPENDED);
    }
    
    @Test
    public void testNotifyOutofPaper() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);      
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        logic.getProductLogic().addBarcodedProductToCart(barcode1); 
        station.getPrinter().addInk(1000);
        station.getPrinter().addPaper(1);
        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        
        assertEquals(this.logic.getStateLogic().getState(), States.SUSPENDED);
    }
    
    @Test
    public void testAttendantError() throws OverloadedDevice {
    	Barcode barcode1 = new Barcode(new Numeral[] {Numeral.one});
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "TestProduct", 1, 100.0);      
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        ProductDatabases.INVENTORY.put(product1, 1);
        logic.getProductLogic().addBarcodedProductToCart(barcode1); 
        station.getPrinter().addInk(1000);
        station.getPrinter().addPaper(1);

        logic.getReceiptPrintingController().handlePrintReceipt(new BigDecimal(0));
        
        assertEquals(logic.getStateLogic().getState(), States.SUSPENDED);
    }
}
