package edu.cnm.deepdive.notes.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.notes.model.entity.Image;
import edu.cnm.deepdive.notes.model.entity.Note;
import java.util.LinkedList;
import java.util.List;

public class NoteWithImages extends Note {

  @Relation(entity = Image.class, parentColumn = "note_id", entityColumn = "note_id")
  private List<Image> images = new LinkedList<>();

  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }

}
