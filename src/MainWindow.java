import java.util.ArrayList;
import java.awt.event.*;
//import java.util.concurrent.Flow;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


public class MainWindow extends JFrame implements ActionListener{
                 
    private int numberOfPlayers;            //maybe needs to be local?
    
    //the actual game (gonna call methods like throwDice() and other stuff from here)
    private Monopoly game;

    private ArrayList<JLabel> sprites = new ArrayList<>();
    
    private CustomButton[] buttons = new CustomButton[40];
    private JLayeredPane boardContainer;    //was JPanel

    //board components
    private JPanel board;
    private JPanel top;
    private JPanel left;
    private JPanel right;
    private JPanel bottom;
    private JPanel center;
    private TitleDeed titleDeed;

    private int numOfNames;             //maybe not needed idk

    //info components
    private JPanel info;
    private JPanel infoTop;
    private JPanel infoCenter;
    private JPanel infoBottom;

    //infoTop
    private JLabel[] playerInfo;

    //infoCenter
    private Commands commands;


    //infoBottom buttons
    private JButton throwDice;
    private JButton done;

    //belongings panel

    private JPanel belongingsPanel;
    BelongingButton[] belongingButtons;

    //pop up window
    private PopupFactory    popupFactory;
    private Popup           popUp;
    private CustomPopUp     popUpWindow;
    // private AuctionPopUp    popUpAuction;

    // private class AuctionPopUp extends JPanel implements ActionListener
    // {
    //     private JPanel              biddersPanel;
    //     private ArrayList<JLabel>   bidders = new ArrayList<>();
    //     private JPanel              slider;
    //     private JPanel              auctionButtons;
    //     private JButton             confirm;
    //     private JButton             giveUp;

    //     private AuctionPopUp()
    //     {
    //         this.setLayout(new BorderLayout());
    //         this.setSize(new Dimension(350, 450));

    //         this.biddersPanel = new JPanel();
    //         this.biddersPanel.setLayout(new GridLayout(game.getNumberOfPlayers(), 1));      //in order to keep things compact
    //         for (int i = 0; i < game.getNumberOfPlayers(); i++)
    //         {
    //             playerInfo[i] = new JLabel();
    //             //playerInfo[i].setFont(new Font("Serif", Font.ITALIC, 18));
    //             playersInfo.add(playerInfo[i]);
    //         }

    //     }

    //     private void refreshPlayersInfo()
    //     {
    //         for (int i = 0; i < game.getNumberOfPlayers(); i++)
    //         {
    //             playerInfo[i].setText(Monopoly.getPlayers().get(i).toString());     //check
    //             if (Monopoly.getPlayers().get(i).equals(game.getActivePlayer()))
    //                 playerInfo[i].setFont(new Font("TimesRoman", Font.BOLD, 14));
    //             else
    //                 playerInfo[i].setFont(new Font("Monaco", Font.PLAIN, 14));
    //         }
    //     }
    // }

    private class CustomPopUp extends JPanel implements ActionListener
    {
        private TitleDeed   titleDeed;
        private JPanel      popUpButtons;
        private JButton     build;
        private JButton     destroy;
        private JButton     mortgage;
        private JButton     liftMortgage;
        private int         coordinate;
        
        private CustomPopUp()
        {
            this.setLayout(new BorderLayout());
            this.setSize(new Dimension(350, 450));
            this.titleDeed = new TitleDeed();
            this.titleDeed.setSize(new Dimension(300, 300));
        
            //button panel
            this.popUpButtons = new JPanel();
            this.popUpButtons.setLayout(new GridLayout(2, 2, 5, 5));
    
            this.build = new JButton("Erect House");
            this.destroy = new JButton("Destroy House");
            this.mortgage = new JButton("Mortgage Property");
            this.liftMortgage = new JButton("Lift Mortgage");
            build.addActionListener(this);
            destroy.addActionListener(this);
            mortgage.addActionListener(this);
            liftMortgage.addActionListener(this);

    
            this.popUpButtons.add(build);
            this.popUpButtons.add(destroy);
            this.popUpButtons.add(mortgage);
            this.popUpButtons.add(liftMortgage);

            this.add(titleDeed, BorderLayout.NORTH);
            this.add(popUpButtons, BorderLayout.CENTER);
        }

        private void updatePopUp(int coordinate)
        {
            if (!(Board.getSquares()[coordinate] instanceof Buyable))
                return ;
            this.coordinate = coordinate;
            this.titleDeed.setEverything(coordinate);
            this.updateButtons(coordinate);
        }

