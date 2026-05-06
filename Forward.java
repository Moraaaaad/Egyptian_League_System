
public class Forward extends Player {
    public Forward(String name, String playerID, int number, String teamName, int age) {
        super(name, playerID, number, teamName, age);
    }
    @Override
    public String getPosition() { return "Forward"; }
}
