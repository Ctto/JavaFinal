package battle;

import creature.Creature;

public class BattleField{
    private int row, col;
    Brick<Creature>[][] bricks;

    @SuppressWarnings("unchecked")
    BattleField(int row, int col) {
        this.row = row;
        this.col = col;
        // bricks = new battle.Brick<creature.Creature>[row][col]; // er: Generic array creation
        // bricks = new battle.Brick[row][col];   // Unchecked assignment: 'battle.Brick[][]' to 'battle.Brick<creature.Creature>[][]
        bricks = (Brick<Creature>[][])new Brick[row][col];   // Unchecked assignment: 'battle.Brick[][]' to 'battle.Brick<creature.Creature>[][]'
        // actually, lose type information in run time --- erasure
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                bricks[i][j] = new Brick<>();
            }
        }
    }

    //void ShowField() {
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col-1; c++){
                //System.out.print(bricks[r][c] + " ");
                res.append(bricks[r][c]);
                res.append(" ");
            }
            //System.out.println(bricks[r][col-1]);
            res.append(bricks[r][col-1]);
            res.append("\n");
        }
        return res.toString();
    }

    public Brick<Creature> getBrick(int row, int col){
        if (0 <= row && row < this.row && 0 <= col && col < this.col)
            return bricks[row][col];
        return null;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}