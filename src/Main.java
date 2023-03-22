/*****************/
public class Main
{
    /*****************/
    public static void main(String[] args)
    {
        try {javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch(Exception ignored){}
        GameEngine vGE = new GameEngine ();
        vGE.startGameLoop();
    }
}