package Task2;
import io.reactivex.rxjava3.core.Observable;
import java.util.stream.Stream;

public class Task212 {
    public static <T> Observable<T> streamToObservable(Stream<T> stream) {
        return Observable.fromIterable(stream::iterator);
    }

    public static void main(String[] args) {
        Stream<Integer> stream = Stream.iterate(0, (i)->i+1).limit(1001);
        Observable<Integer> obs = streamToObservable(stream);
        obs.subscribe((v) -> {
            if(v>500)
                System.out.println("Values > 500:"+ v);
        });
    }
}
