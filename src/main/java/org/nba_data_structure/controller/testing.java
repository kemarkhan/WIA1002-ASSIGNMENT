package org.nba_data_structure.controller;
import java.sql.SQLException;
import java.util.Scanner;

import org.nba_data_structure.bean.compositeBean;
import org.nba_data_structure.dao.PlayerDao;
import org.nba_data_structure.bean.playerBean;
import org.nba_data_structure.data_structures.ArrayList;
import org.nba_data_structure.data_structures.Stack;
import org.nba_data_structure.util.balldontlie;


public class testing {
    public static ArrayList<playerBean> team;
    public static Stack<playerBean> injured;
    public static void main(String[] args) throws SQLException {
        PlayerDao dao = new PlayerDao();
        int f = 0, g = 0, c = 0;
        balldontlie ball = new balldontlie();
        ArrayList<playerBean> players = ball.getPlayers();
        ArrayList<playerBean> team = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            System.out.println("index = " + i + " " + players.get(i).toString());
        }

        try {
            dao.update(players);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void sortArr(ArrayList<playerBean> unsort) {
        ArrayList<compositeBean> com = new ArrayList<>();

        for (int i = 0; i < unsort.size(); i++) {
            compositeBean bean = new compositeBean();
            bean.setId(unsort.get(i).getId());
            bean.setName(unsort.get(i).getName());
            bean.setCompositeScore(calculateCompositeScore(unsort.get(i)));
            com.add(bean);
        }

        // Bubble sort implementation to sort com based on compositeScore
        for (int i = 0; i < com.size() - 1; i++) {
            for (int j = 0; j < com.size() - i - 1; j++) {
                if (com.get(j).getCompositeScore() > com.get(j + 1).getCompositeScore()) {
                    // Swap com[j] and com[j + 1]
                    compositeBean temp = com.get(j);
                    com.set(j, com.get(j + 1));
                    com.set(j + 1, temp);
                }
            }
        }
        int j = 1;
        for (int i = com.size() - 1; i > 0; i--) {
            System.out.println("rank = " + j + " " + com.get(i).toString());
            j++;
        }
    }

    public static double calculateCompositeScore(playerBean player) {
        double compositeScore = 0;
        double pPts, pRb, pBlk, pStl, pAss;

        switch (player.getPosition()) {
            case "G":
                pPts = player.getPoints() * 0.10;
                pRb = player.getRebounds() * 0.30;
                pBlk = player.getBlocks() * 0.25;
                pStl = player.getSteals() * 0.25;
                pAss = player.getAssists() * 0.10;
                break;
            case "C":
                pPts = player.getPoints() * 0.25;
                pRb = player.getRebounds() * 0.20;
                pBlk = player.getBlocks() * 0.15;
                pStl = player.getSteals() * 0.15;
                pAss = player.getAssists() * 0.25;
                break;
            case "F":
                pPts = player.getPoints() * 0.30;
                pRb = player.getRebounds() * 0.15;
                pBlk = player.getBlocks() * 0.15;
                pStl = player.getSteals() * 0.10;
                pAss = player.getAssists() * 0.30;
                break;
            case "F-C":
                pPts = player.getPoints() * 0.30;
                pRb = player.getRebounds() * 0.15;
                pBlk = player.getBlocks() * 0.15;
                pStl = player.getSteals() * 0.10;
                pAss = player.getAssists() * 0.30;
                break;
            case "F-G":
                pPts = player.getPoints() * 0.30;
                pRb = player.getRebounds() * 0.15;
                pBlk = player.getBlocks() * 0.15;
                pStl = player.getSteals() * 0.10;
                pAss = player.getAssists() * 0.30;
                break;
            default:
                throw new IllegalArgumentException("Invalid player position: " + player.getPosition());
        }

        compositeScore = pPts + pRb + pBlk + pStl + pAss;
        return compositeScore;
    }




//    public static ArrayList<playerBean> getAllPlayers() {
//        PlayerDao playerDao = new PlayerDao();
//        ArrayList<playerBean> players = new ArrayList<>();
//        try {
//            ArrayList<playerBean> _players = playerDao.getAllPlayers();
//            for (int i = 0; i < _players.size(); i++) {
//                players.add(_players.get(i));
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//        return players;
//    }

//    public static void DynamicSearch(ArrayList<playerBean> roster, String target) {
//        ArrayList<playerBean> candidates = new ArrayList<>();
//        playerBean  t = null;
//        for (int i = 0; i < roster.size(); i++) {
//            if (roster.get(i).getName().equals(target)) {
//                t = roster.get(i);
//            }
//        }
//        for (int i = 0; i < roster.size(); i++) {
//            double height1Value = Double.parseDouble(roster.get(i).getHeight().substring(0, roster.get(i).getHeight().length() - 1));
//            double height2Value = Double.parseDouble(t.getHeight().substring(0, t.getHeight().length() - 1));
//
//            if (height1Value >= height2Value && roster.get(i).getWeight()  <= t.getWeight() && roster.get(i).getPosition().equals(t.getPosition())) {
//                candidates.add(roster.get(i));
//            }
//        }
//        for (int i = 0; i < candidates.size(); i++) {
//            System.out.println(candidates.get(i).toString());
//        }
//    }


//    public static void addMemTeam(playerBean player){
//        team.add(player);
//        System.out.println("member has been added to the team");
//    }

//    public static void delMemTeam(playerBean player){
//        for (int i = 0; i < team.size(); i++) {
//            if(team.get(i).equals(player)){
//                team.remove(i);
//                System.out.println(team.get(i).getName() + "has been removed");
//            }else{
//                System.out.println("player is not in the team");
//            }
//        }
//    }


    public static void validateTeam(ArrayList<playerBean> target){
        ArrayList<String> errorMessages = new ArrayList<>();

        if (!teamSizeReq(target)) {
            errorMessages.add("The team does not comply with the size requirements.");
        }
        if (!salaryCapLimitReq(target)) {
            errorMessages.add("The team does not comply with the salary cap limitations.");
        }
        if (!positionalReq(target)) {
            errorMessages.add("The team does not comply with the positional requirements.");
        }

        if (errorMessages.isEmpty()) {
            System.out.println("The team has met all the requirements.");
        } else {
            for (String message : errorMessages) {
                System.out.println(message);
            }
        }
    }

    public static boolean teamSizeReq(ArrayList<playerBean> team){
        return team.size() >= 10 && team.size() < 15;
    }

    public static boolean salaryCapLimitReq(ArrayList<playerBean> team) {
        int sum = 0;
        for (playerBean player : team) {
            sum += player.getSalary();
        }

        boolean state = sum <= 20_000;

        if (team.size() < 10) {
            System.out.println("The team has fewer than 10 players. You can add more players.");
        }

        return state;
    }

    public static boolean positionalReq(ArrayList<playerBean> team){
        int guardCount = 0, forwardCount = 0, centerCount = 0;

        for(playerBean player : team) {
            switch(player.getPosition()){
                case "Guard": guardCount++; break;
                case "Forward": forwardCount++; break;
                case "Center": centerCount++; break;
            }
        }

        return guardCount >= 2 && forwardCount >= 2 && centerCount >= 2;
    }


    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
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
