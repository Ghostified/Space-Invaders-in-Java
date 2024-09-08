import java.util.ArrayList;
import java.awt.*;
import java.util.random.*;
import java.awt.event.*;
import javax.swing.*;


public class SpaceInvaders extends JPanel {
  class Block{
    //determines the ships , bullets and aliens position
    int x; 
    int y;
    int width;
    int height;
    Image img;
    boolean alive = true; //used for aliens
    boolean used = false; //used for bullets

    Block(int x, int y, int width, int height,Image img ){
      this.x = x ;
      this.y = y;
      this.width = width;
      this.height = height;
      this.img = img; 
    }
  }
  //Board
  int  tileSize = 32;
  int rows = 16;
  int columns =16;
  int boardHeight= tileSize * rows;
  int boardwidth = tileSize * columns ;

  //Instantiate images on the panel
  Image shipImage;
  Image alienImage;
  Image alienCyanImage;
  Image alienMagentaImage;
  Image alienYellowImage;

  //Create  Array to store the images to be picked  at random
  ArrayList <Image> alienImgArray;  
    
  //ship
  int shipWidth = tileSize*2;
  int shipHeight = tileSize;
  int shipX = tileSize * columns/2 - tileSize;
  int shipY = boardHeight - tileSize*2; 
    
  Block ship;


  //constructor to instantiate the  JPanel
  SpaceInvaders () {
    setPreferredSize(new Dimension(boardHeight, boardHeight));
    setBackground(Color.black);

    //Load Images
    shipImage = new ImageIcon(getClass().getResource("ship.png")).getImage();
    alienImage = new ImageIcon(getClass().getResource("alien.png")).getImage();
    alienCyanImage = new ImageIcon(getClass().getResource("alienCyan.png")).getImage(); 
    alienMagentaImage = new ImageIcon(getClass().getResource("alienMagenta.png")).getImage();
    alienYellowImage = new ImageIcon(getClass().getResource("alienYellow.png")).getImage();

    //add the images to an array list after being loaded 
    alienImgArray = new ArrayList<Image>();
    alienImgArray.add(alienImage);
    alienImgArray.add(alienCyanImage);
    alienImgArray.add(alienYellowImage);
    alienImgArray.add(alienMagentaImage);

    //draw the ship on the JPanel
    //determine the x,y cordinates and width and height
    ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImage);
  }

  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw (Graphics g){
    g.drawImage(shipImage, shipX, shipY, shipHeight,shipWidth ,null);
  }
}
