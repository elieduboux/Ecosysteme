package ui;

import universe.Params;

import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import java.awt.FontMetrics;
import java.util.Comparator;
import java.util.ArrayList;
import java.awt.Dimension;
import java.util.HashMap;
import java.awt.Graphics;
import java.util.List;
import java.awt.Color;
/*****************/
public class DataHandler extends JComponent
{
    private final HashMap<Params, ArrayList<Integer>> aDatas;
    private final  JComboBox<String> aComboBox;
    private Params aP;
    /*****************/
    public DataHandler(JComboBox<String> pJCB)
    {
        this.aDatas = new HashMap<>();
        this.aComboBox = pJCB;
        this.aComboBox.addActionListener(e -> this.repaint());
        this.setPreferredSize(new Dimension(500,500));
    }
    /*****************/
    public JComboBox<String> getComboBox(){return this.aComboBox;}
    /*****************/
    public void addData(Params pP, int pRound)
    {
        this.aP = pP;
        for (Params param : aDatas.keySet())
        {
            if(param.equals(pP))
            {
                this.aDatas.get(param).add(pRound);
                this.repaint();
                return;
            }
        }
        this.aDatas.put(pP,new ArrayList<Integer>());
        this.aDatas.get(pP).add(pRound);
        this.repaint();
    }
    /*****************/
    private int getMaxRound()
    {
        int maxValue = 0;
        for(Params p : aDatas.keySet())
        {
            int sum = 0;
            for (Integer value : aDatas.get(p))
                sum += value;
            int avg = sum / aDatas.get(p).size();
            maxValue = Math.max(maxValue, avg);
        }
        return maxValue;
    }
    /*****************/
    private Dimension getBounds(String text, Graphics g)
    {
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D b= metrics.getStringBounds(text, g);
        return new Dimension((int) b.getWidth(), (int)b.getHeight());
    }
    /*****************/
    @Override public void paintComponent(Graphics g)
    {
        int x = 50;
        int y = 50;
        int width = this.getWidth() - 2 * x;
        int height = this.getHeight() - 2 * y;
        this.drawBackGraph(g,x,y,width,height);
        this.drawTitle(g,x,y,width,height);
        this.drawFixParams(g,x,y,width,height);
        this.drawPoints(g,x,y,width,height);
    }
    /*****************/
    private void drawBackGraph(Graphics g, int x, int y, int width, int height)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.white);
        g.drawLine(x,y,x,y + height);
        g.drawLine(x,y+height,x+width,y+height);
        g.drawLine(x,y,x+5,y+10);
        g.drawLine(x,y,x-5,y+10);
        g.drawLine(x+width,y+height,x+width-10,y+height+5);
        g.drawLine(x+width,y+height,x+width-10,y+height-5);
    }
    /*****************/
    private void drawTitle(Graphics g, int x, int y, int width, int height)
    {
        String text = "Nombre de tours";
        Dimension b = this.getBounds(text,g);
        g.drawString(text,x- b.width/2,y- b.height);
        String var = (String)this.aComboBox.getSelectedItem();
        assert var != null;
        g.drawString(var,this.getWidth() - this.getBounds(var,g).width - 10,y+height - 10);
    }
    /*****************/
    private void drawFixParams(Graphics g, int x, int y, int width, int height)
    {
        if(this.aP ==null){return;}
        String fix1 = aP.clmn*aP.row+ " cases";
        String fix2 =  aP.wolf+ " loups";
        String fix3 = "Deplacement par "+this.getDeplacement(aP.dep);
        String fix4 = "Arrêt si "+this.getStop(aP.stop);
        switch (this.aComboBox.getSelectedIndex())
        {
            case 0:fix1 = aP.sheep+ " moutons";break;
            case 2:fix2 = aP.sheep+ " moutons";break;
            case 3:fix3 = aP.sheep+ " moutons";break;
        }
        g.drawString(fix1 + "|",10,this.getHeight() - 5);
        g.drawString(fix2+ "|",20  + this.getBounds(fix1,g).width,this.getHeight() - 5 );
        g.drawString(fix3+ "|",30 + this.getBounds(fix1,g).width+ this.getBounds(fix2,g).width,this.getHeight() - 5 );
        g.drawString(fix4,40 + this.getBounds(fix1,g).width+ this.getBounds(fix2,g).width+ this.getBounds(fix3,g).width,this.getHeight() - 5 );
    }

    /*****************/
    private void drawPoints(Graphics g, int x, int y, int width, int height)
    {
        g.setColor(Color.red);
        List<Params> sortedKeys = this.getParams();

        int maxValue = this.getMaxRound();
        for(int i=0; i<sortedKeys.size(); i++)
        {
            Params p = sortedKeys.get(i);
            int avgRound = this.getAverageRound(p);
            int vPointX = i*width/sortedKeys.size()+x+10;
            int vPointY = y + height - height * avgRound / maxValue+10;
            g.fillOval(vPointX,vPointY,5,5);
            String text = this.getAbs(p);
            Dimension b = this. getBounds(text,g);
            g.drawLine(vPointX,y+height,vPointX,y+height+5);
            g.drawString(text,vPointX - b.width/2,y+height+ b.height);
            g.drawString(""+avgRound,vPointX-getBounds(""+avgRound,g).width/2+3,vPointY-7);
        }
    }
    /*****************/
    private String getAbs(Params p)
    {
        switch(this.aComboBox.getSelectedIndex())
        {
            case 0:return ""+(p.clmn * p.row);
            case 1:return ""+(p.sheep);
            case 2:return ""+(p.wolf);
            case 3:return this.getDeplacement(p.dep);
            case 4:return this.getStop(p.stop);
            default:return "...";
        }
    }
    /*****************/
    private int getAverageRound(Params p)
    {
        int sum = 0;
        for (Integer value : aDatas.get(p))
            sum += value;
        return sum / aDatas.get(p).size();
    }
    /*****************/
    private List<Params> getParams()
    {
            List<Params> sortedKeys = new ArrayList<>();
            int selectedIndex = this.aComboBox.getSelectedIndex();
            for (Params p : this.aDatas.keySet())
            {
                if(this.aP.isComparableWith(p,selectedIndex))
                    sortedKeys.add(p);
            }
            sortedKeys.sort(Comparator.comparingInt(p -> (selectedIndex == 0) ?
                    (p.clmn * p.row) : (selectedIndex == 1) ? (p.sheep) : (p.wolf)));
            return sortedKeys;
    }
    /*****************/
    private String getDeplacement(int dep)
    {
        switch(dep)
        {
            case 0:return "Reproduction";
            case 1: return "Nourriture";
            case 3: return "Repro + Nourriture";
            default:return "Aleatoire";
        }
    }
    /*****************/
    private String getStop(int stop)
    {
        switch(stop)
        {
            case 0:return "0 loups";
            case 1: return "0 moutons";
            case 2: return "0 loups ou moutons";
            default:return "0 loups et moutons";
        }
    }
}
