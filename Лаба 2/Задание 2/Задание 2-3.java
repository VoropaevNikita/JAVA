package Task2;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class ThirdMethod {
    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy3.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFileUsingApacheCommonsIO(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
