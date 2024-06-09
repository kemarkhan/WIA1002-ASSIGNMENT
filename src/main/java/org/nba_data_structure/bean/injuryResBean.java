package org.nba_data_structure.bean;

public class injuryResBean {
    private int id;
    private String status;
    private String injury;
    private String name;

    public injuryResBean() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInjury() {
        return injury;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "injuryResBean{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", injury='" + injury + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}