package universe.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*****************/
public class Sheep extends Animal
{
    public static int LifeTime = 50;
    public static int ProcreationInterval = 5;
    public static int MeatMaxInterval = 6;

    /*****************/
    public Sheep()
    {
        super();
        this.aTimeBeforeDie = LifeTime;
        this.aTimeBeforeStarving = MeatMaxInterval;
        this.aTimeBeforeProcreate = 0;
    }
    /*****************/
    @Override public Animal giveBirth(){return new Sheep();}
    /*****************/
    @Override public void interact(Animal pAnimal)
    {
        if(this.canReproduceWith(pAnimal))
        {
            this.aTimeBeforeProcreate = ProcreationInterval;
            pAnimal.setTimeBeforeProcreate(ProcreationInterval);
            pAnimal.addProperty("pregnant");
        }
    }
    /*****************/
    @Override public boolean grassInteract(final boolean pGrass)
    {
        if(pGrass)
            this.aTimeBeforeStarving = MeatMaxInterval;
        return false;
    }
    /*****************/
    @Override public Point feedDep(ArrayList<Point> pC, Animal[][] pA,boolean[][] pG)
    {
        Collections.shuffle(pC);
        for(Point p:pC)
            if(pG[p.x][p.y])
                return p;
        return pC.get((new Random()).nextInt(pC.size()));
    }
}
