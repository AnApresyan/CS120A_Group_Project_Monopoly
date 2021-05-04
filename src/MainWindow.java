import java.util.ArrayList;
import java.awt.event.*;
//import java.util.concurrent.Flow;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MainWindow extends JFrame implements ActionListener{
                 
    //private int numberOfPlayers;            //maybe needs to be local?                -----------------
    
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

    //private int numOfNames;             //maybe not needed idk        -----------------

    //info components
    private JPanel info;
    private JPanel infoTop;
    private JPanel infoCenter;
    private JPanel infoBottom;

    //infoTop
    private ArrayList<JLabel>   playersLabels = new ArrayList<>();
    private JPanel playersInfo;
    private JPanel diceInfo;
    private JLabel diceLabel;

    //infoCenter
    private Commands commands;


    //infoBottom buttons
    private GeneralButton throwDice;
    private GeneralButton done;
    private GeneralButton trade;

    //belongings panel

    private JPanel belongingsPanel;
    BelongingButton[] belongingButtons;

    //pop up window
    private CustomPopUp         popUpWindow;
    private AuctionPopUp        popUpAuction;
    private TradeSelectionPopUp popUpTradeSelection;
    private TradePopUp          popUpTrade;
    private JDialog             tradeDialog;
    
    //ifLost or has <0 money
    private JPanel          ifLost;
    private JLabel          ifLostText;
    private JDialog         dialog;

    private class TradePopUp extends JPanel implements ActionListener, ChangeListener
    {
        private JPanel              tradeInfoPanel;
        private JLabel              tradeInfoLabel;
        private JPanel              lobbyPanel;

        private JPanel              traderPanel;
        private JPanel              traderSliderPanel;
        private JSlider             traderSlider;
        private JLabel              traderSliderValue;
        private JPanel              traderBelongingsPanel;
        private JLabel              youHave;
        private JLabel              youGive;
        private JPanel              propsToGivePanel;
        private JPanel              selectedPropsToGivePanel;
        private ArrayList<JButton>  propsToGive = new ArrayList<>();
        private ArrayList<JButton>  selectedPropsToGive = new ArrayList<>();
        private JPanel              traderCardPanel;
        private JLabel              traderCardLabel;
        private JComboBox<String>   traderCard;

        private JPanel              tradeePanel;
        private JPanel              tradeeSliderPanel;
        private JSlider             tradeeSlider;
        private JLabel              tradeeSliderValue;
        private JPanel              tradeeBelongingsPanel;
        private JLabel              theyHave;
        private JLabel              theyGive;
        private JPanel              propsToReceivePanel;
        private JPanel              selectedPropsToReceivePanel;
        private ArrayList<JButton>  propsToReceive = new ArrayList<>();
        private ArrayList<JButton>  selectedPropsToReceive = new ArrayList<>();    
        private JPanel              tradeeCardPanel;
        private JLabel              tradeeCardLabel;
        private JComboBox<String>   tradeeCard;

        private JPanel              tradeButtons;

        private JButton             confirmTrade;
        private JButton             cancelTrade;
        private JButton             acceptTrade;
        private JButton             declineTrade;

        private JLabel              confirmation;
        private JPanel              confirmationPanel;

        private TradePopUp()
        {
            this.confirmation = new JLabel();
            this.confirmationPanel = new JPanel();
            this.confirmationPanel.add(this.confirmation);

            this.traderCardLabel = new JLabel();
            this.traderCardLabel.setText("Include the 'Get out of Jail' card?");
            this.tradeeCardLabel = new JLabel();
            this.tradeeCardLabel.setText("Include the 'Get out of Jail' card?");
            this.traderCard = new JComboBox<>(new String[]{"Yes", "No"});
            this.tradeeCard = new JComboBox<>(new String[]{"Yes", "No"});

            this.theyGive = new JLabel();
            this.theyGive.setText("Selected properties to receive:");
            this.theyHave = new JLabel();
            this.theyHave.setText("Tradee's properties:");

            this.youGive = new JLabel();
            this.youGive.setText("Selected properties to give:");
            this.youHave = new JLabel();
            this.youHave.setText("Your properties:");

            this.setLayout(new BorderLayout());
            //this.setSize(new Dimension(500, 500));    
            // this.setSize(new Dimension(200, 300));
            this.traderSliderValue = new JLabel();
            this.tradeeSliderValue = new JLabel();
            this.tradeInfoPanel = new JPanel();
            this.tradeInfoLabel = new JLabel();
            this.tradeInfoPanel.add(this.tradeInfoLabel);
            this.lobbyPanel = new JPanel();
            this.lobbyPanel.setLayout(new GridLayout(1, 2));
            this.traderPanel = new JPanel();
            this.traderPanel.setLayout(new GridLayout(3, 1));
            this.tradeePanel = new JPanel();
            this.tradeePanel.setLayout(new GridLayout(3, 1));
            this.tradeButtons = new JPanel();
            this.tradeButtons.setLayout(new GridLayout(1, 2));
            this.propsToGivePanel = new JPanel();
            this.selectedPropsToGivePanel = new JPanel();
            this.propsToReceivePanel = new JPanel();
            this.selectedPropsToReceivePanel = new JPanel();
            // lobby components
            this.traderSliderPanel =  new JPanel();
            this.traderSliderPanel.setLayout(new GridLayout(2, 1));
            this.traderSlider = new JSlider();
            this.traderSlider.addChangeListener(this);
            this.traderSliderPanel.add(this.traderSliderValue);
            this.traderSliderPanel.add(this.traderSlider);
            this.traderBelongingsPanel = new JPanel();
            this.traderBelongingsPanel.setLayout(new GridLayout(4, 1));
            this.traderBelongingsPanel.add(this.youHave);
            this.traderBelongingsPanel.add(this.propsToGivePanel);
            this.traderBelongingsPanel.add(this.youGive);
            this.traderBelongingsPanel.add(this.selectedPropsToGivePanel);
            this.traderCardPanel = new JPanel();
            this.traderCardPanel.setLayout(new GridLayout(2, 1));
            

            this.traderPanel.add(this.traderSliderPanel);
            this.traderPanel.add(this.traderBelongingsPanel);
            this.traderPanel.add(this.traderCardPanel);

            this.tradeeSliderPanel =  new JPanel();
            this.tradeeSliderPanel.setLayout(new GridLayout(2, 1));
            this.tradeeSlider = new JSlider();
            this.tradeeSlider.addChangeListener(this);
            this.tradeeSliderPanel.add(this.tradeeSliderValue);
            this.tradeeSliderPanel.add(this.tradeeSlider);
            this.tradeeBelongingsPanel = new JPanel();
            this.tradeeBelongingsPanel.setLayout(new GridLayout(4, 1));
            this.tradeeBelongingsPanel.add(this.theyHave);
            this.tradeeBelongingsPanel.add(this.propsToReceivePanel);
            this.tradeeBelongingsPanel.add(this.theyGive);
            this.tradeeBelongingsPanel.add(this.selectedPropsToReceivePanel);
            this.tradeeCardPanel = new JPanel();
            this.tradeeCardPanel.setLayout(new GridLayout(2, 1));

            this.tradeePanel.add(this.tradeeSliderPanel);
            this.tradeePanel.add(this.tradeeBelongingsPanel);
            this.tradeePanel.add(this.tradeeCardPanel);

            this.confirmTrade = new JButton("Confirm");
            this.cancelTrade = new JButton("Cancel");
            this.acceptTrade = new JButton("Accept");
            this.declineTrade = new JButton("Decline");
            this.confirmTrade.addActionListener(this);
            this.cancelTrade.addActionListener(this);
            this.acceptTrade.addActionListener(this);
            this.declineTrade.addActionListener(this);
            this.lobbyPanel.add(this.traderPanel);
            this.lobbyPanel.add(this.tradeePanel);
           
            // JPanel centerLobby = new JPanel();
            // centerLobby.setLayout(new BorderLayout());
            // centerLobby.setSize(new Dimension (400, 400));
            // centerLobby.add(lobbyPanel, BorderLayout.CENTER);
            this.setPreferredSize(new Dimension(300, 600));
            this.add(this.tradeInfoPanel, BorderLayout.NORTH);
            this.add(this.lobbyPanel, BorderLayout.CENTER);
            this.add(this.tradeButtons, BorderLayout.SOUTH);
        }

        private void refreshBelongingsPanel()
        {
            // deleting old buttons
            for (JButton b : this.propsToGive)
                this.propsToGivePanel.remove(b);
            this.propsToGivePanel.revalidate();
            this.propsToGivePanel.repaint();
            for (JButton b : this.selectedPropsToGive)
                this.selectedPropsToGivePanel.remove(b);
            this.selectedPropsToGivePanel.revalidate();
            this.selectedPropsToGivePanel.repaint();
            for (JButton b : this.propsToReceive)
                this.propsToReceivePanel.remove(b);
            this.propsToReceivePanel.revalidate();
            this.propsToReceivePanel.repaint();
            for (JButton b : this.selectedPropsToReceive)
                this.selectedPropsToReceivePanel.remove(b);
            this.selectedPropsToReceivePanel.revalidate();
            this.selectedPropsToReceivePanel.repaint();
            // removing old buttons
            this.propsToGive = new ArrayList<>();
            this.selectedPropsToGive = new ArrayList<>();
            this.propsToReceive = new ArrayList<>();
            this.selectedPropsToReceive = new ArrayList<>();
            // creating new buttons
            for (int i = 0; i < game.getPropsToGive().size(); i++)
                this.propsToGive.add(new JButton(game.getPropsToGive().get(i).toString()));
            for (int i = 0; i < game.getSelectedPropsToGive().size(); i++)
                this.selectedPropsToGive.add(new JButton(game.getSelectedPropsToGive().get(i).toString()));
            for (int i = 0; i < game.getPropsToReceive().size(); i++)
                this.propsToReceive.add(new JButton(game.getPropsToReceive().get(i).toString()));
            for (int i = 0; i < game.getSelectedPropsToReceive().size(); i++)
                this.selectedPropsToReceive.add(new JButton(game.getSelectedPropsToReceive().get(i).toString()));
            // adding new buttons
            this.propsToGivePanel.setLayout(new GridLayout(game.getPropsToGive().size(), 1));
            for (JButton b : this.propsToGive)
            {
                b.addActionListener(this);
                this.propsToGivePanel.add(b);
            }
            this.selectedPropsToGivePanel.setLayout(new GridLayout(game.getSelectedPropsToGive().size(), 1));
            for (JButton b : this.selectedPropsToGive)
            {
                b.addActionListener(this);
                this.selectedPropsToGivePanel.add(b);
            }
            this.propsToReceivePanel.setLayout(new GridLayout(game.getPropsToReceive().size(), 1));
            for (JButton b : this.propsToReceive)
            {
                b.addActionListener(this);
                this.propsToReceivePanel.add(b);
            }
            this.selectedPropsToReceivePanel.setLayout(new GridLayout(game.getSelectedPropsToReceive().size(), 1));
            for (JButton b : this.selectedPropsToReceive)
            {
                b.addActionListener(this);
                this.selectedPropsToReceivePanel.add(b);
            }
        }

        private void initTradePopUp()
        {
            game.setTradeLists();
            this.tradeInfoLabel.setText(game.getActivePlayer().getName() + " is trading with " + game.getTradee().getName());
            //sliders
            this.traderSlider.setMinimum(0);
            if (game.getActivePlayer().getMoney() >= 0)
                this.traderSlider.setMaximum(game.getActivePlayer().getMoney());
            else
                this.traderSlider.setMaximum(0);
            this.traderSliderValue.setText("Money to give: $" + this.traderSlider.getValue());
            this.tradeeSlider.setMinimum(0);
            this.tradeeSlider.setMaximum(game.getTradee().getMoney());
            this.tradeeSliderValue.setText("Money to receive: $" + this.tradeeSlider.getValue());
            this.tradeButtons.add(confirmTrade);
            this.tradeButtons.add(cancelTrade);
            // System.out.println("Trader: " + game.getActivePlayer() + " and their card is " + game.getActivePlayer().getGetOutOfJail());
            // System.out.println("Tradee: " + game.getTradee() + " and their card is " + game.getTradee().getGetOutOfJail());
            if (game.getActivePlayer().getGetOutOfJail() && !(game.getTradee().getGetOutOfJail()))
            {
                // System.out.println("ADDED TO TRADER");
                this.traderCardPanel.add(this.traderCardLabel);
                this.traderCardPanel.add(this.traderCard);
            }
            if (!(game.getActivePlayer().getGetOutOfJail()) && game.getTradee().getGetOutOfJail())
            {
                // System.out.println("ADDED TO TRADEE");
                this.tradeeCardPanel.add(this.tradeeCardLabel);
                this.tradeeCardPanel.add(this.tradeeCard);
            }
            this.refreshBelongingsPanel();
        }

        @Override
        public void stateChanged(ChangeEvent e) 
        {
            this.traderSliderValue.setText("Money to give: $" + this.traderSlider.getValue());
            this.tradeeSliderValue.setText("Money to receive: $" + this.tradeeSlider.getValue());
        }
        private void recoverPanel()
        {
            this.tradeButtons.remove(acceptTrade);
            this.tradeButtons.remove(declineTrade);
            this.remove(confirmationPanel);
            this.tradeButtons.add(confirmTrade);
            this.tradeButtons.add(cancelTrade);
            this.traderCardPanel.remove(this.traderCardLabel);
            this.traderCardPanel.remove(this.traderCard);
            this.traderCard.setSelectedItem("Yes");
            this.tradeeCard.setSelectedItem("Yes");
            this.tradeeCardPanel.remove(this.tradeeCardLabel);
            this.tradeeCardPanel.remove(this.tradeeCard);
            this.add(this.tradeInfoPanel, BorderLayout.NORTH);
            this.add(this.lobbyPanel, BorderLayout.CENTER);
            tradeDialog.setVisible(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if (e.getSource() == cancelTrade)
            {
                this.traderCardPanel.remove(this.traderCardLabel);
                this.traderCardPanel.remove(this.traderCard);
                this.traderCard.setSelectedItem("Yes");
                this.tradeeCard.setSelectedItem("Yes");
                this.tradeeCardPanel.remove(this.tradeeCardLabel);
                this.tradeeCardPanel.remove(this.tradeeCard);
                tradeDialog.setVisible(false);
            }
            if (e.getSource() == confirmTrade)
            {
                this.remove(this.tradeInfoPanel);
                this.remove(this.lobbyPanel);
                this.tradeButtons.remove(confirmTrade);
                this.tradeButtons.remove(cancelTrade);

                this.tradeButtons.revalidate();
                this.tradeButtons.repaint();

                this.tradeButtons.add(acceptTrade);
                this.tradeButtons.add(declineTrade);

                this.confirmation.setText(game.getTradee().getName() + ", do you want to accept the offer from " + game.getActivePlayer().getName() + "?");
                this.add(confirmationPanel, BorderLayout.NORTH);
            }

            if (e.getSource() == acceptTrade || e.getSource() == declineTrade)
            {
                if (e.getSource() == acceptTrade)
                {
                    // handing over money
                    game.getActivePlayer().receiveMoney(this.tradeeSlider.getValue());
                    game.getActivePlayer().receiveMoney(-this.traderSlider.getValue());
                    game.getTradee().receiveMoney(this.traderSlider.getValue());
                    game.getTradee().receiveMoney(-this.tradeeSlider.getValue());
                    // handing over props
                    for (Buyable b : game.getSelectedPropsToGive())
                    {
                        game.getActivePlayer().getBelongings().remove(b);
                        b.setOwner(game.getTradee());
                        game.getTradee().getBelongings().add(b);
                    }
                    for (Buyable b : game.getSelectedPropsToReceive())
                    {
                        game.getTradee().getBelongings().remove(b);
                        b.setOwner(game.getActivePlayer());
                        game.getActivePlayer().getBelongings().add(b);
                    }
                    // handing over cards
                    if (game.getActivePlayer().getGetOutOfJail() && !(game.getTradee().getGetOutOfJail()))
                    {
                        if (traderCard.getSelectedItem().equals("Yes"))
                        {
                            game.getActivePlayer().setGetOutOfJail(false);
                            game.getTradee().setGetOutOfJail(true);
                        }
                    }
                    else if (!(game.getActivePlayer().getGetOutOfJail()) && game.getTradee().getGetOutOfJail())
                    {
                        if (tradeeCard.getSelectedItem().equals("Yes"))
                        {
                            game.getTradee().setGetOutOfJail(false);
                            game.getActivePlayer().setGetOutOfJail(true);
                        }
                    }
                    updateBelongingsPane();
                    setUpInfoTop();
                }
                recoverPanel();
                game.nullifyTradeProps();
            }

            for (JButton button : this.propsToGive)
            {
                if (e.getSource() == button)
                {
                    for (Buyable buyable : game.getPropsToGive())
                        if (buyable.toString().equals(button.getText()))
                        {
                            game.getSelectedPropsToGive().add(buyable);
                            game.getPropsToGive().remove(buyable);
                            this.refreshBelongingsPanel();
                            return ;
                        }
                }
            }
            for (JButton button : this.selectedPropsToGive)
            {
                if (e.getSource() == button)
                {
                    for (Buyable buyable : game.getSelectedPropsToGive())
                        if (buyable.toString().equals(button.getText()))
                        {
                            
                            game.getPropsToGive().add(buyable);
                            game.getSelectedPropsToGive().remove(buyable);
                            this.refreshBelongingsPanel();
                            return ;
                        }
                }
            }
            for (JButton button : this.propsToReceive)
            {
                if (e.getSource() == button)
                {
                    for (Buyable buyable : game.getPropsToReceive())
                    {
                        if (buyable.toString().equals(button.getText()))
                        {
                            game.getSelectedPropsToReceive().add(buyable);
                            game.getPropsToReceive().remove(buyable);
                            this.refreshBelongingsPanel();
                            return ;
                        }
                    }
                }
            }
            for (JButton button : this.selectedPropsToReceive)
            {
                if (e.getSource() == button)
                {
                    for (Buyable buyable : game.getSelectedPropsToReceive())
                    {
                        if (buyable.toString().equals(button.getText()))
                        {
                            game.getPropsToReceive().add(buyable);
                            game.getSelectedPropsToReceive().remove(buyable);
                            this.refreshBelongingsPanel();
                            return ;
                        }
                    }
                }
            }
        }
        
    }
    private class TradeSelectionPopUp extends JPanel implements ActionListener
    {
        private JPanel              mainPanel;
        private ArrayList<JButton>  selectButtons = new ArrayList<>();
        private JLabel              selectText;

        private TradeSelectionPopUp()
        {
            this.setLayout(new BorderLayout());
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new GridLayout(4, 1));
            this.selectText = new JLabel();
            this.selectText.setText("Please select a player to trade with:");
            this.selectText.setFont(new Font("Futura", Font.PLAIN, 14));
            this.mainPanel.add(this.selectText);
            this.add(mainPanel);
        }

        private void initTradeSelectionPopUp()
        {
            for (JButton b : this.selectButtons)
                this.mainPanel.remove(b);
            this.selectButtons = new ArrayList<>();
            for (int i = 0; i < Monopoly.getPlayers().size(); i++)
            {
                if (!(Monopoly.getPlayers().get(i).equals(game.getActivePlayer())))
                    this.selectButtons.add(new JButton(Monopoly.getPlayers().get(i).toString()));
            }
            for (JButton b : this.selectButtons)
            {
                b.addActionListener(this);
                this.mainPanel.add(b);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            for (JButton b : this.selectButtons)
            {
                if (e.getSource() == b)
                {
                    for (Player p : Monopoly.getPlayers())
                        if (p.toString().equals(b.getText()))
                        {
                            game.setTradee(p);
                            break ;
                        }
                }
            }
            popUpTrade.initTradePopUp();
            JOptionPane popTrade = new JOptionPane(popUpTrade, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            tradeDialog = new JDialog();
            tradeDialog.setTitle("Trade");
            tradeDialog.setModal(true);
            tradeDialog.setContentPane(popTrade);
            tradeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            tradeDialog.pack();
            tradeDialog.setVisible(true);
        }
    }

    private class AuctionPopUp extends JPanel implements ActionListener, ChangeListener
    {
        private JPanel              biddersPanel;
        private ArrayList<JLabel>   biddersLabels = new ArrayList<>();
        private JPanel              sliderPanel;
        private JLabel              sliderValue;
        private JPanel              auctionButtons;
        private JButton             confirm;
        private JButton             giveUp;
        private JButton             cool;
        private int                 coordinate;
        private JSlider             slider;

        private AuctionPopUp()
        {
            this.setLayout(new BorderLayout());
            this.setSize(new Dimension(600, 600));
            dialog.setLayout(new BorderLayout());
            dialog.setSize(new Dimension(600, 600));
            this.biddersPanel = new JPanel();
            this.sliderValue = new JLabel();
            this.biddersPanel.setLayout(new GridLayout(2, 2));

            this.sliderPanel = new JPanel();
            this.sliderPanel.setLayout(new GridLayout(2, 1));

            this.auctionButtons = new JPanel();
            this.auctionButtons.setLayout(new GridLayout(1, 3));

            this.slider = new JSlider();
            this.slider.addChangeListener(this);

            this.confirm = new JButton("Confirm");
            this.giveUp = new JButton("Give Up");
            this.cool = new JButton("Cool");
            this.confirm.addActionListener(this);
            this.giveUp.addActionListener(this);
            this.cool.addActionListener(this);
        }

        private void refreshPanel()
        {
            // setBidders
            // don't forget to set to NULL after it's done
            if (game.getBidders() == null)
                game.setBidders(this.coordinate);
            // delete old labels
            
            for (JLabel l : biddersLabels)
                biddersPanel.remove(l);
            biddersPanel.revalidate();
            biddersPanel.repaint();
            // add new labels
            biddersLabels = new ArrayList<>();
            for (int i = 0; i < game.getBidders().size(); i++)
            {
                biddersLabels.add(new JLabel());
                
                biddersLabels.get(i).setText(game.getBidders().get(i).toString());
                if (game.getBidders().get(i).equals(game.getActiveBidder()))
                    biddersLabels.get(i).setFont(new Font("Futura", Font.BOLD, 14));
                else
                    biddersLabels.get(i).setFont(new Font("Futura", Font.PLAIN, 14));
            }
            for (JLabel l : biddersLabels)
                biddersPanel.add(l);

            // set slider
            // don't forget to reset choice to 1 after the auction is over
            slider.setMinimum(game.getChoice() + 1);
            slider.setMaximum(game.getActiveBidder().getMoney());
            // slider.addChangeListener(this);

            // sliderPanel.add(slider);
            // sliderValue.setText("Current bid: $" + slider.getValue());
            // sliderPanel.add(sliderValue);
        }

        private void    emptyPanel()
        {
            this.auctionButtons.remove(confirm);
            this.auctionButtons.remove(giveUp);
            this.auctionButtons.add(cool);
            this.remove(sliderPanel);
            this.auctionButtons.revalidate();
            this.auctionButtons.repaint();
        }

        private void    initAuctionPopUp(int coordinate)
        {
            this.add(this.biddersPanel, BorderLayout.NORTH);
            this.add(this.sliderPanel, BorderLayout.CENTER);
            this.add(this.auctionButtons, BorderLayout.SOUTH);
            this.sliderPanel.add(this.slider);
            this.sliderPanel.add(this.sliderValue);
            this.auctionButtons.add(this.confirm);
            this.auctionButtons.add(this.giveUp);

            this.coordinate = coordinate;
            refreshPanel();
        }

        @Override
        public void stateChanged(ChangeEvent e)
        {
            this.sliderValue.setText("Current bid: $" + slider.getValue());
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (this.coordinate > Board.getSquares().length)
                return ;

            if (e.getSource() == confirm)
            {
                game.setChoice(slider.getValue());
                game.changeBidder();
                refreshPanel();
                if (game.getActiveBidder().getMoney() <= game.getChoice())
                {
                    game.removeActiveBidder();
                    refreshPanel();
                }
            }
            else if (e.getSource() == giveUp)
            {
                game.removeActiveBidder();
                refreshPanel();
            }
            else if (e.getSource() == cool)
            {
                dialog.setVisible(false);
                game.getActiveBidder().getBelongings().add(((Buyable) Board.getSquares()[this.coordinate]));
                ((Buyable) Board.getSquares()[this.coordinate]).setOwner(game.getActiveBidder());
                System.out.println("ending with CHOICE equal to " + game.getChoice());
                if (game.getChoice() != 0)
                    game.getActiveBidder().receiveMoney(-game.getChoice());
                else
                    game.getActiveBidder().receiveMoney(-1);
                game.setChoice(0);
                game.nullifyBidders();
                this.auctionButtons.remove(cool);
            }
            // System.out.println("");
            if (game.getBidders() != null)
            {
                if (game.getBidders().size() == 1)
                {
                    emptyPanel();
                }
            }
        }
    }

    private class CustomPopUp extends JPanel implements ActionListener
    {
        private TitleDeed   titleDeedPopUp;
        private JPanel      popUpButtons;
        private GeneralButton     build;
        private GeneralButton     destroy;
        private GeneralButton     mortgage;
        private GeneralButton     liftMortgage;
        private int               coordinate;
        
        private CustomPopUp()
        {
            this.setLayout(new BorderLayout());
            //this.setSize(new Dimension(350, 450));
            this.setPreferredSize(new Dimension(180, 300));
            
            this.titleDeedPopUp = new TitleDeed();
            //this.titleDeedPopUp.setSize(new Dimension(300, 400));
        
            //button panel
            this.popUpButtons = new JPanel();
            this.popUpButtons.setLayout(new GridLayout(2, 2, 5, 5));
    
            this.build = new GeneralButton("Erect House");
            this.destroy = new GeneralButton("Destroy House");
            this.mortgage = new GeneralButton("Mortgage");
            this.liftMortgage = new GeneralButton("Lift Mortgage");
            build.addActionListener(this);
            destroy.addActionListener(this);
            mortgage.addActionListener(this);
            liftMortgage.addActionListener(this);

    
            this.popUpButtons.add(build);
            this.popUpButtons.add(destroy);
            this.popUpButtons.add(mortgage);
            this.popUpButtons.add(liftMortgage);

            this.add(titleDeedPopUp, BorderLayout.CENTER);
            this.add(popUpButtons, BorderLayout.SOUTH);
        }

        private void updatePopUp(int coordinate)
        {
            if (!(Board.getSquares()[coordinate] instanceof Buyable))
                return ;
            this.coordinate = coordinate;
            this.titleDeedPopUp.setEverything(coordinate);
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
            
            
            titleDeed.setEverything(this.coordinate);
            updateButtons(this.coordinate);
            updatePopUp(this.coordinate);
            setUpInfoTop();
            
            if (game.getActivePlayer().getMoney() > 0){
                updateBottomButtons();
            }
            //     done.setEnabled(true);
        }
    }

    private class CutomLabel extends JLabel{
        private CutomLabel(){
            this.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            this.setFont(new Font("Futura", Font.CENTER_BASELINE, 13));
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
        private int     coordinate;
        //private JLabel mortgageValue;

        private TitleDeed(){
            super();
            this.titleOfDeed = new CutomLabel();
            this.priceOfDeed = new CutomLabel();
            this.ownerNameOfDeed = new CutomLabel();
            this.rentOfDeed = new CutomLabel();
            this.housePriceOfDeed = new CutomLabel();
            this.isMortgagedOfDeed = new CutomLabel();
            this.housesOfDeed = new CutomLabel();

            //this.setLayout(new GridLayout(10, 1));
           this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

           this.add(Box.createRigidArea(new Dimension(20, 0)));
           //this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
           //this.setHori
            //this.setPreferredSize(new Dimension(150, 200));
            //this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(Box.createVerticalGlue());
            this.add(titleOfDeed);
            this.add(priceOfDeed);
            this.add(isMortgagedOfDeed);
            this.add(ownerNameOfDeed);
            this.add(rentOfDeed);
            this.add(housePriceOfDeed);
            this.add(housesOfDeed);
            this.setBorder(BorderFactory.createLineBorder(new Color(82, 82, 82, 70), 3));
            this.add(Box.createVerticalGlue());
            // this.setBackground(Color.GRAY);
            // this.setOpaque(true);
        }
        private TitleDeed(int i)
        {
            this();
            this.coordinate = i;
        }
        // private TitleDeed(){
        //     super();
        // }

        private void setEverything(int i)
        {
            this.coordinate = i;
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
            this.repaint();

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

        public void paintComponent(Graphics g){
            
            super.paintComponent(g);
            if (coordinate < buttons.length){
                if(buttons[coordinate].color != null)
                    g.setColor(buttons[coordinate].color);
                else
                    g.setColor(new Color(214, 219, 223 ));
                g.fillRect(0, 0, this.getWidth(), this.getHeight()/5);
            }
            
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
            //this.setSize(new Dimension(20, 10));
            this.setFocusable(false);
            this.addActionListener(this);
            this.setBackground(Color.WHITE);
            //this.setCoordinate(index);
        }

        private void setCoordinate(int coordinate){
            this.coordinate = coordinate;
            //this.setSize(20, 100);
            //this.setText(Integer.toString(coordinate));
            this.setText(" ");      //until I find a way to give it a proper size
            // this.setFocusable(false);
            
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
                    JOptionPane.showMessageDialog(null, popUpWindow, "Title Deed", JOptionPane.PLAIN_MESSAGE);
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
        private GeneralButton yes = new GeneralButton();
        private GeneralButton no = new GeneralButton();

        //buttons for the cards
        private GeneralButton ok = new GeneralButton();

        //buttons for the Tax
        private GeneralButton money = new GeneralButton();
        private GeneralButton percent = new GeneralButton();

        //buttons for Jail
        private GeneralButton pay;
        private GeneralButton useTheCard;


        private Commands()
        {
            //BoxLayout theLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
           // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            // this.setLayout(new GridLayout(2, 1));
            // this.setPreferredSize(new Dimension(250, 250));
            
            //this.add(Box.createVerticalGlue()); 
            containerForLabel = new JPanel();
            containerForLabel.setLayout(new GridLayout());

            this.message = new JTextArea();
            this.message.setOpaque(false);
            this.message.setWrapStyleWord(true);
            this.message.setLineWrap(true);
            this.message.setEditable(false);
            this.message.setFocusable(false);
            //this.message.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
            this.message.setFont(new Font("Futura", Font.CENTER_BASELINE, 13));

            this.button = new JPanel();
            this.button.setLayout(new FlowLayout());
            
            this.yes = new GeneralButton("Yes");
            this.no = new GeneralButton("No");
            this.ok = new GeneralButton("Ok");
            this.money = new GeneralButton("200$");
            this.percent = new GeneralButton("10% of your wealth");
            this.pay = new GeneralButton("Pay 50$");
            this.useTheCard = new GeneralButton("Use the card");

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
            

            this.add(Box.createVerticalGlue());
            this.add(containerForLabel);
            this.add(button);
            this.add(Box.createVerticalGlue());
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
                this.no.setVisible(true);
                if (game.getActivePlayer().getMoney() < ((Buyable)Board.getSquares()[game.getActivePlayerCoordinate()]).getPrice())
                    this.yes.setEnabled(false);
                else   
                    this.yes.setEnabled(true);
                
            }
            else{
                
            }
            setTheState();
            
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
                ok.setVisible(true);
                done.setEnabled(false);         //------------------------
            }
            else
                game.play();
            setTheState();
        }

        private void setJail(){
            if (!(Board.getSquares()[game.getActivePlayerCoordinate()].getClass().getName().equals("Jail"))){
                System.out.println("Checked Jail");
                return;
            }
            //ATTENTION : Just Trying
            game.setMessage();//game.play();
            setTheState();
            this.message.setVisible(true);
            this.message.setText(Board.getSquares()[game.getActivePlayerCoordinate()].getMessage());
            if (((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).allowCard())
                this.useTheCard.setVisible(true);
            //if (((Jail)Board.getSquares()[game.getActivePlayerCoordinate()]).allowPay())
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
                this.setVisible(false);
                popUpAuction.initAuctionPopUp(game.getActivePlayerCoordinate());
                JOptionPane popAuction = new JOptionPane(popUpAuction, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                dialog = new JDialog();
                dialog.setTitle("Auction");
                dialog.setModal(true);
                dialog.setContentPane(popAuction);
                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                dialog.pack();
                dialog.setVisible(true);
            }
            else if (e.getSource() == ok){
                int previousCoordinate = game.getActivePlayerCoordinate();
                // remove sprite
                System.out.println("REMOVING THE SPRITE AT " + previousCoordinate + " OF THE PLAYER UNDER INDEX OF " + game.getActivePlayerIndex());
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
                System.out.println("PUTTING A NEW SPRITE AT " + game.getActivePlayerCoordinate() + " OF THE PLAYER UNDER INDEX OF " + game.getActivePlayerIndex());
                buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                buttons[previousCoordinate].revalidate();
                buttons[game.getActivePlayerCoordinate()].repaint();
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
                
                //throwDice.setEnabled(true);
                //Board.getSquares()[game.getActivePlayerCoordinate()].doAction(game.activePlayer);
                //game.play();      //JUST CHANGED
                if (!game.getActivePlayerState())
                    message.setText("You lost :(");
                else
                    message.setText(Board.getSquares()[game.getActivePlayerCoordinate()].getMessage());
            }
            
            if (e.getSource() != pay && e.getSource() != useTheCard)
                updateBottomButtons();


            titleDeed.setEverything(game.getActivePlayerCoordinate());
            setUpInfoTop();
            setTheState();
        }
    }

    private void updateBottomButtons(){
        if (!commands.yes.isVisible() && !commands.no.isVisible() && !commands.ok.isVisible() &&
            !commands.pay.isVisible() && !commands.percent.isVisible() && !commands.useTheCard.isVisible() && !commands.money.isVisible())
        {
            if (game.ifPlayerHoldsDoubles() && !game.getActivePlayer().isPrisoned())
            {      //moved to After doAction is complete
                //System.out.println("Holds doubles");
                System.out.println("first");
                throwDice.setEnabled(true);
                done.setEnabled(false);
                trade.setEnabled(false);
            }
            else
            {
                System.out.println("Second");
                throwDice.setEnabled(false);
                done.setEnabled(true);
                trade.setEnabled(true);
            }
        }
        else
        {
            System.out.println("Last");
            throwDice.setEnabled(false);
            done.setEnabled(false);
            trade.setEnabled(false);
        } 
    }

    private void setTheState()
    {
        if (!game.getActivePlayerState()){
            // System.out.println("Player removed");
            // game.removePlayer();
            // for (Player player : Monopoly.getPlayers())
            //     System.out.println(player.toString());
            //optionPane telling the player lost
            ifLostText.setText("You lost. Please proceed and click done to get out of the game.");
            JOptionPane.showMessageDialog(null, ifLost, "Your state", JOptionPane.PLAIN_MESSAGE); 
            done.setEnabled(true);
            throwDice.setEnabled(false);
        }
        else if (game.getActivePlayer().getMoney() < 0 && game.getActivePlayerState()){
            ifLostText.setText("Please mortgage any of your properties or destroy the houses to pay your debts");
            JOptionPane.showMessageDialog(null, ifLost, "Your state", JOptionPane.PLAIN_MESSAGE); 
            if (game.getActivePlayerState() && game.getActivePlayer().getMoney() < 0){
                throwDice.setEnabled(false);
                throwDice.setEnabled(false);
            }
        }
        else{
            //this.message.setText("You can not give the turn to the next player");
            //You can not give the turn to the next player
            //done.setEnabled(true);
        }
    }
    


    // public void updateBottomButtons(){
        
    // }


    public MainWindow(ArrayList<Player> players){
        super("Monopoly");                                                       // ------------------
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1300, 750));
        this.setLayout(new BorderLayout());
        this.setResizable(false); 
        this.setLocationRelativeTo(null);  
        
        ImageIcon icon = new ImageIcon("./images/icon.png");
        this.setIconImage(icon.getImage());//-----------------
        // this.getContentPane().setBackground(new Color(64, 184, 182));
        //setting up
        //this.board = new JPanel();
        this.info = new JPanel();
        this.infoTop = new JPanel();
        this.belongingsPanel = new JPanel();
        this.infoCenter = new JPanel();
        this.infoBottom = new JPanel();
        this.popUpWindow = new CustomPopUp();
        this.dialog = new JDialog();
        game = new Monopoly(players);
        this.setTheFlow();
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

            
            ImageIcon image = new ImageIcon("./images/image" + i + ".png");
            buttons[i].setIcon(image);
            //buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            // if (Board.getSquares()[i].getClass().getName().equals("Property")){
            //     JTextArea text =  new JTextArea(5,10);                      //Exception
            //     text.setOpaque(false);
            //     text.setWrapStyleWord(true);
            //     text.setLineWrap(true);
            //     text.setEditable(false);
            //     text.setFocusable(false);
            //     text.setText(Board.getSquares()[i].getTitle());
            // //  this.message.setFont(new Font("Futura", Font.CENTER_BASELINE, 14));
                
            //     buttons[i].add(text);   
            // }    
        }

        // ImageIcon image = new ImageIcon("./images/chance.png");
        // buttons[2].setIcon(image);
        // buttons[2].setHorizontalTextPosition(SwingConstants.CENTER);

        // ImageIcon image2 = new ImageIcon("./images/image1.png");
        // buttons[1].setIcon(image2);
        // buttons[1].setHorizontalTextPosition(SwingConstants.CENTER);

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
        popUpAuction = new AuctionPopUp();
        popUpTradeSelection = new TradeSelectionPopUp();
        popUpTrade = new TradePopUp();
        // initializing player sprites
        sprites.add(new JLabel("", new ImageIcon(new ImageIcon("./images/p1.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
        sprites.add(new JLabel("", new ImageIcon(new ImageIcon("./images/p2.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
        if (Monopoly.getPlayers().size() >= 3)
            sprites.add(new JLabel("", new ImageIcon(new ImageIcon("./images/p3.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
        if (Monopoly.getPlayers().size() >= 4)
            sprites.add(new JLabel("", new ImageIcon(new ImageIcon("./images/p4.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)), JLabel.CENTER));
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
        

        //center.setBackground(new Color(254, 245, 231));        //----------------------------
        //center.setOpaque(true);

        JLabel image = new JLabel();
        ImageIcon bg = new ImageIcon("./images/bg2.png");
        
        image.setIcon(bg);
        center.add(image);                                      //---------------------------
        //ifLost popUp
        ifLost = new JPanel();
        ifLostText = new JLabel();
        ifLost.add(ifLostText);
        


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
        titleDeed.setPreferredSize(new Dimension(150, 150));

        //the components of commands
        this.commands = new Commands();
        
        info.setLayout(new BorderLayout(20, 50));
        info.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        infoTop.setSize(new Dimension(300, 700));
        //infoTop.setLayout(new GridLayout(2, 1, 0, 20));                           SOS changed
        infoTop.setLayout(new BorderLayout(20, 20)); 

        
 
        //this.setButtonsColor(belongingButtons);
        
       
        infoCenter.setSize(new Dimension(300, 600));
        infoCenter.setLayout(new GridLayout(1, 2, 70, 20));           //TRYING      GridBag
        info.setBorder(new EmptyBorder(30, 30, 30, 30));

 
        infoCenter.add(titleDeed); //, BorderLayout.WEST
        infoCenter.add(commands);  //, BorderLayout.NORTH
        

        infoBottom.setSize(new Dimension(300, 600));


        //infoTop
        this.playersInfo = new JPanel();
        this.playersInfo.setLayout(new GridLayout(game.getNumberOfPlayers(), 1));      //in order to keep things compact
        setUpBelongings();
        this.diceInfo = new JPanel();
        this.diceInfo.setLayout(new GridLayout(1, 1));
        this.diceLabel = new JLabel();
        this.diceLabel.setFont(new Font("Futura", Font.PLAIN, 14));
        this.diceInfo.add(this.diceLabel);
        //containerOfBelongings.add(belongingsPanel);
        infoTop.add(playersInfo, BorderLayout.NORTH);
        infoTop.add(diceInfo, BorderLayout.CENTER);
        infoTop.add(belongingsPanel, BorderLayout.SOUTH);


        // infoTop.setBackground(Color.yellow);        //SOS just added
        // infoTop.setOpaque(true);
        setUpInfoTop();
        setUpInfoBottom();
        info.add(infoTop, BorderLayout.NORTH);
        info.add(infoCenter, BorderLayout.CENTER);
        info.add(infoBottom, BorderLayout.SOUTH);
    }

    private void setUpBelongings()
    {
        belongingButtons = new BelongingButton[28];
        belongingsPanel.setLayout(new GridLayout(4, 7, 25, 15));        //3, 10

        belongingsPanel.setSize(new Dimension(400, 400));
        
        
        for (int i = 0; i < belongingButtons.length; i++)
                belongingButtons[i] = new BelongingButton();

        this.setCoordinatesOfBelongings(belongingButtons);
    
        for (int i = 0; i < belongingButtons.length; i++)
        {
            //belongingButtons[i].setSize(new Dimension(10, 10));
            belongingButtons[i].setColor(buttons[belongingButtons[i].getCoordinate()].getColor());
            belongingsPanel.add(belongingButtons[i]);
        }
    }

    private void setUpInfoTop()
    {
        // delete old labels
        for (JLabel l : playersLabels)
            this.playersInfo.remove(l);
        this.playersInfo.revalidate();
        this.playersInfo.repaint();
        // add new labels
        playersLabels = new ArrayList<>();
        for (int i = 0; i < Monopoly.getPlayers().size(); i++)
        {
            playersLabels.add(new JLabel());

            playersLabels.get(i).setText(Monopoly.getPlayers().get(i).toString());
            if (Monopoly.getPlayers().get(i).equals(game.getActivePlayer()))
                playersLabels.get(i).setFont(new Font("Futura", Font.BOLD, 14));
            else
                playersLabels.get(i).setFont(new Font("Futura", Font.PLAIN, 14));
        }
        for (JLabel l : playersLabels)
            this.playersInfo.add(l);
        // update the dice info
        this.diceLabel.setText("Current roll: " + game.getActivePlayer().getFirstDice() + " and " + game.getActivePlayer().getSecondDice());
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
        

        commands.message.setVisible(false);
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
        if (game.getActivePlayer().getMoney() < 0)
            done.setEnabled(false);
        
        updateBottomButtons();    
        setUpInfoTop();
    }

    private void setUpInfoBottom()
    {
        infoBottom.setLayout(new GridLayout());
        this.throwDice = new GeneralButton("Throw the dice");
        this.done = new GeneralButton("Done");
        this.trade = new GeneralButton("Trade");
        done.setEnabled(false);
        throwDice.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {   
                //throwDice.setEnabled(false);
                
                // remove the old sprite
                buttons[game.getActivePlayerCoordinate()].remove(sprites.get(game.getActivePlayerIndex()));
                buttons[game.getActivePlayerCoordinate()].revalidate();
                buttons[game.getActivePlayerCoordinate()].repaint();

                if (game.ifPlayerIsPrisoned()){
                    game.startGame();
                    // if (!game.ifPlayerHoldsDoubles() || game.ifPlayerIsPrisoned()){      //moved to After doAction is complete
                    //     //System.out.println("Holds doubles");
                    //     throwDice.setEnabled(false);
                    //     done.setEnabled(true);
                    // } 


                    commands.setAllButtonsVisible(false);
                    if (!game.ifPlayerIsPrisoned()){
                
                        commands.message.setText("You rolled doubles and are free to go.");
                        setUpInfoCenter();
                        //throwDice.setEnabled(false);
                        //done.setEnabled(true);
                    }
                    else{
                        commands.message.setText("You failed to roll doubles. See you on the next turn!");
                        throwDice.setEnabled(false);

                    }
                    done.setEnabled(true);
                    commands.message.setVisible(true);
                    setUpInfoTop();
                    buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                    buttons[game.getActivePlayerCoordinate()].revalidate();
                    buttons[game.getActivePlayerCoordinate()].repaint();
                    return;
                }
                game.startGame();
                

                //throwDice.setEnabled(false);            //JUST ADDED

                // if (!game.ifPlayerHoldsDoubles()){      //moved to After doAction is complete
                //     //System.out.println("Holds doubles");
                //     throwDice.setEnabled(false);
                //     done.setEnabled(true);
                // }   
                setUpInfoTop(); 
                buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                buttons[game.getActivePlayerCoordinate()].revalidate();
                buttons[game.getActivePlayerCoordinate()].repaint();
                updateBottomButtons();
                if (game.getMoveToJail()){
                    commands.message.setText("You rolled doubles 3 times in a row. GO to Jail");
                    commands.message.setVisible(true);
                    return ;
                }
                // put the sprite at a new space

                //setUpInfoCenter();
                setUpInfoCenter();
                //setUpInfoTop();  
                
            }
            
        });
        trade.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                popUpTradeSelection.initTradeSelectionPopUp();
                JOptionPane.showMessageDialog(null, popUpTradeSelection, "Trade", JOptionPane.PLAIN_MESSAGE);
            }
        });
        done.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!game.getActivePlayerState())
                     game.removePlayer();
                

                commands.setVisible(false);
                // put the sprite at a new space
                //buttons[game.getActivePlayerCoordinate()].add(sprites.get(game.getActivePlayerIndex()));
                
                game.changePlayer();
                if (game.activePlayerWon()){
                    new WinWindow(game.getActivePlayer());
                    dispose();
                }
                done.setEnabled(false);
                titleDeed.setEverything(game.getActivePlayerCoordinate());
                System.out.println(game.getActivePlayer().getName());
                throwDice.setEnabled(true);
                if (game.getActivePlayer().isPrisoned())
                {
                    System.out.println("Entered if");
                    setUpInfoCenter();
                    //System.out.println("He is prisoned");
                }
                setUpInfoTop();

                //commands.setVisible(false);
            }
        });
        infoBottom.add(throwDice);
        infoBottom.add(trade);
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
                break ;
            }
        }
    }
}
