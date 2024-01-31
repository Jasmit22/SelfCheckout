package com.thelocalmarketplace.software.test.gui.attendant;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.partials.attendant.AttendantAddItemViaSearchPanel;

import powerutility.PowerGrid;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

public class AddItembyTextGUITest {
		private SelfCheckoutStationSoftware logic;
		private SelfCheckoutStationGold station;
	    private AttendantAddItemViaSearchPanel panel;
	    private JTextField textField;
	    private JButton startSearchButton;
	    private JButton backToSearchButton;
	    private JLabel messageLabel;
	    private JList<String> list;
	    private DefaultListModel<String> listModel;
	    private PLUCodedProduct pluProduct1;
	    private PriceLookUpCode pluCode1;
	    private Product p;
	    private BarcodedProduct product;
	    private Barcode barcode;
		@SuppressWarnings("unchecked")
		@Before
	    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        PowerGrid.engageUninterruptiblePowerSource();
	        SelfCheckoutStationGold.resetConfigurationToDefaults();
	    	station = new SelfCheckoutStationGold();
	        logic = new SelfCheckoutStationSoftware(station);
	        panel = new AttendantAddItemViaSearchPanel(logic);
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
			
	        p = product;
	        ProductDatabases.INVENTORY.put(p, 5);
	        
	        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
			ProductDatabases.PLU_PRODUCT_DATABASE.put(pluCode1, pluProduct1);
			p = pluProduct1;
	        ProductDatabases.INVENTORY.put(p, 1);
	        
	        textField = getComponent("textField", JTextField.class, panel);
	        startSearchButton = getComponent("startSearch", JButton.class, panel);
	        backToSearchButton = getComponent("backToSearch", JButton.class, panel);
	        messageLabel = getComponent("messageLabel2", JLabel.class, panel); 
	        list = getComponent("list", JList.class, panel);
	        listModel = (DefaultListModel<String>) list.getModel();
	    }

	    @Test
	    public void testEmptySearchField() {
	        textField.setText("");
	        startSearchButton.doClick();
	        assertEquals("Description cannot be empty.", messageLabel.getText());
	    }

	    @Test
	    public void testNoProductFound() {
	        textField.setText("non-existing product");
	        startSearchButton.doClick();
	        assertEquals("No products found.", messageLabel.getText());
	    }

	    @Test
	    public void testProductFoundPLU() {
	        textField.setText("test");
	        startSearchButton.doClick();
	 
	        assertFalse("Product list should not be empty", listModel.isEmpty());
	        List<String> expectedProducts = List.of("test");
	        List<String> actualProducts = Collections.list(listModel.elements())
            .stream()
            .collect(Collectors.toList());
	        assertTrue(actualProducts.containsAll(expectedProducts));
	    }
	    
	    @Test
	    public void testProductFoundBarcoded() {
	        textField.setText("test2");
	        startSearchButton.doClick();
	 
	        assertFalse("Product list should not be empty", listModel.isEmpty());
	        List<String> expectedProducts = List.of("test2");
	        List<String> actualProducts = Collections.list(listModel.elements())
            .stream()
            .collect(Collectors.toList());
	        assertTrue(actualProducts.containsAll(expectedProducts));
	    }


	    private <T extends Component> T getComponent(String fieldName, Class<T> fieldType, Object instance) 
	            throws NoSuchFieldException, IllegalAccessException {
	        Field field = instance.getClass().getDeclaredField(fieldName);
	        field.setAccessible(true);
	        return fieldType.cast(field.get(instance));
	    }
	    
	    @Test
	    public void testBackToSearchButton() throws IllegalAccessException, NoSuchFieldException {
	        textField.setText("test");
	        startSearchButton.doClick();
	        backToSearchButton.doClick();
	        // verify that the message label is cleared
	        assertEquals("", messageLabel.getText());
	        assertTrue("Search panel should be visible", isSearchPanelVisible(panel));
	    }
	    	    
	    private boolean isSearchPanelVisible(JPanel panel) {
	        
	        for (Component comp : panel.getComponents()) {
	        	if (comp.isVisible() && comp instanceof Container) {
	        	    Container container = (Container) comp;
	        	    if (container.getLayout() instanceof BorderLayout) {
	        	    }
	                return true;
	            }
	        }
	        return false;
	    }
	    }



