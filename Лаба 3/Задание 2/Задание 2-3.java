package Task2;
import io.reactivex.rxjava3.core.Observable;
import java.util.Random;
import java.util.stream.Stream;

public class Task232 {
    public static <T> Observable<T> streamToObservable(Stream<T> stream) {
        return Observable.fromIterable(stream::iterator);
    }

    public static void main(String[] args) {
        //Stream<Integer> stream = Stream.generate(() -> (new Random()).nextInt(100)).limit(10);
        Stream<Integer> stream = Stream.iterate(0, (i)->i+1).limit(10);
        Observable<Integer> obs1 = streamToObservable(stream);
        obs1.take(4).subscribe(System.out::println);
    }
}
