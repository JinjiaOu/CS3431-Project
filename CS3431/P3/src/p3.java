// Ryan Rabbitt, Ethan Moynihan, Jinjia Ou
import java.sql.*;
/*import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;*/
import java.util.Scanner;
//

public class p3 {

    public static void main(String[] args) throws SQLException {
        if (args.length == 0)
        {
            System.out.println("You need to include your UserID and Password parameters on the command line");
        }
        else if(args.length < 3)
        {
            System.out.println("Include the number of the following menu item as the third parameter on the \n" +
                    "command line.\n" +
                    "1 - Report Patient Information\n" +
                    "2 - Report Employee Information\n" +
                    "3 - Update Employee's Password");
        }
        else {
            System.out.println("-------- Oracle JDBC Connection Testing ------");
            System.out.println("-------- Step 1: Registering Oracle Driver ------");
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your Oracle JDBC Driver? Did you follow the execution steps. ");
                System.out.println("");
                System.out.println("*****Open the file and read the comments in the beginning of the file****");
                System.out.println("");
                e.printStackTrace();
                return;
            }
            System.out.println("Oracle JDBC Driver Registered Successfully !");
            System.out.println("-------- Step 2: Building a Connection ------");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(
                        "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl",
                        args[0],
                        args[1]);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
            }
            if (connection != null) {
                System.out.println("You made it. Connection is successful. Take control of your database now!");
            } else {
                System.out.println("Failed to make connection!");
            }


            // start
            Scanner s = new Scanner(System.in);

            if (args[2].equals("1")) {
                System.out.println("Enter Patient First Name :");
                String first = s.nextLine();

                System.out.println("Enter Patient Last Name :");
                String last = s.nextLine();
                String sql = "SELECT patientid, firstname || ' ' || lastname as fullname, city || ', ' || state as address from patient where firstname = ? and lastname = ?";
                PreparedStatement pstatement = connection.prepareStatement(sql);
                pstatement.setString(1, first);
                pstatement.setString(2, last);
                ResultSet rset = pstatement.executeQuery();
                int patientID = 0;
                String fullName = "", address = "";
                while (rset.next()) {
                    patientID = rset.getInt("patientid");
                    fullName = rset.getString("fullname");
                    address = rset.getString("address");
                    System.out.println("Patient Information\nPatient ID: " + patientID + "\nPatient Name: " + fullName + "\nAddress: " + address);
                }

            } else if (args[2].equals("2")) {
                System.out.println("Enter Employee ID:");
                int employee = s.nextInt();

                String sql = "SELECT employeeid, npi, firstname || ' ' || lastname as fullname, username, password, salaryGrade, securityClearance from employee where employeeid = ?";
                PreparedStatement pstatement = connection.prepareStatement(sql);
                pstatement.setInt(1, employee);
                ResultSet rset = pstatement.executeQuery();
                int employeeid = 0, salary = 0;
                String npi = "";
                String fullName = "", username = "", password = "", security = "";
                while (rset.next()) {
                    employeeid = rset.getInt("employeeid");
                    npi = rset.getString("npi");
                    fullName = rset.getString("fullname");
                    username = rset.getString("username");
                    password = rset.getString("password");
                    salary = rset.getInt("salarygrade");
                    security = rset.getString("securityclearance");
                    System.out.println("Employee Information\nEmployee ID: " + employeeid);
                    if (npi != null) {
                        System.out.println("NPI: " + npi + "\nEmployee Name: Dr. " + fullName + "\nUsername: " + username + "\nPassword: " + password);
                    } else {
                        System.out.println("Employee Name: " + fullName + "\nUsername: " + username + "\nPassword: " + password);
                    }
                    if (salary != 0) {
                        System.out.println("Salary Grade: " + salary);
                    }
                    if (security != null) {
                        System.out.println("Security Clearance: " + security);
                    }
                }
            } else if (args[2].equals("3")) {
                System.out.println("Enter the employee ID:");
                String user = s.nextLine();

                System.out.println("Enter the updated password:");
                String password = s.nextLine();
                String sql = "update Employee set password = ? where employeeid = ?";
                PreparedStatement pstatement = connection.prepareStatement(sql);
                pstatement.setString(1, password);
                pstatement.setString(2, user);
                int rset = pstatement.executeUpdate();
                if (rset == 1) {
                    System.out.println("Your password was updated.");
                }
            }

            connection.close();
        }
    }
}

