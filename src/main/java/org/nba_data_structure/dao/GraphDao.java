package org.nba_data_structure.dao;

import java.sql.Connection;
import org.nba_data_structure.data_structures.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.nba_data_structure.util.dbConnection;
import org.nba_data_structure.bean.GraphBean;

public class GraphDao {
    public ArrayList<GraphBean> getAllNode() throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<GraphBean> graphs = new ArrayList<>();

        try {
            con = dbConnection.createConnection();
            String VALID_QUERY = "SELECT\n" +
                    "    TD.distance_id,\n" +
                    "    S.city AS source_city,\n" +
                    "    D.city AS destination_city,\n" +
                    "    TD.distance_km\n" +
                    "FROM\n" +
                    "    TeamDistances TD\n" +
                    "JOIN\n" +
                    "    NBA_teams S ON TD.source_team_id = S.team_id\n" +
                    "JOIN\n" +
                    "    NBA_teams D ON TD.destination_team_id = D.team_id;\n";
            preparedStatement = con.prepareStatement(VALID_QUERY);
            resultSet = preparedStatement.executeQuery();


//            String INSERT_QUERY = "INSERT INTO InjuryReserve (player_id, injury, status) VALUES (?,? , 'Added to Injury Reserve')";
//            preparedStatement = con.prepareStatement(INSERT_QUERY);
//            preparedStatement.setInt(1, id);
//            preparedStatement.setString(2, injury);
            while (resultSet.next()) {
                GraphBean graph = new GraphBean();
                graph.setSource_city(resultSet.getString("source_city"));
                graph.setDestination_city(resultSet.getString("destination_city"));
                graph.setWeight(resultSet.getInt("distance_km"));

                graphs.add(graph);
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
        return graphs;
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
