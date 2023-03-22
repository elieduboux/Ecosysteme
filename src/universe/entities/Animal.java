package universe.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

/*****************/
public abstract class Animal
{
    protected String aSpecies;
    protected String aGender;

    protected int aTimeBeforeProcreate;
    protected int aTimeBeforeStarving;
    protected int aTimeBeforeDie;

    protected ArrayList<String> aProperties;
    /*****************/
    public Animal()
    {
        this.aSpecies = this.getClass().getName();
        this.aGender = (new Random().nextBoolean() ? "Male" : "Female");
        this.aProperties = new ArrayList<String>();
    }
    /*****************/
    public void update()
    {
        this.aTimeBeforeDie--;
        this.aTimeBeforeStarving--;
        this.aTimeBeforeProcreate--;
        if(this.aTimeBeforeStarving == 0 || this.aTimeBeforeDie == 0)
            this.addProperty("naturaldead");
    }
    /*****************/
    public boolean canReproduceWith(final Animal pAnimal)
    {
        return (this.aGender.equals("Male") && pAnimal.getGender().equals("Female")
            && this.aSpecies.equals(pAnimal.getSpecies())
            && this.getTBProcreate() <= 0 && pAnimal.getTBProcreate() <= 0);
    }
    /*****************/
    public void setTimeBeforeProcreate(final int pTime){this.aTimeBeforeProcreate = pTime;}
    /*****************/
    public String getGender(){return this.aGender;}
    public String getSpecies(){return this.aSpecies;}
    public int getTBProcreate(){return this.aTimeBeforeProcreate;}
    /*****************/
    public boolean hasProperty(final String pProperty) {return this.aProperties.contains(pProperty);}
    public void removeProperty(final String pProperty){this.aProperties.remove(pProperty);}
    public void addProperty(final String pProperty) {this.aProperties.add(pProperty);}
    /*****************/
    public abstract Animal giveBirth();
    public abstract void interact(Animal pAnimal);
    public abstract boolean grassInteract(final boolean pGrass);
    /*****************/
    public Point getBestCell(int dep, ArrayList<Point> pC, Animal[][] pA,boolean[][] pG)
    {
        switch(dep)
        {
            case 0:
            case 3:Point vP = this.reproDep(pC, pA, pG);
                    if(vP!=null)return vP;
                    else if(dep==3)
                        return this.feedDep(pC, pA, pG);break;
            case 1:return this.feedDep(pC, pA, pG);
        }
        return pC.get((new Random()).nextInt(pC.size()));
    }
    /*****************/
    public int getDistance(Point vP1, Point vP2)
    {
        int xDiff = vP1.x - vP2.x;
        int yDiff = vP1.y - vP2.y;
        return (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    /*****************/
    public  abstract Point feedDep(ArrayList<Point> pC, Animal[][] pA,boolean[][] pG);
    /*****************/
    private Point reproDep(ArrayList<Point> pC, Animal[][] pA, boolean[][] pG) {
        if (this.aTimeBeforeProcreate <= 0)
            return findBestPoint(pC, pA, pG, vA -> (this.canReproduceWith(vA) || vA.canReproduceWith(this)));
        return null;
    }
    /*****************/
    public Point findBestPoint(ArrayList<Point> pC, Animal[][] pA, boolean[][] pG, Predicate<Animal> pCheck)
    {
        int vShortestDistance = -1;
        Point vShortestCompatibleAnimal = null;
        for (int vRow = 0; vRow < pA[0].length; vRow++)
            for (int vClmn = 0; vClmn < pA.length; vClmn++)
            {
                Animal vA = pA[vClmn][vRow];
                if (vA == null)
                    continue;
                Point vP = new Point(vClmn, vRow);
                int d = this.getDistance(pC.get(0), vP);
                if (pCheck.test(vA) && (vShortestDistance == -1 || d < vShortestDistance)) {
                    vShortestDistance = d;
                    vShortestCompatibleAnimal = vP;
                }
            }
        if (vShortestDistance != -1) {
            Point vBest = pC.get(0);
            vShortestDistance = -1;
            for (Point point : pC) {
                int distance = this.getDistance(point, vShortestCompatibleAnimal);
                if (vShortestDistance == -1 || distance < vShortestDistance) {
                    vShortestDistance = distance;
                    vBest = point;
                }
            }
            return vBest;
        }
        return pC.get((new Random()).nextInt(pC.size()));
    }



}
