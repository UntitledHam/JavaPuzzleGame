import java.util.*;
public class Game{
    private String[][] board; 
    private ArrayList<String[][]> maps;
    private ArrayList<Integer[]> mapInfo;
    private int playerX;
    private int playerY; 
    private String player;
    private String emptySpace; 
    private String wall;
    private String block;
    private String stepPickup; 
    private String flagPole;
    private int currentMap;
    private int numberOfSteps;
    private int extraSteps; 
    private boolean infiniteSteps; 
    private boolean debugMode;
    private boolean changingStages;
    private boolean gameDone;
    private boolean randomizeSymbols;
    
    public Game(){
        maps = new ArrayList<String[][]>();
        mapInfo = new ArrayList<Integer[]>();
        changingStages = false;
        gameDone = false;
        
        //Change what characters objects show up as.
        player = "P";
        emptySpace = ".";
        wall = "#";
        block = "B";
        flagPole = "F";
        stepPickup = "O"; 
        extraSteps = 5; 
        
        
        //Prints numbers on the grid making map design easier.
        debugMode = false;
        
        //Makes you have infinite steps. 
        infiniteSteps = false;
        
        //Do not do this or else the game will be so bad it is unplayable.
        //Randomizes the characters
        randomizeSymbols = false;
        
        if (randomizeSymbols){
            randomizeCharacters();
        }
        
        
        generateMaps();
        selectMap(0);
        refreshBoard();
        
    }
    
    //This is useful I swear
    public void randomizeCharacters(){
        String characters = "~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:ZXCVBNM<>?`1234567890-=qwertyuiop[]asdfghjkl;'zxcvbnm,.";
        int num = 0;
        
        num = (int)(Math.random() * characters.length()); 
        player = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
        
        num = (int)(Math.random() * characters.length());
        emptySpace = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
        
        num = (int)(Math.random() * characters.length());
        wall = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
        
        num = (int)(Math.random() * characters.length());
        block = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
        
        num = (int)(Math.random() * characters.length());
        flagPole = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
        
        num = (int)(Math.random() * characters.length());
        stepPickup = characters.substring(num, num+1);
        characters = characters.substring(0, num) + characters.substring(num+1,characters.length());
    }
    
