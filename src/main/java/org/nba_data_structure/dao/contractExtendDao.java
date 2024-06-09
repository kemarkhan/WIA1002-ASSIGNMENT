package org.nba_data_structure.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nba_data_structure.bean.playerBean;
import org.nba_data_structure.data_structures.Queue;
import org.nba_data_structure.util.dbConnection;

public class contractExtendDao {

    public void updateContract(Queue<playerBean> targets) throws SQLException {
        Connection con = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish connection
            con = dbConnection.createConnection();
            // Set auto-commit to false
            con.setAutoCommit(false);

            // Delete query
            String deleteQuery = "DELETE FROM queue";
            deleteStatement = con.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();

            // Insert query
            String insertQuery = "INSERT INTO queue (name, ID) VALUES (?, ?)";
            insertStatement = con.prepareStatement(insertQuery);

            // Use temp to reverse order of targets
            Queue<playerBean> temp = new Queue<>();
            while (!targets.isEmpty()) {
                temp.enqueue(targets.dequeue());
            }

            while (!temp.isEmpty()) {
                playerBean bean = temp.dequeue();
                insertStatement.setString(1, bean.getName());
                insertStatement.setInt(2, bean.getId());
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
                System.out.println("contract saved successfully.");
            } else {
                System.out.println("Failed to insert some contract");
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


    public Queue<playerBean> getContract() throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Queue<playerBean> q = new Queue<>();

        try {
            con = dbConnection.createConnection();
            // Create a scrollable ResultSet
            String VALID_QUERY = "SELECT * FROM queue";
            preparedStatement = con.prepareStatement(VALID_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                playerBean bean = new playerBean();
                bean.setName(resultSet.getString("name"));
                bean.setId(resultSet.getInt("ID"));
                q.enqueue(bean);
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
        return q;
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
