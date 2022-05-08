package tetris.datahandler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import tetris.Tetris;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class Data {

    private final static File FILE = new File("data.json");

    private static Data instance;

    @Expose
    @SerializedName("players")
    private final HashMap<String, PlayerData> players;

    private Data() {
        players = new HashMap<>();
    }

    public PlayerData getPlayerData(String name) {
        if (players.get(name) == null) {
            players.put(name, new PlayerData(name));
        }

        return players.get(name);
    }

    public void save() {
        if (FILE == null) {
            System.out.println("Failed to save data file.");
            return;
        }

        try (OutputStream outputStream = new FileOutputStream(FILE)) {
            outputStream.write(Tetris.getGson().toJson(this).getBytes());
        } catch (Exception e) {
            System.out.println("Failed to save data file.");
        }
    }

    public Collection<PlayerData> getAllPlayersData() {
        return players.values();
    }

    public static Data getInstance() {
        if (instance != null) {
            return instance;
        }

        JsonReader jsonReader;

        try {
            jsonReader = new JsonReader(new FileReader(FILE));
        } catch (FileNotFoundException e) {
            instance = new Data();
            return instance;
        }

        instance = Tetris.getGson().fromJson(jsonReader, Data.class);

        if (instance == null) {
            try (OutputStream outputStream = new FileOutputStream(FILE)) {
                outputStream.write("{\"players\":{}}".getBytes());
            } catch (Exception e) {
                System.out.println("Failed to manually write to data file.");
            }

            getInstance();
        }

        return instance;
    }

    @Override
    public String toString() {
        return players.toString();
    }
}
