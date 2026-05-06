
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Match {
    private String matchID;
    private LocalDate date;
    private String team1Name;
    private String team2Name;
    private String referee;
    private int scoreTeam1;
    private int scoreTeam2;
    private String stadiumName;
    private boolean played; // has match been played?

    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Match(String matchID, String date, String team1Name, String team2Name,
                 String referee, String stadiumName) {
        this.matchID = matchID;
        this.date = LocalDate.parse(date, FMT);
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.referee = referee;
        this.stadiumName = stadiumName;
        this.scoreTeam1 = 0;
        this.scoreTeam2 = 0;
        this.played = false;
    }

    // Getters
    public String    getMatchID()     { return matchID; }
    public LocalDate getDate()        { return date; }
    public String    getTeam1Name()   { return team1Name; }
    public String    getTeam2Name()   { return team2Name; }
    public String    getReferee()     { return referee; }
    public int       getScoreTeam1()  { return scoreTeam1; }
    public int       getScoreTeam2()  { return scoreTeam2; }
    public String    getStadiumName() { return stadiumName; }
    public boolean   isPlayed()       { return played; }

    // Setters
    public void setDate(String d)          { this.date = LocalDate.parse(d, FMT); }
    public void setTeam1Name(String t)     { this.team1Name = t; }
    public void setTeam2Name(String t)     { this.team2Name = t; }
    public void setReferee(String r)       { this.referee = r; }
    public void setStadiumName(String s)   { this.stadiumName = s; }
    public void setPlayed(boolean b)       { this.played = b; }

    public void setScore(int s1, int s2) {
        this.scoreTeam1 = s1;
        this.scoreTeam2 = s2;
        this.played = true;
    }

    public boolean isUpcoming() {
        return date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now());
    }

    public void displayInfo() {
        System.out.println("=============================");
        System.out.println("Match ID  : " + matchID);
        System.out.println("Date      : " + date.format(FMT));
        System.out.println("Teams     : " + team1Name + " vs " + team2Name);
        System.out.println("Referee   : " + referee);
        System.out.println("Stadium   : " + stadiumName);
        if (played) {
            System.out.println("Score     : " + scoreTeam1 + " - " + scoreTeam2);
        } else {
            System.out.println("Status    : Not played yet");
        }
    }

    // CSV for file
    public String toCSV() {
        return matchID + "," + date.format(FMT) + "," + team1Name + "," + team2Name
                + "," + referee + "," + scoreTeam1 + "," + scoreTeam2
                + "," + stadiumName + "," + played;
    }
}
