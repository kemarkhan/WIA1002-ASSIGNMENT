package org.nba_data_structure.controller;

import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.nba_data_structure.application.GraphRepresentation;
import org.nba_data_structure.bean.compositeBean;
import org.nba_data_structure.bean.injuryResBean;
import org.nba_data_structure.bean.playerBean;
import org.nba_data_structure.dao.InjuryDao;
import org.nba_data_structure.dao.PlayerDao;
import org.nba_data_structure.dao.contractExtendDao;
import org.nba_data_structure.data_structures.ArrayList;
import org.nba_data_structure.data_structures.Queue;
import org.nba_data_structure.data_structures.Stack;
import org.nba_data_structure.util.balldontlie;
import org.nba_data_structure.dao.PlayerDao;

public class DashboardController {
    private balldontlie ball = new balldontlie();
    private ArrayList<playerBean> players;
    private ArrayList<playerBean> team = new ArrayList<>();
    private Queue<playerBean> extension = new Queue<>();
    private Stack<injuryResBean> injuryRes = new Stack<>();

    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    @FXML
    private TableView<playerBean> playerTable;
    @FXML
    private TableColumn<playerBean, String> positionColumn;
    @FXML
    private TableColumn<playerBean, String> nameColumn;
    @FXML
    private TableColumn<playerBean, String> pointsColumn;
    @FXML
    private TableColumn<playerBean, Void> actionColumn;

    @FXML
    private TableView<playerBean> teamTable;
    @FXML
    private TableColumn<playerBean, String> teamPositionColumn;
    @FXML
    private TableColumn<playerBean, String> teamNameColumn;
    @FXML
    private TableColumn<playerBean, String> teamPointsColumn;
    @FXML
    private TableColumn<playerBean, Void> teamActionColumn;

    @FXML
    private TableView<injuryResBean> injuryReserveTable;
    @FXML
    private TableColumn<playerBean, String> extensionNameColumn;

    @FXML
    private TableView<playerBean> extensionTable;
    @FXML
    private TableColumn<injuryResBean, String> injuryResNameColumn;
    @FXML
    private TableColumn<injuryResBean, String> injuryResInjuryColumn;
    @FXML
    private TableColumn<injuryResBean, String> injuryResStatusColumn;

