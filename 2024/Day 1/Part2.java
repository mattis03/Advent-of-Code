import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part2 {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("input"));
        LinkedList<Integer> Llist = new LinkedList<>();
        LinkedList<Integer> Rlist = new LinkedList<>();
        while (sc.hasNext()) {
            Llist.add(sc.nextInt());
            Rlist.add(sc.nextInt());
        }
        sc.close();

        int similSum = 0;
        for (int i = 0; i < Llist.size(); i++) {
            int n = Llist.get(i);
            int count = 0;

            for (Integer elem : Rlist)
                if (elem == n) count++;
            
            similSum += n * count;
        }

        System.out.println(similSum);
    }
}