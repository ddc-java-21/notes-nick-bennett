package edu.cnm.deepdive.notes.service;

import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.notes.model.entity.Note;
import edu.cnm.deepdive.notes.service.dao.NoteDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
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

  public LiveData<Note> get(long noteId) {
    return noteDao.select(noteId);
  }

  public Completable remove(Note note) {
    return noteDao
        .delete(note)
        .subscribeOn(scheduler)
        .ignoreElement();
  }

  public Single<Note> save(Note note) {
    return (
        (note.getId() == 0)
            ? noteDao.insert(note)
            : noteDao.update(note)
    )
        .subscribeOn(scheduler);
  }

}