        public void updateButtons(int coordinate)
        {
            if (Board.getSquares()[coordinate].getClass().getName().equals("Property"))
            {
                if (((Property)Board.getSquares()[coordinate]).canBeImproved())
                    build.setEnabled(true);
                else
                    build.setEnabled(false);
                if (((Property)Board.getSquares()[coordinate]).getHouses() > 0)
                    destroy.setEnabled(true);
                else
                    destroy.setEnabled(false);
            }
            else
            {
                build.setEnabled(false);
                destroy.setEnabled(false);
            }

            if (((Buyable) Board.getSquares()[coordinate]).canBeMortgaged())
                mortgage.setEnabled(true);
            else
                mortgage.setEnabled(false);

            if (((Buyable) Board.getSquares()[coordinate]).isMortgaged())
                liftMortgage.setEnabled(true);
            else
                liftMortgage.setEnabled(false);;
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (this.coordinate > Board.getSquares().length)                //or maybe exception
                return ;

            if (e.getSource() == build)
                game.build(Board.getSquares()[coordinate]);
            else if (e.getSource() == destroy)
                game.destroy(Board.getSquares()[coordinate]);
            else if (e.getSource() == mortgage)
                game.mortgage(Board.getSquares()[coordinate]);
            else if (e.getSource() == liftMortgage)
                game.liftMortgage(Board.getSquares()[coordinate]);

            MainWindow.this.titleDeed.setEverything(this.coordinate);
            updateButtons(this.coordinate);
            updatePopUp(this.coordinate);
            setUpInfoTop();   
        }
    }

     
    private class TitleDeed extends JPanel
    {
        private JLabel  titleOfDeed;
        private JLabel  priceOfDeed;
        private JLabel  ownerNameOfDeed;
        private JLabel  rentOfDeed;
        private JLabel  housePriceOfDeed;
        private JLabel  housesOfDeed;
        private JLabel  isMortgagedOfDeed;
        //private JLabel mortgageValue;

        private TitleDeed()
        {
            super();
            this.titleOfDeed = new JLabel();
            this.priceOfDeed = new JLabel();
            this.ownerNameOfDeed = new JLabel();
            this.rentOfDeed = new JLabel();
            this.housePriceOfDeed = new JLabel();
            this.isMortgagedOfDeed = new JLabel();
            this.housesOfDeed = new JLabel();

            this.setLayout(new GridLayout(10, 1));
            //this.setPreferredSize(new Dimension(150, 200));
            //this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(titleOfDeed);
            this.add(priceOfDeed);
            this.add(isMortgagedOfDeed);
            this.add(ownerNameOfDeed);
            this.add(rentOfDeed);
            this.add(housePriceOfDeed);
            this.add(housesOfDeed);
            // this.setBackground(Color.GRAY);
            // this.setOpaque(true);
        }
        // private TitleDeed(){
        //     super();
        // }

        private void setEverything(int i)
        {
            setEmpty();
            setTilteOfDeed(Board.getSquares()[i].getTitle());
            if(Board.getSquares()[i] instanceof Buyable)
            {
                setPriceOfDeed(((Buyable)Board.getSquares()[i]).getPrice());
                if (Board.getSquares()[i].getClass().getName().equals("Property"))
                {
                    setHousePriceOfDeed(((Property)Board.getSquares()[i]).getHousePrice());
                    setHousesOfDeed(((Property)Board.getSquares()[i]).getHouses());
                }
                if (!(Board.getSquares()[i].getClass().getName().equals("Utility")))
                    setRentOfDeed(((Buyable)Board.getSquares()[i]).getRent());
                setIsMortgagedOfDeed(((Buyable)Board.getSquares()[i]).isMortgaged());
                if (((Buyable)Board.getSquares()[i]).getOwner() != null)
                    setOwnerNameOfDeed(((Buyable)Board.getSquares()[i]).getOwner().getName());
                else
                    setOwnerNameOfDeed("none");
            }


        }
        private void setEmpty()
        {
            this.priceOfDeed.setText("");
            this.ownerNameOfDeed.setText("");
            this.rentOfDeed.setText("");
            this.isMortgagedOfDeed.setText("");
            this.housePriceOfDeed.setText("");
            this.housesOfDeed.setText("");
        }

        private void setTilteOfDeed(String title)
        {
            this.titleOfDeed.setText(title);
        }

        private void setPriceOfDeed(int price)
        {
            this.priceOfDeed.setText("Price: $" + price);
        }

        private void setOwnerNameOfDeed(String ownerName)
        {
            this.ownerNameOfDeed.setText("Owner: " + ownerName);
        }

        private void setRentOfDeed(int rent)
        {
            this.rentOfDeed.setText("Current rent: $" + rent);
        }

        private void setIsMortgagedOfDeed(boolean state)
        {
            this.isMortgagedOfDeed.setText("Is mortgaged? " + state);
        }

        private void setHousesOfDeed(int houses)
        {
            this.housesOfDeed.setText("Houses built: " + houses);
        }

        private void setHousePriceOfDeed(int housePrice)
        {
            this.housePriceOfDeed.setText("Houses cost $" + housePrice + " each");
        }
    }
   
    private class CustomButton extends JButton
    {
        protected Color color;
        protected int direction;

        public CustomButton(){}

