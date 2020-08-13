import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k == 0) return;

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        int count = 0;
        for (; count < k; count++) queue.enqueue(StdIn.readString());

        while (!StdIn.isEmpty()) {
            String in = StdIn.readString();
            int index = StdRandom.uniform(count + 1);
            if (index < k) {
                queue.dequeue();
                queue.enqueue(in);
            }
            count++;
        }

        for (String s : queue) System.out.println(s);
    }
}
