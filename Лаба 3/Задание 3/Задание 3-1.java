package Task3;
import io.reactivex.rxjava3.core.Observable;
import java.util.*;
import java.util.stream.Collectors;

public class Task3 {
    private static int min = 1;
    private static int max = 10;
    private static ArrayList<UserFriend> userFriends = new ArrayList<>();

    public static void main(String[] args) {
        List<UserFriend> userFriendSet = new ArrayList<UserFriend>();
        for (int i = 0; i < 100; i++) {
            int newUserId = (int) Math.round(Math.random() * (max - min) + min);
            int newFriendId = (int) Math.round(Math.random() * (max - min) + min);
            if (newFriendId != newUserId) {
                userFriendSet.add(new UserFriend(newUserId, newFriendId));
            }
        }
        userFriends = new ArrayList<>(userFriendSet);
        getObservableUserId()
                .map(userId -> {
                    System.out.println("FRIENDS OF USER WITH UserId=" + userId+ ":");
                    return getFriends(userId);
                })
                .subscribe(userFriendsObservable -> {
userFriendsObservable.forEach(System.out::println);
                    System.out.println("\n");
                });
    }

    private static Observable<Integer> getObservableUserId() {
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] idSet = new Integer[ids.length];
        Arrays.setAll(idSet, i -> ids[i]);
        return Observable.fromArray(idSet);
    }

    private static Observable<UserFriend> getFriends(int userId) {
        List<UserFriend> listFilteredUserFriend = userFriends
                .stream()
                .filter(userFriend -> userFriend.getUserId() == userId)
                .collect(Collectors.toList());
        List<UserFriend> listWithoutDuplicates = listFilteredUserFriend.stream()
                .distinct()
                .collect(Collectors.toList());
        UserFriend[] arrFilteredUserFriend = new UserFriend[100];
        return Observable.fromArray(listWithoutDuplicates.toArray(arrFilteredUserFriend));
    }
}