        private CustomButton(int direction){
            super();
            this.direction = direction;
        }

        protected void setColor(Color color){
            this.color = color;
        }

        private Color getColor(){
            return this.color;
        }

        public void paintComponent(Graphics g){
            
            super.paintComponent(g);
            if (this.color != null){
                g.setColor(color);
                if (direction == 1)
                    g.fillRect(0, 0, this.getWidth(), this.getHeight()/4);
                else if (direction == 3)
                    g.fillRect(0, 3*this.getHeight()/4, this.getWidth(), this.getHeight()/4);
                else if (direction == 2)
                    g.fillRect(3*this.getWidth()/4, 0, this.getWidth()/4, this.getHeight());
                else if (direction == 4)
                    g.fillRect(0, 0, this.getWidth()/4, this.getHeight());
            }
            
        }
    
    }   
    
    private class BelongingButton extends CustomButton implements ActionListener
    {
        private int coordinate;
        private BelongingButton()
        {
            super();
            this.setFocusable(false);
            this.addActionListener(this);
            this.setBackground(Color.WHITE);
            //this.setCoordinate(index);
        }

        private void setCoordinate(int coordinate){
            this.coordinate = coordinate;
            //this.setText(Integer.toString(coordinate));
            this.setText(" ");      //until I find a way to give it a proper size
            
        }
        private int getCoordinate(){
            return this.coordinate;
        }

        private void setClickable(boolean bool){
            if (bool == true){
                this.setEnabled(true);
                this.setBackground(Color.white);
                this.setOpaque(true);
                repaint();
            }
            else{
                this.setBackground(new Color(234, 236, 238));
                this.setEnabled(false);
                this.setOpaque(true);

                repaint();
            }
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if (this.color != null){
                g.setColor(color);
                g.fillRect(0, 0, this.getWidth(), this.getHeight()/2);
            }
            
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (belongingButtons.length > Board.getSquares().length)        //Checking not to have an exception
                return ;
            for (int i = 0; i < belongingButtons.length; i++){
                if (e.getSource() == belongingButtons[i])
                {
                    popUpWindow.updatePopUp(belongingButtons[i].getCoordinate());
                    JOptionPane.showMessageDialog(null, popUpWindow, "Title Deed", JOptionPane.PLAIN_MESSAGE);;
                }
            }        
            
        }

        
    }

    private void setCoordinatesOfBelongings(BelongingButton[] buttons)
    {
        int indexOfButtons = 0;
        int indexOfSquares = 0;
        while (indexOfButtons < buttons.length && indexOfSquares < Board.getSquares().length){
            while (!(Board.getSquares()[indexOfSquares] instanceof Buyable)){
                indexOfSquares++;
            }
            buttons[indexOfButtons++].setCoordinate(indexOfSquares++);
        }   
    }

    
    //for Easy grouping and access
    private class Commands extends JPanel implements ActionListener
    {
        private JTextArea message;
        private JPanel containerForLabel;

        //button panel 
        private JPanel button = new JPanel();

        //buttons for Buyable;
        private JButton yes = new JButton();
        private JButton no = new JButton();

        //buttons for the cards
        private JButton ok = new JButton();

        //buttons for the Tax
        private JButton money = new JButton();
        private JButton percent = new JButton();

        //buttons for Jail
        private JButton pay;
        private JButton useTheCard;


        private Commands()
        {
            //BoxLayout theLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
           // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setLayout(new GridLayout(2, 1));
            this.setPreferredSize(new Dimension(250, 250));
            
            //this.add(Box.createVerticalGlue()); 
            containerForLabel = new JPanel();
            containerForLabel.setLayout(new GridLayout());

            this.message = new JTextArea();
            this.message.setOpaque(false);
            this.message.setWrapStyleWord(true);
            this.message.setLineWrap(true);
            this.message.setEditable(false);
            this.message.setFocusable(false);
            this.message.setFont(new Font("Futura", Font.CENTER_BASELINE, 14));

            this.button = new JPanel();
            this.button.setLayout(new FlowLayout());
            
            this.yes = new JButton("Yes");
            this.no = new JButton("No");
            this.ok = new JButton("Ok");
            this.money = new JButton("200$");
            this.percent = new JButton("10% of your wealth");
            this.pay = new JButton("Pay 50$");
            this.useTheCard = new JButton("Use the card");

            yes.addActionListener(this);
            no.addActionListener(this);
            ok.addActionListener(this);
            money.addActionListener(this);
            percent.addActionListener(this);
            pay.addActionListener(this);
            useTheCard.addActionListener(this);

            this.button.add(yes);
            this.button.add(no);
            this.button.add(ok);
            this.button.add(money);
            this.button.add(percent);
            this.button.add(pay);
            this.button.add(useTheCard);

            //this.setBackground(Color.DARK_GRAY);
            this.setOpaque(true);
            this.setVisible(false);
            containerForLabel.add(message);
            this.add(containerForLabel);
            this.add(button);
        }

