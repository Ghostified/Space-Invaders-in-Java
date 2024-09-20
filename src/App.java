import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        //window variables
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardwidth = tileSize * columns;
        int boardHeight = tileSize * rows;

        //set JFrame 
        JFrame frame = new JFrame("Space Invaders");
        //frame.setVisible(true);
        frame.setSize(boardwidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

        SpaceInvaders spaceInvaders = new SpaceInvaders();
        // System.out.println(spaceInvaders);
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
    }
}
