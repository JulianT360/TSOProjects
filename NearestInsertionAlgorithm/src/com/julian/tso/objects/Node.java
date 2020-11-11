package com.julian.tso.objects;

public class Node {

    private String name;
    private Integer positionX;
    private Integer positionY;

    public Node() {

    }

    public Node(String name, Integer positionX, Integer positionY) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
