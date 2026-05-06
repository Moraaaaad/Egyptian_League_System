
import java.util.*;
import java.util.stream.Collectors;

public class LeagueSystem {

    private List<Team>   teams   = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<Match>  matches = new ArrayList<>();

    // ==================== INIT ====================
    public void loadData() {
        FileManager.loadAll(teams, players, matches);
    }

    public void saveData() {
        FileManager.saveAll(teams, players, matches);
    }

    // ==================== TEAM ====================
    public void addTeam(Team t) {
        teams.add(t);
        System.out.println("Team '" + t.getName() + "' added.");
    }

    public Team findTeamByID(String id) {
        for (Team t : teams)
            if (t.getTeamID().equalsIgnoreCase(id)) return t;
        return null;
    }

    public Team findTeamByName(String name) {
        for (Team t : teams)
            if (t.getName().equalsIgnoreCase(name)) return t;
        return null;
    }

    public boolean deleteTeam(String teamID) {
        Team t = findTeamByID(teamID);
        if (t == null) return false;
        // Remove players of this team
        players.removeIf(p -> p.getTeamName().equalsIgnoreCase(t.getName()));
        // Remove matches involving this team
        matches.removeIf(m -> m.getTeam1Name().equalsIgnoreCase(t.getName())
                           || m.getTeam2Name().equalsIgnoreCase(t.getName()));
        teams.remove(t);
        Team.setTeamCount(teams.size());
        return true;
    }

    public void displayAllTeams() {
        if (teams.isEmpty()) { System.out.println("No teams found."); return; }
        for (Team t : teams) t.displayInfo();
    }

    public void displayTeamMatchesForTeam(String teamName) {
        System.out.println("--- Held matches ---");
        for (Match m : matches)
            if (teamInMatch(m, teamName) && m.isPlayed()) m.displayInfo();
        System.out.println("--- Upcoming matches ---");
        for (Match m : matches)
            if (teamInMatch(m, teamName) && !m.isPlayed() && m.isUpcoming()) m.displayInfo();
    }

    private boolean teamInMatch(Match m, String teamName) {
        return m.getTeam1Name().equalsIgnoreCase(teamName)
            || m.getTeam2Name().equalsIgnoreCase(teamName);
    }

    public int getTotalTeams() { return teams.size(); }

    // ==================== PLAYER ====================
    public void addPlayerToSystem(Player p) {
        players.add(p);
    }

    public Player findPlayerByID(String id) {
        for (Player p : players)
            if (p.getPlayerID().equalsIgnoreCase(id)) return p;
        return null;
    }

    public List<Player> searchPlayerByName(String name) {
        List<Player> result = new ArrayList<>();
        for (Player p : players)
            if (p.getName().equalsIgnoreCase(name)) result.add(p);
        return result;
    }

    public Player searchPlayerByNameAndTeam(String name, String teamName) {
        for (Player p : players)
            if (p.getName().equalsIgnoreCase(name)
                    && p.getTeamName().equalsIgnoreCase(teamName)) return p;
        return null;
    }

    public boolean deletePlayer(String playerName, String playerID) {
        Player p = findPlayerByID(playerID);
        if (p == null || !p.getName().equalsIgnoreCase(playerName)) return false;
        // Remove from team list too
        Team t = findTeamByName(p.getTeamName());
        if (t != null) t.deletePlayer(playerName, playerID);
        players.removeIf(pl -> pl.getPlayerID().equalsIgnoreCase(playerID));
        return true;
    }

    // Top 3 scorers
    public void displayTopScorers() {
        List<Player> sorted = players.stream()
                .sorted((a, b) -> b.getScore() - a.getScore())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("=== Top 3 Scorers ===");
        for (Player p : sorted) {
            System.out.println(p.getName() + " (" + p.getTeamName() + ") - " + p.getScore() + " goals");
        }
    }

    // Top 3 goalkeepers with fewest goals conceded
    public void displayTopGoalkeepers() {
        List<Goalkeeper> gks = players.stream()
                .filter(p -> p instanceof Goalkeeper)
                .map(p -> (Goalkeeper) p)
                .sorted(Comparator.comparingInt(Goalkeeper::getGoalsConceded))
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("=== Top 3 Goalkeepers (fewest goals conceded) ===");
        for (Goalkeeper gk : gks) {
            System.out.println(gk.getName() + " (" + gk.getTeamName() + ") - "
                    + gk.getGoalsConceded() + " conceded");
        }
    }

    // ==================== MATCH ====================
    public void addMatch(Match m) {
        matches.add(m);
        System.out.println("Match added: " + m.getMatchID());
    }

    public Match findMatchByID(String id) {
        for (Match m : matches)
            if (m.getMatchID().equalsIgnoreCase(id)) return m;
        return null;
    }

    public boolean deleteMatch(String matchID) {
        return matches.removeIf(m -> m.getMatchID().equalsIgnoreCase(matchID));
    }

    // Check if a stadium is free on a given date
    public boolean isStadiumFree(String stadiumName, String date, String excludeMatchID) {
        for (Match m : matches) {
            if (m.getMatchID().equalsIgnoreCase(excludeMatchID)) continue;
            if (m.getStadiumName().equalsIgnoreCase(stadiumName)
                    && m.getDate().format(Match.FMT).equals(date)) {
                return false;
            }
        }
        return true;
    }

