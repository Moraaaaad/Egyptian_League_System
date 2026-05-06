
import java.time.LocalDate;
import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static LeagueSystem league = new LeagueSystem();

    public static void main(String[] args) {
        league.loadData();
        seedDataIfEmpty(); // adds 4 teams & 8 matches if files are empty

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt();
            switch (choice) {
                case 1:  teamMenu();    break;
                case 2:  playerMenu();  break;
                case 3:  matchMenu();   break;
                case 4:  statsMenu();   break;
                case 0:
                    league.saveData();
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // ==================== MENUS ====================
    static void printMainMenu() {
        System.out.println("\n╔═══════════════════════════════╗");
        System.out.println("║   Egyptian League System      ║");
        System.out.println("╠═══════════════════════════════╣");
        System.out.println("║ 1. Team Management            ║");
        System.out.println("║ 2. Player Management          ║");
        System.out.println("║ 3. Match Management           ║");
        System.out.println("║ 4. Statistics & Rankings      ║");
        System.out.println("║ 0. Save & Exit                ║");
        System.out.println("╚═══════════════════════════════╝");
        System.out.print("Choice: ");
    }

    // ==================== TEAM MENU ====================
    static void teamMenu() {
        System.out.println("\n--- Team Menu ---");
        System.out.println("1. Add team");
        System.out.println("2. Update team");
        System.out.println("3. Display team info");
        System.out.println("4. Display team players");
        System.out.println("5. Display team matches");
        System.out.println("6. Add player to team");
        System.out.println("7. Delete player from team");
        System.out.println("8. Delete team");
        System.out.println("9. Total number of teams");
        System.out.println("10. Display all teams");
        System.out.print("Choice: ");
        int c = readInt();
        switch (c) {
            case 1: addTeam();          break;
            case 2: updateTeam();       break;
            case 3: displayTeamInfo();  break;
            case 4: displayTeamPlayers(); break;
            case 5: displayTeamMatches(); break;
            case 6: addPlayerToTeam();  break;
            case 7: deletePlayerFromTeam(); break;
            case 8: deleteTeam();       break;
            case 9: System.out.println("Total teams: " + league.getTotalTeams()); break;
            case 10: league.displayAllTeams(); break;
            default: System.out.println("Invalid.");
        }
    }

    static void addTeam() {
        System.out.print("Team Name: ");     String name = sc.nextLine().trim();
        System.out.print("Team ID: ");       String id   = sc.nextLine().trim();
        System.out.print("Captain Name: ");  String cap  = sc.nextLine().trim();
        if (league.findTeamByID(id) != null) {
            System.out.println("Team ID already exists!");
            return;
        }
        league.addTeam(new Team(name, id, cap));
    }

    static void updateTeam() {
        System.out.print("Enter Team ID to update: ");
        String id = sc.nextLine().trim();
        Team t = league.findTeamByID(id);
        if (t == null) { System.out.println("Team not found."); return; }
        System.out.println("1. Name  2. Captain");
        System.out.print("Choice: ");
        int c = readInt();
        if (c == 1) { System.out.print("New Name: "); t.setName(sc.nextLine().trim()); }
        else if (c == 2) { System.out.print("New Captain: "); t.setCaptainName(sc.nextLine().trim()); }
        System.out.println("Updated.");
    }

    static void displayTeamInfo() {
        System.out.print("Team ID: ");
        Team t = league.findTeamByID(sc.nextLine().trim());
        if (t == null) { System.out.println("Not found."); return; }
        t.displayInfo();
    }

    static void displayTeamPlayers() {
        System.out.print("Team ID: ");
        Team t = league.findTeamByID(sc.nextLine().trim());
        if (t == null) { System.out.println("Not found."); return; }
        t.displayPlayers();
    }

    static void displayTeamMatches() {
        System.out.print("Team Name: ");
        String name = sc.nextLine().trim();
        league.displayTeamMatchesForTeam(name);
    }

    static void addPlayerToTeam() {
        System.out.print("Team ID: ");
        Team t = league.findTeamByID(sc.nextLine().trim());
        if (t == null) { System.out.println("Team not found."); return; }

        System.out.print("Player Name: ");   String name = sc.nextLine().trim();
        System.out.print("Player ID: ");     String pid  = sc.nextLine().trim();
        System.out.print("Number: ");        int num     = readInt();
        System.out.print("Age: ");           int age     = readInt();
        System.out.println("Position: 1.Forward  2.Midfielder  3.Defender  4.Goalkeeper");
        System.out.print("Choice: ");        int pos     = readInt();

        Player p;
        switch (pos) {
            case 1: p = new Forward(name, pid, num, t.getName(), age);    break;
            case 2: p = new Midfielder(name, pid, num, t.getName(), age); break;
            case 3: p = new Defender(name, pid, num, t.getName(), age);   break;
            case 4: p = new Goalkeeper(name, pid, num, t.getName(), age); break;
            default: System.out.println("Invalid position."); return;
        }
        if (t.addPlayer(p)) {
            league.addPlayerToSystem(p);
            System.out.println("Player added.");
        }
    }

    static void deletePlayerFromTeam() {
        System.out.print("Player Name: ");  String name = sc.nextLine().trim();
        System.out.print("Player ID: ");    String pid  = sc.nextLine().trim();
        if (league.deletePlayer(name, pid)) System.out.println("Player deleted.");
        else System.out.println("Player not found.");
    }

    static void deleteTeam() {
        System.out.print("Team ID to delete: ");
        if (league.deleteTeam(sc.nextLine().trim())) System.out.println("Team deleted.");
        else System.out.println("Team not found.");
    }

    // ==================== PLAYER MENU ====================
    static void playerMenu() {
        System.out.println("\n--- Player Menu ---");
        System.out.println("1. Display player info");
        System.out.println("2. Update player");
        System.out.println("3. Search player by name");
        System.out.println("4. Search player by name & team");
        System.out.println("5. Delete player");
        System.out.print("Choice: ");
        int c = readInt();
        switch (c) {
            case 1: displayPlayerInfo();           break;
            case 2: updatePlayer();                break;
            case 3: searchPlayerByName();          break;
            case 4: searchPlayerByNameAndTeam();   break;
            case 5: deletePlayerMenu();            break;
            default: System.out.println("Invalid.");
        }
    }

    static void displayPlayerInfo() {
        System.out.print("Player ID: ");
        Player p = league.findPlayerByID(sc.nextLine().trim());
        if (p == null) { System.out.println("Not found."); return; }
        p.displayInfo();
    }

    static void updatePlayer() {
        System.out.print("Player ID: ");
        Player p = league.findPlayerByID(sc.nextLine().trim());
        if (p == null) { System.out.println("Not found."); return; }
        System.out.println("Update: 1.Name 2.Number 3.Age 4.Score 5.Rank 6.Goals Conceded(GK only)");
        System.out.print("Choice: ");
        int c = readInt();
        switch (c) {
            case 1: System.out.print("New Name: ");   p.setName(sc.nextLine().trim()); break;
            case 2: System.out.print("New Number: "); p.setNumber(readInt()); break;
            case 3: System.out.print("New Age: ");    p.setAge(readInt()); break;
            case 4: System.out.print("New Goals: ");  p.setScore(readInt()); break;
            case 5: System.out.print("New Rank: ");   p.setRank(sc.nextLine().trim()); break;
            case 6:
                if (p instanceof Goalkeeper) {
                    System.out.print("Goals Conceded: ");
                    ((Goalkeeper) p).setGoalsConceded(readInt());
                } else System.out.println("Not a goalkeeper.");
                break;
            default: System.out.println("Invalid.");
        }
        System.out.println("Updated.");
    }

    static void searchPlayerByName() {
        System.out.print("Player Name: ");
        List<Player> results = league.searchPlayerByName(sc.nextLine().trim());
        if (results.isEmpty()) System.out.println("No player found.");
        else results.forEach(Player::displayInfo);
    }

    static void searchPlayerByNameAndTeam() {
        System.out.print("Player Name: "); String name = sc.nextLine().trim();
        System.out.print("Team Name: ");   String team = sc.nextLine().trim();
        Player p = league.searchPlayerByNameAndTeam(name, team);
        if (p == null) System.out.println("Not found."); else p.displayInfo();
    }

    static void deletePlayerMenu() {
        System.out.print("Player Name: "); String name = sc.nextLine().trim();
        System.out.print("Player ID: ");   String pid  = sc.nextLine().trim();
        if (league.deletePlayer(name, pid)) System.out.println("Deleted.");
        else System.out.println("Not found.");
    }

    // ==================== MATCH MENU ====================
    static void matchMenu() {
        System.out.println("\n--- Match Menu ---");
        System.out.println("1. Add match");
        System.out.println("2. Display match");
        System.out.println("3. Update match");
        System.out.println("4. Delete match");
        System.out.println("5. Display matches on a date");
        System.out.print("Choice: ");
        int c = readInt();
        switch (c) {
            case 1: addMatch();            break;
            case 2: displayMatch();        break;
            case 3: updateMatch();         break;
            case 4: deleteMatch();         break;
            case 5: matchesOnDate();       break;
            default: System.out.println("Invalid.");
        }
    }

    static void addMatch() {
        System.out.print("Match ID: ");      String mid  = sc.nextLine().trim();
        System.out.print("Date (yyyy-MM-dd): "); String date = sc.nextLine().trim();
        System.out.print("Team 1 Name: ");   String t1   = sc.nextLine().trim();
        System.out.print("Team 2 Name: ");   String t2   = sc.nextLine().trim();
        System.out.print("Referee: ");       String ref  = sc.nextLine().trim();
        System.out.print("Stadium: ");       String stad = sc.nextLine().trim();

        if (league.findMatchByID(mid) != null) { System.out.println("Match ID exists."); return; }
        if (league.findTeamByName(t1) == null || league.findTeamByName(t2) == null) {
            System.out.println("One or both teams not found."); return;
        }
        if (!league.isStadiumFree(stad, date, "")) {
            System.out.println("Stadium is already booked on this date."); return;
        }
        try {
            league.addMatch(new Match(mid, date, t1, t2, ref, stad));
        } catch (Exception e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd");
        }
    }

    static void displayMatch() {
        System.out.print("Match ID: ");
        Match m = league.findMatchByID(sc.nextLine().trim());
        if (m == null) { System.out.println("Not found."); return; }
        m.displayInfo();
    }

    static void updateMatch() {
        System.out.print("Match ID: ");
        Match m = league.findMatchByID(sc.nextLine().trim());
        if (m == null) { System.out.println("Not found."); return; }

        System.out.println("Update:");
        System.out.println("1.Date  2.Teams  3.Referee  4.Score  5.Stadium");
        System.out.print("Choice: ");
        int c = readInt();

        switch (c) {
            case 1:
                System.out.print("New Date (yyyy-MM-dd): ");
                String newDate = sc.nextLine().trim();
                try {
                    LocalDate ld = LocalDate.parse(newDate, Match.FMT);
                    if (ld.isBefore(LocalDate.now())) {
                        System.out.println("Date must be upcoming."); return;
                    }
                    m.setDate(newDate);
                    System.out.println("Date updated.");
                } catch (Exception e) { System.out.println("Invalid date format."); }
                break;

            case 2:
                System.out.print("New Team 1: "); String nt1 = sc.nextLine().trim();
                System.out.print("New Team 2: "); String nt2 = sc.nextLine().trim();
                if (league.findTeamByName(nt1) == null || league.findTeamByName(nt2) == null) {
                    System.out.println("One or both teams not found."); return;
                }
                m.setTeam1Name(nt1); m.setTeam2Name(nt2);
                System.out.println("Teams updated.");
                break;

            case 3:
                System.out.print("New Referee: ");
                m.setReferee(sc.nextLine().trim());
                System.out.println("Referee updated.");
                break;

            case 4:
                System.out.print("Score for " + m.getTeam1Name() + ": "); int s1 = readInt();
                System.out.print("Score for " + m.getTeam2Name() + ": "); int s2 = readInt();
                league.updateMatchScore(m, s1, s2);
                System.out.println("Score updated & points applied.");
                break;

            case 5:
                System.out.print("New Stadium: ");
                String newStad = sc.nextLine().trim();
                if (!league.isStadiumFree(newStad, m.getDate().format(Match.FMT), m.getMatchID())) {
                    System.out.println("Stadium already booked on this date."); return;
                }
                m.setStadiumName(newStad);
                System.out.println("Stadium updated.");
                break;

            default: System.out.println("Invalid.");
        }
    }

    static void deleteMatch() {
        System.out.print("Match ID: ");
        if (league.deleteMatch(sc.nextLine().trim())) System.out.println("Match deleted.");
        else System.out.println("Not found.");
    }

    static void matchesOnDate() {
        System.out.print("Date (yyyy-MM-dd): ");
        league.displayMatchesOnDate(sc.nextLine().trim());
    }

    // ==================== STATS MENU ====================
    static void statsMenu() {
        System.out.println("\n--- Statistics ---");
        System.out.println("1. Top 3 scorers");
        System.out.println("2. Top 3 goalkeepers (fewest goals conceded)");
        System.out.println("3. Teams ordered by goals scored");
        System.out.println("4. Teams ordered by average age");
        System.out.print("Choice: ");
        int c = readInt();
        switch (c) {
            case 1: league.displayTopScorers();     break;
            case 2: league.displayTopGoalkeepers(); break;
            case 3: league.displayTeamsByGoals();   break;
            case 4: league.displayTeamsByAverageAge(); break;
            default: System.out.println("Invalid.");
        }
    }

    // ==================== SEED DATA ====================
    static void seedDataIfEmpty() {
        if (!league.getTeams().isEmpty()) return; // already has data

        System.out.println("[System] Loading sample data...");

        // 4 Teams
        Team ahly    = new Team("Al Ahly",    "T01", "Mohamed El Shennawy");
        Team zamalek = new Team("Zamalek",    "T02", "Mahmoud Kahraba");
        Team pyramids= new Team("Pyramids",   "T03", "Ahmed Sayed");
        Team smouha  = new Team("Smouha",     "T04", "Hassan Metwaly");

        // Players for Al Ahly
        Player p1 = new Forward("Taher Mohamed", "P001", 10, "Al Ahly", 24);
        p1.setScore(8);
        Player p2 = new Goalkeeper("Mohamed El Shennawy", "P002", 1, "Al Ahly", 31);
        ((Goalkeeper)p2).setGoalsConceded(3);
        Player p3 = new Defender("Yasser Ibrahim", "P003", 5, "Al Ahly", 27);
        Player p4 = new Midfielder("Amr El Solia", "P004", 8, "Al Ahly", 26);
        p4.setScore(3);
        ahly.addPlayer(p1); ahly.addPlayer(p2); ahly.addPlayer(p3); ahly.addPlayer(p4);

        // Players for Zamalek
        Player p5 = new Forward("Mostafa Mohamed", "P005", 9, "Zamalek", 23);
        p5.setScore(10);
        Player p6 = new Goalkeeper("Mahmoud Kahraba", "P006", 1, "Zamalek", 28);
        ((Goalkeeper)p6).setGoalsConceded(5);
        Player p7 = new Midfielder("Ferjani Sassi", "P007", 7, "Zamalek", 30);
        p7.setScore(4);
        zamalek.addPlayer(p5); zamalek.addPlayer(p6); zamalek.addPlayer(p7);

        // Players for Pyramids
        Player p8  = new Forward("Ramadan Sobhi", "P008", 11, "Pyramids", 26);
        p8.setScore(6);
        Player p9  = new Goalkeeper("Ahmed Sayed", "P009", 1, "Pyramids", 29);
        ((Goalkeeper)p9).setGoalsConceded(7);
        Player p10 = new Defender("Omar Gaber", "P010", 3, "Pyramids", 30);
        pyramids.addPlayer(p8); pyramids.addPlayer(p9); pyramids.addPlayer(p10);

        // Players for Smouha
        Player p11 = new Forward("Khaled Qasem", "P011", 10, "Smouha", 25);
        p11.setScore(5);
        Player p12 = new Goalkeeper("Hassan Metwaly", "P012", 1, "Smouha", 32);
        ((Goalkeeper)p12).setGoalsConceded(9);
        smouha.addPlayer(p11); smouha.addPlayer(p12);

        league.addTeam(ahly); league.addTeam(zamalek);
        league.addTeam(pyramids); league.addTeam(smouha);

        for (Player p : new Player[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12})
            league.addPlayerToSystem(p);

        // 8 Matches (some played, some upcoming)
        Match m1 = new Match("M01", "2024-12-01", "Al Ahly",  "Zamalek",   "Ref1", "Cairo Stadium");
        Match m2 = new Match("M02", "2024-12-05", "Pyramids", "Smouha",    "Ref2", "Air Defense");
        Match m3 = new Match("M03", "2024-12-10", "Al Ahly",  "Pyramids",  "Ref3", "Borg El Arab");
        Match m4 = new Match("M04", "2024-12-15", "Zamalek",  "Smouha",    "Ref4", "Petrosport");
        Match m5 = new Match("M05", "2025-06-01", "Zamalek",  "Al Ahly",   "Ref1", "Cairo Stadium");
        Match m6 = new Match("M06", "2025-06-08", "Smouha",   "Pyramids",  "Ref2", "Air Defense");
        Match m7 = new Match("M07", "2025-06-15", "Pyramids", "Al Ahly",   "Ref3", "Borg El Arab");
        Match m8 = new Match("M08", "2025-06-22", "Smouha",   "Zamalek",   "Ref4", "Petrosport");

        // Apply scores to past matches
        league.addMatch(m1); league.updateMatchScore(m1, 2, 1);
        league.addMatch(m2); league.updateMatchScore(m2, 1, 1);
        league.addMatch(m3); league.updateMatchScore(m3, 3, 0);
        league.addMatch(m4); league.updateMatchScore(m4, 2, 0);
        league.addMatch(m5);
        league.addMatch(m6);
        league.addMatch(m7);
        league.addMatch(m8);

        System.out.println("[System] Sample data loaded.");
    }

    // ==================== HELPER ====================
    static int readInt() {
        try {
            String line = sc.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.");
            return 0;
        }
    }
}
