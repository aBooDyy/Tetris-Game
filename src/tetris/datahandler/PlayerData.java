package tetris.datahandler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerData {

    @Expose
    @SerializedName("player_name")
    private String playerName;

    @Expose
    @SerializedName("total_lines")
    private int lines;

    @Expose
    @SerializedName("total_score")
    private int totalScore;

    @Expose
    @SerializedName("highest_score")
    private int highestScore;

    @Expose
    @SerializedName("games_played")
    private int gamesPlayed;

    @Expose
    @SerializedName("total_playtime")
    private long playTime;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public void increaseLines(int increaseValue) {
        this.lines += increaseValue;
    }

    public void increaseTotalScore(int increaseValue) {
        this.totalScore += increaseValue;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = Math.max(this.highestScore, highestScore);
    }

    public void increaseGamesPlayed(int increaseValue) {
        this.gamesPlayed += increaseValue;
    }

    public void increasePlayTime(long increaseValue) {
        this.playTime += increaseValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLines() {
        return lines;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public long getPlayTime() {
        return playTime;
    }



    @Override
    public String toString() {
        return "Total Lines: " + lines
                + "\nTotal Score: " + totalScore
                + "\nHighest Score: " + highestScore
                + "\nGames Played: " + gamesPlayed
                + "\nTotal Playtime: " + playTime;
    }
}
