package universe;

import universe.entities.Animal;
import universe.entities.Sheep;
import universe.entities.Wolf;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;
/*****************/
public class Universe
{
    private int aRound;
    private boolean aIsDead;
    private final Params aP;
    private Animal[][] aAnimals;
    final private boolean[][] aMinerals;
    final private boolean[][] aGrass;
    /*****************/
    public Universe(Params pP)
    {
        this.aP = pP;
        this.aRound = 0;
        this.aIsDead = false;
        this.aGrass = new boolean[aP.clmn][aP.row];
        this.aMinerals = new boolean[aP.clmn][aP.row];
        this.aAnimals = new Animal[aP.clmn][aP.row];
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
            {
                this.aGrass[vClmn][vRow] = true;
                this.aMinerals[vClmn][vRow] = false;
            }
        for(int s = 0; s < pP.sheep; s++)
            this.addAnimal(new Sheep());
        for(int w = 0; w < pP.wolf; w++)
            this.addAnimal(new Wolf());
    }
    /*****************/
    public boolean isDead(){return this.aIsDead;}
    public int getRound(){return this.aRound;}
    public Params getParams(){return this.aP;}
    public boolean hasGrass(int pClmn, int pRow){return this.aGrass[pClmn][pRow];}
    public boolean hasMinerals(int pClmn, int pRow){return this.aMinerals[pClmn][pRow];}
    public Animal getAnimal(int pClmn, int pRow){return this.aAnimals[pClmn][pRow];}
    /*****************/
    public void nextRound()
    {
        if(!this.aIsDead)
        {
            this.aRound++;
            this.addNews();
            this.moveAnimals();
            this.makeInteract();
            this.updateAnimals();
            this.updateGrass();
            this.removeDead();
            testUniverseDead();
        }
    }
    /*****************/
    private void testUniverseDead()
    {
        boolean wolf = false, sheep = false;
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
                if(this.aAnimals[vClmn][vRow] != null)
                    if (this.aAnimals[vClmn][vRow].getSpecies().equals("universe.entities.Sheep"))
                        sheep = true;
                    else if(this.aAnimals[vClmn][vRow].getSpecies().equals("universe.entities.Wolf"))
                        wolf = true;
        switch(this.aP.stop)
        {
            case 0:this.aIsDead  = !wolf;break;
            case 1: this.aIsDead =  !sheep;break;
            case 2:this.aIsDead = !wolf || !sheep;break;
            case 3:this.aIsDead = !wolf && !sheep;break;
        }
    }
    /*****************/
    private void updateAnimals()
    {
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
                if(this.aAnimals[vClmn][vRow]!=null)
                    this.aAnimals[vClmn][vRow].update();
    }
    /*****************/
    private void moveAnimals()
    {
        Animal[][] vAnimals = new Animal[aP.clmn][aP.row];
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
            {
                Animal vAnimal = this.aAnimals[vClmn][vRow];
                if(vAnimal!=null)
                {
                    ArrayList<Point> vEmptyNeighboringCells = new ArrayList<Point>();
                    for(Point vPoint: this.getNeighboringCells(vClmn, vRow))
                        if(vAnimals[vPoint.x][vPoint.y] == null && this.aAnimals[vPoint.x][vPoint.y] == null)
                            vEmptyNeighboringCells.add(vPoint);
                    vEmptyNeighboringCells.add(new Point(vClmn,vRow));
                    Point vPoint = vAnimal.getBestCell(this.aP.dep,vEmptyNeighboringCells,this.aAnimals,this.aGrass);
                    vAnimals[vPoint.x][vPoint.y] = vAnimal;
                }
            }
        this.aAnimals = vAnimals;
    }
    /*****************/
    private void makeInteract()
    {
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
            {
                Animal vAnimal = this.aAnimals[vClmn][vRow];
                if(vAnimal != null)
                {
                    this.aGrass[vClmn][vRow] = vAnimal.grassInteract(this.aGrass[vClmn][vRow]);
                    for(Point vCell:this.getNeighboringCells(vClmn , vRow))
                    {
                        //Interact with neighboring animals
                        Animal vAnimal2 = this.aAnimals[vCell.x][vCell.y];
                        if(vAnimal2 != null)
                            vAnimal.interact(vAnimal2);
                    }
                }
            }
    }
    /*****************/
    private void removeDead()
    {
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
                if(this.aAnimals[vClmn][vRow] != null && this.aAnimals[vClmn][vRow].hasProperty("naturaldead"))
                {
                    this.aAnimals[vClmn][vRow] = null;
                    this.aMinerals[vClmn][vRow] = true;
                }
                else if(this.aAnimals[vClmn][vRow] != null && this.aAnimals[vClmn][vRow].hasProperty("dead"))
                    this.aAnimals[vClmn][vRow] = null;
    }
    /*****************/
    private void addNews()
    {
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
            {
                Animal vAnimal = this.aAnimals[vClmn][vRow];
                if(vAnimal != null && vAnimal.hasProperty("pregnant"))
                {
                    vAnimal.removeProperty("pregnant");
                    ArrayList<Point> vEmptyNeighboringCells = new ArrayList<Point>();
                    for(Point vPoint: this.getNeighboringCells(vClmn, vRow))
                        if(this.aAnimals[vPoint.x][vPoint.y] == null)
                            vEmptyNeighboringCells.add(vPoint);
                    if(!vEmptyNeighboringCells.isEmpty())
                    {
                        Point vPoint = vEmptyNeighboringCells.get((new Random()).nextInt(vEmptyNeighboringCells.size()));
                        this.aAnimals[vPoint.x][vPoint.y] = vAnimal.giveBirth();
                    }
                }
            }
    }
    /*****************/
    private void updateGrass()
    {
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn < aP.clmn; vClmn++)
                if(this.aMinerals[vClmn][vRow] && !this.aGrass[vClmn][vRow])
                {
                    this.aGrass[vClmn][vRow] = true;
                    this.aMinerals[vClmn][vRow] = false;
                }
    }
    /*****************/
    ArrayList<Point> getNeighboringCells(final int pClmn, final int pRow)
    {
        ArrayList<Point> vNeighboringCells = new ArrayList<Point>();

        if(pRow-1 >= 0 && pClmn-1 >= 0){vNeighboringCells.add(new Point(pClmn - 1, pRow - 1));}
        if(pRow-1 >= 0){vNeighboringCells.add(new Point(pClmn , pRow- 1));}
        if(pRow-1 >= 0 && pClmn+1 < aP.clmn){vNeighboringCells.add(new Point(pClmn + 1, pRow - 1));}

        if(pClmn-1 >= 0){vNeighboringCells.add(new Point(pClmn - 1, pRow));}
        vNeighboringCells.add(new Point(pClmn , pRow));
        if(pClmn+1 <aP.clmn){vNeighboringCells.add(new Point(pClmn + 1, pRow));}

        if(pRow+1 < aP.row && pClmn-1 >= 0){vNeighboringCells.add(new Point(pClmn - 1, pRow + 1));}
        if(pRow+1 < aP.row){vNeighboringCells.add(new Point(pClmn , pRow+ 1));}
        if(pRow+1 < aP.row && pClmn+1 < aP.clmn){vNeighboringCells.add(new Point(pClmn + 1, pRow + 1));}

        return vNeighboringCells;
    }
    /*****************/
    public void addAnimal(Animal pAnimal)
    {
        ArrayList<Point> vEmptyCells = new ArrayList<Point>();
        for(int vRow = 0; vRow < aP.row; vRow++)
            for(int vClmn = 0; vClmn< aP.clmn; vClmn++)
                if(this.aAnimals[vClmn][vRow] == null)
                    vEmptyCells.add(new Point(vClmn,vRow));
        if(!vEmptyCells.isEmpty())
        {
            Point vPoint = vEmptyCells.get((new Random()).nextInt(vEmptyCells.size()));
            this.aAnimals[vPoint.x][vPoint.y] = pAnimal;
        }
    }
}