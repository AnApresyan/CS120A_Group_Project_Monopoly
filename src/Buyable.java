public abstract class Buyable extends Square
{

    private Player  owner;
    private int     price;

    public static final int[][] COLORS = {{1, 3}, {6, 8, 9}, {11, 13, 14}, {16, 18, 19}, {21, 23, 24}, {26, 27, 29}, {31, 32, 34}, {37, 39}};
    
    
    
    public  Buyable(int coordinate)
    {
        super(coordinate);
    }
    public void setPrice(int price){
        this.price = price;
    } 
    // public int getPrice(){
    //     return (25 * (int)Math.pow(2, this.owner.ownsOfThisColor(this) - 1));
    // }
    public int getPrice(){
        return this.price;
    }
    public void setOwner(Player player){
        this.owner = player;
    }
    
    public abstract int getRent();

    public void doAction(int[] dice, Player activePlayer){
        if (this.owner == null)
            activePlayer.buyProperty(this);
        else{
            if (!this.owner.equals(activePlayer)){
                activePlayer.rentProperty(this);
            }
        }

    }
    public Player getOwner()
    {
        return (this.owner);
    }
}
