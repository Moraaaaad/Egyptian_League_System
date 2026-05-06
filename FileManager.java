

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String TEAMS_FILE   = "teams.csv";
    private static final String PLAYERS_FILE = "players.csv";
    private static final String MATCHES_FILE = "matches.csv";

    // ===================== WRITE (one call saves everything) =====================
    public static void saveAll(List<Team> teams, List<Player> players, List<Match> matches) {
        writeTeams(teams);
        writePlayers(players);
        writeMatches(matches);
        System.out.println("[FileManager] Data saved successfully.");
    }

    private static void writeTeams(List<Team> teams) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEAMS_FILE))) {
            for (Team t : teams) pw.println(t.toCSV());
        } catch (IOException e) {
            System.out.println("Error saving teams: " + e.getMessage());
        }
    }

    private static void writePlayers(List<Player> players) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PLAYERS_FILE))) {
            for (Player p : players) pw.println(p.toCSV());
        } catch (IOException e) {
            System.out.println("Error saving players: " + e.getMessage());
        }
    }

    private static void writeMatches(List<Match> matches) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MATCHES_FILE))) {
            for (Match m : matches) pw.println(m.toCSV());
        } catch (IOException e) {
            System.out.println("Error saving matches: " + e.getMessage());
        }
    }

    // ===================== READ (one call loads everything) =====================
    public static void loadAll(List<Team> teams, List<Player> players, List<Match> matches) {
        loadTeams(teams);
        loadPlayers(players);
        loadMatches(matches);
        // Link players to teams
        for (Player p : players) {
            for (Team t : teams) {
                if (t.getName().equals(p.getTeamName())) {
                    t.addPlayer(p);
                    break;
                }
            }
        }
        Team.setTeamCount(teams.size());
        System.out.println("[FileManager] Data loaded: "
                + teams.size() + " teams, "
                + players.size() + " players, "
                + matches.size() + " matches.");
    }

    private static void loadTeams(List<Team> teams) {
        File f = new File(TEAMS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                // name, teamID, captainName, totalScore
                Team t = new Team(p[0], p[1], p[2]);
                t.setTotalScore(Integer.parseInt(p[3].trim()));
                teams.add(t);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading teams: " + e.getMessage());
        }
    }

    private static void loadPlayers(List<Player> players) {
        File f = new File(PLAYERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                // position, name, playerID, number, teamName, age, score, rank [, goalsConceded]
                String position  = p[0].trim();
                String name      = p[1].trim();
                String id        = p[2].trim();
                int    number    = Integer.parseInt(p[3].trim());
                String teamName  = p[4].trim();
                int    age       = Integer.parseInt(p[5].trim());
                int    score     = Integer.parseInt(p[6].trim());
                String rank      = p[7].trim();

                Player player;
                switch (position) {
                    case "Forward":    player = new Forward(name, id, number, teamName, age); break;
                    case "Midfielder": player = new Midfielder(name, id, number, teamName, age); break;
                    case "Defender":   player = new Defender(name, id, number, teamName, age); break;
                    case "Goalkeeper":
                        Goalkeeper gk = new Goalkeeper(name, id, number, teamName, age);
                        if (p.length > 8) gk.setGoalsConceded(Integer.parseInt(p[8].trim()));
                        player = gk;
                        break;
                    default:
                        player = new Forward(name, id, number, teamName, age);
                }
                player.setScore(score);
                player.setRank(rank);
                players.add(player);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading players: " + e.getMessage());
        }
    }

    private static void loadMatches(List<Match> matches) {
        File f = new File(MATCHES_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                // matchID, date, team1, team2, referee, score1, score2, stadium, played
                Match m = new Match(p[0].trim(), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim(), p[7].trim());
                int s1 = Integer.parseInt(p[5].trim());
                int s2 = Integer.parseInt(p[6].trim());
                boolean played = Boolean.parseBoolean(p[8].trim());
                if (played) m.setScore(s1, s2);
                matches.add(m);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading matches: " + e.getMessage());
        }
    }
}
