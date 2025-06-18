package edu.cnm.deepdive.notes.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.notes.model.entity.Note;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import edu.cnm.deepdive.notes.service.dao.NoteDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NoteRepository {

  private final NoteDao noteDao;
  private final Scheduler scheduler;

  @Inject
  NoteRepository(NoteDao noteDao) {
    this.noteDao = noteDao;
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

  public Single<Note> save(NoteWithImages note) {
    // TODO: 6/17/25 Modify to insert/update images
    note.setUserId(1); // FIXME: 6/18/25 Replace after implementing Google Sign In.
    return (
        (note.getId() == 0)
            ? noteDao.insert(note)
            : noteDao.update(note)
    )
        .subscribeOn(scheduler);
  }

  public LiveData<List<NoteWithImages>> getAll() {
    return noteDao.selectWhereUserIdOrderByCreatedDesc(1); // FIXME: 6/17/25 Replace after implementing Google Sign In.
  }

}
