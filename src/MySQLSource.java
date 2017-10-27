import java.sql.*;
import javax.swing.JOptionPane;

public class MySQLSource {
    private Connection connect = null;
    private Statement statement = null;
    private final PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    private final String SQLConnect =
            "jdbc:mysql://localhost:3306/bankusers?"
            + "autoReconnect=true&useSSL=false";
    

    public boolean isUserValid(String user, String password) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select * from users "
                            + "where username = '" + user +"' "
                            + "and password = '" + password + "'");
            if (resultSet.next()) {
                return true;
            }
            else {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public Double getUserBalance(int id) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select balance from users where id = "
                            + "'" + id + "'");
            if (resultSet.next()) {
                Double balance = resultSet.getDouble("balance");
  
                return balance;
            }
            else {
                return 0.0;
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public Double getTransactionsAmount(String username) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select t.amount "
                            + "from transactions t, users u "
                            + "where t.user = u.id "
                            + "and u.username = '" + username + "' "
                            + "ORDER BY t.id DESC LIMIT 1");
            if (resultSet.next()) {
                Double balance = resultSet.getDouble("amount");
  
                return balance;
            }
            else {
                return 0.0;
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public int getUserID (String username) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select id "
                            + "from users "
                            + "where username = '" + username + "'");
            
            if (resultSet.next()) {
                int UserID = resultSet.getInt("id");
  
                return UserID;
            }
            else {
                return -1;
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public void createTransaction(String username, double deposit) throws Exception {
        int primaryKey = getUserID(username);
        
        Statement writeSet = null;
        
        try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
            writeSet = connect.createStatement();
        
            writeSet.addBatch("insert into transactions(amount, balance, user)"
                    + " values('" + deposit + "',"
                    + " '" + deposit + "',"
                    + " '" + primaryKey + "')");
            
            int[] updateCounts = writeSet.executeBatch();
            connect.commit();
        
        } catch (SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public void createUser(String username, String password, int socialNumber,
            double deposit, String accountType) throws Exception {
        
        Statement writeSet = null;
        
        isUserValid(username,password);
        
        if (isUserValid(username,password) == true) {
            JOptionPane.showMessageDialog(null,
                    "A user of the same information exists. "
                            + "Please choose different a different username or "
                            + "password.");
        }
        
        else {
            try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
            writeSet = connect.createStatement();
            
            writeSet.addBatch("insert into users"
                    + "(username,password,socialNum,balance,accounttype)"
                    + " values('" + username + "',"
                    + " '" + password + "',"
                    + " '" + socialNumber + "',"
                    + " '" + deposit + "',"
                    + " '" + accountType + "')");
          
            int[] updateCounts = writeSet.executeBatch();
            connect.commit();
            
            } finally {
                close();
            }
        
            createTransaction(username, deposit);
        }
        
    }
    
    public Double checkBalance(int ID) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select balance from users where"
                            + " id = '" + ID + "'");
            if (resultSet.next()) {
                Double balance = resultSet.getDouble("balance");
  
                return balance;
            }
            else {
                return 0.0;
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }
    
    public void updateUser(int ID, double deposit) throws Exception {
        Statement writeSet = null;
        
        try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
            writeSet = connect.createStatement();
            
            writeSet.addBatch("update users set balance = '"
                    + deposit + "' where id = '" + ID + "'");
          
            int[] updateCounts = writeSet.executeBatch();
            connect.commit();
            
        } finally {
            close();
        }
    }
    
    public void updateTransactionsAmount(int ID, double deposit) throws Exception {
        Statement writeSet = null;
        
        try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
            writeSet = connect.createStatement();
            
            writeSet.addBatch("update transactions set amount = '"
                    + deposit + "' where user = '" + ID + "'");
          
            int[] updateCounts = writeSet.executeBatch();
            connect.commit();
            
        } finally {
            close();
        }
    }
    
    public void updateTransactionsBalance(int ID, double deposit) throws Exception {
        Statement writeSet = null;
        
        try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
            writeSet = connect.createStatement();
            
            writeSet.addBatch("update transactions set balance = '"
                    + deposit + "' where user = '" + ID + "'");
          
            int[] updateCounts = writeSet.executeBatch();
            connect.commit();
            
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}
