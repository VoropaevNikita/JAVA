package Task4;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        Observer<File> queue = main.getQueue();
        Observable<File> generator = getGenerator();
        generator.subscribe(queue);
    }

    public static class FileProcessing implements Runnable{
        File file;
        FileProcessing(File file){
            this.file = file;
            run();
        }

        @Override
        public void run() {
            try{
                Integer time = file.size*7;
                Thread.sleep(time);
                System.out.println("Файл " + file.name + " обработан за " + time + " мс");
                System.out.println("Файл " + file.name + " удален из очереди");
            }
            catch (Exception e){}
        }
    }

        Observer<File> getQueue() {
            return new Observer<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {}

                @Override
                public void onNext(@NonNull File file) {
                    Main.FileProcessing fileProcessing = new FileProcessing(file);
                    new Thread(fileProcessing).start();
                }

                @Override
                public void onError(Throwable throwable) {}
                @Override
                public void onComplete() {}
            };
        }

    public static class FileQueue {
        Queue<File> queue = new LinkedList<>();
        public synchronized void put(File file) {
            System.out.println("Файл " + file.name + " добавлен в очередь");
            queue.add(file);
        }
    }

    public static Observable<File> getGenerator(){
        FileQueue fileQueue = new FileQueue();
        String[] typeList={"XML", "JSON", "XLS"};
        Queue<String> nameList = new LinkedList<String>();
        return Observable.create(subscriber -> {
                    Runnable r = () -> {
                        while (true){
                            for (int i = 0; i < 7; i++) {
                                nameList.add("file" + i);
                            }
                            Integer time = (int) (Math.random() * 900 + 100);
                            try {
                                Thread.sleep(time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String type = typeList[(int) (Math.random() * 2)];
                            Integer size = (int) (Math.random() * 900 + 100);
                            File file = new File(nameList.poll(), type, size);
                            System.out.println(file.name + "." + file.type + " (" + file.size + " байт)"
                                    + " создан за " + time + " мс");
                            fileQueue.put(file);
                            subscriber.onNext(file);
                        }
                    };
                    new Thread(r).start();});
    }
}
