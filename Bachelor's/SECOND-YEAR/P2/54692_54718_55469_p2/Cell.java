public class Cell extends Grassland{
    
    int coelhoadj;
    int cenouraadj;
    int cont;
    int contfuturo;
    int ST;

    public Cell (int a, int b, int c, int d, int e) {
        super(0, 0, 0);
        cont=a;
        contfuturo=b;
        cenouraadj=c;
        coelhoadj=d;
        ST=e;
    }
}