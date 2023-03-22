package universe.entities;

import java.awt.*;
import java.util.ArrayList;

/*****************/
public class Wolf extends Animal
{
    public static int LifeTime = 60;
    public static int ProcreationInterval = 5;
    public static int MeatMaxInterval = 10;
    /*****************/
    public Wolf()
    {
        super();
        this.aTimeBeforeDie = LifeTime;
        this.aTimeBeforeStarving = MeatMaxInterval;
        this.aTimeBeforeProcreate = 0;
    }
    /*****************/
    @Override public Animal giveBirth(){return new Wolf();}
    /*****************/
    @Override public void interact(Animal pAnimal)
    {
        if(pAnimal.getSpecies().equals("universe.entities.Sheep"))
        {
            pAnimal.addProperty("dead");
            this.aTimeBeforeStarving =  MeatMaxInterval;
        }
        else if(this.canReproduceWith(pAnimal))
        {
            this.aTimeBeforeProcreate = ProcreationInterval;
            pAnimal.setTimeBeforeProcreate(ProcreationInterval);
            pAnimal.addProperty("pregnant");
        }
    }
    /*****************/
    @Override public boolean grassInteract(final boolean pGrass){return pGrass;}
    /*****************/
    @Override
    public Point feedDep(ArrayList<Point> pC, Animal[][] pA, boolean[][] pG) {
        return findBestPoint(pC, pA, pG, vA -> vA.getSpecies().equals("universe.entities.Sheep"));
    }
}