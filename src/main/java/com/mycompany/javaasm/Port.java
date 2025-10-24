package com.mycompany.javaasm;

public class Port implements Displayable {
    private String name;
    private int x, y; // coordinates on map

    public Port(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
