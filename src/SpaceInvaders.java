import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.util.random.*;
import java.awt.event.*;
import javax.swing.*;


public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

  // Board
  int tileSize = 32;
  int rows = 16;
  int columns = 16;
  int boardwidth = tileSize * columns;
  int boardHeight = tileSize * rows;


  // Instantiate images on the panel
  Image shipImage;
  Image alienImage;
  Image alienCyanImage;
  Image alienMagentaImage;
  Image alienYellowImage;

  // Create Array to store the images to be picked at random
  ArrayList<Image> alienImgArray;


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

  //ship
  int shipWidth = tileSize * 2;
  int shipHeight = tileSize;
  int shipX = tileSize * columns/2 - tileSize;
  int shipY = boardHeight - tileSize*2; 
  int shipVelocityX = tileSize;
  Block ship;

  //aliens
  ArrayList <Block> alienArray;
  int alienWidth = tileSize * 2;
  int alienHeight = tileSize;
  int alienX = tileSize;
  int alienY = tileSize;

  //How many aliens in a row, a column and the count (2 rows and three columns)
  int alienRows = 2;
  int alienColumns = 3;
  int alienCount =0;
  int alienVelocityX = 1; // velocity of the alien speed by 1 px along the x axis of the panel

  //Bullets
  ArrayList <Block> bulletArray;
  int bulletWidth = tileSize/8;
  int bulletHeight = tileSize/2;
  int bulletCount = 0;
  int bulletVelocityY = -10; //bullet speed in y direction
  


  //
  Timer gameLoop;
  int score = 0; 
  boolean gameOver = false;
 

  //constructor to instantiate the  JPanel
  SpaceInvaders () {
    setPreferredSize(new Dimension(boardHeight, boardHeight));
    setBackground(Color.black);
    setFocusable(true);
    addKeyListener(this);

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
    alienArray = new ArrayList<Block>();
    bulletArray = new ArrayList<Block>();

    //game timer
    gameLoop = new Timer(1000/60, this);
    createAliens();
    gameLoop.start();
  }

  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw (Graphics g){
    g.drawImage(ship.img, ship.x, ship.y, ship.width ,ship.height, null);

    //draw the aliens
    for (int i =0; i < alienArray.size(); i++){
      Block alien = alienArray.get(i);

      //do not display any destroyed by a bullet
      if(alien.alive){
        g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
      }
    }
    //draw bullets
    g.setColor(Color.red);
    for (int i =0; i< bulletArray.size(); i++){
      Block bullet = bulletArray.get(i);

      //check if the bullet is not used, draw the bullet
      if(!bullet.used){
        g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
        //g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
      }
    }

    //score
    g.setColor(Color.green);
    g.setFont(new Font("Arial", Font.PLAIN, 32));
    
    //draw the game over
    if(gameOver){
      g.drawString("Game Over: " + String.valueOf(score) , 10, 35);
    }
    else {
      g.drawString(String.valueOf(score), 10, 35); 
    }
  }

  //function to handle the movement of the aliens and bullets
  public void move (){
    //aliens
    for (int i =0; i< alienArray.size(); i++){
      Block alien = alienArray.get(i);
      
      //chack if alien is alive
      if(alien.alive){
        alien.x += alienVelocityX;

        //if alien touches border
        if(alien.x + alien.width >= boardwidth || alien.x <= 0){
          alienVelocityX *= -1;
          alien.x += alienVelocityX *2;

          //move alien down one row
          for(int j = 0; j< alienArray.size(); j++){
            alienArray.get(j).y += alienHeight;
          }
        }

        //check if the ship is touched to end game
        if (alien.y >= ship.y){
          gameOver = true;
        }
      }
    }

    //update the bullet movement after draw
    for(int i = 0; i<bulletArray.size(); i++){
      Block bullet = bulletArray.get(i);
      bullet.y += bulletVelocityY;

      // check for bullet collision with aliens
      for (int j =0; j< alienArray.size(); j++){
        Block alien = alienArray.get(j);
        if(!bullet.used && alien.alive && detectCollision(bullet,alien)){
          bullet.used = true;
          alien.alive = false;
          alienCount--;
          score += 100; //create a score for each alien shot
          //break out of the loop to prevent further collision
          break;
        }
      }
    }
    //clear bullets
    //change arrry list to a linked list
    while (bulletArray.size()> 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
      bulletArray.remove(0);
    }

    //next level of the game
    if(alienCount == 0){
      // bonus points when a level is passed
      score += alienColumns * alienRows * 100;

      //increase the number of aliens in columns by 1and set the max number of columns
      alienColumns = Math.min(alienColumns + 1, columns/2 - 2);
      alienRows = Math.min(alienRows + 1, rows - 6 );

      //clear aliens for the next round
      alienArray.clear();

      //clear bullets
      bulletArray.clear();

      //mover aliens to the right
      alienVelocityX = 1;

      //create new aliens
      createAliens();
    }
  }
  
  //create aliens
  public void createAliens(){
    Random random = new Random();
    for (int row = 0; row < alienRows ; row++){
      for (int column = 0; column < alienColumns; column++){
        int randomImageIndex = random.nextInt(alienImgArray.size());
        Block alien = new Block(
          alienX + column*alienWidth,
          alienY + row * alienHeight,
          alienWidth,
          alienHeight,
          alienImgArray.get(randomImageIndex)
        );
        alienArray.add(alien);
      }
    }
    alienCount = alienArray.size();
  }

  //collission detection function to detect the bullet hitting the aliens
  //if all conditions are true then there is an overlap
  public boolean detectCollision(Block a, Block b){
    return  a.x < b.x + b.width  &&  //checks if the left edge of a is to the left edge of b
            a.x + a.width > b.x && // checks if the right edge of a is to the right of the left edge of b
            a.y < b.y + b.height && //checks if the top edge of a is above the bottom edge of b
            a.y + a.height > b.y; //checks if the bottom edge of a is below top edge of b
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    //update position 
    move();
    repaint();

    //end game completely 
    if (gameOver){
      gameLoop.stop();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {}
  

  @Override
  public void keyReleased(KeyEvent e) {
    //allow user to reset the game by any key 
    if (gameOver){
      ship.x = shipX;
      alienArray.clear();
      bulletArray.clear();
      score = 0;
      alienVelocityX = 1;
      alienColumns = 3;
      alienRows = 2;
      gameOver = false;
      createAliens();
      gameLoop.start();
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x -shipVelocityX >= 0) {
      ship.x -= shipVelocityX; //move left one tile
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width + shipVelocityX <= boardwidth ){
      ship.x += shipVelocityX; //move right one tile
    }
    //click handler for bullet
    else if (e.getKeyCode()== KeyEvent.VK_SPACE){
      Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null);
      bulletArray.add(bullet);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) { }
}
