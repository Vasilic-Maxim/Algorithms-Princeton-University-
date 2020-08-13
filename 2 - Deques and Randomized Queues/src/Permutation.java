import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        if (k == 0) return;

        int i = 0;
        for (; i < k; i++) queue.enqueue(StdIn.readString());
        while (!StdIn.isEmpty()) {
            String in = StdIn.readString();
            int index = StdRandom.uniform(0, i + 1);
            if (index < k) {
                queue.dequeue();
                queue.enqueue(in);
            }

            i++;
        }

        for (String s : queue) System.out.println(s);
    }
}
