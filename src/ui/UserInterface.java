package ui;

import universe.Params;
import universe.Universe;

import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.HashMap;
import java.awt.Color;
/*****************/
public class UserInterface extends JFrame implements ActionListener
{
    private final DataHandler aDataHandler;
    private final HashMap<String, JComponent> aConfigMap;
    private final HashMap<String, JComponent> aGameMap;
    private final UniverseCanvas aUniCanvas;
    private boolean aAuto;

    /*****************/
    public UserInterface()
    {
        String[] options = {"Nombre de cases", "Nombre de moutons","Nombre de loups","Deplacement","Condition d'arrêt"};
        this.aDataHandler = new DataHandler(this.createJComboBox( options,"Paramètre variable"));
        this.setFrame();
        this.aAuto = false;
        this.aUniCanvas = new UniverseCanvas();
        this.aConfigMap = new HashMap<>();
        this.setConfigMap();
        this.aGameMap = new HashMap<>();
        this.setGameMap();
        this.addComponents();
    }
    /*****************/
    public boolean auto() { return this.aAuto;}
    public UniverseCanvas getUniCanvas(){return this.aUniCanvas;}
    /*****************/
    private void setFrame()
    {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 1100, 700);
        this.setVisible(true);
    }
    /*****************/
    private void setConfigMap()
    {
        this.aConfigMap.put("Column", this.createJTextField("Nombre de colonnes ", 10));
        this.aConfigMap.put("Row", this.createJTextField("Nombre de lignes ", 10));
        this.aConfigMap.put("universe.entities.Sheep", this.createJTextField("Nombre de moutons", 8));
        this.aConfigMap.put("universe.entities.Wolf", this.createJTextField("Nombre de loups", 2));
        String[] options = {"Repoduction","Nourriture","Aléatoire","Repr+Nour"};
        this.aConfigMap.put("Deplacement", this.createJComboBox(options, "Prioritée de déplacement"));
        String[] options2 = {"Plus de loups","Plus de moutons","Plus de loups ou de moutons","Plus de loups et de moutons"};
        this.aConfigMap.put("Stop", this.createJComboBox(options2, "Condition d'arrêt"));
    }
    /*****************/
    private void setGameMap()
    {
        this.aGameMap.put("Tour suivant", this.createJButton("Tour suivant"));
        this.aGameMap.put("Auto ", this.createJButton("Auto "));
        this.aGameMap.put("Reset", this.createJButton("Reset"));
        this.aGameMap.put("IntervalAuto", this.createJTextField("Interval auto", 100));
    }
    /*****************/
    private void setConfigMode(boolean pConfig)
    {
        for (JComponent c : this.aConfigMap.values())
            c.setEnabled(pConfig);
        for (JComponent c : this.aGameMap.values())
            c.setEnabled(!pConfig);
    }
    /*****************/
    private JTextField createJTextField(String pTitle, int pValue)
    {
        JTextField vTF = new JTextField("" + pValue, 10);
        TitledBorder vTB = BorderFactory.createTitledBorder(pTitle);
        vTF.setBorder(new CompoundBorder(vTB, vTF.getBorder()));
        return vTF;
    }
    /*****************/
    private JButton createJButton(String pText)
    {
        JButton vB = new JButton(pText);
        vB.addActionListener(this);
        return vB;
    }
    /*****************/
    private JComboBox<String> createJComboBox(String[] options,String pTitle)
    {
        JComboBox<String> vJCB = new JComboBox<>(options);
        TitledBorder b = BorderFactory.createTitledBorder(pTitle);
        vJCB.setBorder(new CompoundBorder(b,  vJCB.getBorder()));
        vJCB.setSelectedIndex(0);
        return vJCB;
    }
    /*****************/
    private boolean createUniverse()
    {
        try
        {
            int vClmn = Integer.parseInt(((JTextField) this.aConfigMap.get("Column")).getText());
            int vRow = Integer.parseInt(((JTextField) this.aConfigMap.get("Row")).getText());
            int vSheep = Integer.parseInt(((JTextField) this.aConfigMap.get("universe.entities.Sheep")).getText());
            int vWolf = Integer.parseInt(((JTextField) this.aConfigMap.get("universe.entities.Wolf")).getText());
            int vDep = ((JComboBox<?>)this.aConfigMap.get("Deplacement")).getSelectedIndex();
            int vStop = ((JComboBox<?>)this.aConfigMap.get("Stop")).getSelectedIndex();
            if(vClmn > 250 || vRow > 250 || vClmn*vRow < vSheep + vWolf)
                return false;
            this.aUniCanvas.setUniverse(new Universe(new Params(vClmn, vRow, vSheep, vWolf,vDep,vStop)));
            return true;
        }
        catch (Exception et) {return false;}
    }
    /*****************/
    private void addComponents()
    {
        JPanel vPanel = new JPanel();
        vPanel.setBackground(Color.WHITE);
        for (JComponent c : this.aConfigMap.values())
            vPanel.add(c);
        vPanel.add(this.createJButton("Valider les paramètres"));
        vPanel.add(this.aUniCanvas);
        vPanel.add(this.aDataHandler);
        for (JComponent c : this.aGameMap.values())
        {
            vPanel.add(c);
            c.setEnabled(false);
        }
        vPanel.add(this.aDataHandler.getComboBox());
        this.add(vPanel);
        this.revalidate();
    }
    /*****************/
    public int getInterval()
    {
        try {return Integer.parseInt(((JTextField) this.aGameMap.get("IntervalAuto")).getText());}
        catch (Exception e) {return 1000;}
    }
    /*****************/
    @Override public void actionPerformed(ActionEvent e)
    {
        JButton vB = (JButton) e.getSource();
        String vText = vB.getText();
        switch (vText)
        {
            case "Valider les paramètres" -> handleValidateParams(vB);
            case "Modifier les paramètres" -> handleModifyParams(vB);
            case "Tour suivant" -> nextRound();
            case "Auto " -> handleAuto();
            case "Stop " -> handleStop();
            case "Reset" -> createUniverse();
        }
    }
    /*****************/
    private void handleValidateParams(JButton button)
    {
        if (this.createUniverse())
        {
            this.setConfigMode(false);
            button.setText("Modifier les paramètres");
        }
    }
    /*****************/
    private void handleModifyParams(JButton button)
    {
        ((JButton) this.aGameMap.get("Auto ")).setText("Auto ");
        button.setText("Valider les paramètres");
        this.aUniCanvas.setUniverse(null);
        this.setConfigMode(true);
        this.aAuto = false;
    }
    /*****************/
    public void nextRound()
    {
        Universe vUni = this.aUniCanvas.getUniverse();
        if (vUni != null && !vUni.isDead())
        {
            vUni.nextRound();
            if(vUni.isDead())
                this.addData();
        }
    }
    /*****************/
    private void addData()
    {
        Universe vUni = this.aUniCanvas.getUniverse();
        if(vUni != null)
            this.aDataHandler.addData(vUni.getParams(),vUni.getRound());
    }
    /*****************/
    private void handleAuto()
    {
        this.aAuto = true;
        ((JButton) this.aGameMap.get("Auto ")).setText("Stop ");
        this.aGameMap.get("Tour suivant").setEnabled(false);
        this.aGameMap.get("IntervalAuto").setEnabled(false);
    }
    /*****************/
    private void handleStop()
    {
        this.aAuto = false;
        ((JButton) this.aGameMap.get("Auto ")).setText("Auto ");
        this.aGameMap.get("Tour suivant").setEnabled(true);
        this.aGameMap.get("IntervalAuto").setEnabled(true);
    }
}
