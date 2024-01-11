package Task2;
import java.io.*;

public class FirstMethod {
    private static void copyFile(File source, File dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy1.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFile(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
