package edu.cnm.deepdive.notes.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.notes.model.entity.Image;
import edu.cnm.deepdive.notes.model.entity.Note;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import edu.cnm.deepdive.notes.service.dao.ImageDao;
import edu.cnm.deepdive.notes.service.dao.NoteDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NoteRepository {

  private final NoteDao noteDao;
  private final ImageDao imageDao;
  private final Scheduler scheduler;

  @Inject
  NoteRepository(NoteDao noteDao, ImageDao imageDao) {
    this.noteDao = noteDao;
    this.imageDao = imageDao;
    scheduler = Schedulers.io();
  }

  public LiveData<NoteWithImages> get(long noteId) {
    return noteDao.select(noteId);
  }

  public Completable remove(Note note) {
    return noteDao
        .delete(note)
        .subscribeOn(scheduler)
        .ignoreElement();
  }

  public Single<NoteWithImages> save(NoteWithImages note) {
    note.setUserId(1); // FIXME: 6/18/25 Replace after implementing Google Sign In.
    return (
        (note.getId() == 0)
            ? noteDao.insert(note)
            : noteDao.update(note)
    )
        .doOnSuccess((n) -> note.getImages().forEach((img) -> img.setNoteId(n.getId())))
        .map((n) -> note.getImages())
        .flatMap(this::saveImages)
        .doOnSuccess((images) -> {
          note.getImages().clear();
          note.getImages().addAll(images);
        })
        .map((images) -> note)
        .subscribeOn(scheduler);
  }

  public LiveData<List<NoteWithImages>> getAll() {
    return noteDao.selectWhereUserIdOrderByCreatedDesc(
        1); // FIXME: 6/17/25 Replace after implementing Google Sign In.
  }

  private Single<List<Image>> saveImages(List<Image> images) {
    List<Single<Image>> singles = images
        .stream()
        .map((image) -> (image.getId() == 0)
            ? imageDao.insert(image)
            : imageDao.update(image)
                .map((count) -> image))
        .collect(Collectors.toList());
    return Single.just(singles)
        .flatMap((sngls) -> Single.zip(sngls, Arrays::asList))
        .map((imgObjects) -> imgObjects
            .stream()
            .map((imgObject) -> (Image) imgObject)
            .collect(Collectors.toList()));
  }
  
}
