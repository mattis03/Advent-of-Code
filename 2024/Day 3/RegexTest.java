import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.regex.*;

public class RegexTest {
    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("input"));
        String in = "";
        while (sc.hasNext())
            in += sc.next();
        sc.close();

        Pattern p = Pattern.compile("mul\\((-?\\d+),\s*(-?\\d+)\\)");
        Matcher m = p.matcher(in);

        while (m.find())
            System.out.println(in.substring(m.start(), m.end()));
    }
}
