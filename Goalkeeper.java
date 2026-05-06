
public class Goalkeeper extends Player {
    private int goalsConceded; // goals let in (for ranking)

    public Goalkeeper(String name, String playerID, int number, String teamName, int age) {
        super(name, playerID, number, teamName, age);
        this.goalsConceded = 0;
    }

    public int getGoalsConceded()              { return goalsConceded; }
    public void setGoalsConceded(int g)        { this.goalsConceded = g; }

    @Override
    public String getPosition() { return "Goalkeeper"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Goals Conceded: " + goalsConceded);
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + goalsConceded;
    }
}
