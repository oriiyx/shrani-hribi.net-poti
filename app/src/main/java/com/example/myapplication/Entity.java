package com.example.myapplication;

public class Entity {
    private int id;
    private String entityName;

    public Entity(int id, String entityName) {
        this.id = id;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }
}
