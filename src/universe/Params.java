package universe;

/*****************/
public class Params
{
    public int clmn , row , sheep , wolf, dep, stop;
    /*****************/
    public Params(int pClmn, int pRow, int pSheep, int pWolf, int pDep,int pStop)
    {
        clmn = pClmn;
        row = pRow;
        sheep = pSheep;
        wolf = pWolf;
        dep = pDep;
        stop = pStop;
    }
    /*****************/
    @Override public boolean equals(Object pO)
    {
        if(pO instanceof Params pP)
            return (pP.clmn*pP.row == clmn*row && pP.sheep == sheep && pP.wolf == wolf && pP.dep == dep && pP.stop == stop);
        return false;
    }
    /*****************/
    public boolean isComparableWith(Params p, int variable)
    {
        boolean[] equals = {p.clmn*p.row==clmn*row,p.sheep==this.sheep,p.wolf==wolf,p.dep==dep,p.stop==stop};
        for(int i = 0; i < equals.length;i++)
            if(variable!=i && !equals[i])
                return false;
        return true;
    }
}

