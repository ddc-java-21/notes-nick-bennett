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

  public Image setId(long id) {
    this.id = id;
    return this;
  }

  public String getCaption() {
    return caption;
  }

  public Image setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Image setMimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  @NonNull
  public Uri getUri() {
    return uri;
  }

  public Image setUri(@NonNull Uri uri) {
    this.uri = uri;
    return this;
  }

  @NonNull
  public Instant getCreated() {
    return created;
  }

  public Image setCreated(@NonNull Instant created) {
    this.created = created;
    return this;
  }

  public long getNoteId() {
    return noteId;
  }

  public Image setNoteId(long noteId) {
    this.noteId = noteId;
    return this;
  }
}
