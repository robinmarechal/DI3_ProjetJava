package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Created by Robin on 07/04/2017.
 */
public class EmployeeTest {

    private Employee e1, e2, e3, e4, e5;
    private int nextId;
    private int nbEmployees;

    @Before
    public void setUp() throws Exception {
        nextId = Employee.getNextId();
        nbEmployees = Company.getCompany().getNbEmployees();
        e1 = new Employee("abc", "def");
        e2 = new Employee("emp", "loyer");
        e3 = new Employee("test", "test");
    }

    @Test
    public void basics() throws Exception
    {
        assertEquals(nextId + 2, e3.getId());
        assertEquals(nextId + 3, Employee.getNextId());

        assertEquals(nbEmployees + 3, Company.getCompany().getNbEmployees());
        assertEquals(e1, Company.getCompany().getEmployee(e1.getId()));

        Company.getCompany().fire(e2);

        assertEquals(nbEmployees + 2, Company.getCompany().getNbEmployees());
        assertNull(Company.getCompany().getEmployee(2));

        try{
            e4 = new Employee("test2", "test2", 2);
        }catch(Exception e)
        {
            fail("Exception shouldn't have been thrown : Employee with id 2 should have been created succesfuly.");
        }

        try{
            e5 = new Employee("aze", "aze", 2);
            fail("Exception should have been thrown : Employee with id 2 already exists.");
        }catch (Exception e) {}
    }
}