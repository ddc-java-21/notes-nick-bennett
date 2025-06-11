package edu.cnm.deepdive.notes.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.notes.service.NotesDatabase;
import edu.cnm.deepdive.notes.service.dao.ImageDao;
import edu.cnm.deepdive.notes.service.dao.NoteDao;
import edu.cnm.deepdive.notes.service.dao.UserDao;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

  @Provides
  @Singleton
  NotesDatabase provideDatabase(@ApplicationContext Context context) { // TODO: 6/11/25 Add preloader if necesssary.
    return Room.databaseBuilder(context, NotesDatabase.class, NotesDatabase.getName())
//        .addCallback()
        .build();
  }

  @Provides
  @Singleton
  UserDao provideUserDao(NotesDatabase database) {
    return database.getUserDao();
  }

  @Provides
  @Singleton
  NoteDao provideNoteDao(NotesDatabase database) {
    return database.getNoteDao();
  }

  @Provides
  @Singleton
  ImageDao provideImageDao(NotesDatabase database) {
    return database.getImageDao();
  }

}
