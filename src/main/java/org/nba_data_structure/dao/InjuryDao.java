package org.nba_data_structure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nba_data_structure.bean.injuryResBean;
import org.nba_data_structure.bean.playerBean;
import org.nba_data_structure.data_structures.ArrayList;
import org.nba_data_structure.data_structures.Stack;
import org.nba_data_structure.util.dbConnection;

public class InjuryDao {

    public void updateInjured(Stack<injuryResBean> targets) throws SQLException {
        Connection con = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish connection
            con = dbConnection.createConnection();
            // Set auto-commit to false
            con.setAutoCommit(false);

            // Delete query
            String deleteQuery = "DELETE FROM injuryreserve";
            deleteStatement = con.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();

            // Insert query
            String insertQuery = "INSERT INTO injuryreserve (id, name, injury, status) VALUES (?, ?, ?, ?)";
            insertStatement = con.prepareStatement(insertQuery);

            // Use temp stack to reverse order of targets
            Stack<injuryResBean> temp = new Stack<>();
            while (!targets.isEmpty()) {
                temp.push(targets.pop());
            }

            while (!temp.isEmpty()) {
                injuryResBean bean = temp.pop();
                insertStatement.setInt(1, bean.getId());
                insertStatement.setString(2, bean.getName());
                insertStatement.setString(3, bean.getInjury());
                insertStatement.setString(4, bean.getStatus());
                insertStatement.addBatch();
            }

            // Execute batch
            int[] rowsAffected = insertStatement.executeBatch();

            // Commit transaction
            con.commit();

            boolean allInsertsSuccessful = true;
            for (int i : rowsAffected) {
                if (i != PreparedStatement.SUCCESS_NO_INFO && i != 1) {
                    allInsertsSuccessful = false;
                    break;
                }
            }

            if (allInsertsSuccessful) {
                System.out.println("injured inserted successfully.");
            } else {
                System.out.println("Failed to insert some injured");
            }

        } catch (SQLException e) {
            printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    printSQLException(rollbackEx);
                }
            }
        } finally {
            // Close resources
            if (deleteStatement != null) {
                try {
                    deleteStatement.close();
                } catch (SQLException e) {
                    printSQLException(e);
                }
            }
            if (insertStatement != null) {
                try {
                    insertStatement.close();
                } catch (SQLException e) {
                    printSQLException(e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    printSQLException(e);
                }
            }
        }
    }


    public Stack<injuryResBean> getInjured() throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Stack<injuryResBean> reserve = new Stack<>();

        try {
            con = dbConnection.createConnection();
            // Create a scrollable ResultSet
            String VALID_QUERY = "SELECT * FROM injuryreserve";
            preparedStatement = con.prepareStatement(VALID_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injuryResBean bean = new injuryResBean();
                bean.setId(resultSet.getInt("id"));
                bean.setName(resultSet.getString("name"));
                bean.setStatus(resultSet.getString("status"));
                bean.setInjury(resultSet.getString("injury"));
                reserve.push(bean);
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        } finally {
            // Close resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return reserve;
    }




    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