        private void setAllButtonsVisible(boolean bool)
        {
            this.yes.setVisible(bool);
            this.no.setVisible(bool);
            this.ok.setVisible(bool);
            this.money.setVisible(bool);
            this.percent.setVisible(bool);
            this.pay.setVisible(bool);
            this.useTheCard.setVisible(bool);
        }

        public void setVisible(boolean bool)
        {
            this.message.setVisible(bool);
            this.setAllButtonsVisible(bool);
        }

        private void setBuyable()
        {
            if (!(Board.getSquares()[game.getActivePlayerCoordinate()] instanceof Buyable))
                return;
            
            //game.setMessage();  
            game.play(); //just added 
            this.message.setText((Board.getSquares()[game.getActivePlayerCoordinate()]).getMessage());
            this.message.setVisible(true);
            //game.play();
            if (((Buyable)Board.getSquares()[game.getActivePlayerCoordinate()]).getOwner() == null)
            {
                this.yes.setVisible(true);
                if (game.getActivePlayer().getMoney() < ((Buyable)Board.getSquares()[game.getActivePlayerCoordinate()]).getPrice())
                    this.yes.setEnabled(false);
                this.no.setVisible(true);
            }
            
        }

        private void setCustomCards(){
            if (!(Board.getSquares()[game.getActivePlayerCoordinate()] instanceof Deck)){
                return;
            }
            this.message.setText((Board.getSquares()[game.getActivePlayerCoordinate()]).getMessage());
            this.message.setVisible(true);
            this.ok.setVisible(true);
        }

        private void setGoTaxFree(){
            if (!Board.getSquares()[game.getActivePlayerCoordinate()].getClass().getName().equals("GOTaxFree")){
                return;
            }
            this.message.setText(Board.getSquares()[game.getActivePlayerCoordinate()].getMessage());
            this.message.setVisible(true);
            if (game.getActivePlayerCoordinate() == 4){
                this.money.setVisible(true);
                this.percent.setVisible(true);
            }
            if (game.getActivePlayerCoordinate() == 30){
                throwDice.setEnabled(false);
                done.setEnabled(true);
            }
            game.play();
        }

        private void setJail(){
            if (!(Board.getSquares()[game.getActivePlayerCoordinate()].getClass().getName().equals("Jail"))){
                System.out.println("Checked Jail");
                return;
            }
            //ATTENTION : Just Trying
            game.play();//game.setMessage();
            this.message.setVisible(true);
            this.message.setText(Board.getSquares()[game.getActivePlayerCoordinate()].getMessage());
            if (((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).allowCard())
                this.useTheCard.setVisible(true);
            if (((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).allowPay())
                this.pay.setVisible(true);
            if (((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).allowThrow()){
                throwDice.setEnabled(true);
            }
            else{
                throwDice.setEnabled(false);

            }
            
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
            setAllButtonsVisible(false);
            if (e.getSource() == yes)
            {
                //((Buyable)Board.getSquares()[game.getActivePlayerCoordinate()]).setWantsToBuy(true);  JUST REMOVED FOR TESTING
                
                //Board.getSquares()[game.getActivePlayerCoordinate()]).doAction(game.activePlayer);
                game.play(true);
                this.setVisible(false);
            }
            else if (e.getSource() == no)
            {
               // ((Buyable)Board.getSquares()[game.getActivePlayerCoordinate()]).setWantsToBuy(false);     //JUST REMOVED FOR TESTING
                //Board.getSquares()[game.getActivePlayerCoordinate()]).doAction(game.activePlayer);
                ///need to open the auction part
                game.play(false);
                this.setVisible(false);
            }
            else if (e.getSource() == ok){
                int previousCoordinate = game.getActivePlayerCoordinate();
                // remove sprite
                buttons[previousCoordinate].remove(sprites.get(game.getActivePlayerIndex()));
                buttons[previousCoordinate].revalidate();
                buttons[previousCoordinate].repaint();
                //Board.getSquares()[game.getActivePlayerCoordinate()].doAction(game.activePlayer);
                game.play();
                setVisible(false);
                if (Board.getSquares()[previousCoordinate].getClass().getName().equals("Chance")){
                    if (((Chance)Board.getSquares()[previousCoordinate]).ifcallDoAction())
                        setUpInfoCenter();
                    else
                        this.setVisible(false);
                }
                // put sprite
                buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));

            }
            else if (e.getSource() == money){
                ((GOTaxFree)Board.getSquares()[game.getActivePlayerCoordinate()]).setChoice(1);
                //Board.getSquares()[game.getActivePlayerCoordinate()].doAction(game.activePlayer);
                game.play();
                this.setVisible(false);
            }

            else if (e.getSource() == percent){
                ((GOTaxFree)Board.getSquares()[game.getActivePlayerCoordinate()]).setChoice(2);
                //Board.getSquares()[game.getActivePlayerCoordinate()].doAction(game.activePlayer);
                game.play();
                this.setVisible(false);
            }
            else if (e.getSource() == pay || e.getSource() == useTheCard){
                if (e.getSource() == pay){
                    //((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).setUserChoice(1);        //JUST CHANGED
                    game.play(1);
                }
                else
                    game.play(2);//((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).setUserChoice(2);

                throwDice.setEnabled(true);
                //Board.getSquares()[game.getActivePlayerCoordinate()].doAction(game.activePlayer);
                //game.play();      //JUST CHANGED
                message.setText(Board.getSquares()[game.getActivePlayerCoordinate()].getMessage());
            }
            titleDeed.setEverything(game.getActivePlayerCoordinate());
            setUpInfoTop();
            
        }
    }
    

