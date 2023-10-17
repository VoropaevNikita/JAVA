package pr_1.task1;
//Метод для последовательной обработки массива
class ImperativeExample {
    public static void main() {
        UtilsRskp uRskp = new UtilsRskp();

        int[] array = UtilsRskp.getArr();

        try {
            uRskp.startClock();

            int finalMin = 0;
            for (int i = 0; i < array.length; i++) {
                if (i == 0) 
                    finalMin = array[i];
                else
                    if (finalMin > array[i])
                        finalMin = array[i];
                Thread.sleep(1);
            }

            uRskp.finishClock();

            System.out.println("---Imperative style---");
            System.out.println("Min: "+finalMin);
            System.out.println("Time: "+uRskp.getClockDiff()+"ms.");
        
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }    }
}
//Метод для обработки массива при помощи многопоточности
public class ThreadExample {
    public static void main() {
        MyThread myThread = new MyThread("myThread");
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
//Метод для обработки массива при помощи ForkJoin
public class ForkJoinExample {
    public static void main() {
        ValueMinCounter counter = new ValueMinCounter(UtilsRskp.getArr());
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        UtilsRskp uRskp = new UtilsRskp();

        uRskp.startClock();
        int finalMin = forkJoinPool.invoke(counter);
        uRskp.finishClock();
        System.out.println("---ForkJoin style---");
        System.out.println("Min: "+finalMin);
        System.out.println("Time: "+uRskp.getClockDiff()+"ms.");
    }
}
//Класс MyThread
class MyThread extends Thread{
    MyThread(String name){
        super(name);
    }

    public void run(){
        UtilsRskp uRskp = new UtilsRskp();

        int[] array = UtilsRskp.getArr();

        try {
            uRskp.startClock();

            int finalMin = 0;
            for (int i = 0; i < array.length; i++) {
                if (i == 0)
                    finalMin = array[i];
                else if (finalMin > array[i])
                    finalMin = array[i];
                Thread.sleep(1);
            }

            uRskp.finishClock();

            System.out.println("---Thread style---");
            System.out.println("Min: "+finalMin);
            System.out.println("Time: "+uRskp.getClockDiff()+"ms.");
        
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
//Класс ValueMinCounter
class ValueMinCounter extends RecursiveTask<Integer> {

    private int[] array;

    public ValueMinCounter(int[] array) {
        this.array = array;
    }

    @SneakyThrows
    @Override
    protected Integer compute()  {
        if(array.length <= 2) {
            Thread.sleep(1);
            return Arrays.stream(array).min().getAsInt();
        }
        ValueMinCounter firstHalfArrayValueMinCounter = new ValueMinCounter(Arrays.copyOfRange(array, 0, array.length/2));
        ValueMinCounter secondHalfArrayValueMinCounter = new ValueMinCounter(Arrays.copyOfRange(array, array.length/2, array.length));
        firstHalfArrayValueMinCounter.fork();
        secondHalfArrayValueMinCounter.fork();
        if (firstHalfArrayValueMinCounter.join() > secondHalfArrayValueMinCounter.join())
            return secondHalfArrayValueMinCounter.get();
        else
            return firstHalfArrayValueMinCounter.get();
    }
}


