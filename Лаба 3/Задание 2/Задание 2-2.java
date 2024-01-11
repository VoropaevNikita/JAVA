package Task2;
import io.reactivex.rxjava3.core.Observable;
import java.util.stream.Stream;

public class Task222 {
    public static <T> Observable<T> streamToObservable(Stream<T> stream) {
        return Observable.fromIterable(stream::iterator);
    }

    public static void main(String[] args) {
        Stream<Integer> stream1 = Stream.iterate(1, (i) -> i + 1).limit(1000);
        Stream<Integer> stream2 = Stream.iterate(1001, i -> ++i).limit(1000);
        Observable<Integer> obs1 = streamToObservable(stream1);
        Observable<Integer> obs2 = streamToObservable(stream2);
        Observable.concat(obs1, obs2).subscribe(System.out::println);
    }
}
