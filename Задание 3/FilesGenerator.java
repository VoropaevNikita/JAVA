//Класс FilesGenerator
package pr_1.task3;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FilesGenerator {
    final Consumer<MyFile> fileConsumer;

    FilesGenerator(Consumer<MyFile> fileConsumer){
        this.fileConsumer = fileConsumer;
    }   
    
    public Stream<MyFile> generateFiles(){
        ArrayList<MyFile> userNameSource = new ArrayList<>();
        Runnable task = () -> {
            int i = 0;
            while (true){
                Random r = new Random();
                try {
                    final int timeToSleep = r.nextInt(900)+100;
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                final MyFile newFile = new MyFile(FileType.values()[r.nextInt(3)], r.nextInt(90)+10, i);
                userNameSource.add(newFile);

                // Notify listener
                CompletableFuture.completedFuture(newFile).thenAccept(fileConsumer);
                i+=1;
            }
        };
        new Thread(task).start();
        return userNameSource.stream();
    }
   
}
//Класс FileProcessor
package pr_1.task3;

public class FileProcessor {
    public void process(MyFile file){
        System.out.println("Начало обработки файла " + file.type + " размером " + file.size + " с индексом " + file.index);
        try {
            Thread.sleep(file.size*70);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Закончена обработка файла " + file.type + " размером " + file.size + " с индексом " + file.index);
    }
}
