import java.sql.*;

public class MySQLSource {
    private Connection connect = null;
    private Statement statement = null;
    private final PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    private final String SQLConnect =
            "jdbc:mysql://localhost:3306/bankusers?autoReconnect=true&useSSL=false";
    

    public boolean isUserValid(String user, String password) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from users "
                            + "where username = '" + user +"' "
                            + "and password = '" + password + "'");
            //String records = resultSet.getString("count(*)");
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
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
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
    
    public Double getBalance(String username) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select t.balance "
                            + "from transactions t and user u "
                            + "where t.user = u.id "
                            + "and u.username = '" + username + "' "
                            + "ORDER BY t.id DESC LIMIT 1");
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
    
    public int getUserID (String username) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager.getConnection(SQLConnect, "root", "root");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
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
        
        try {
            connect = DriverManager.getConnection(SQLConnect, "root", "root");
            connect.setAutoCommit(false);
//            System.out.println(connect);
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
            
        /*} catch (ClassNotFoundException | SQLException e) {
            throw e;*/
        } finally {
            close();
        }
        
        createTransaction(username, deposit);
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
                    + deposit + "' where user = '" + ID + "'");
          
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