    private ObservableList<playerBean> playerData = FXCollections.observableArrayList();
    private ObservableList<playerBean> teamData = FXCollections.observableArrayList();
    private ObservableList<injuryResBean> injuryReserveData = FXCollections.observableArrayList();
    private ObservableList<playerBean> extensionData = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                textArea.appendText(String.valueOf((char) b));
            }
        }) {
            @Override
            public void write(byte[] buf, int off, int len) {
                String s = new String(buf, off, len);
                textArea.appendText(s);
            }
        };
        System.setOut(printStream);

        players = ball.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            playerData.add(players.get(i));
        }

        PlayerDao teamDao = new PlayerDao();
        team = teamDao.getTeam();
        for (int i = 0; i < team.size(); i++) {
            teamData.add(team.get(i));
        }

        InjuryDao injuryDao = new InjuryDao();
        injuryRes = injuryDao.getInjured();

        contractExtendDao contractExtendDao = new contractExtendDao();
        extension = contractExtendDao.getContract();


        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        actionColumn.setCellFactory(new Callback<TableColumn<playerBean, Void>, TableCell<playerBean, Void>>() {
            @Override
            public TableCell<playerBean, Void> call(final TableColumn<playerBean, Void> param) {
                final TableCell<playerBean, Void> cell = new TableCell<playerBean, Void>() {

                    private final Button btn = new Button("Add to Team");

                    {
                        btn.setOnAction((event) -> {
                            playerBean player = getTableView().getItems().get(getIndex());
                            addToTeam(player);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });


        playerTable.setItems(playerData);

        teamPositionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        teamPointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        teamActionColumn.setCellFactory(new Callback<TableColumn<playerBean, Void>, TableCell<playerBean, Void>>() {
            @Override
            public TableCell<playerBean, Void> call(final TableColumn<playerBean, Void> param) {
                final TableCell<playerBean, Void> cell = new TableCell<playerBean, Void>() {

                    private final Button removeFromTeamButton = new Button("Remove from Team");
                    private final Button injuredButton = new Button("Injured");
                    private final Button extendButton = new Button("Extend");

                    {
                        removeFromTeamButton.setOnAction((event) -> {
                            playerBean player = getTableView().getItems().get(getIndex());
                            removeFromTeam(player);
                        });

                        injuredButton.setOnAction((event) -> {
                            playerBean player = getTableView().getItems().get(getIndex());
                            moveToInjuryReserve(player);
                        });

                        extendButton.setOnAction((event) -> {
                            playerBean player = getTableView().getItems().get(getIndex());
                            extend(player);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(removeFromTeamButton, injuredButton, extendButton);
                            buttons.setSpacing(5);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        });

        teamTable.setItems(teamData);

        injuryResNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        injuryResInjuryColumn.setCellValueFactory(new PropertyValueFactory<>("injury"));
        injuryResStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        extensionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Populate Injury Reserve TableView with data from the injuryRes stack
        populateInjuryReserveTable();
        populateExtensionTable();

        // Add listener to teamData to validate team when it changes
        teamData.addListener((ListChangeListener<playerBean>) change -> validateTeam(team));
    }

    private void populateInjuryReserveTable() {
        // Clear existing data in the table
        injuryReserveTable.getItems().clear();

        // Iterate over the injuryRes stack and add data to the table
        Stack<injuryResBean> tempStack = new Stack<>();
        while (!injuryRes.isEmpty()) {
            injuryResBean injury = injuryRes.pop();
            injuryReserveTable.getItems().add(injury);
            tempStack.push(injury);
        }

        // Restore the stack to its original state
        while (!tempStack.isEmpty()) {
            injuryRes.push(tempStack.pop());
        }
    }

    private void populateExtensionTable() {
        // Clear existing data in the table
        extensionData.clear();

        // Iterate over the extension queue and add data to the extensionData list
        Queue<playerBean> tempQueue = new Queue<>();
        while (!extension.isEmpty()) {
            playerBean player = extension.dequeue();
            extensionData.add(player);
            tempQueue.enqueue(player);
        }

        // Set the extensionData list as the items for the extensionTable
        extensionTable.setItems(extensionData);

        // Restore the queue to its original state
        while (!tempQueue.isEmpty()) {
            extension.enqueue(tempQueue.dequeue());
        }
    }

    private void addToTeam(playerBean player) {
        for (playerBean teamPlayer : team) {
            if (teamPlayer.getId() == player.getId()) {
                System.out.println(player.getName() + " is already added to the team.");
                return;
            }
        }

        Stack<injuryResBean> tempInjuryRes = new Stack<>();
        boolean isInjured = false;
        while (!injuryRes.isEmpty()) {
            injuryResBean injuredPlayer = injuryRes.pop();
            if (injuredPlayer.getId() == player.getId()) {
                System.out.println(player.getName() + " is currently in the injury reserve and cannot be added to the team.");
                isInjured = true;
            }
            tempInjuryRes.push(injuredPlayer);
        }

        while (!tempInjuryRes.isEmpty()) {
            injuryRes.push(tempInjuryRes.pop());
        }

        if (!isInjured) {
            team.add(player);
            teamData.add(player);
            System.out.println(player.getName() + " added to the team.");
        }
    }

    private void removeFromTeam(playerBean player) {
        team.remove(player);
        teamData.remove(player);
        System.out.println(player.getName() + " removed from the team.");
    }

    private void moveToInjuryReserve(playerBean player) {
        String searchText = textField.getText();
        if (!searchText.isEmpty()) {
            injuryResBean injured = new injuryResBean(); // Initialize injured object
            System.out.println("Adding Player into injury reserve");
            injured.setId(player.getId());
            injured.setName(player.getName());
            injured.setInjury(searchText);
            injured.setStatus("Added to Injury Reserve");
            injuryRes.push(injured);
            System.out.println(injured.toString());
            team.remove(player);
            teamData.remove(player);
            System.out.println(player.getName() + " moved to injury reserve.");
        } else {
            System.out.println("text bar is empty");
        }
        // Update the Injury Reserve TableView
        populateInjuryReserveTable();
    }

    private void extend(playerBean player) {
        Queue<playerBean> tempQueue = new Queue<>();
        boolean alreadyInQueue = false;
        while (!extension.isEmpty()) {
            playerBean currentPlayer = extension.dequeue();
            if (currentPlayer.getId() == player.getId()) {
                alreadyInQueue = true;
                System.out.println(player.getName() + " is already in the extension queue.");
            }
            tempQueue.enqueue(currentPlayer);
        }

        while (!tempQueue.isEmpty()) {
            extension.enqueue(tempQueue.dequeue());
        }

        if (!alreadyInQueue) {
            extension.enqueue(player);
            System.out.println(player.toString());
            System.out.println(player.getName() + " added to the queue");
            populateExtensionTable();
        }
    }


    @FXML
    public void saveEverything() throws SQLException {
        PlayerDao saveDao = new PlayerDao();
        InjuryDao saveInjury = new InjuryDao();
        contractExtendDao extendDao = new contractExtendDao();
        saveDao.update(team);
        saveInjury.updateInjured(injuryRes);
        extendDao.updateContract(extension);
    }

    @FXML
    public void dynamicSearch() {
        String searchText = textField.getText();
        ArrayList<playerBean> candidates = new ArrayList<>();
        playerBean t = null;

        if (searchText == null || searchText.equals("")) {
            System.out.println("\n" + "Search is empty");
            return;
        }

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(searchText)) {
                t = players.get(i);
                break; // Stop searching once found
            }
        }
        if (t == null) {
            System.out.println("\n" + "Player not found");
            return;
        } else {
            System.out.println("Find Attributes: Height => " + t.getHeight() + " Weight <= " + t.getWeight() + " Position = " + t.getPosition());
        }

        // Parse height
        String[] tHeightParts = t.getHeight().split("-");
        int tFeet = Integer.parseInt(tHeightParts[0]);
        int tInches = Integer.parseInt(tHeightParts[1]);

        for (int i = 0; i < players.size(); i++) {
            playerBean currentPlayer = players.get(i);
            if (!currentPlayer.equals(t)) { // Skip adding t itself
                String[] currentPlayerHeightParts = currentPlayer.getHeight().split("-");
                int currentPlayerFeet = Integer.parseInt(currentPlayerHeightParts[0]);
                int currentPlayerInches = Integer.parseInt(currentPlayerHeightParts[1]);

                if ((currentPlayerFeet > tFeet || (currentPlayerFeet == tFeet && currentPlayerInches >= tInches))
                        && currentPlayer.getWeight() <= t.getWeight() && currentPlayer.getPosition().equals(t.getPosition())) {
                    candidates.add(currentPlayer);
                }
            }
        }

        if (candidates.isEmpty()) {
            System.out.println("\n" + "No candidates found");
        } else {
            System.out.println("\n");
            for (playerBean candidate : candidates) {
                System.out.println(candidate.toString());
            }
        }
    }


    @FXML
    public void actionForInjuryReserve() {
        if (!injuryRes.isEmpty()) {
            injuryResBean t = injuryRes.top();
            injuryRes.pop();
            injuryReserveData.remove(t);
            System.out.println("Cleared to play: " + t.getName());

            // Update the Injury Reserve TableView
            populateInjuryReserveTable();
        } else {
            System.out.println("No players in the injury reserve.");
        }
    }

    @FXML
    public void composite() {
        ArrayList<compositeBean> com = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            compositeBean bean = new compositeBean();
            bean.setId(players.get(i).getId());
            bean.setName(players.get(i).getName());
            bean.setCompositeScore(calculateCompositeScore(players.get(i)));
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
        for (int i = com.size() - 1; i >= 0; i--) {
            System.out.println("Rank = " + j + " " + com.get(i).toString());
            j++;
        }
    }

    public double calculateCompositeScore(playerBean player) {
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
            case "F-C":
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

    @FXML
    public void dequeue() {
        if (!extension.isEmpty()) {
            playerBean t = extension.dequeue();
            extensionData.remove(t);
            System.out.println(t.getName() + " contract has been renewed");
        } else {
            System.out.println("No players in the extension queue");
        }
    }

    @FXML
    private void switchG() {
        try {
            // Get the current stage
            Stage stage = (Stage) playerTable.getScene().getWindow();

            // Close the current stage
            stage.close();

            // Start the GraphRepresentation application
            GraphRepresentation graphRepresentation = new GraphRepresentation();
            Stage graphStage = new Stage();
            graphRepresentation.start(graphStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateTeam(ArrayList<playerBean> target) {
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

    public static boolean teamSizeReq(ArrayList<playerBean> team) {
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

    public static boolean positionalReq(ArrayList<playerBean> team) {
        int guardCount = 0, forwardCount = 0, centerCount = 0;

        for (playerBean player : team) {
            switch (player.getPosition()) {
                case "G":
                    guardCount++;
                    break;
                case "F":
                    forwardCount++;
                    break;
                case "C":
                    centerCount++;
                    break;
            }
        }

        return guardCount >= 2 && forwardCount >= 2 && centerCount >= 2;
    }

    private void printSQLException(SQLException ex) {
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
