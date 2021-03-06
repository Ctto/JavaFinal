package battle;

public enum Formation {
    OVEREDGE("鹤翼",1), ECHELON("雁行",2), YOKE("冲轭",3), HYDRA("长蛇",4),
    SCALE("鱼鳞",5), SQUARE("方円",6), HOOKEDSPEAR("偃月",7), ARROW("锋矢",8);

    String Cname;
    public boolean[][] form;
    int num;

    Formation(String Cname, int n) {
        this.Cname = Cname;
        form = new boolean[11][10];
        switch (n) {
            case 1:     //鹤翼
                num = 7;
                for (int i = 3; i < 7; i++){
                    form[i][i-1] = true;
                }
                for (int i = 3; i < 6; i++){
                    form[i][11-i] = true;
                }
                break;
            case 2:     //雁行
                num = 5;
                for (int i = 3; i < 3+5; i++) {
                    form[i][10-i] = true;
                }
                break;
            case 3:     //冲轭
                num = 6;
                for (int i = 3; i < 8; i += 2) {
                    form[i][3] = true;
                }
                for (int i = 2; i < 7; i += 2){
                    form[i][4] = true;
                }
                break;
            case 4:     //长蛇
                num = 7;
                for  (int i = 0; i < 7; i++) {
                    form[i+2][5] = true;
                }
                break;
            case 5:     //鱼鳞
                num = 13;
                form[6][5] = true;
                form[2][5] = true;
                form[3][6] = true;
                for (int i = 3; i < 9; i++){
                    form[5][i] = true;
                }
                for (int i = 2; i < 8; i++) {
                    form[4][i] = true;
                }
                break;
            case 6:     //方円
                num = 8;
                for (int i = 3; i < 6; i++) {
                    form[i][8-i] = true;
                }
                for (int i = 3; i < 6; i++) {
                    form[i][i+2] = true;
                }
                form[6][4] = true;
                form[6][6] = true;
                form[6][5] = true;
                break;
            case 7:     //偃月
                num = 19;
                for (int i = 1; i < 7; i++) {
                    form[i][8-i] = true;
                }
                for (int i = 2; i < 6; i++) {
                    form[i][7-i] = true;
                }
                for (int i = 6; i < 9; i++) {
                    form[i][i-3] = true;
                }
                for (int i = 6; i < 10; i++) {
                    form[i][i-2] = true;
                }
                form[4][2] = true;
                form[5][4] = true;
                break;
            case 8:     //锋矢
                num = 12;
                for (int i = 2; i < 8; i++){
                    form[i][5] = true;
                }
                for (int i = 3; i < 6; i++) {
                    form[i][7-i] = true;
                }
                for (int i = 3; i < 6; i++) {
                    form[i][i+3] = true;
                }
                break;
        }
    }
}
