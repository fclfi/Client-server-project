package Chatserver;

import java.sql.*;

/**
 *
 * @author Asus
 */
public final class DatabaseConnection {

    private Connection myCon = null;
    private Statement myStat = null;
    private ResultSet myRes = null;

    public DatabaseConnection() {}

    private void initiate(String str) throws SQLException {
        // 1 - Get a connection to database (ALSO, MAKING SURE THE SERVER AND DATABASE ARE IN THE SAME TIMEZONE)
        myCon = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test01?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "root", "joka20001");

        // 2 - Create a Statement
        myStat = myCon.createStatement();

        // 3 - Execute SQL query
        if (str != null) {
            myRes = myStat.executeQuery(str);
        }
    }

    public boolean checkLogin(Login newlog) {
        boolean aux = false;
        try {
            initiate("select * from user");

            while (myRes.next()) {
                if (newlog.getUsername().equals(myRes.getString("username"))) {
                    if (newlog.getPassword().equals(myRes.getString("password"))) {
                        aux = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseConnection class - checkLogin()" + e);
            System.out.println(e);
        } finally {
            try {
                if (myRes != null) {
                    myRes.close();
                }
                if (myStat != null) {
                    myStat.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                System.out.println("Exception in DatabaseConnection class - 2 checkLogin()" + e);
            }
        }
        return aux;
    }

    public boolean newClient(Login newlog) {
        try {
            initiate(null);
            myStat.executeUpdate("insert into user (username, password) values ('" + newlog.getUsername() + "', '" + newlog.getPassword() + "')");
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseConnection class - newClient()" + e);
            return false;
        } finally {
            try {
                if (myRes != null) {
                    myRes.close();
                }
                if (myStat != null) {
                    myStat.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                System.out.println("Exception in DatabaseConnection class - 2 newClient()" + e);
                return false;
            }
        }
        return true;
    }

    public void deleteClient(String name) {
        try {
            initiate(null);
            myStat.executeUpdate("delete from user where username='" + name + "'");
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseConnection class - deleteClient()" + e);
        } finally {
            try {
                if (myRes != null) {
                    myRes.close();
                }
                if (myStat != null) {
                    myStat.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                System.out.println("Exception in DatabaseConnection class - 2 deleteClient()" + e);
            }
        }
    }
    
    public boolean changeUsername(String oldname, String newname) {
        //UPDATE user set username = 'qwe' where username = 'rty';
        try {
            initiate(null);
            myStat.executeUpdate("update user set username='" + newname + "' where username='" + oldname + "'");
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseConnection class - changeUsername()" + e);
            return false;
        } finally {
            try {
                if (myRes != null) {
                    myRes.close();
                }
                if (myStat != null) {
                    myStat.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                System.out.println("Exception in DatabaseConnection class - 2 changeUsername()" + e);
                return false;
            }
        }
        return true;
    }
    
    public boolean changePassword(String oldpass, String newpass) {
        //UPDATE user set username = 'qwe' where username = 'rty';
        try {
            initiate(null);
            myStat.executeUpdate("update user set password='" + newpass + "' where password='" + oldpass + "'");
        } catch (SQLException e) {
            System.out.println("Exception in DatabaseConnection class - changePassword()" + e);
            return false;
        } finally {
            try {
                if (myRes != null) {
                    myRes.close();
                }
                if (myStat != null) {
                    myStat.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                System.out.println("Exception in DatabaseConnection class - 2 changePassword()" + e);
                return false;
            }
        }
        return true;
    }
}
