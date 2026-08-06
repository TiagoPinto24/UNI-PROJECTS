import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main { 
    public static void main(String[] argv) throws IOException {

        BufferedReader B = new BufferedReader(new InputStreamReader(System.in));

        Max a = new Max();

        int S = Integer.parseInt(B.readLine());
        int N = Integer.parseInt(B.readLine());


        String[][] bgn = new String[N][3];

        String[] Saux;

        for (int i = 0; i < N; i++) {
            Saux = B.readLine().split(" ");
            bgn[i][0] = Saux[0]; // nome do jogo
            bgn[i][1] = Saux[1]; // para laer em int, usar parseint && numero de jogadore
            bgn[i][2] = Saux[2]; // para laer em int, usar parseint && entusiasmo
        }

        a.compare(bgn);

        String[][] respostas = a.func(bgn, S);

        // ver a melhor opção para preenchimento da sala
        String[] fim = respostas[0];
        for (int i = 0; i < respostas.length-1; i++) {
            if (Integer.parseInt(fim[1]) > Integer.parseInt(respostas[i][1]))
                fim = respostas[i];
        }

        // output
        int counter = 0;
        for (int i = 2; i < fim.length - 2 && fim[i] != null; i++)
            counter++;
        System.out.println(counter + " " + fim[0] + " " + fim[1]);
        for (int i = 2; i < fim.length - 2 && fim[i] != null; i++)
            System.out.println(fim[i]);
    }
}
