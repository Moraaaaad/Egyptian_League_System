
public abstract class Player {
    private String name;
    private String playerID;
    private int number;
    private String teamName;
    private int age;
    private int score; // goals scored
    private String rank;

    public Player(String name, String playerID, int number, String teamName, int age) {
        this.name = name;
        this.playerID = playerID;
        this.number = number;
        this.teamName = teamName;
        this.age = age;
        this.score = 0;
        this.rank = "Regular";
    }

    // Getters
    public String getName()     { return name; }
    public String getPlayerID() { return playerID; }
    public int    getNumber()   { return number; }
    public String getTeamName() { return teamName; }
    public int    getAge()      { return age; }
    public int    getScore()    { return score; }
    public String getRank()     { return rank; }

    // Setters
    public void setName(String name)         { this.name = name; }
    public void setNumber(int number)        { this.number = number; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public void setAge(int age)              { this.age = age; }
    public void setScore(int score)          { this.score = score; }
    public void setRank(String rank)         { this.rank = rank; }

    public abstract String getPosition();

    public void displayInfo() {
        System.out.println("-----------------------------");
        System.out.println("Name      : " + name);
        System.out.println("ID        : " + playerID);
        System.out.println("Position  : " + getPosition());
        System.out.println("Number    : " + number);
        System.out.println("Team      : " + teamName);
        System.out.println("Age       : " + age);
        System.out.println("Goals     : " + score);
        System.out.println("Rank      : " + rank);
    }

    // For file saving: CSV line
    public String toCSV() {
        return getPosition() + "," + name + "," + playerID + "," + number + ","
                + teamName + "," + age + "," + score + "," + rank;
    }
}
