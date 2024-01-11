package Task2;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FourthMethod {
    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy4.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFile(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
