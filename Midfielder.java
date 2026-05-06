
public class Midfielder extends Player {
    public Midfielder(String name, String playerID, int number, String teamName, int age) {
        super(name, playerID, number, teamName, age);
    }
    @Override
    public String getPosition() { return "Midfielder"; }
}
