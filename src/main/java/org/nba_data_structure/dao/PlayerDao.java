package org.nba_data_structure.dao;

import java.sql.Connection;
import org.nba_data_structure.data_structures.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.nba_data_structure.util.dbConnection;
import org.nba_data_structure.bean.playerBean;

public class PlayerDao {
    public ArrayList<playerBean> getTeam() throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<playerBean> players = new ArrayList<>();

        try {
            con = dbConnection.createConnection();
            String VALID_QUERY = "SELECT * FROM team";
            preparedStatement = con.prepareStatement(VALID_QUERY);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                playerBean player = new playerBean();
                player.setId(resultSet.getInt("id"));
                player.setName(resultSet.getString("name"));
                player.setHeight(resultSet.getString("height"));
                player.setWeight(resultSet.getDouble("weight"));
                player.setPosition(resultSet.getString("position"));
                player.setSalary(resultSet.getDouble("salary"));
                player.setPoints(resultSet.getDouble("points"));
                player.setRebounds(resultSet.getDouble("rebounds"));
                player.setAssists(resultSet.getDouble("assists"));
                player.setSteals(resultSet.getDouble("steals"));
                player.setBlocks(resultSet.getDouble("blocks"));

                players.add(player);
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
        return players;
    }

    public void update(ArrayList<playerBean> targets) throws SQLException {
        Connection con = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement insertStatement = null;

        try {
            // Establish connection
            con = dbConnection.createConnection();
            // Set auto-commit to false
            con.setAutoCommit(false);

            // Delete query
            String deleteQuery = "DELETE FROM team";
            deleteStatement = con.prepareStatement(deleteQuery);
            deleteStatement.executeUpdate();

            // Insert query
            String insertQuery = "INSERT INTO team (id, name, height, weight, position, salary, points, rebounds, assists, steals, blocks) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            insertStatement = con.prepareStatement(insertQuery);

            // Set parameters for each player
            for (playerBean player : targets) {
                insertStatement.setInt(1, player.getId()); // id
                insertStatement.setString(2, player.getName()); // name
                insertStatement.setString(3, player.getHeight()); // height
                insertStatement.setDouble(4, player.getWeight()); // weight
                insertStatement.setString(5, player.getPosition()); // position
                insertStatement.setDouble(6, player.getSalary()); // salary
                insertStatement.setDouble(7, player.getPoints()); // points
                insertStatement.setDouble(8, player.getRebounds()); // rebounds
                insertStatement.setDouble(9, player.getAssists()); // assists
                insertStatement.setDouble(10, player.getSteals()); // steals
                insertStatement.setDouble(11, player.getBlocks()); // blocks
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
                System.out.println("Players inserted successfully.");
            } else {
                System.out.println("Failed to insert some players.");
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
                deleteStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }
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
