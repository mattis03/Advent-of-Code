import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        MemorySpace ms = new MemorySpace(ip);

        for (int i = 0; i < 1024; i++)
            ms.addByte();

        int b = 0;
        while (ms.solve() != Integer.MAX_VALUE)
            b = ms.addByte();

        int x = b & 0x7f;
        int y = b >>> 7;
        System.out.println(x + "," + y);
    }

    private static class MemorySpace {
        private final int SIZE = 71;
        private final boolean MAP[][] = new boolean[SIZE][SIZE];
        private final ArrayList<Integer> BYTE_QUEUE = new ArrayList<>();

        public MemorySpace(InputParser ip) {
            for (int y = 0; y < SIZE; y++)
                for (int x = 0; x < SIZE; x++)
                    MAP[x][y] = false;

            for (String s : ip.lines()) {
                String[] split = s.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                BYTE_QUEUE.add(x | (y << 7));
            }
        }

        public int addByte() {
            int b = BYTE_QUEUE.removeFirst();
            int x = b & 0x7f;
            int y = b >>> 7;
            MAP[x][y] = true;
            return b;
        }

        public int solve() {
            boolean visited[][] = new boolean[SIZE][SIZE];
            int distance[][] = new int[SIZE][SIZE];
            for (int y = 0; y < SIZE; y++)
            for (int x = 0; x < SIZE; x++) {
                visited[x][y] = false;
                distance[x][y] = Integer.MAX_VALUE;
            }
            
            Queue<Integer> queue = new LinkedList<>();
            queue.add(0);
            visited[0][0] = true;
            distance[0][0] = 0;
            while (!queue.isEmpty()) {
                int x = queue.remove();
                int y = x >>> 7;
                x &= 0x7f;

                if (x == 70 && y == 70)
                    break;

                bfs(x, y, queue, visited, distance);
            }
            return distance[70][70];
        }

        private void bfs(int x, int y, Queue<Integer> queue, boolean visited[][], int distance[][]) {
            if (x+1 >= 0 && x+1 < SIZE && !MAP[x+1][y] && !visited[x+1][y]) {
                queue.add((x+1) | (y << 7));
                visited[x+1][y] = true;
                distance[x+1][y] = distance[x][y] + 1;
            }
            if (y-1 >= 0 && y-1 < SIZE && !MAP[x][y-1] && !visited[x][y-1]) {
                visited[x][y-1] = true;
                queue.add(x | ((y-1) << 7));
                distance[x][y-1] = distance[x][y] + 1;
            }
            if (x-1 >= 0 && x-1 < SIZE && !MAP[x-1][y] && !visited[x-1][y]) {
                visited[x-1][y] = true;
                queue.add((x-1) | (y << 7));
                distance[x-1][y] = distance[x][y] + 1;
            }
            if (y+1 >= 0 && y+1 < SIZE && !MAP[x][y+1] && !visited[x][y+1]) {
                visited[x][y+1] = true;
                queue.add(x | ((y+1) << 7));
                distance[x][y+1] = distance[x][y] + 1;
            }
        }
    }
}