    // Update match score + apply points to teams
    public void updateMatchScore(Match m, int s1, int s2) {
        // Reverse old points if already played
        if (m.isPlayed()) {
            reversePoints(m);
        }
        m.setScore(s1, s2);
        applyPoints(m);
    }

    private void applyPoints(Match m) {
        Team t1 = findTeamByName(m.getTeam1Name());
        Team t2 = findTeamByName(m.getTeam2Name());
        if (t1 == null || t2 == null) return;
        if (m.getScoreTeam1() > m.getScoreTeam2()) {
            t1.addPoints(3);
        } else if (m.getScoreTeam2() > m.getScoreTeam1()) {
            t2.addPoints(3);
        } else {
            t1.addPoints(1); t2.addPoints(1);
        }
    }

    private void reversePoints(Match m) {
        Team t1 = findTeamByName(m.getTeam1Name());
        Team t2 = findTeamByName(m.getTeam2Name());
        if (t1 == null || t2 == null) return;
        if (m.getScoreTeam1() > m.getScoreTeam2()) {
            t1.setTotalScore(Math.max(0, t1.getTotalScore() - 3));
        } else if (m.getScoreTeam2() > m.getScoreTeam1()) {
            t2.setTotalScore(Math.max(0, t2.getTotalScore() - 3));
        } else {
            t1.setTotalScore(Math.max(0, t1.getTotalScore() - 1));
            t2.setTotalScore(Math.max(0, t2.getTotalScore() - 1));
        }
    }

    public void displayMatchesOnDate(String date) {
        System.out.println("Matches on " + date + ":");
        boolean found = false;
        for (Match m : matches) {
            if (m.getDate().format(Match.FMT).equals(date)) {
                m.displayInfo(); found = true;
            }
        }
        if (!found) System.out.println("No matches on this date.");
    }

    // ==================== RANKINGS ====================
    public void displayTeamsByGoals() {
        System.out.println("=== Teams ordered by total goals ===");
        teams.stream()
             .sorted((a, b) -> b.getTotalGoals() - a.getTotalGoals())
             .forEach(t -> System.out.println(t.getName() + " - " + t.getTotalGoals() + " goals"));
    }

    public void displayTeamsByAverageAge() {
        System.out.println("=== Teams ordered by average player age ===");
        teams.stream()
             .sorted(Comparator.comparingDouble(Team::getAverageAge))
             .forEach(t -> System.out.printf("%-20s Avg Age: %.1f%n", t.getName(), t.getAverageAge()));
    }

    // ==================== GETTERS ====================
    public List<Team>   getTeams()   { return teams; }
    public List<Player> getPlayers() { return players; }
    public List<Match>  getMatches() { return matches; }
    // ==================== GUI STRING METHODS (الواجهة الرسومية) ====================

    // 1. ميثود إرجاع كل الفرق كنص
    public String getAllTeamsString() {
        if (teams.isEmpty()) return "No teams found in the system.";
        StringBuilder sb = new StringBuilder("=== All Teams ===\n\n");
        for (Team t : teams) {
            sb.append("Team: ").append(t.getName())
              .append(" | ID: ").append(t.getTeamID())
              .append(" | Captain: ").append(t.getCaptainName())
              .append(" | Points: ").append(t.getTotalScore())
              .append(" | Total Goals: ").append(t.getTotalGoals())
              .append("\n-----------------------------\n");
        }
        return sb.toString();
    }

    // 2. ميثود إرجاع الهدافين كنص
    public String getTopScorersString() {
        if (players.isEmpty()) return "No players found.";
        StringBuilder sb = new StringBuilder("=== Top 3 Scorers ===\n\n");
        List<Player> sorted = players.stream()
                .sorted((a, b) -> b.getScore() - a.getScore())
                .limit(3)
                .collect(Collectors.toList());
        
        for (Player p : sorted) {
            sb.append(p.getName()).append(" (").append(p.getTeamName()).append(") - ")
              .append(p.getScore()).append(" goals\n");
        }
        return sb.toString();
    }

    // 3. ميثود إرجاع أفضل حراس المرمى كنص
    public String getTopGoalkeepersString() {
        StringBuilder sb = new StringBuilder("=== Top 3 Goalkeepers ===\n\n");
        List<Goalkeeper> gks = players.stream()
                .filter(p -> p instanceof Goalkeeper)
                .map(p -> (Goalkeeper) p)
                .sorted(Comparator.comparingInt(Goalkeeper::getGoalsConceded))
                .limit(3)
                .collect(Collectors.toList());
                
        if (gks.isEmpty()) return "No goalkeepers found.";
        for (Goalkeeper gk : gks) {
            sb.append(gk.getName()).append(" (").append(gk.getTeamName()).append(") - ")
              .append(gk.getGoalsConceded()).append(" conceded\n");
        }
        return sb.toString();
    }

    // 4. ميثود إرجاع كل المباريات كنص
    public String getAllMatchesString() {
        if (matches.isEmpty()) return "No matches found.";
        StringBuilder sb = new StringBuilder("=== All Matches ===\n\n");
        for (Match m : matches) {
            sb.append("Date: ").append(m.getDate().format(Match.FMT))
              .append(" | ").append(m.getTeam1Name()).append(" VS ").append(m.getTeam2Name());
            if (m.isPlayed()) {
                sb.append(" | Score: ").append(m.getScoreTeam1()).append(" - ").append(m.getScoreTeam2());
            } else {
                sb.append(" | Status: Upcoming");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
