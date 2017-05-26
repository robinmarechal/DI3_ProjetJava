package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public class Manager extends Employee implements Jsonable
{
    /**
     * The {@link StandardDepartment} instance managed by this manager
     */
    private ObjectProperty<StandardDepartment> managedDepartment = new SimpleObjectProperty<>(this, "managedDepartment", null);

    /**
     * 2 parameters constructor <br/>
     * Creates an instance of {@link Manager} and gives it a unique ID (based on NEXT_ID static field) <br/>
     * To force a specific ID, use the 3 parameters constructor.
     *
     * @param firstName The first-name of the manager
     * @param lastName  The last-name of the manager
     */
    public Manager (String firstName, String lastName)
    {
        super(firstName, lastName);
        Company.getCompany().getManagementDepartment().addManager(this);
    }

    /**
     * 3 parameters constructor <br />
     * Creates an instance of {@link Manager} with a specific unique ID <br/>
     * If you don't know which ID to give to the manager, use the 2 parameters constructor.
     *
     * @param firstName The first-name of the manager
     * @param lastName  The last-name of the manager
     * @param id        the ID of the manager.
     * @throws Exception if there already is an {@link Manager} or {@link Employee} instance (by inheritance) with the given ID.
     */
    public Manager (String firstName, String lastName, int id)
    {
        super(firstName, lastName, id);
        Company.getCompany().getManagementDepartment().addManager(this);
    }

    /**
     * To know whether the current {@link Manager} instance is the manager of the given {@link StandardDepartment} instance.
     *
     * @param department the {@link StandardDepartment} instance you want to test
     * @return True if the manager manages the department, false altherwise.
     */
    public boolean isManagerOf (StandardDepartment department)
    {
        return this.managedDepartment.getValue() == department;
    }

    /**
     * Retrieve the {@link StandardDepartment} managed by this manager
     *
     * @return the standard department managed or null of the manger doesn't manage any department
     */
    public StandardDepartment getManagedDepartment ()
    {
        return managedDepartment.getValue();
    }

    /**
     * Fires a manager
     *
     * @return this
     */
    @Override
    public Manager fire () throws Exception
    {
        if(managedDepartment != null)
        {
            throw new Exception("Failed to fire the manager, he is still managing a department.");
        }

        super.fire();
        Company.getCompany().removeManager(this);
        return this;
    }

    /**
     * Creates a String representing a manager.
     *
     * @return
     */
    @Override
    public String toString ()
    {
        return super.toString() + " (manager)";
    }

    /**
     * Create a {@link JSONObject} from a Manager instance.
     *
     * @return the {@link JSONObject}
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = super.toJson();
        json.put("manager", true);

        return json;
    }

    /**
     * Modifies the managed department.
     *
     * @param managedDepartment the new managed department
     * @return this
     * @warning this method is unsafe and should only be used by StandardDepartment's methods.
     */
    protected Manager setManagedDepartment (StandardDepartment managedDepartment)
    {
        this.managedDepartment.setValue(managedDepartment);
        return this;
        /*
        if(this.managedDepartment != null)
        {
            StandardDepartment tmp = this.managedDepartment;
            this.managedDepartment = managedDepartment;
            tmp.setManager(null);
        }

        this.managedDepartment = managedDepartment;

        return this;*/
    }

//    public static Manager loadFromJson (Object json) throws Exception
//    {
//        int id = Integer.parseInt(json.toString());
//        Employee employee = Company.getCompany().getEmployee(id);
//        return employee.upgradeToManager();
//    }

}
