package Task3;
import java.util.Objects;

public class UserFriend {
    int userId, friendId;
    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserFriend {" + "userId=" + userId + ", friendId=" + friendId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFriend that = (UserFriend) o;
        return userId == that.userId && friendId == that.friendId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
