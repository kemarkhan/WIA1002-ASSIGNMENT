package org.nba_data_structure.bean;

public class playerBean {

    private int id;
    private String name;
    private String height;
    private double weight;
    private String position;
    private double salary;
    private double points;
    private double rebounds;
    private double assists;
    private double steals;
    private double blocks;

    // Default constructor
    public playerBean() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getRebounds() {
        return rebounds;
    }

    public void setRebounds(double rebounds) {
        this.rebounds = rebounds;
    }

    public double getAssists() {
        return assists;
    }

    public void setAssists(double assists) {
        this.assists = assists;
    }

    public double getSteals() {
        return steals;
    }

    public void setSteals(double steals) {
        this.steals = steals;
    }

    public double getBlocks() {
        return blocks;
    }

    public void setBlocks(double blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "playerBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", weight=" + weight +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", points=" + points +
                ", rebounds=" + rebounds +
                ", assists=" + assists +
                ", steals=" + steals +
                ", blocks=" + blocks +
                '}';
    }
}
