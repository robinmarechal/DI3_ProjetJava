package views.dialogs;

import controllers.EmployeesController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.util.form.FieldTypes;
import lib.util.form.FieldValueTypes;
import lib.util.form.Form;
import lib.views.custom.components.Dialog;
import models.Employee;
import models.Manager;
import models.StandardDepartment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class EditEmployeeDialog extends Dialog implements Initializable
{
    private Employee employee;
    private ObservableList<StandardDepartment> departments;

    private final String title = "Update the employee";

    @FXML private Stage dialog;
    @FXML private Label labTitle;
    @FXML private TextField fieldFirstName;
    @FXML private TextField fieldLastName;
    @FXML private ComboBox comboDepartments;
    @FXML private Button btnSubmit;
    @FXML private TextField startingHourHour;
    @FXML private TextField startingHourMin;
    @FXML private TextField endingHourHour;
    @FXML private TextField endingHourMin;
    @FXML private CheckBox cbManager;

    public EditEmployeeDialog (Employee employee, ObservableList<StandardDepartment> list)
    {
        this.employee = employee;
        this.departments = list;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/fxml/createEmployee.fxml"));
        loader.setController(this);

        try
        {
            dialog = loader.load();
        }
        catch (IOException e)
        {
            dialog = new Stage();
            dialog.setTitle("Error");
            System.out.println("Failed to load employee's edition dialog...");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        dialog.setTitle(title);
        labTitle.setText(title);
        setDialogShortcut(dialog);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.show();

        comboDepartments.setItems(departments);

        fieldFirstName.setText(employee.getFirstName());
        fieldLastName.setText(employee.getLastName());
        startingHourHour.setText(employee.getStartingHour().getHour() + "");
        startingHourMin.setText(employee.getStartingHour().getMinute() + "");
        endingHourHour.setText(employee.getEndingHour().getHour() + "");
        endingHourMin.setText(employee.getEndingHour().getMinute() + "");
        cbManager.setSelected(employee instanceof Manager);


        Form form = new Form();
        form.add("firstName", FieldValueTypes.FIRSTNAME, fieldFirstName);
        form.add("lastName", FieldValueTypes.LASTNAME, fieldLastName);
        form.add("startingHourHour", FieldValueTypes.HOURS, startingHourHour);
        form.add("startingHourMinutes", FieldValueTypes.MINUTES, startingHourMin);
        form.add("endingHourHour", FieldValueTypes.HOURS, endingHourHour);
        form.add("endingHourMinutes", FieldValueTypes.MINUTES, endingHourMin);
        form.add("department", FieldValueTypes.UNDEFINED, comboDepartments, FieldTypes.COMBOBOX);
        form.add("cbManager", FieldValueTypes.UNDEFINED, cbManager, FieldTypes.CHECKBOX);

        btnSubmit.setOnAction(event -> new Thread(() -> Platform.runLater(() ->
        {
            if (new EmployeesController().updateEmployee(employee, form))
            {
                dialog.close();
            }
        })).start());
    }
}

