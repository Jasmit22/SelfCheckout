package com.thelocalmarketplace.software.gui;

import org.junit.Test;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.gui.view.partials.attendant.StationControlPanel;
import powerutility.PowerGrid;

import static org.junit.Assert.*;

import java.util.Timer;

import javax.swing.JFrame;

import org.junit.Before;

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

public class DiscrepencyThreadControllerTest {

	private SelfCheckoutStationSoftware logic;
	private SelfCheckoutStationGold station;
    private DiscrepencyThreadController controller;
    private StationControlPanel panel;
    private JFrame attendantViewFrame;

	@Before
	public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	        PowerGrid.engageUninterruptiblePowerSource();
	        SelfCheckoutStationGold.resetConfigurationToDefaults();
	    	station = new SelfCheckoutStationGold();
	        logic = new SelfCheckoutStationSoftware(station);
	        controller = new DiscrepencyThreadController(logic);
	        panel = new StationControlPanel(logic);
	        attendantViewFrame = new JFrame();
	        
	        station.plugIn(PowerGrid.instance());
			station.turnOn();
			logic.setEnabled(true);
			logic.startSession();
	}
	
    @Test
    public void testStartWhenViewIsReady() {
        // Create a mock logic object with a ready view    	
		logic.getViewController().connectToAttendantView(panel, attendantViewFrame);
    	logic.getViewController().start();
        
        controller.start();

        // Timer should be initialized
        assertNotNull(controller.timer);
    }

    @Test
    public void testStartWhenViewIsNotReady() {
    	// View is set to false and is not ready so 
        // timer should not get initialized
        assertNull(controller.timer);
    }

    
    @Test
    public void testStopMethod() {
        // Create a mock logic object with a ready view    	
		logic.getViewController().connectToAttendantView(panel, attendantViewFrame);
    	logic.getViewController().start();
  
        controller.start(); // Start the timer

        Timer timer = controller.timer;
        assertNotNull(timer); // Ensure the timer is initialized

        controller.stop(); // Stop the timer

        // Check if the timer is canceled after calling stop()
        assertFalse("Timer should be canceled", timer.purge() > 0);
    }
    
}
