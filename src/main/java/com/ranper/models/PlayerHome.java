package com.ranper.models;

// Classe para criar as informações do Player no banco de dados
public class PlayerHome {

    // variaveis da classe
    private String playerId;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;


    // construtor da classe
    public PlayerHome(String playerId, double x, double y, double z, float pitch, float yaw) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    //getters da classe

    public String getPlayerId() {
        return playerId;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }
}


