package edu.cnm.deepdive.notes.service.util;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.google.gson.Gson;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.model.entity.Note;
import edu.cnm.deepdive.notes.model.entity.User;
import edu.cnm.deepdive.notes.model.pojo.UserWithNotes;
import edu.cnm.deepdive.notes.service.dao.NoteDao;
import edu.cnm.deepdive.notes.service.dao.UserDao;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.inject.Inject;

public class Preloader extends RoomDatabase.Callback {

  private final Context context;
  private final UserDao userDao;
  private final NoteDao noteDao;

  @Inject
  Preloader(@ApplicationContext Context context, UserDao userDao, NoteDao noteDao) {
    this.context = context;
    this.userDao = userDao;
    this.noteDao = noteDao;
  }

  @Override
  public void onCreate(@NonNull SupportSQLiteDatabase db) {
    super.onCreate(db);
    try (
        InputStream input = context.getResources().openRawResource(R.raw.preload);
        Reader reader = new InputStreamReader(input)
    ) {
      Gson gson = new Gson();
      UserWithNotes user = gson.fromJson(reader, UserWithNotes.class);
      Scheduler scheduler = Schedulers.io();
      userDao
          .insert(user)
          .doOnSuccess((u) -> {
            long userId = u.getId();
            user.getNotes().forEach((n) -> n.setUserId(userId));
          })
          .flatMap((u) -> noteDao.insert(user.getNotes()))
          .subscribeOn(scheduler)
          .subscribe();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