    public MainWindow(){
        super("Monopoly");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 700);                 //this.setSize(1300, 750);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        // this.getContentPane().setBackground(new Color(64, 184, 182));
        //setting up
        //this.board = new JPanel();
        this.info = new JPanel();
        this.infoTop = new JPanel();
        this.belongingsPanel = new JPanel();
        this.infoCenter = new JPanel();
        this.infoBottom = new JPanel();
        this.popUpWindow = new CustomPopUp();
        // popupFactory = new PopupFactory();
        // popUp = popupFactory.getPopup(this, popUpWindow, 500, 500);
        
        //popUpWindow.setAlwaysOnTop(true);
        numOfNames = 1;
        
    

        //The images
        ImageIcon image = new ImageIcon("./images/LOGO1.png");
        //ImageIcon image = new ImageIcon("CS120A_Group_Project_Monopoly/images/logo2.png");
        
        
        //The main panel
        JPanel mainMenu = new JPanel();
        // mainMenu.setBackground(new Color(64, 184, 182));

        //Labels
        JLabel mainLabel = new JLabel("Welcome to Alexander's and Anahit's Monopoly");
        // mainLabel.setFont(new Font("Futura", Font.ROMAN_BASELINE, 14));
        JPanel setUp = new JPanel();        //change
        
        //the array list of players that will be given to the game when it is initialized
        ArrayList<Player> players = new ArrayList<>(); 
        
