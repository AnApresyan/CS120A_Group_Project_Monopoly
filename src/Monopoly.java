import java.util.ArrayList;
/**
 * >>>>> CHANGELOG <<<<<
 * An & Al: MONOPOLY 0.1.0  04/13/2021
 * 1) created the whole hierarchy of the game;
 * 2) created Player class.
 * 
 * An & Al: MONOPOLY 0.2.0  04/14/2021
 * 1) implemented the main gameplay loop;
 * 2) created the rest of trivial classes.
 * 
 * MONOPOLY 0.2.1           04/15/2021
 * Al: 
 * 1) added getRent() computation method for Utility & Railroad;
 * 2) improved overall performance of the gameplay loop;
 * 3) implemented detection of Monopoly and count of owned props of the same color.
 * An:
 * 3) implemented mortgaging;
 * 4) implemented canBeMortgaged() and the bankruptcy loop.
 * 
 * Al: MONOPOLY 0.3.0.      04/18/2021
 * 1) moved dice[2] to Player;
 * 2) created holdsDoubles() for Player class;
 * 3) added getters and setters here and there;
 * 4) added "Go To Jail" functionality and changed the startGame loop accordingly;
 * 5) cleaned up the code a bit;
 * 6) added daysInJail int in Player to force the player leave the jail after 3 days;
 * 7) fixed a bug causing the player omit GO each time a new round around the map is passed;
 * 8) modified movePlayer() such that if GO is passed, the player is granted $200.
 * 
 * An: MONOPOLY 0.4.0       04/19/2021
 * 1) added basic Swing support;
 * 2) added a tester for play window; added basic interface;
 * 3) added logo on the starting screen; added START button.
 * 
 * An: MONOPOLY 0.4.1       04/20/2021
 * 1) fixed an issue causing the content not to display until the window is resized.
 * 
 * MONOPOLY 0.5.0           04/21/2021
 * Al: 
 * 1) movePlayer() now receives an int;
 * 2) implemented the functionality of Community Chest and Chance squares;
 * 3) movePlayer() improved further such that the renewal of coordinates depenends on getCoordinate();
 * 4) scrapped index instance variable in Player class (was unused);
 * 5) moved the board setup into separate static Board class to access it everywhere.
 * An:
 * 6) overall improvement of interface.
 * 
 * MONOPOLY 0.6.0           04/22/2021
 * An:
 * 1) added the basic grid for interface;
 * 2) created a visual representation of the playboard;
 * Al:
 * 3) added functionality of Community Chest;
 * 4) made the board static to be able to access it everywhere;
 * 5) made the players static to be able to access it everywhere;
 * 6) started working on housing mechanics.
 */
public class Monopoly 
{
    public static final int    JAIL_FINE = 50;

    private static ArrayList<Player>    players = new ArrayList<>();
    private Player                      activePlayer;
    private int                         indexOfPlayer;

    public Monopoly(ArrayList<Player> players)
    {
        setPlayers(players);
        indexOfPlayer = 0;          // An: we should probably change this to allow each player throw dice and the one with the biggest dice value to be the first player
                                    // Al: good idea, but let's leave it for later, if we have time 
    }
    
    public void startGame(){
        printHeader();
        printMap();
        printFooter();
        while (true)
        {
            if (indexOfPlayer == players.size())
                this.indexOfPlayer = 0;
            this.activePlayer = players.get(indexOfPlayer);
            // Al: added a check on throwing the dice to move for if the player's prisoned.
            // if they are, then the dice will not be thrown, and the player won't move.
            if (!(activePlayer.getIsPrisoned()))
                Utility.setDice(activePlayer.throwDice());
            // Al: updated movePlayer() such that if the player's prisoned, the coords do not change.
            activePlayer.movePlayer(activePlayer.getDice());
            // Al: having the dice not thrown, the player not moved, and the coords = 10 after landing 
            // on "Go To Jail", the jail's doAction() will fire asking for more options 
            Board.getSquares()[activePlayer.getCoordinate()].doAction(activePlayer);
            if (!(activePlayer.holdsDoubles()))
                indexOfPlayer++;
            printHeader();
            printMap();
            printFooter();
            if (players.size() == 1)
                break ;
        }
        System.out.println("Congratulations, " + players.get(0).getName() + "! You are the ultimate monopolist!");
    }

    private void printHeader()
    {

    }

    private void printMap()
    {

    }

    private void printFooter()
    {

    }

    public void setPlayers(ArrayList<Player> newplayers)
    {
        players = newplayers;             
    }

    public static ArrayList<Player> getPlayers()
    {
        return (players);
    }
}
