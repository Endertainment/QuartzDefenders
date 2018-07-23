/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.endertainment.quartzdefenders.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Admin
 */
public class DataAdapterTest {

    public DataAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getWoolByColor method, of class DataAdapter.
     */
    @Test
    public void testGetWoolByColor() {
        System.out.println("getWoolByColor");
        for (DyeColor color : DyeColor.values()) {
            String expResult = color.name().toUpperCase()+"_WOOL";
            System.out.println(expResult);
            Material result = DataAdapter.getWoolByColor(color);
            assertNotNull(result);
        }
    }
    
    /**
     * Test of getGlassPaneByColor method, of class DataAdapter.
     */
    @Test
    public void testGetGlassPaneByColor() {
        System.out.println("getGlassPaneByColor");
        for (DyeColor color : DyeColor.values()) {
            String expResult = color.name().toUpperCase()+"_STAINED_GLASS_PANE";
            System.out.println(expResult);
            Material result = DataAdapter.getGlassPaneByColor(color);
            assertNotNull(result);
        }
    }

    /**
     * Test of getGlassByColor method, of class DataAdapter.
     */
    @Test
    public void testGetGlassByColor() {
        System.out.println("getGlassByColor");
        for (DyeColor color : DyeColor.values()) {
            String expResult = color.name().toUpperCase()+"_STAINED_GLASS";
            System.out.println(expResult);
            Material result = DataAdapter.getGlassByColor(color);
            assertNotNull(result);
        }
    }

    /**
     * Test of getMaterialByColor method, of class DataAdapter.
     */
    @Test
    public void testGetMaterialByColor() {
        System.out.println("getMaterialByColor");
        for (DyeColor color : DyeColor.values()) {
            String expResult = color.name().toUpperCase()+"_WOOL";
            System.out.println(expResult);
            Material result = DataAdapter.getMaterialByColor(color,"WOOL");
            assertNotNull(result);
        }
    }

    /**
     * Test of getDyeColor method, of class DataAdapter.
     */
    @Test
    public void testGetDyeColor() {
        System.out.println("getDyeColor");
        assertEquals(DyeColor.RED, DataAdapter.getDyeColor(ChatColor.RED));
        assertEquals(DyeColor.LIGHT_BLUE, DataAdapter.getDyeColor(ChatColor.BLUE));
        assertEquals(DyeColor.GRAY, DataAdapter.getDyeColor(ChatColor.DARK_GRAY));
        assertEquals(DyeColor.LIGHT_GRAY, DataAdapter.getDyeColor(ChatColor.GRAY));
    }

}
