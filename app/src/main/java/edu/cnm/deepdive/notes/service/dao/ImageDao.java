package edu.cnm.deepdive.notes.service.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.notes.model.entity.Image;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

@Dao
public interface ImageDao {

  @Insert
  Single<Long> _insert(Image image);

  default Single<Image> insert(Image image) {
    return Single
        .just(image)
        .doOnSuccess((img) -> img.setCreated(Instant.now()))
        .flatMap(this::_insert)
        .map(image::setId);
  }

  @Insert
  Single<List<Long>> _insert(List<Image> images);

  default Single<List<Image>> insert(List<Image> images) {
    return Single
        .just(images)
        .doOnSuccess((imgs) -> {
          Instant now = Instant.now();
          imgs.forEach((img) -> img.setCreated(now));
        })
        .flatMap(this::_insert)
        .map((ids) -> {
          Iterator<Long> idIterator = ids.iterator();
          Iterator<Image> imgIterator = images.iterator();
          while (idIterator.hasNext() && imgIterator.hasNext()) {
            imgIterator.next().setId(idIterator.next());
          }
          return images;
        });
  }

  @Update
  Single<Integer> update(Image image);

  @Delete
  Single<Integer> delete(Image image);

  @Delete
  Single<Integer> delete(List<Image> images);

  @Query("SELECT * FROM image WHERE image_id = :imageId")
  LiveData<Image> select(long imageId);

  @Query("SELECT * FROM image WHERE note_id = :noteId ORDER BY created ASC")
  LiveData<List<Image>> selectWhereNoteIdOrderByCreatedAsc(long noteId);

  // TODO: 6/11/25 Add queries as appropriate.

}
