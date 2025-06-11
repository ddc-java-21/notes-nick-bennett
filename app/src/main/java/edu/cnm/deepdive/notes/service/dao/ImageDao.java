package edu.cnm.deepdive.notes.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.notes.model.entity.Image;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface ImageDao {

  @Insert
  Single<Long> insert(Image image);

  @Insert
  Single<List<Long>> insert(List<Image> images);

  @Insert
  Single<List<Long>> insert(Image... images);

  @Update
  Completable update(Image image);

  @Delete
  Completable delete(Image image);

  @Delete
  Completable delete(List<Image> images);

  @Delete
  Completable delete(Image... images);

  @Query("SELECT * FROM image WHERE image_id = :imageId")
  LiveData<Image> select(long imageId);

  @Query("SELECT * FROM image WHERE note_id = :noteId ORDER BY created ASC")
  LiveData<List<Image>> selectWhereNoteIdOrderByCreatedAsc(long noteId);

  // TODO: 6/11/25 Add queries as appropriate.

}
