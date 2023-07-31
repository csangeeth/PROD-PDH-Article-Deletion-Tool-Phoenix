package util;

import io.github.cozyloon.EzConfig;

import java.sql.*;

import static common.Constants.TICKET_ID;

public class DBHelper {
    private static final String SQL_ERROR = "SQL Error";
    private String dbUrl;
    private String userName;
    private String password;

    public DBHelper(String dbUrl, String dbUserName, String dbPassword) {
        this.dbUrl = dbUrl;
        this.userName = dbUserName;
        this.password = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(dbUrl, userName, password);
        } catch (Exception e) {
            EzConfig.logERROR("DB Connection error", e);
        }
        return DriverManager.getConnection(dbUrl, userName, password);
    }

    public void executeQuery(String query) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    EzConfig.logERROR(SQL_ERROR, e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    EzConfig.logERROR(SQL_ERROR, e);
                }
        }
    }

    public void executeDBPostStepsWithLoggers(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {
        if (resultSet != null)
            try {
                resultSet.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
        if (preparedStatement != null)
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
    }

    public void executeUpdateProcedure(int dh_id) {
        Connection connection = null;
        CallableStatement callableStatement = null;

        int p_dh_prod_id = dh_id;
        String p_ticket_name = TICKET_ID;

        // Procedure call with placeholders
        String procedureCall = "{ call pdh_utils_pkg.mark_delete_product(?, ?) }";

        try {
            connection = getConnection();
            callableStatement = connection.prepareCall(procedureCall);

            // Set input parameters for the update procedure
            callableStatement.setInt(1, p_dh_prod_id);
            callableStatement.setString(2, p_ticket_name);

            // Execute the update procedure
            callableStatement.execute();

            System.out.println("Update procedure executed successfully.");

        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        } finally {
            executeDBPostStepsWithLoggers(null, connection, callableStatement);
        }
    }

    public void executeSurvivorShip() {
        Connection connection = null;
        CallableStatement callableStatement = null;


        // Procedure call with placeholders
        String procedureCall = "{ call merge_util.process_source('WPP') }";

        try {
            connection = getConnection();
            callableStatement = connection.prepareCall(procedureCall);

            // Execute the update procedure
            callableStatement.execute();

            System.out.println("SurvivorShip procedure executed successfully.");

        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        } finally {
            executeDBPostStepsWithLoggers(null, connection, callableStatement);
        }
    }

    public void executeCommit() {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // Execute the COMMIT statement
            connection.commit();
            System.out.println("COMMIT successful.");

        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        }
    }
}
