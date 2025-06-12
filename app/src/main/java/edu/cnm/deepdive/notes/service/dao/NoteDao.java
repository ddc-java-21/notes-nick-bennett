package edu.cnm.deepdive.notes.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.notes.model.entity.Note;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;

@Dao
public interface NoteDao {

  @Insert
  Single<Long> _insert(Note note);

  default Single<Note> insert(Note note) {
    return Single
        .just(note)
        .doOnSuccess((n) -> {
          Instant now = Instant.now();
          n.setCreated(now).setModified(now);
        })
        .flatMap(this::_insert)
        .map(note::setId);
  }

  @Update
  Single<Integer> _update(Note note);

  default Single<Note> update(Note note) {
    return Single
        .just(note)
        .doOnSuccess((n) -> n.setModified(Instant.now()))
        .flatMap(this::_update)
        .map((count) -> note);
  }

  @Delete
  Single<Integer> delete(Note note);

  @Delete
  Single<Integer> delete(List<Note> notes);

  @Delete
  Single<Integer> delete(Note... notes);

  @Query("SELECT * FROM note WHERE note_id = :noteId")
  LiveData<Note> select(long noteId);

  @Query("SELECT * FROM note WHERE user_id = :userId ORDER BY created DESC")
  LiveData<List<Note>> selectWhereUserIdOrderByCreatedDesc(long userId);

  // TODO: 6/11/25 Add more queries as appropriate.

}