    //The chaos that is the movePlayer method not only moves the player but it also deals with block pushing.
    public void movePlayer(int xAmount, int yAmount){
        if (numberOfSteps > 0){
            //Block to the right of player
            if (xAmount !=0 && playerX + xAmount < board[0].length && playerX + xAmount >= 0 && !(board[playerY][playerX + xAmount].equals(wall))){
                if (xAmount > 0 && playerX + xAmount +1 < board[0].length && board[playerY][playerX + xAmount].equals(block) && !(board[playerY][playerX + xAmount+1].equals(block)) && !(board[playerY][playerX + xAmount+1].equals(wall))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    //A quick and dirty patch to the bug that makes you automatically take a step when you change stages
                    if (!(changingStages)){
                        board[playerY][playerX + xAmount+1] = block;
                        board[playerY][playerX + xAmount] = emptySpace;
                        board[playerY][playerX] = emptySpace;
                        playerX += xAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }   
                //Block to the left of player
                else if (xAmount < 0 && playerX + xAmount -1 >= 0 && board[playerY][playerX + xAmount].equals(block) && !(board[playerY][playerX + xAmount-1].equals(block)) && !(board[playerY][playerX + xAmount-1].equals(wall))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    if (!(changingStages)){
                        board[playerY][playerX + xAmount-1] = block;
                        board[playerY][playerX + xAmount] = emptySpace;
                        board[playerY][playerX] = emptySpace;
                        playerX += xAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }
                //No block to left or right of the player.
                else if (!(board[playerY][playerX + xAmount].equals(block))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    if (!(changingStages)){
                        board[playerY][playerX] = emptySpace;
                        playerX += xAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }
            }
            
            if (yAmount != 0 && playerY - yAmount < board.length && playerY - yAmount >= 0 && !(board[playerY - yAmount][playerX].equals(wall))){
                //Box above the player.
                if (yAmount > 0 && playerY - yAmount -1 >= 0 && board[playerY - yAmount][playerX].equals(block) && !(board[playerY - yAmount -1][playerX].equals(block)) && !(board[playerY - yAmount -1][playerX].equals(wall))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    if (!(changingStages)){
                        board[playerY - yAmount -1][playerX] = block;
                        board[playerY - yAmount][playerX] = emptySpace;
                        board[playerY][playerX] = emptySpace;
                        playerY -= yAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }
                //Box below the player.
                else if (yAmount < 0 && playerY - yAmount +1 < board.length && board[playerY - yAmount][playerX].equals(block) && !(board[playerY - yAmount +1][playerX].equals(block)) && !(board[playerY - yAmount +1][playerX].equals(wall))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    if (!(changingStages)){
                        board[playerY - yAmount +1][playerX] = block;
                        board[playerY - yAmount][playerX] = emptySpace;
                        board[playerY][playerX] = emptySpace;
                        playerY -= yAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }
                //No block above or below the player.
                else if (!(board[playerY - yAmount][playerX].equals(block))){
                    detectPickup(xAmount, yAmount);
                    detectFlagPole(xAmount, yAmount);
                    if (!(changingStages)){
                        board[playerY][playerX] = emptySpace;
                        playerY -= yAmount;
                        board[playerY][playerX] = player;
                        takeStep();
                    }
                    changingStages = false;
                }
            }
        }
        refreshBoard();
    }
    
    public void generateMaps(){
        //Generate Blank Grids//
        maps.add(new String[11][7]);
        maps.add(new String[20][5]);
        maps.add(new String[11][7]);
        maps.add(new String[5][11]);
        maps.add(new String[11][5]);
        
        //Generate start positons.
        //The first 2 numbers encode the start positon.
        //The third is for the number of steps.
        Integer[] map0Info = {3,10, 5};
        Integer[] map1Info = {2, 0, 5};
        Integer[] map2Info = {3,10, 5};
        Integer[] map3Info = {0, 2, 5};
        Integer[] map4Info = {2, 10, 5};
        mapInfo.add(map0Info);
        mapInfo.add(map1Info);
        mapInfo.add(map2Info);
        mapInfo.add(map3Info);
        mapInfo.add(map4Info);
        
        repairMaps();
    }
    
    public void repairMaps(){
        //Fills the maps with the empty character and makes a boarder of walls
        for (String[][] map : maps){
            for (int x = 0; x < map.length; x++){
                for (int y = 0; y < map[0].length; y++){
                    //Makes a border of walls around the stage
                    if (x == 0 || y == 0 || x == map.length -1 || y == map[0].length -1){
                        map[x][y] = wall;
                    }
                    else{
                        map[x][y] = emptySpace;
                    }
                }
            }
        }
        
        
        //Manual Tweaks//
        
        //Level 0 Tweaks
        
        //Sets the flagpoles
        for (int x = 1; x < maps.get(0)[0].length -1; x++){
            maps.get(0)[0][x] = flagPole;
        }
        maps.get(0)[10][2] = emptySpace;
        maps.get(0)[10][4] = emptySpace;
        
        //Sets the step pickups
        maps.get(0)[5][3] = stepPickup;
        
        //Level 1 tweaks
        maps.get(1)[0][1] = emptySpace; 
        maps.get(1)[0][3] = emptySpace;
        
        boolean left = false; 
        
        for (int x = 1; x < maps.get(1).length; x+=2){
            if (left){
                maps.get(1)[x][1] = wall; 
                maps.get(1)[x][2] = wall;
                left = false;
            }
            else{
                maps.get(1)[x][2] = wall;
                maps.get(1)[x][3] = wall;
                left = true; 
            }    
        } 
        for (int x = 2; x < maps.get(1).length; x+=2){
            maps.get(1)[x][2] = stepPickup; 
        }
        
        maps.get(1)[18][4] = flagPole;
        
        
        
        
        //Level 2 Tweaks
        maps.get(2)[5][2] = block;
        maps.get(2)[5][1] = wall;
        maps.get(2)[5][3] = wall;
        maps.get(2)[5][4] = wall;
        maps.get(2)[5][5] = wall;
        maps.get(2)[8][2] = stepPickup;
        maps.get(2)[2][1] = stepPickup;
        maps.get(2)[4][1] = stepPickup;
        maps.get(2)[2][6] = flagPole;
        
        
        //Level 3 Tweaks
        for (int x = 1; x < maps.get(3)[0].length -1; x++){
            maps.get(3)[1][x] = wall;
            maps.get(3)[3][x] = wall;
        }
        maps.get(3)[1][2] = stepPickup; 
        maps.get(3)[2][2] = block; 
        maps.get(3)[1][5] = emptySpace;
        maps.get(3)[1][6] = emptySpace;
        maps.get(3)[3][5] = emptySpace;
        maps.get(3)[3][6] = emptySpace;
        maps.get(3)[2][8] = stepPickup;
        maps.get(3)[2][10] = flagPole;
        maps.get(3)[1][5] = stepPickup;
        
        //Levels 4 Tweaks
        maps.get(4)[10][1] = emptySpace;
        maps.get(4)[10][3] = emptySpace;
        
        for (int x = 3; x < maps.get(4).length -1; x+=2){
            maps.get(4)[x][1] = wall;
            maps.get(4)[x][2] = wall;
        }
        //Places the step pickups
        for (int x = 2; x < 10; x+=2){
            maps.get(4)[x][2] = stepPickup;
        }
        maps.get(4)[9][3] = block; 
        maps.get(4)[1][3] = emptySpace;
        maps.get(4)[1][4] = emptySpace;
        maps.get(4)[0][3] = flagPole;
        maps.get(4)[1][1] = wall;
        maps.get(4)[2][1] = wall;
        
    }
        
    
    //Tests for if the player is going to pickup a step pickup
    public void detectPickup(int xAmount, int yAmount){
        if (xAmount != 0 && board[playerY][playerX + xAmount].equals(stepPickup)){
            numberOfSteps = extraSteps+1; 
        } 
            
        else if (yAmount != 0 && board[playerY - yAmount][playerX].equals(stepPickup)){
            numberOfSteps = extraSteps +1; 
        }
    }
    
    public void detectFlagPole(int xAmount, int yAmount){
        if (xAmount != 0 && board[playerY][playerX + xAmount].equals(flagPole)){
            nextLevel();   
        } 

        else if (yAmount != 0 && board[playerY - yAmount][playerX].equals(flagPole)){
            nextLevel();
        }
    }
    
    //Clears the console and prints it again. 
    public void refreshBoard(){
        clearConsole();
        printBoard();
    }
    
    //Moves onto the next level
    public void nextLevel(){
        numberOfSteps = 0;
        changingStages = true;
        currentMap++;
        if (currentMap >=maps.size()){
            gameDone = true;
            refreshBoard();
        }
        else{
            selectMap(currentMap);
            reset();
        }
    }
    
    //Resets the current map to it's defaults
    public void reset(){
        if (randomizeSymbols){
            randomizeCharacters();
        }
        repairMaps();
        if (gameDone){
            gameDone = false;
            currentMap = 0;
        }
        selectMap(currentMap);
        refreshBoard();
        
    }
    
    //Selects the inputted map.
    public void selectMap(int x){
        currentMap = x;
        board = maps.get(x); 
        playerX = mapInfo.get(x)[0];
        playerY = mapInfo.get(x)[1];
        numberOfSteps = mapInfo.get(x)[2];
        board[playerY][playerX] = player;
    }
    
    
    //If you don't have infinite steps the step counter increases.
    public void takeStep(){
        if (!(infiniteSteps)){
            numberOfSteps--;
        }
    }
    
    //Prints the board and the step counter
    public void printBoard(){
        String output = "";
        if (!(gameDone)){
                output += "Level: " + (currentMap +1) + "\n"; 
            if (infiniteSteps){
                output += "Number of Steps: ∞\n\n";
            }
            else if (numberOfSteps == 0){
                output+= "Game Over. Press R to reset.\n\n";
            }
            else{
                output += "Number of Steps: " + numberOfSteps + "\n\n";
            }
        
            if (debugMode){
                output+= "Num of Maps: " + maps.size() + "\n\n";
                for (int z = 0; z < board[0].length; z++){
                    output+= z + " ";
                }
                output+="\n\n";
            }
        
            for (int x = 0; x < board.length; x++){
                for (int y = 0; y < board[0].length; y++){
                    output += board[x][y] + " ";
                
                }
                if (debugMode){
                    output += " " + x;
                }
                output+="\n";
            }
        }
        else{
            output+= "You Win!\nPress R to restart the game.";
        }
        
        System.out.print(output);
    }
    
    
    
    //Clears the console through using what I only know as witchcraft. 
    public void clearConsole(){
        System.out.print("\033[H\033[2J"); 
        System.out.flush();
    }
}
