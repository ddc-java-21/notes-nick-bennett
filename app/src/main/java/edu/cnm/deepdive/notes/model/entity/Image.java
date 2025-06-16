package edu.cnm.deepdive.notes.model.entity;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.time.Instant;

@Entity(
    tableName = "image",
    foreignKeys = {
        @ForeignKey(
            entity = Note.class,
            parentColumns = "note_id",
            childColumns = "note_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Image {

  /*
  C - Attach an image to a note
  R - Retrieve all images for a note
  U - Maybe update caption
  D - Remove image from note
   */

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "image_id")
  private long id;

  @ColumnInfo(collate = ColumnInfo.NOCASE)
  private String caption;

  @ColumnInfo(name = "mime_type", index = true)
  private String mimeType;

  /** @noinspection NotNullFieldNotInitialized*/
  @NonNull
  private Uri uri;

  @NonNull
  @ColumnInfo(index = true)
  private Instant created = Instant.now();

  @ColumnInfo(name = "note_id", index = true)
  private long noteId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;

  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;

  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;

  }

  @NonNull
  public Uri getUri() {
    return uri;
  }

  public void setUri(@NonNull Uri uri) {
    this.uri = uri;

  }

  @NonNull
  public Instant getCreated() {
    return created;
  }

  public void setCreated(@NonNull Instant created) {
    this.created = created;

  }

  public long getNoteId() {
    return noteId;
  }

  public void setNoteId(long noteId) {
    this.noteId = noteId;

  }
}
