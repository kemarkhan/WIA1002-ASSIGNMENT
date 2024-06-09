package org.nba_data_structure.bean;

public class compositeBean {
    private int id;
    private String name;
    private double compositeScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "compositeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", compositeScore=" + compositeScore +
                '}';
    }
}
