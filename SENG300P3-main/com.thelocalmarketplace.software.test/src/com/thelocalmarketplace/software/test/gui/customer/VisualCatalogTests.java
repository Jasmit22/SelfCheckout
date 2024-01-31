package com.thelocalmarketplace.software.test.gui.customer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedItem;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.demo.DemoSetup;
import com.thelocalmarketplace.software.gui.view.CustomerView;
import com.thelocalmarketplace.software.gui.view.partials.attendant.AttendantAddItemViaSearchPanel;
import com.thelocalmarketplace.software.gui.view.partials.customer.VisualCataloguePanel;

import powerutility.PowerGrid;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

@SuppressWarnings("unused")
public class VisualCatalogTests {
		private SelfCheckoutStationSoftware logic;
		private SelfCheckoutStationGold station;
	    private VisualCataloguePanel catalogue;
	    private JPanel mainPanel;
	    private CustomerView cv;
	    
	  
	    private JList<String> list;
	    private DefaultListModel<String> listModel;
	    private PLUCodedProduct pluProduct1;
	    private PriceLookUpCode pluCode1;
	    private Product p;
	    private BarcodedProduct product;
	    private Barcode barcode;

		@Before
	    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        PowerGrid.engageUninterruptiblePowerSource();
	        SelfCheckoutStationGold.resetConfigurationToDefaults();
	    	station = new SelfCheckoutStationGold();
	        logic = new SelfCheckoutStationSoftware(station);
	        cv = new CustomerView(logic);
	        catalogue = new VisualCataloguePanel(logic, cv);
	       
	        station.plugIn(PowerGrid.instance());
			station.turnOn();
			logic.setEnabled(true);
			logic.startSession();
			
	        Numeral[] testBarcode = new Numeral[4];
	        testBarcode[0] = Numeral.nine;
	        testBarcode[1] = Numeral.five;
	        testBarcode[2] = Numeral.eight;
	        testBarcode[3] = Numeral.eight;
	        barcode = new Barcode(testBarcode);
	        product = new BarcodedProduct(barcode, "test2", 5, 100);

			pluCode1 = new PriceLookUpCode("1234");
			pluProduct1 = new PLUCodedProduct(pluCode1, "test", 2);
			ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
			ProductDatabases.PLU_PRODUCT_DATABASE.clear();
			ProductDatabases.INVENTORY.clear();

	    }
		private <T extends Component> T getComponent(String fieldName, Class<T> fieldType, Object instance) 
	            throws NoSuchFieldException, IllegalAccessException {
	        Field field = instance.getClass().getDeclaredField(fieldName);
	        field.setAccessible(true);
	        return fieldType.cast(field.get(instance));
	    }
	    
		@Test public void testSelectItemToAddBarcoded() {
			p = product;
		    ProductDatabases.INVENTORY.put(p, 1);
		    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		    catalogue.getVisualCatalog("test visual catalog", 300, 300);
			JButton button = catalogue.allImageButtons.get(0);
			button.doClick();
			assertTrue("item was not successfully added to cart", logic.getProductLogic().getCart().containsKey(p));
			
		}
		@Test public void testSelectItemToAddPLU() {
			p = pluProduct1;
			ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode1, pluProduct1);
	        ProductDatabases.INVENTORY.put(p, 1);
	        catalogue.getVisualCatalog("test visual catalog", 300, 300);
			JButton button = catalogue.allImageButtons.get(0);
			button.doClick();
			logic.getHardware().getBaggingArea().addAnItem(new PLUCodedItem(pluCode1, new Mass(200.0)));
			assertTrue("item was not successfully added to cart", logic.getProductLogic().getCart().containsKey(p));
		}
		@Test public void testBackButton() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        catalogue.getVisualCatalog("test visual catalog", 300, 300);
			JButton backButton = getComponent("backButton", JButton.class, catalogue);
			backButton.doClick();
			assertTrue("back button did not go back to normal screen", !catalogue.isShowing());
			
		}@Test public void testPanelNumberItiailalization() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        DemoSetup.intializeDatabase();
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			assertTrue("wrong number of panels initialized", catalogue.allPanels.size() == 2);
		}
		@Test public void testSecondPanelVisibleAfterRightClick() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        DemoSetup.intializeDatabase();
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			System.out.println("number of panels: "+catalogue.allPanels.size());
			JButton rightButton = getComponent("rightButton", JButton.class, catalogue);
			rightButton.doClick();
			assertTrue("right click was not made", catalogue.getCurrentPanel()==1);
		}
		@Test public void testSecondPanelVisibleAfterLeftClick() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        DemoSetup.intializeDatabase();
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			System.out.println("number of panels: "+catalogue.allPanels.size());
			JButton leftButton = getComponent("leftButton", JButton.class, catalogue);
			leftButton.doClick();
			assertTrue("right click was not made", catalogue.getCurrentPanel()==1);
		}@Test public void testFirstPanelVisibleAfterLeftClickOnSecondPane() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        DemoSetup.intializeDatabase();
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			System.out.println("number of panels: "+catalogue.allPanels.size());
			JButton leftButton = getComponent("leftButton", JButton.class, catalogue);
			leftButton.doClick();
			JButton rightButton = getComponent("rightButton", JButton.class, catalogue);
			rightButton.doClick();
			//JPanel panelVisible = getComponent("cardLayoutImages", CardLayout.class, catalogue);
			
			assertTrue("right click was not made", catalogue.getCurrentPanel()==0);
		}@Test public void testFirstPanelVisibleAfterTwoClicks() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        DemoSetup.intializeDatabase();
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			System.out.println("number of panels: "+catalogue.allPanels.size());
			JButton leftButton = getComponent("leftButton", JButton.class, catalogue);
			leftButton.doClick();
			leftButton.doClick();
			assertTrue("right click was not made", catalogue.getCurrentPanel()==0);
		}
		@Test public void testDisableButtons() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			p = product;
		    ProductDatabases.INVENTORY.put(p, 1);
		    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			catalogue.disableAddingButtons();
			JButton leftButton = getComponent("leftButton", JButton.class, catalogue);
			JButton rightButton = getComponent("rightButton", JButton.class, catalogue);
			JButton backButton = getComponent("backButton", JButton.class, catalogue);
			JButton button = catalogue.allImageButtons.get(0);
			assertTrue("right click was not made", !leftButton.isEnabled() && !rightButton.isEnabled() && !button.isEnabled() && backButton.isEnabled());
		}@Test public void testEnableButtons() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			p = product;
		    ProductDatabases.INVENTORY.put(p, 1);
		    ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
			catalogue.getVisualCatalog("test visual catalog", 300, 300);
			catalogue.disableAddingButtons();
			catalogue.enableAddingButtons();
			JButton leftButton = getComponent("leftButton", JButton.class, catalogue);
			JButton rightButton = getComponent("rightButton", JButton.class, catalogue);
			JButton backButton = getComponent("backButton", JButton.class, catalogue);
			JButton button = catalogue.allImageButtons.get(0);
			assertTrue("right click was not made", leftButton.isEnabled() && rightButton.isEnabled() && button.isEnabled() && backButton.isEnabled());
		}
		
		
}