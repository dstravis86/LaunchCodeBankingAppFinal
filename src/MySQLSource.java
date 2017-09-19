import java.sql.*;

public class MySQLSource {
    private Connection connect = null;
    private Statement statement = null;
    private final PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    private final String SQLConnect = "jdbc:mysql://localhost:3306/bankusers";
    

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
            Double balance = resultSet.getDouble("balance");
  
            return balance;
        } catch (ClassNotFoundException | SQLException e) {
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
