package ui;

import universe.Params;
import universe.Universe;
import universe.entities.Animal;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
/*****************/
public class UniverseCanvas  extends JComponent
{
    private Universe aUni;
    private int aCaseSize;
    private final Point aO;
    /*****************/
    public UniverseCanvas()
    {
        this.aCaseSize = 32;
        this.aO = new Point();
        this.setPreferredSize(new Dimension(500,500));
        this.setFont(new Font("Serif",Font.BOLD,40));
    }
    /*****************/
    public Universe getUniverse(){return this.aUni;}
    public void setUniverse(Universe pUni)
    {
        this.aUni = pUni;
        if(this.aUni != null)
        {
            Params vP = this.aUni.getParams();
            int vCaseWidth = this.getWidth()/vP.clmn;
            int vCaseHeight = this.getHeight()/vP.row;
            this.aCaseSize = Math.min( vCaseWidth,vCaseHeight);
            this.aO.x = (this.getWidth() - this.aCaseSize * vP.clmn)/2;
            this.aO.y = (this.getHeight() - this.aCaseSize * vP.row)/2;
        }
    }
    /*****************/
    @Override public void paintComponent(Graphics g) {
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.aUni != null)
        {
            this.drawGround(g);
            this.drawMinerals(g);
            this.drawAnimals(g);
            if (this.aUni.isDead())
                this.drawDeadRound(g);
        }
    }
    /*****************/
    private void drawDeadRound(Graphics g)
    {
        String vText = "Univers mort au tour "+this.aUni.getRound();
        int stringWidth = g.getFontMetrics().stringWidth(vText);
        g.setColor(Color.WHITE);
        g.drawString(vText, this.getWidth()/2 - stringWidth/2, this.getHeight()/2);
    }
    /*****************/
    private void drawGround(Graphics g)
    {
        Color vBrown = new Color(109, 100, 94);
        Color vGreen = new Color(116, 193, 127);
        for(int vRow = 0; vRow < this.aUni.getParams().row; vRow++)
            for (int vClmn = 0; vClmn < this.aUni.getParams().clmn; vClmn++)
            {
                g.setColor(this.aUni.hasGrass(vClmn,vRow)?vGreen:vBrown);
                g.fillRect(aO.x + vClmn * aCaseSize, aO.y +vRow * aCaseSize , aCaseSize-1, aCaseSize-1);
            }
    }
    /*****************/
    private void drawMinerals(Graphics g)
    {
        g.setColor(Color.YELLOW);
        for(int vRow = 0; vRow < this.aUni.getParams().row; vRow++)
            for (int vClmn = 0; vClmn < this.aUni.getParams().clmn; vClmn++)
                if(this.aUni.hasMinerals(vClmn,vRow))
                    g.drawOval(aO.x + vClmn * aCaseSize, aO.y +vRow * aCaseSize , aCaseSize-1, aCaseSize-1);
    }
    /*****************/
    private void drawAnimals(Graphics g)
    {
        for(int vRow = 0; vRow < this.aUni.getParams().row; vRow++)
            for (int vClmn = 0; vClmn < this.aUni.getParams().clmn; vClmn++)
            {
                Animal vAnimal = this.aUni.getAnimal(vClmn,vRow);
                if(vAnimal != null)
                {
                    g.setColor(vAnimal.getSpecies().equals("universe.entities.Wolf")?Color.BLACK:Color.WHITE);
                    g.fillOval(aO.x + vClmn * aCaseSize, aO.y + vRow * aCaseSize , aCaseSize-1, aCaseSize-1);
                    g.setColor(vAnimal.getGender().equals("Male")?Color.BLUE:Color.RED);
                    g.drawOval(aO.x + vClmn * aCaseSize, aO.y + vRow * aCaseSize , aCaseSize-1, aCaseSize-1);
                }
            }
    }
}