        //The names of the players
        JLabel namesLabel = new JLabel();
        JTextField nameFromField = new JTextField();
        nameFromField.setPreferredSize(new Dimension(80, 40));
        JButton submitName = new JButton("Submit");
        submitName.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameFromField.getText();
                if (name.equals("")){
                    namesLabel.setText( "Empty name. " + namesLabel.getText());
                    return;
                }
                for (Player player : players) {
                    if (name.equalsIgnoreCase(player.getName())){
                        namesLabel.setText( "There is already a player with that name. " + namesLabel.getText());
                        nameFromField.setText("");
                        return;
                    }
                }
                players.add(new Player(name));
                /*numberOfPlayers--;*/
                if (numOfNames < numberOfPlayers){  
                    namesLabel.setText("Please enter the name of Player " + (players.size() + 1));
                    
                    nameFromField.setText("");
                    numOfNames++;
                }
                else{
                    setUp.setVisible(false);
                    mainMenu.setVisible(false);
                    game = new Monopoly(players);
                            // initializing player sprites
                    sprites.add(new JLabel("", new ImageIcon(new ImageIcon("CS120A_Group_Project_Monopoly/images/Player1.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
                    sprites.add(new JLabel("", new ImageIcon(new ImageIcon("CS120A_Group_Project_Monopoly/images/Player2.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
                    if (numOfNames >= 3)
                        sprites.add(new JLabel("", new ImageIcon(new ImageIcon("CS120A_Group_Project_Monopoly/images/Player3.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
                    if (numOfNames >= 4)
                        sprites.add(new JLabel("", new ImageIcon(new ImageIcon("CS120A_Group_Project_Monopoly/images/Player4.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
                    setTheFlow();

                }  
            }

        });

        namesLabel.setVisible(false);
        nameFromField.setVisible(false);
        submitName.setVisible(false);

        //Number of Players
        SpinnerModel values = new SpinnerNumberModel(2, 2, 4, 1);
        JSpinner howManyPlayers = new JSpinner(values);
        howManyPlayers.setVisible(false);
        JButton submitNumberOfPlayers = new JButton("Submit");
        submitNumberOfPlayers.setVisible(false);
        submitNumberOfPlayers.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfPlayers = (Integer) howManyPlayers.getValue();
                submitNumberOfPlayers.setVisible(false);
                howManyPlayers.setVisible(false);
                namesLabel.setText("Please enter the name of Player 1");
                namesLabel.setVisible(true);
                nameFromField.setVisible(true);
                submitName.setVisible(true);
            }
        
        });

        //Start button
        JButton start = new JButton("START");
        start.setVerticalAlignment(JButton.BOTTOM);
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start.setVisible(false);
                howManyPlayers.setVisible(true);
                submitNumberOfPlayers.setVisible(true);         
            }
        });

        //Adding to the setUp           //change
        setUp.add(start); 
        setUp.add(howManyPlayers);
        setUp.add(submitNumberOfPlayers);
        setUp.add(namesLabel);
        setUp.add(nameFromField);
        setUp.add(submitName);

        //Adding to the mainLabel
        mainLabel.setIcon(image);
        mainLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainLabel.setVerticalTextPosition(JLabel.BOTTOM);
        mainLabel.setVerticalAlignment(JLabel.BOTTOM);
        
        //Adding to the mainMenu
        mainMenu.add(mainLabel);
       
        //Adding to the frame
        this.add(mainMenu, BorderLayout.NORTH);
        this.add(setUp, BorderLayout.CENTER);            //change        mainWindiw.add(setUp)

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        
    }



    private void setButtons(){
        for (int i = 0; i < buttons.length; i++){                   //need to change this back, they do not need direction for now
            if (i > 0 && i < 10)
                buttons[i] = new CustomButton(1);
            else if (i > 10 && i < 20)
                buttons[i] = new CustomButton(2);
            else if (i > 20 && i < 30)
                buttons[i] = new CustomButton(3);
            else if (i > 30 && i < 40)
                buttons[i] = new CustomButton(4);
            else{
                buttons[i] = new CustomButton();
            }

            if (i == 1 || i == 3)
                buttons[i].setColor(new Color(192, 57, 43));
            else if (i == 6 || i == 8 || i == 9)
                buttons[i].setColor(new Color(76, 167, 236));
            else if (i == 11 || i == 13 || i == 14)
                buttons[i].setColor(new Color(236, 76, 100));
            else if (i == 16 || i == 18 || i == 19)
                buttons[i].setColor(new Color(255, 133, 51));
            else if (i == 21 || i == 23 || i == 24)
                buttons[i].setColor(new Color(247, 73, 36));
            else if (i == 26 || i == 27 || i == 29)
                buttons[i].setColor(new Color(255, 223, 19));
            else if (i == 31 || i == 32 || i == 34)
                buttons[i].setColor(new Color(31, 136, 64));
            else if (i == 37 || i == 39)
                buttons[i].setColor(new Color(31, 93, 136));

            // if (i != 10 || i != 20 || i != 30 || i != 0)
            //     buttons[i].setText(Board.getSquares()[i].getTitle());
        }

        //setButtonsColor(buttons);
        
    }

    /*private void setButtonsColor(CustomButton[] buttons){
        for (int i = 0; i < Buyable.COLORS.length; i++){
            for (int j = 0; j < Buyable.COLORS[i].length; j++){
                if (Buyable.COLORS[i][j] < buttons.length){
                    switch(i){
                        case 0:  buttons[Buyable.COLORS[i][j]].setColor(new Color(192, 57, 43));   break;
                        case 3:  buttons[Buyable.COLORS[i][j]].setColor(new Color(76, 167, 236));  break;
                        case 4:  buttons[Buyable.COLORS[i][j]].setColor(new Color(236, 76, 100));  break;
                        case 5:  buttons[Buyable.COLORS[i][j]].setColor(new Color(255, 133, 51));  break;
                        case 6:  buttons[Buyable.COLORS[i][j]].setColor(new Color(247, 73, 36));   break;
                        case 7:  buttons[Buyable.COLORS[i][j]].setColor(new Color(255, 223, 19));  break;
                        case 8:  buttons[Buyable.COLORS[i][j]].setColor(new Color(31, 136, 64));   break;
                        case 9:  buttons[Buyable.COLORS[i][j]].setColor(new Color(31, 93, 136));   break;
                    }
                }
            }
        }
    }*/


    //the board and the info
    private void setTheFlow()
    {
        this.setSize(new Dimension(1300, 750));
        this.setLocationRelativeTo(null);


        Border border = BorderFactory.createLineBorder(new Color(192, 192, 192), 1);
        this.setButtons();
        for (int i = 0; i < 40; i++)
        {
            buttons[i].addActionListener(this);
            buttons[i].setBackground(Color.white);
            buttons[i].setBorder(border);
        }

        this.setLayout(new BorderLayout());
        boardContainer = new JLayeredPane();//JPanel();
        boardContainer.setLayout(new FlowLayout());

        board = new JPanel();
        board.setSize(1000, 700);
        board.setLayout(new BorderLayout());

        top = new JPanel();
        left = new JPanel();
        right = new JPanel();
        bottom = new JPanel();
        center = new JPanel();


        top.setPreferredSize(new Dimension(693,90));
        left.setPreferredSize(new Dimension(90,513));
        right.setPreferredSize(new Dimension(90,513));
        bottom.setPreferredSize(new Dimension(693,90));
        //center.setPreferredSize(new Dimension(90,90));


        this.setUpTop();
        this.setUpBottom();
        this.setUpLeft();
        this.setUpRight();
        this.setUpInfo();
        //setUpColors();

        board.add(top,BorderLayout.NORTH);
        board.add(left,BorderLayout.WEST);
        board.add(right,BorderLayout.EAST);
        board.add(bottom,BorderLayout.SOUTH);
        board.add(center,BorderLayout.CENTER);
    
        
        boardContainer.add(board);

        //adding to the frame
        this.add(boardContainer, BorderLayout.WEST);
        this.add(info);

        // adding the players
        for (int i = 0; i < sprites.size(); i++)
            buttons[0].add(sprites.get(i));
    }


    private void setUpInfo(){

        this.titleDeed = new TitleDeed();
        titleDeed.setEverything(0);
        titleDeed.setPreferredSize(new Dimension(250, 250));

        //the components of commands
        this.commands = new Commands();
        
        info.setLayout(new BorderLayout());
        info.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        infoTop.setSize(new Dimension(300, 500));
        //infoTop.setLayout(new GridLayout(2, 1, 0, 20));                           SOS changed
        infoTop.setLayout(new BorderLayout(20, 20)); 

        
 
        //this.setButtonsColor(belongingButtons);
        
       
        infoCenter.setSize(new Dimension(300, 600));
        infoCenter.setLayout(new GridBagLayout());           //TRYING
        info.setBorder(new EmptyBorder(30, 30, 30, 30));

        infoCenter.add(titleDeed); //, BorderLayout.WEST
        infoCenter.add(commands);  //, BorderLayout.NORTH
        

        infoBottom.setSize(new Dimension(300, 600));


        //infoTop
        JPanel playersInfo = new JPanel();
        playersInfo.setLayout(new GridLayout(game.getNumberOfPlayers(), 1));      //in order to keep things compact
        playerInfo = new JLabel[game.getNumberOfPlayers()];
        for (int i = 0; i < game.getNumberOfPlayers(); i++){
            playerInfo[i] = new JLabel();
            //playerInfo[i].setFont(new Font("Serif", Font.ITALIC, 18));
            playersInfo.add(playerInfo[i]);
        }
        setUpBelongings();
        //containerOfBelongings.add(belongingsPanel);
        infoTop.add(playersInfo, BorderLayout.NORTH);
        infoTop.add(belongingsPanel, BorderLayout.SOUTH);

        setUpInfoTop();
        setUpInfoBottom();
        info.add(infoTop, BorderLayout.NORTH);
        info.add(infoCenter, BorderLayout.CENTER);
        info.add(infoBottom, BorderLayout.SOUTH);
    }

    private void setUpBelongings()
    {
        belongingButtons = new BelongingButton[28];
        belongingsPanel.setLayout(new GridLayout(3, 10, 10, 10));

        belongingsPanel.setSize(new Dimension(200, 200));
        
        
        for (int i = 0; i < belongingButtons.length; i++)
                belongingButtons[i] = new BelongingButton();

        this.setCoordinatesOfBelongings(belongingButtons);
    
        for (int i = 0; i < belongingButtons.length; i++)
        {
            belongingButtons[i].setSize(new Dimension(10, 10));
            belongingButtons[i].setColor(buttons[belongingButtons[i].getCoordinate()].getColor());
            belongingsPanel.add(belongingButtons[i]);
        }
    }

    private void setUpInfoTop()
    {
        for (int i = 0; i < game.getNumberOfPlayers(); i++)
        {
            playerInfo[i].setText(Monopoly.getPlayers().get(i).toString());     //check
            if (Monopoly.getPlayers().get(i).equals(game.getActivePlayer()))
                playerInfo[i].setFont(new Font("TimesRoman", Font.BOLD, 14));
            else
                playerInfo[i].setFont(new Font("Monaco", Font.PLAIN, 14));
        }
        updateBelongingsPane();
        //infoTop.add(belongingsPanel);
    }

    private void updateBelongingsPane()
    {
        for (int i = 0; i < belongingButtons.length; i++)
        {
            if (Board.getSquares()[belongingButtons[i].getCoordinate()] instanceof Buyable){        //check or exception handling
                if (game.getActivePlayer().owns(belongingButtons[i].getCoordinate()))
                    belongingButtons[i].setClickable(true);
                else
                    belongingButtons[i].setClickable(false);
            }
        }
    }


    private void setUpInfoCenter(){

        titleDeed.setEverything(game.getActivePlayerCoordinate());
        if(Board.getSquares()[game.getActivePlayerCoordinate()] instanceof Buyable){
            this.commands.setBuyable();
        }
        else if (Board.getSquares()[game.getActivePlayerCoordinate()] instanceof Deck){
            this.commands.setCustomCards();
        }
        else if (Board.getSquares()[game.getActivePlayerCoordinate()].getClass().getName().equals("GOTaxFree")){
            this.commands.setGoTaxFree();
        }
        else if (Board.getSquares()[game.getActivePlayerCoordinate()].getClass().getName().equals("Jail")){
            if (game.ifPlayerIsPrisoned()){
                System.out.println("the if of setUpInfoCenter");
                this.commands.setJail();
            }
            else{
                this.commands.setAllButtonsVisible(false);
                this.commands.message.setText("Just Visiting the Jail");
                this.commands.message.setVisible(true);
            }
        }
        setUpInfoTop();
    }

    private void setUpInfoBottom(){
        infoBottom.setLayout(new GridLayout());
        this.throwDice = new JButton("Throw the dice");
        // Al: WHY DOESN'T THIS WORK AAAAAAAAAAAAAAAAAAAAAAA
        // this.throwDice.setFont(new Font("Futura", Font.PLAIN, 14));
        this.done = new JButton("Done");
        // this.done.setFont(new Font("Futura", Font.PLAIN, 14));
        throwDice.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {   
                
                // remove the old sprite
                buttons[game.getActivePlayerCoordinate()].remove(sprites.get(game.getActivePlayerIndex()));
                buttons[game.getActivePlayerCoordinate()].revalidate();
                buttons[game.getActivePlayerCoordinate()].repaint();

                if (game.ifPlayerIsPrisoned()){
                    game.startGame();
                    if (!game.ifPlayerHoldsDoubles() || game.ifPlayerIsPrisoned()){      //moved to After doAction is complete
                        //System.out.println("Holds doubles");
                        throwDice.setEnabled(false);
                        done.setEnabled(true);
                    } 
                    commands.setAllButtonsVisible(false);
                    if (!game.ifPlayerIsPrisoned()){
                
                        commands.message.setText("You rolled doubles and are free to go.");
                        //setUpInfoCenter();
                        throwDice.setEnabled(false);
                        done.setEnabled(true);
                    }
                    else{
                        commands.message.setText("You failed to roll doubles. See you on the next turn!");
                        throwDice.setEnabled(false);
                    }
                    commands.message.setVisible(true);
                    return;
                }
                game.startGame();
                if (!game.ifPlayerHoldsDoubles()){      //moved to After doAction is complete
                    //System.out.println("Holds doubles");
                    throwDice.setEnabled(false);
                    done.setEnabled(true);
                }   
                if (game.getMoveToJail()){
                    return;
                }
                // put the sprite at a new space
                buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                //setUpInfoCenter();
                setUpInfoCenter();
                setUpInfoTop();  
            }
            
        });

        done.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                commands.setVisible(false);
                // put the sprite at a new space
                buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                
                game.changePlayer();
                done.setEnabled(false);
                titleDeed.setEverything(game.getActivePlayerCoordinate());
                throwDice.setEnabled(true);
                if (game.getActivePlayer().isPrisoned()){
                    System.out.println("Entered if");
                    setUpInfoCenter();
                    //System.out.println("He is prisoned");
                }
                setUpInfoTop();

                //commands.setVisible(false);
            }
            
        });
        infoBottom.add(throwDice);
        infoBottom.add(done);

    }


    //board
    private void setUpTop(){                                        //all of these setUps can be compressed to one method probably
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        buttons[20].setPreferredSize(new Dimension(90, 90));
        top.add(buttons[20]);
        for (int i = 21; i < 30; i++){
            buttons[i].setPreferredSize(new Dimension(57, 90));
            buttons[i].setLayout(new FlowLayout());
            top.add(buttons[i]);
        }

        buttons[30].setPreferredSize(new Dimension(90, 90));
        buttons[30].setLayout(new FlowLayout());
        top.add(buttons[30]);
    }

    private void setUpBottom(){
        bottom.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        buttons[10].setPreferredSize(new Dimension(90, 90));
        bottom.add(buttons[10]);
        for (int i = 9; i > 0; i--){
            buttons[i].setPreferredSize(new Dimension(57, 90));
            buttons[i].setLayout(new FlowLayout());
            bottom.add(buttons[i]);
        }

        buttons[0].setPreferredSize(new Dimension(90, 90));
        buttons[0].setLayout(new FlowLayout());
        bottom.add(buttons[0]);
    }

    private void setUpLeft(){
        left.setLayout(new FlowLayout());
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        //left.setLayout(new GridLayout(9, 1, 0, 0));

        for (int i = 19; i > 10; i--){
            buttons[i].setMaximumSize(new Dimension(90, 57));
            buttons[i].setLayout(new FlowLayout());
            left.add(buttons[i]);
        }
    }

    private void setUpRight(){
        right.setLayout(new FlowLayout());
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        for (int i = 31; i < 40; i++){
            buttons[i].setMaximumSize(new Dimension(90, 57));
            buttons[i].setLayout(new FlowLayout());
            right.add(buttons[i]);
        }
    }


    public void actionPerformed(ActionEvent e)
    {
        for (int i = 0; i < buttons.length; i++)
        {
            if (e.getSource() == buttons[i])
            {
                System.out.println("Square coordinate: " + i); 
                titleDeed.setEverything(i);
                break;
            }
        }
    }
}
