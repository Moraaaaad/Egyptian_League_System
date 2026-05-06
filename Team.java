
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String teamID;
    private List<Player> players;
    private String captainName;
    private int totalScore; // league points (3/1/0)
    private static int teamCount = 0;

    public Team(String name, String teamID, String captainName) {
        this.name = name;
        this.teamID = teamID;
        this.captainName = captainName;
        this.players = new ArrayList<>();
        this.totalScore = 0;
        teamCount++;
    }

    // Getters
    public String getName()        { return name; }
    public String getTeamID()      { return teamID; }
    public String getCaptainName() { return captainName; }
    public int    getTotalScore()  { return totalScore; }
    public List<Player> getPlayers() { return players; }
    public static int getTeamCount() { return teamCount; }

    // Setters
    public void setName(String name)            { this.name = name; }
    public void setCaptainName(String captain)  { this.captainName = captain; }
    public void addPoints(int pts)              { this.totalScore += pts; }
    public void setTotalScore(int s)            { this.totalScore = s; }
    public static void setTeamCount(int c)      { teamCount = c; }

    // Add player (no duplicate IDs)
    public boolean addPlayer(Player p) {
        for (Player existing : players) {
            if (existing.getPlayerID().equals(p.getPlayerID())) {
                System.out.println("Player ID already exists in this team.");
                return false;
            }
        }
        players.add(p);
        return true;
    }

    // Delete player by name AND id
    public boolean deletePlayer(String playerName, String playerID) {
        return players.removeIf(p ->
                p.getName().equalsIgnoreCase(playerName) &&
                p.getPlayerID().equals(playerID));
    }

    // Total goals scored by all players
    public int getTotalGoals() {
        int total = 0;
        for (Player p : players) total += p.getScore();
        return total;
    }

    // Average age
    public double getAverageAge() {
        if (players.isEmpty()) return 0;
        int sum = 0;
        for (Player p : players) sum += p.getAge();
        return (double) sum / players.size();
    }

    public void displayInfo() {
        System.out.println("=============================");
        System.out.println("Team Name : " + name);
        System.out.println("Team ID   : " + teamID);
        System.out.println("Captain   : " + captainName);
        System.out.println("Points    : " + totalScore);
        System.out.println("Players   : " + players.size());
        System.out.println("Total Goals: " + getTotalGoals());
    }

    public void displayPlayers() {
        if (players.isEmpty()) {
            System.out.println("No players in this team.");
            return;
        }
        System.out.println("Players of " + name + ":");
        for (Player p : players) p.displayInfo();
    }

    // CSV for file
    public String toCSV() {
        return name + "," + teamID + "," + captainName + "," + totalScore;
    }
}
