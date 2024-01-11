package Task2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SecondMethod {
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String path = "src/Task2/100MB.txt";
        File f = new File(path);
        String path1 = "src/Task2/100MBcopy2.txt";
        File f1 = new File(path1);
        long start = System.nanoTime();
        copyFile(f, f1);
        System.out.println("Время копирования файла = " + (System.nanoTime() - start) + "\nИспользовано памяти: " + usedBytes);
    }
}
