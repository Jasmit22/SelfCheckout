package com.thelocalmarketplace.software.test.gui.attendant;

import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.reflect.Field;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.software.gui.LocalViewController;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.partials.attendant.*;

import powerutility.PowerGrid;

/**
 * Contains methods for simulating an attempt at servicing ink level by the attendant,
 * triggered by an ActionEvent
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
 * @author Royce Knoeplfi		(30172598)
 */
public class RefillInkAndRefillPaperButtonListenerTest {
	
	AbstractSelfCheckoutStation station;
	SelfCheckoutStationSoftware logic;
	AttendantMaintainPanel maintainPanel;
	JButton inkButton;
	JButton paperButton;
	
	
	private <T extends Component> T getComponent(String fieldName, Class<T> fieldType, Object instance) 
			throws NoSuchFieldException, IllegalAccessException {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return fieldType.cast(field.get(instance));
	}
	
	private class mockLogic extends SelfCheckoutStationSoftware {

		public mockLogic(AbstractSelfCheckoutStation hardware) throws NullPointerException {
			
			super(hardware);
			
			this.view = new ViewStub(this);
			
			}
		
	}
	
	public class ViewStub extends LocalViewController {
		
		public int timesInkFullCalled = 0;
		public int timesPaperFullCalled = 0;

		public ViewStub(SelfCheckoutStationSoftware logic) {
			super(logic);
		}
		
		@Override
		public void updateOnInkFull() {
			timesInkFullCalled++;
		}
		
		@Override
		public void updateOnPaperFull() {
			timesPaperFullCalled++;
		}
		
	}

	@Before
	public void setUp() throws Exception {
		PowerGrid.engageUninterruptiblePowerSource();
		station = new SelfCheckoutStationGold();
		station.plugIn(PowerGrid.instance());
		station.turnOn();
		install();
		
	}
	
	public void install() throws Exception {
		logic = new mockLogic(station);
		maintainPanel = new AttendantMaintainPanel(logic);
		inkButton = getComponent("refillInkButton", JButton.class, maintainPanel);
		paperButton = getComponent("refillPaperButton", JButton.class, maintainPanel);
	}
	
	
	@Test
	public void testRefillInkWhenEmpty() {
		
		
		inkButton.doClick();
		
		//Fill notification should only occur once
		int expectedCalls = 1;
		int resultingCalls = ((ViewStub) logic.getViewController()).timesInkFullCalled;
		assertEquals(expectedCalls,resultingCalls);
		
		boolean printerFilled = false;
		
		try {
			station.getPrinter().addInk(1);
			
		} catch (OverloadedDevice e) {
			printerFilled = true;
		}
		assertTrue(printerFilled);
	}
	

	
	@Test
	public void testRefillInkWhenInkHigh() throws OverloadedDevice, Exception {
		
		station.getPrinter().addInk((1<<20) -1);
		
		//Fresh software instance with no listener calls from manual ink addition setup
		
		install();
		
		
		inkButton.doClick();
		
		//Fill notification should only occur once, through refillInk's method
		int expected = 1;		
		int result = ((ViewStub) logic.getViewController()).timesInkFullCalled;		
		assertEquals(expected,result);
		
		boolean inkFilled = false;
		
		try {
			station.getPrinter().addInk(1);
			
		} catch (OverloadedDevice e) {
			inkFilled = true;
		}
		assertTrue(inkFilled);
		
	}
	
	@Test
	public void testRefillInkWhenInkLow() throws OverloadedDevice, Exception {
		
		//Fill to one below "buffer"
		station.getPrinter().addInk(199);
		
		//Fresh software instance with no listener calls from manual ink addition setup
		
		install();
		
		
		inkButton.doClick();
		
		//Fill notification should only occur once, through refillInk's method
		int expected = 1;		
		int result = ((ViewStub) logic.getViewController()).timesInkFullCalled;		
		assertEquals(expected,result);
		
		boolean inkFilled = false;
		
		try {
			station.getPrinter().addInk(1);
			
		} catch (OverloadedDevice e) {
			inkFilled = true;
		}
		assertTrue(inkFilled);
		
	}
	
	@Test
	public void testRefillPaperWhenEmpty() {
		
		paperButton.doClick();
		
		int expected = 1;
		int result = ((ViewStub) logic.getViewController()).timesPaperFullCalled;
		
		assertEquals(expected,result);
		
		boolean paperFilled = false;
		
		try {
			station.getPrinter().addPaper(1);
			
		} catch (OverloadedDevice e) {
			paperFilled = true;
		}
		assertTrue(paperFilled);
	}
	
	@Test	
	public void testRefillPaperWhenHigh() throws Exception {
		
		station.getPrinter().addPaper((1<<10)-1);
		
		install();
		
		paperButton.doClick();
		
		int expected = 1;
		int result = ((ViewStub) logic.getViewController()).timesPaperFullCalled;
		
		assertEquals(expected, result);
		
		
		
	}
	
	@Test
	public void testRefillPaperWhenLow() throws Exception {
		
		station.getPrinter().addPaper(10);
		
		install();
		
		paperButton.doClick();
		
		int expected = 1;
		int result = ((ViewStub) logic.getViewController()).timesPaperFullCalled;
		
		assertEquals(expected,result);
		
		boolean paperFilled = false;
		
		try {
			station.getPrinter().addPaper(1);
			
		} catch (OverloadedDevice e) {
			paperFilled = true;
		}
		
		assertTrue(paperFilled);
	}
	
}