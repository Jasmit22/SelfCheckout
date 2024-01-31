package com.thelocalmarketplace.software.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.SelfCheckoutStationSoftware;
import com.thelocalmarketplace.software.general.AbstractLogicDependant;

/**
 * Logic for adding items to the system by text description.
 * This class provides functionality to search for products
 * based on a text description and then add them to the customer's cart
 * 
 * @author Ernest Shukla		(30156303)
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
 * @author Hillary Nguyen		(30161137)
 * @author Robin Bowering		(30123373)
 * @author Anne Lumumba			(30171346)
 * @author Jasmit Saroya		(30170401)
 * @author Fion Lei				(30134327)
 * @author Royce Knoepfli		(30172598) */
public class AddItemByTextLogic extends AbstractLogicDependant {

    /**
     * Constructs an instance of AddItemByTextLogic.
     *
     * @param logic The central station logic that is required for this logic.
     * @throws NullPointerException if the logic provided is null.
     */
    public AddItemByTextLogic(SelfCheckoutStationSoftware logic) {
        super(logic);
    }

    /**
     * Finds products by text description by searching through both PLU and barcoded product databases.
     *
     * @param description The text description to search for.
     * @return A list of products that match the description, sorted and without duplicates.
     * @throws IllegalArgumentException if the description is null or empty.
     */
    public List<Product> findProductByTextDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be null or empty.");
        }
        
        List<Product> allFoundItems = new ArrayList<>();

        allFoundItems.addAll(searchDatabasePLU(ProductDatabases.PLU_PRODUCT_DATABASE, description));
        allFoundItems.addAll(searchDatabaseBarcode(ProductDatabases.BARCODED_PRODUCT_DATABASE, description));

        return sortAndDeduplicate(allFoundItems);
    }

    /**
     * Searches the barcoded product database for products that match the given description.
     *
     * @param database    The barcode product database to search.
     * @param description The text description to search for.
     * @return A list of found barcoded products.
     */
    private List<Product> searchDatabaseBarcode(Map<Barcode, BarcodedProduct> database, String description) {
        List<Product> foundItems = new ArrayList<>();
        String searchDescription = description.toLowerCase();

        for (BarcodedProduct product : database.values()) {
            if (product.getDescription().toLowerCase().startsWith(searchDescription)) {
                foundItems.add(product);
            }
        }

        return foundItems;
    }

    /**
     * Searches the PLU product database for products that match the given description.
     *
     * @param database    The PLU product database to search.
     * @param description The text description to search for.
     * @return A list of found PLU-coded products.
     */
    private List<Product> searchDatabasePLU(Map<PriceLookUpCode, PLUCodedProduct> database, String description) {
        List<Product> foundItems = new ArrayList<>();
        String searchDescription = description.toLowerCase();

        for (PLUCodedProduct product : database.values()) {
            if (product.getDescription().toLowerCase().startsWith(searchDescription)) {
                foundItems.add(product);
            }
        }
        return foundItems;
    }

    /**
     * Sorts a list of products and removes duplicates.
     * Sorting is based on the product description and is case-insensitive.
     *
     * @param products The list of products to sort and deduplicate.
     * @return A new list of products that is sorted and without duplicates.
     */
    private List<Product> sortAndDeduplicate(List<Product> products) {   
        // Sort the products first
        products.sort(Comparator.comparing(p -> {
            if (p instanceof PLUCodedProduct) {
                return ((PLUCodedProduct) p).getDescription();
            } else if (p instanceof BarcodedProduct) {
                return ((BarcodedProduct) p).getDescription();
            } else {
            	throw new IllegalArgumentException("Product must be of two types PLUCodedProduct or BarcodedProduct");
            }
        }, Comparator.nullsLast(String::compareToIgnoreCase)));

        Map<String, Product> uniqueProducts = new LinkedHashMap<>();
        for (Product product : products) {
            String description = "";
            if (product instanceof PLUCodedProduct) {
                description = ((PLUCodedProduct) product).getDescription().toLowerCase();
            } else if (product instanceof BarcodedProduct) {
                description = ((BarcodedProduct) product).getDescription().toLowerCase();
            }

            uniqueProducts.putIfAbsent(description, product);
        }

        return new ArrayList<>(uniqueProducts.values());
    }
}