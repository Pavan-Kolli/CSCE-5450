import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) 
	{
        
        Puzzle b = new Puzzle();

        ArrayList<Puzzle.Scope> initial = new ArrayList<Puzzle.Scope>();

        initial.add(new Puzzle.Scope(0,0));
        initial.add(new Puzzle.Scope(1,0));
        initial.add(new Puzzle.Scope(1,1));
        initial.add(new Puzzle.Scope(2,0));
        initial.add(new Puzzle.Scope(2,1));

        initial.forEach(begin -> {
            System.out.println("\n--- " + begin + " ---");
            b.initialize(5, begin);
            ArrayList<Puzzle.Manoeuvre> bs = b.solutionPath();
            System.out.println();
            b.printLocation(b.board);
            bs.forEach(move -> {
                System.out.println("\n" + move + "\n");
                b.move(move);
                b.printLocation(b.board);
            });
        });
    }
}