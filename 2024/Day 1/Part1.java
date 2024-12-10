import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Part1 {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("input"));
        LinkedList<Integer> Llist = new LinkedList<>();
        LinkedList<Integer> Rlist = new LinkedList<>();
        while (sc.hasNext()) {
            Llist.add(sc.nextInt());
            Rlist.add(sc.nextInt());
        }
        sc.close();

        Llist.sort(null);
        Rlist.sort(null);

        int distSum = 0;
        for (int i = 0; i < Llist.size(); i++)
            distSum += Math.abs(Llist.get(i) - Rlist.get(i));

        System.out.println(distSum);
    }
}