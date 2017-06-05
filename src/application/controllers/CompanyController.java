package application.controllers;

import application.lib.BaseController;
import application.lib.annotations.DisplayView;
import application.lib.annotations.UpdateModel;
import application.lib.form.Form;
import application.lib.views.Tabs;
import application.lib.views.Template;
import application.models.Boss;
import application.models.Company;
import application.models.ManagementDepartment;
import application.views.CompanyViewController;
import application.views.company.HomeCompany;
import application.views.dialogs.EditCompanyDialog;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Robin on 26/04/2017. <br/>
 * MVC Controller class that handle every <br/>
 * actions related to the company data only
 */
public class CompanyController extends BaseController
{
    /**
     * Display the Company's tab home view
     */
    @Override
    @DisplayView
    public void home ()
    {
        CompanyViewController view = new HomeCompany(Company.getCompany(), SimpleDate.TODAY);

        Template.getInstance().setView(Tabs.COMPANY, view);
    }

    // Dialog boxes

    /**
     * Open a diamlog box to update the company's information
     */
    @DisplayView
    public void openEditCompanyDialog ()
    {
        new EditCompanyDialog(Company.getCompany());
    }


    /**
     * Update the company's information <br/>
     *
     * @param form the form containing the company's information.
     * @return  - true : The company has been updated. <br>
     *          - false : at least one field has been unvalidated, the company has not been updated.
     */
    @UpdateModel
    public boolean updateCompany (@NotNull Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return false;
        }
        else
        {
            Company company = Company.getCompany();

            Boss boss = company.getBoss();

            ManagementDepartment managementDepartment = company.getManagementDepartment();

            try
            {
                String companyName = ((TextField) form.get("companyName").getField()).getText();
                company.setName(companyName);
            }
            catch (Exception e)
            {
                System.out.println("Company's name couldn't be updated.");
            }

            try
            {
                String bossFirstName = ((TextField) form.get("bossFirstName").getField()).getText();
                String bossLastName  = ((TextField) form.get("bossLastName").getField()).getText();
                boss.setFirstName(bossFirstName);
                boss.setLastName(bossLastName);
            }
            catch (Exception e)
            {
                System.out.println("Boss's firstname and lastname couldn't be updated.");
            }

            try
            {
                String manDepName   = ((TextField) form.get("managementDepartmentName").getField()).getText();
                String manDepSector = ((TextField) form.get("managementDepartmentActivitySector").getField()).getText();
                managementDepartment.setName(manDepName);
                managementDepartment.setActivitySector((manDepSector));
            }
            catch (Exception e)
            {
                System.out.println("Management department's name and activity sector couldn't be updated.");
            }

            return true;
        }
    }

    // CSV import/export

    /**
     * Open a file chooser to import a .csv file containing employees information (including checks)
     */
    public void importEmployeesCSV ()
    {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());

        new Thread(() -> Platform.runLater(() ->
        {
            try
            {
                Company.getCompany().loadEmployeesFromCSVFile(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Your file has been imported succesfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failure");
                alert.setContentText("An error occurred, your file might have not been imported entirely.");
                alert.show();
                e.printStackTrace();
            }
        })).start();
    }

    /**
     * Open a file chooser to import a .csv file containing departments information (including list of employees)
     */
    public void importDepartmentsCSV ()
    {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());

        new Thread(() -> Platform.runLater(() ->
        {
            try
            {
                Company.getCompany().loadDepartmentsFromCSVFile(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Your file has been imported succesfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failure");
                alert.setContentText("An error occurred, your file might have not be imported entirely.");
                alert.show();
                e.printStackTrace();
            }
        })).start();
    }

    /**
     * Open a file chooser to save the employees (including checks) into a .csv file
     */
    public void exportEmployeesCSV ()
    {
        new Thread(() -> Platform.runLater(() ->
        {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            fc.setInitialFileName("employees.csv");
            File dest = fc.showSaveDialog(new Stage());

            Company.getCompany().saveEmployeesToCSV(dest);
        })).start();
    }

    /**
     * Open a file chooser to save the departments (including list of employees) into a .csv file
     */
    public void exportDepartmentsCSV ()
    {
        new Thread(() -> Platform.runLater(() ->
        {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            fc.setInitialFileName("departments.csv");
            File dest = fc.showSaveDialog(new Stage());

            Company.getCompany().saveDepartmentsToCSV(dest);
        })).start();
    }
}
