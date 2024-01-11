package Task4;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class Task4 {
    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("src/catalog");
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        boolean poll = true;
        while (poll) {
            WatchKey key;
            System.out.println("Start monitoring...");
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    var new_path = event.context();
                    byte[] bytes = Files.readAllBytes(Paths.get("src/catalog/"+ new_path));
                    String fileContent = new String(bytes);
                    System.out.println(event.kind() + ": " + event.context());
                    if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {
                        byte[] bytes1 = Files.readAllBytes(Paths.get("src/catalog/"+ new_path));
                        String fileContent1 = new String(bytes1);
                        if (fileContent.length() < fileContent1.length()) {     System.out.println(StringUtils.difference(fileContent, fileContent1));
                        } else {     System.out.println(StringUtils.difference(fileContent1, fileContent));
                        }
                    }
                        if (event.kind().name().equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {
FileInputStream fis = new FileInputStream("src/catalog/"+new_path);
                            FileChannel fc = fis.getChannel();
                            int sz = (int) fc.size();
                            System.out.println("Checksum: " + CalculateSum("src/catalog/"+ new_path));
                        }
                }
                poll = key.reset();
            }
        }
    }

    public static int CalculateSum(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fis.read(arr);
            ByteBuffer bb = ByteBuffer.wrap(arr);
            int sum = 0;
            while (bb.hasRemaining()) {
                sum += bb.getShort();
            }
            fis.close();
            return sum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
