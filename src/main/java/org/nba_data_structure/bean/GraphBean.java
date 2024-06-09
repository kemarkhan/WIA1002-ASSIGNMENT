package org.nba_data_structure.bean;

public class GraphBean {
    private String source_city;
    private String destination_city;
    private int weight;

    public GraphBean() {}

    public String getDestination_city() {
        return destination_city;
    }

    public void setDestination_city(String destination_city) {
        this.destination_city = destination_city;
    }

    public String getSource_city() {
        return source_city;
    }

    public void setSource_city(String source_city) {
        this.source_city = source_city;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "GraphBean{" +
                "source_city=" + source_city +
                ", destination_city='" + destination_city + '\'' +
                ", weight=" + weight +
                '}';
    }
}