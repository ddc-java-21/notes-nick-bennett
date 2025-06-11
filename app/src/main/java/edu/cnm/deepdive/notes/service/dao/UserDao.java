package edu.cnm.deepdive.notes.service.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import edu.cnm.deepdive.notes.model.entity.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  Single<Long> insert(User user); // When should we use user.setId(...) to update the object in memory.

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(List<User> users);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(User... users);

  @Update
  Completable update(User user);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  Single<Integer> update(List<User> users);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  Single<Integer> update(User... users);


}
