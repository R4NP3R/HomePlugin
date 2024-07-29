package com.ranper.db;

import com.ranper.models.PlayerHome;

import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection connection;

    public Connection getConnection() throws SQLException{

        if (connection != null) {
            return connection;
        }

        String url = "jdbc:mysql://localhost/homes";
        String user = "root";
        String password = "root";

        this.connection = DriverManager.getConnection(url, user, password);

        System.out.println("Connected to the Homes Database.");

        return this.connection;
    }

    public void initializeDataBase() throws SQLException {

        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS homes(player_id varchar(255) primary key unique, x double, y double, z double, pitch float, yaw float)";
        statement.execute(sql);
        statement.close();

        System.out.println("Created the homes table in the database");
    }


    public PlayerHome findPlayerHomeByUUID (String uuid) throws SQLException{

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM homes WHERE player_id = ?");
        statement.setString(1, uuid);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            double x = result.getDouble("x");
            double y = result.getDouble("y");
            double z = result.getDouble("z");
            float pitch = result.getFloat("pitch");
            float yaw = result.getFloat("yaw");

            PlayerHome playerHome = new PlayerHome(uuid, x, y, z, pitch, yaw);
            statement.close();

            return playerHome;
        }
        statement.close();

        return null;
    }

    public void createPlayerHome(PlayerHome home) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO homes(player_id, x, y, z, pitch, yaw) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, home.getPlayerId());
        statement.setDouble(2, home.getX());
        statement.setDouble(3, home.getY());
        statement.setDouble(4, home.getZ());
        statement.setDouble(5, home.getPitch());
        statement.setDouble(6, home.getYaw());

        statement.executeUpdate();

        statement.close();
    }

    public void updatePlayerHome(PlayerHome home) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE homes SET x = ?, y = ?, z = ?, pitch = ?, yaw = ? WHERE player_id = ?");

        System.out.println(home.getX() + " " + home.getY() + " " + home.getZ());

        statement.setDouble(1, home.getX());
        statement.setDouble(2, home.getY());
        statement.setDouble(3, home.getZ());
        statement.setFloat(4, home.getPitch());
        statement.setFloat(5, home.getYaw());
        statement.setString(6, home.getPlayerId());
        statement.executeUpdate();

        statement.close();
    }
}
