package edu.cnm.deepdive.notes.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.notes.model.entity.User;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  Single<Long> _insert(User user);

  default Single<User> insert(User user) {
    return Single
        .just(user)
        .doOnSuccess((u) -> {
          Instant now = Instant.now();
          u.setCreated(now)
              .setModified(now);
        })
        .flatMap(this::_insert)
        .map(user::setId);
  }

  @Update
  Single<Integer> _update(User user);

  default Single<User> update(User user) {
    return Single
        .just(user)
        .doOnSuccess((u) -> u.setModified(Instant.now()))
        .flatMap(this::update)
        .map((count) -> user);
  }

  @Delete
  Single<Integer> delete(User user);

  @Query("SELECT * FROM user WHERE user_id = :userId")
  LiveData<User> select(long userId);

  @Query("SELECT * FROM user WHERE oauth_key = :oauthKey")
  Maybe<User> select(String oauthKey);

  @Query("SELECT * FROM user ORDER BY display_name ASC")
  LiveData<List<User>> select();

}
