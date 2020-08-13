import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private static class Node {
        private final Board board;
        private final Node parent;
        private final int moves;
        private final int priority;

        public Node(Board board, Node parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }

        public boolean visited(Board that) {
            if (parent == null) return false;
            return parent.board.equals(that);
        }

        public int compareTo(Node that) {
            int priorityDiff = Integer.compare(priority, that.priority);
            if (priorityDiff != 0) return priorityDiff;
            return -1 * Integer.compare(moves, that.moves);
        }
    }

    private final Stack<Board> solution = new Stack<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null instead of Board object");


        MinPQ<Node> mainPQ = new MinPQ<>(Node::compareTo);
        MinPQ<Node> twinPQ = new MinPQ<>(Node::compareTo);
        mainPQ.insert(new Node(initial, null, 0));
        twinPQ.insert(new Node(initial.twin(), null, 0));
        Node minPath = null;
        while (!mainPQ.isEmpty()) {
            Node node = mainPQ.delMin();
            Node twinNode = twinPQ.delMin();

            // if twin board is solvable then the main board cannot be solved
            if (twinNode.board.isGoal()) break;

            // if we reached the goal and current path is shorter then previous
            // one, then hold the current node
            if (node.board.isGoal()) {
                minPath = node;
                break;
            }

            // take all the possible neighbors and add them into mainPQ
            for (Board neighbor : node.board.neighbors()) {
                if (!node.visited(neighbor)) {
                    mainPQ.insert(new Node(neighbor, node, node.moves + 1));
                }
            }

            // take all the possible neighbors and add them into twinPQ
            for (Board neighbor : twinNode.board.neighbors()) {
                if (!twinNode.visited(neighbor)) {
                    twinPQ.insert(new Node(neighbor, twinNode, twinNode.moves + 1));
                }
            }
        }

        // path recovery
        Node node = minPath;
        while (node != null) {
            solution.push(node.board);
            node = node.parent;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return !solution.isEmpty();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
