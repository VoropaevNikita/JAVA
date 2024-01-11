package Task1;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task1 {
    public static void main(String[] args) throws IOException{
        String path = "src/Task1/file.txt";
        Path filePath = Paths.get(path);
        Files.createFile(filePath);
        String str = "Hello! This file was made by Vi for task1.\nHope program is working well";
        byte[] fileSize = str.getBytes();
        Path writtenFilePath = Files.write(filePath, fileSize);
        System.out.println("File content:\n"+ new String(Files.readAllBytes(writtenFilePath)));}
