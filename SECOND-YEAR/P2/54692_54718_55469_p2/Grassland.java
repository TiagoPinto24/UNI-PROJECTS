public class Grassland {

  Cell[][] campo;
  int StarveTime;
    /**
     *  Do not rename these constants.  WARNING:  if you change the numbers, you
     *  will need to recompile Test4.java.  Failure to do so will give you a very
     *  hard-to-find bug.
     */
  
    public final static int EMPTY = 0;
    public final static int RABBIT = 1;
    public final static int CARROT = 2;
  
    /**
     *  Define any variables associated with an Grassland object here.  These
     *  variables MUST be private.
     */
  
    /**
     *  Grassland() is a constructor that creates an empty meadow having width i and
     *  height j, in which rabbits starve after starveTime timesteps.
     *  @param i is the width of the meadow.
     *  @param j is the height of the meadow.
     *  @param starveTime is the number of timesteps rabbits survive without food.
     */
  
    public Grassland(int i, int j, int starveTime) {
      campo = new Cell[i][j];
      for (int k = 0; k< campo.length; k++) {
        for (int k2 = 0; k2 < campo[k].length; k2++) {
          this.campo[k][k2]=new Cell(0, 0, 0, 0, 5);
          this.campo[k][k2].cont=EMPTY;
        }
      }
      this.StarveTime = starveTime();
    }
  
    /**
     *  width() returns the width of an Grassland object.
     *  @return the width of the meadow.
     */
  
    public int width() {
      return 100;
    }
  
    /**
     *  height() returns the height of an Grassland object.
     *  @return the height of the meadow.
     */
  
    public int height() {
      return 100;
    }
  
    /**
     *  starveTime() returns the number of timesteps rabbits survive without food.
     *  @return the number of timesteps rabbits survive without food.
     */
  
    public int starveTime() {
      return 5;
    }
  
  
    /**
     *  addCarrot() places a carrot in cell (x, y) if the cell is empty.  If the
     *  cell is already occupied, leave the cell as it is.
     *  @param x is the x-coordinate of the cell to place a carrot in.
     *  @param y is the y-coordinate of the cell to place a carrot in.
     */
  
    public void addCarrot(int x, int y) {
      if (campo[x][y].cont==EMPTY) {
        campo[x][y].cont=CARROT;
      }
    }
  
    /**
     *  addRabbit() (with two parameters) places a newborn rabbit in cell (x, y) if
     *  the cell is empty.  A "newborn" rabbit is equivalent to a rabbit that has
     *  just eaten.  If the cell is already occupied, leave the cell as it is.
     *  @param x is the x-coordinate of the cell to place a rabbit in.
     *  @param y is the y-coordinate of the cell to place a rabbit in.
     */
  
    public void addRabbit(int x, int y) {
      if (this.campo[x][y].cont==EMPTY) {
        this.campo[x][y].cont=RABBIT;
        this.campo[x][y].ST=starveTime();
      }
    }
  
    /**
     *  cellContents() returns EMPTY if cell (x, y) is empty, CARROT if it contains
     *  a carrot, and RABBIT if it contains a rabbit.
     *  @param x is the x-coordinate of the cell whose contents are queried.
     *  @param y is the y-coordinate of the cell whose contents are queried.
    */
  
    public int cellContents(int x, int y) {
      if (this.campo[x][y].cont==0)
        return EMPTY;
  
      if (this.campo[x][y].cont==1)
        return RABBIT;
  
      return CARROT;
    }
  
    public void CreateCarrot (int x, int y) {
      if (campo[x][y].cenouraadj >= 2 && campo[x][y].coelhoadj<=1 && campo[x][y].cont==EMPTY) {
        campo[x][y].contfuturo=CARROT;
      }
    }
  
    public void CreateRabbit (int x, int y) {
      if (campo[x][y].coelhoadj >=2 && (campo[x][y].cont==CARROT || campo[x][y].cont==EMPTY && campo[x][y].cenouraadj>=2)) {
        campo[x][y].contfuturo=RABBIT;
        campo[x][y].ST=starveTime();
      }
    }
  
    public void EatCarrot (int x, int y) {
      if (campo[x][y].cont ==CARROT && campo[x][y].coelhoadj>=1)
        campo[x][y].contfuturo=EMPTY;
    }
  
    public void Starve(int x, int y){
      if (campo[x][y].cont==RABBIT) {
        if (campo[x][y].ST==0)
          campo[x][y].contfuturo=EMPTY;
        else if (campo[x][y].cenouraadj==0) 
          campo[x][y].ST--;
        else if(campo[x][y].cenouraadj!=0){
          if(campo[x][y].ST<5)
            campo[x][y].ST=starveTime();
        campo[x][y].cenouraadj--;
        }
      }
    } 
  
    public void Countviz(int x, int y) {
      for (int i = x-1; i <= x+1; i++) {
        for (int j = y-1; j <= y+1; j++) {
          if (i>=0 && j>=0 && (i!=x || j!=y) && i<width() && j<height()) { 
            if (campo[i][j].cont == RABBIT)
                  campo[x][y].coelhoadj++;
            if (campo[i][j].cont == CARROT) 
                  campo[x][y].cenouraadj++;
          }
        }
      }
    }
    
    public void feed (int i, int j) {
      if (i+1<width()) {
        if (j+1<height())
          Starve(i+1, j+1);
        if (j-1>=0)
          Starve(i+1, j-1);
      Starve(i+1, j);
      }
      if (j+1<height())
        Starve(i, j+1);
    }
  
    /**
    *  timeStep() performs a simulation timestep 
    *  @return a meadow representing the elapse of one timestep.
    */
  
    public Grassland timeStep() {
      Grassland gl = new Grassland(width(), height(), starveTime());
      for (int i = 0; i < campo.length; i++) {
        for (int j = 0; j < campo[i].length; j++) {
          
          //primeiro alimenta-se os coelhos
          feed(i,j);
  
          //contar os vizinhos de novo
          campo[i][j].cenouraadj=0;
          campo[i][j].coelhoadj=0;
          Countviz(i, j);
  
          //guardar os valores antigos (para o caso de serem precisos)
          campo[i][j].contfuturo=campo[i][j].cont;
  
          //alteracões nos valores
          CreateRabbit(i, j);
          CreateCarrot(i, j);
          EatCarrot(i, j);
          Starve(i, j);
        }
        
      }
  
      //gerar o novo campo com os valores atualizados
      for (int i = 0; i < campo.length; i++) {
          for (int j = 0; j < campo[i].length; j++) {
            gl.campo[i][j]=this.campo[i][j];
            gl.campo[i][j].cont=gl.campo[i][j].contfuturo;
            gl.campo[i][j].contfuturo=0;
          }
        }
  
      return gl;
    }
  
  }
  