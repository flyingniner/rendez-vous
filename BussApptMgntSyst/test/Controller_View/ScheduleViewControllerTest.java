/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chip
 */
public class ScheduleViewControllerTest
{
    
    public ScheduleViewControllerTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    
    @Test
    public void testparseTimeField()
    {
        //assign        
        int hour = 12;
        int min = 00;
        String period = "AM";
        ScheduleViewController instance = new ScheduleViewController();
        //act
        LocalTime actual = instance.parseTimeField(hour, min, period);
        //assert
        assertEquals(LocalTime.of(00, 00), actual);
    }
    
}

