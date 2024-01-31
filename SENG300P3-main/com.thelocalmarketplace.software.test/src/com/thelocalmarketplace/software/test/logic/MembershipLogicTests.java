package com.thelocalmarketplace.software.test.logic;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.general.Membership;
import com.thelocalmarketplace.software.logic.MembershipLogic;

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
public class MembershipLogicTests {
    public String membershipNumber = "01234";
    public MembershipLogic memLogic;

    /**
     * Creates new MembershipLogic to clear the database
     */
    @Before
    public void setup(){
        memLogic = new MembershipLogic();
    }

    /**
     * Checks that membership numbers are added to the database
     */
    @Test
    public void testMembershipAddedToDatabase(){
        memLogic.registerMembershipToLocalDatabase(membershipNumber);
        memLogic.setSelectedMembership(membershipNumber);
        Membership foundMembership = memLogic.findMembership(memLogic.convertStringToBarcode(membershipNumber));
        assertEquals(memLogic.getSelectedMembership(), foundMembership);
    }

    /**
     * Expecting a NullPointerException if checking whether a null membership number 
     * is valid or not
     */
    @Test
    public void testMembershipNumberIsNull(){
        assertThrows(NullPointerException.class, () -> memLogic.checkIfMembershipNumberValid(null));
    }

    /**
     * Expecting a NullPointerException if checking whether an empty membership number 
     * is valid or not
     */
    @Test
    public void testMembershipNumberIsEmpty(){
        assertThrows(NullPointerException.class, () -> memLogic.checkIfMembershipNumberValid(""));
    }

    /**
     * A membership number is invalid if it is not in the database. 
     */
    @Test
    public void testMembershipNumberIsNotAddedToDatabase(){
        assertFalse(memLogic.checkIfMembershipNumberValid(membershipNumber));
    }

    /**
     * Checks that membership numbers are valid when added to database and not 
     * empty or null
     */
    @Test
    public void testMembershipNumberIsValidAndInDatabase(){
        memLogic.registerMembershipToLocalDatabase(membershipNumber);
        assertTrue(memLogic.checkIfMembershipNumberValid(membershipNumber));
    }

    /**
     * Selecting a null membership should throw a NullPointerException
     */
    @Test
    public void testSelectedMembershipIsNull(){
        assertThrows(NullPointerException.class, () -> memLogic.setSelectedMembership(null));
    }

    /**
     * Selecting an empty membership should throw a NullPointerException
     */
    @Test
    public void testSelectedMembershipIsEmpty(){
        assertThrows(NullPointerException.class, () -> memLogic.setSelectedMembership(""));
    }

    /**
     * Selecting a membership that is not in the database should throw 
     * a NullPointerException
     */
    @Test
    public void testSelectedMembershipIsNotFound(){
        assertThrows(NullPointerException.class, () -> memLogic.setSelectedMembership(membershipNumber));
    }

    /**
     * Checks that the correct membership is selected with no exceptions
     */
    @Test
    public void testSetSelectedMembership(){
        memLogic.registerMembershipToLocalDatabase(membershipNumber);
        memLogic.setSelectedMembership(membershipNumber);
        Membership selectedMem = memLogic.getSelectedMembership();
        assertEquals(membershipNumber, selectedMem.getMembershipNumber());
    }
}
