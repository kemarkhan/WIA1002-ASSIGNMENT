package org.nba_data_structure.util;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.io.IOException;
import java.net.http.HttpResponse;
import org.nba_data_structure.bean.playerBean;
import org.nba_data_structure.data_structures.ArrayList;
import com.google.gson.Gson;

public class balldontlie {

    public ArrayList<playerBean> getPlayers() {
        ArrayList<playerBean> players = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.balldontlie.io/v1/stats?seasons[]=2023"))
                .header("Authorization", "11abd3bf-b3aa-4dca-b4e7-970795b063b3")
                .method("GET", HttpRequest.BodyPublishers.noBody())//Get all data form db
                .build();

        HttpResponse<String> response = null;

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Gson gson = new Gson();
                PlayerStatsResponse statsResponse = gson.fromJson(responseBody, PlayerStatsResponse.class);
                if (statsResponse != null && statsResponse.getData() != null && statsResponse.getData().length > 0) {
                    for (PlayerStats playerStats : statsResponse.getData()) {
                        playerBean player = new playerBean();
                        player.setId(playerStats.getId());
                        player.setName(playerStats.getPlayer().getFirst_name() + " " + playerStats.getPlayer().getLast_name());
                        player.setHeight(playerStats.getPlayer().getHeight());
                        player.setWeight(Double.parseDouble(playerStats.getPlayer().getWeight()));
                        player.setPosition(playerStats.getPlayer().getPosition());
                        player.setPoints(playerStats.getPts());
                        player.setSalary(salaryGet(player.getPoints()));
                        player.setRebounds(playerStats.getReb());
                        player.setAssists(playerStats.getAst());
                        player.setSteals(playerStats.getStl());
                        player.setBlocks(playerStats.getBlk());
                        players.add(player);
                    }
                } else {
                    System.out.println("No player statistics found.");
                }
            } else {
                System.out.println("Failed to retrieve data. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return players;
    }

    public double salaryGet(double pt){
        double salary = 0;
        if(pt > 20){
            salary = 3000;
        }else{
            salary = 1000;
        }
        return salary;
    }
}


class PlayerStatsResponse {
    private PlayerStats[] data;

    public PlayerStats[] getData() {
        return data;
    }

    public void setData(PlayerStats[] data) {
        this.data = data;
    }
}

class PlayerStats {
    private int id;
    private int pts;
    private int reb;
    private int ast;
    private int stl;
    private int blk;
    private Player player;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getReb() {
        return reb;
    }

    public void setReb(int reb) {
        this.reb = reb;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getStl() {
        return stl;
    }

    public void setStl(int stl) {
        this.stl = stl;
    }

    public int getBlk() {
        return blk;
    }

    public void setBlk(int blk) {
        this.blk = blk;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

class Player {
    private int id;
    private String first_name;
    private String last_name;
    private String position;
    private String height;
    private String weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}

