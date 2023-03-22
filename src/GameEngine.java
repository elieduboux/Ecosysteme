import ui.UserInterface;

/*****************/
public class GameEngine
{
    private final UserInterface aUI;
    /*****************/
    public GameEngine()
    {
        this.aUI = new UserInterface();
    }
    /*****************/
    public void startGameLoop()
    {
        this.startRepaintThread();
        this.startUpdateThread();
    }
    /*****************/
    private void startRepaintThread()
    {
        (new Thread(() -> {
            while(true)
            {
                this.aUI.getUniCanvas().repaint();
                try{Thread.sleep(1000/60);}
                catch(InterruptedException e){return;}
            }
        })).start();
    }
    /*****************/
    private void startUpdateThread()
    {
        (new Thread(() -> {
            while(true)
            {
                if(this.aUI.auto())
                    this.aUI.nextRound();
                try{Thread.sleep(Math.min(this.aUI.getInterval(),1000));}
                catch(InterruptedException e){return;}
            }
        })).start();
    }
}