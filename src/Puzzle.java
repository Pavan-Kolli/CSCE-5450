import java.util.ArrayList;

import static java.lang.Math.*;

public class Puzzle {
    boolean[][] board;
    int pegsRemaining;
    int anInt;

    public static char charFromBool(Boolean b) {
        return !b ? '.' : 'x';
    }

    public void printLocation(boolean[][] board) {
        for (int i = 0; i < anInt; i++) {
            System.out.print("  ");
            for (int k = 0; k < (anInt - i - 1); k++) {
                System.out.print(" ");
            }
            for (int j = 0; j <= i; j++) {
                System.out.print(Puzzle.charFromBool(board[i][j]));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void initialize(int n, Scope h) {
        anInt = n;
        board = new boolean[n][n];
        pegsRemaining = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                board[i][j] = true;
                pegsRemaining++;
            }
        }

        board[h.x][h.y] = false;
    }

    public boolean move(Manoeuvre m) {
        if (!m.getValidity(this)) {
            System.out.println("Invalid move.");
            return false;
        }

        board[m.start.x][m.start.y] = false;

        board[m.end.x][m.end.y] = true;

        board[(m.start.x + m.end.x) / 2][(m.start.y + m.end.y) / 2] = false;

        pegsRemaining--;

        return true;
    }

    public void undoMove(Manoeuvre m) {

        board[m.start.x][m.start.y] = true;

        board[m.end.x][m.end.y] = false;

        board[(m.start.x + m.end.x) / 2][(m.start.y + m.end.y) / 2] = true;

        pegsRemaining++;
    }

    public ArrayList<Manoeuvre> movesFromLoc(Scope start) {
        ArrayList<Manoeuvre> temp = new ArrayList<Manoeuvre>();

        if (!start.isScopeValid(anInt)) {
            return temp;
        }

        if (!board[start.x][start.y]) {
            return temp;
        }

        Manoeuvre possible = new Manoeuvre(start, new Scope(start.x - 2, start.y));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        possible = new Manoeuvre(start, new Scope(start.x - 2, start.y - 2));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        possible = new Manoeuvre(start, new Scope(start.x, start.y - 2));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        possible = new Manoeuvre(start, new Scope(start.x, start.y + 2));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        possible = new Manoeuvre(start, new Scope(start.x + 2, start.y));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        possible = new Manoeuvre(start, new Scope(start.x + 2, start.y + 2));

        if (possible.getValidity(this)) {
            temp.add(possible);
        }

        return temp;
    }

    public ArrayList<Manoeuvre> validMoves() {
        Scope tmpScope;
        ArrayList<Manoeuvre> scopeMoves;
        ArrayList<Manoeuvre> tmpMoves = new ArrayList<Manoeuvre>();

        for (int i = 0; i < anInt; i++) {
            for (int j = 0; j <= i; j++) {
                tmpScope = new Scope(i, j);
                scopeMoves = this.movesFromLoc(tmpScope);
                tmpMoves.addAll(scopeMoves);
            }
        }

        return tmpMoves;
    }

    public ArrayList<Manoeuvre> solutionPath() {
        ArrayList<Manoeuvre> p = new ArrayList<Manoeuvre>();
        ArrayList<Manoeuvre> manoeuvres = this.validMoves();

        if (manoeuvres.isEmpty()) {
            return p;
        }

        for (int i = 0; i < manoeuvres.size(); i++) {
            this.move(manoeuvres.get(i));

            // Win condition
            if (pegsRemaining == 1) {
                p.add(manoeuvres.get(i));
                this.undoMove(manoeuvres.get(i));

                return p;
            }

            // Recurse
            ArrayList<Manoeuvre> movePath = this.solutionPath();

            if (movePath.size() + 1 > p.size()) {
                p.clear();
                p.add(manoeuvres.get(i));
                p.addAll(movePath);
            }

            this.undoMove(manoeuvres.get(i));
        }

        return p;
    }

    public static class Scope {
        int x, y;

        public Scope(int r, int c) {
            this.x = r;
            this.y = c;
        }

        public String toString() {
            return "[" + x + "," + y + "]";
        }

        public boolean isScopeValid(int board_size) {
            return (x >= 0) && (x < board_size) && (y >= 0) && (y <= x);
        }
    }

    public static class Manoeuvre {
        Scope start;
        Scope end;

        public Manoeuvre(Scope start, Scope end) {
            this.start = start;
            this.end = end;
        }

        public String toString() {
            return "from " + start.toString() + " to " + end.toString();
        }

        public boolean getValidity(Puzzle board) {

            if (!start.isScopeValid(board.anInt) || !end.isScopeValid(board.anInt))
                return false;
            if (!board.board[start.x][start.y] || board.board[end.x][end.y]) {
                return false;
            }

            int rowj = abs(start.x - end.x);
            int colj = abs(start.y - end.y);

            if (rowj == 0) {
                if (colj != 2) {
                    return false;
                }
            } else if (rowj == 2) {
                if (colj != 0 && colj != 2) {
                    return false;
                }
            } else {
                return false;
            }

            return board.board[(start.x + end.x) / 2][(start.y + end.y) / 2];
        }
    }
}