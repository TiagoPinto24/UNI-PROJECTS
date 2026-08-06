public class Max {
    public static String[][] func(String[][] bgn, int S) {
        String[][] res = new String[bgn.length][62];
        for (int i = 0; i < bgn.length; i++) {
            for (int j = 0; j < 5; j++) {
                res[i][j]= "0";
            }
        }
        int aux = 2;
        for (int i = 0; i < bgn.length; i++) {
            for (int j = 0; j < bgn.length; j++) {
                if (Integer.parseInt(res[i][0]) + Integer.parseInt(bgn[j][1]) <= S) {
                    res[i][0] = Integer.toString(Integer.parseInt(res[i][0]) + Integer.parseInt(bgn[j][1])); // numero de jogadors
                    res[i][1] = Integer.toString(Integer.parseInt(res[i][1]) + Integer.parseInt(bgn[j][2])); // entusiamo
                    res[i][aux] = bgn[j][0]; // daqui para a frente guarda os nomes dos jogos
                    aux++;
                    if (Integer.parseInt(res[i][0]) > S) {
                        res[i][0] = Integer.toString(Integer.parseInt(res[i][0]) - Integer.parseInt(bgn[j][1])); // numero de jogadors
                        res[i][1] = Integer.toString(Integer.parseInt(res[i][1]) - Integer.parseInt(bgn[j][2])); // entusiamo
                        res[i][aux-1] = null; // daqui para a frente guarda os nomes dos jogos
                    }
                }
    
            }
        }
        return res;
    }
    public static void compare(String bgn[][]){
        for (int i = 0; i < bgn.length - 1; i++) {
            for (int j = 0; j < bgn.length - 1; j++) {
                int enthusiasm1 = Integer.parseInt(bgn[j][2]);
                int enthusiasm2 = Integer.parseInt(bgn[j + 1][2]);
    
                if (enthusiasm1 < enthusiasm2 ) {
                    String[] temp = bgn[j];
                    bgn[j] = bgn[j + 1];
                    bgn[j + 1] = temp;
                }
            }
        }
    }
}
