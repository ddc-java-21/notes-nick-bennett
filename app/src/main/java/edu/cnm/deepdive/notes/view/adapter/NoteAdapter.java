package edu.cnm.deepdive.notes.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.notes.databinding.ItemNoteBinding;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@FragmentScoped
public class NoteAdapter extends Adapter<ViewHolder> {

  private final List<NoteWithImages> notes;
  private final LayoutInflater inflater;
  private final DateTimeFormatter formatter;

  @NonNull
  private OnNoteClickListener listener;

  @Inject
  NoteAdapter(@ActivityContext Context context) {
    notes = new ArrayList<>();
    inflater = LayoutInflater.from(context);
    formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    listener = (note, position) -> {};
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int itemType) {
    ItemNoteBinding binding = ItemNoteBinding.inflate(inflater, container, false);
    return new Holder(binding, formatter, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    ((Holder) viewHolder).bind(position, notes.get(position));
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  @SuppressLint("NotifyDataSetChanged")
  public void setNotes(List<NoteWithImages> notes) {
    this.notes.clear();
    this.notes.addAll(notes);
    notifyDataSetChanged();
  }

  public void setListener(@NonNull OnNoteClickListener listener) {
    this.listener = listener;
  }

  private static class Holder extends ViewHolder {

    private final ItemNoteBinding binding;
    private final DateTimeFormatter formatter;
    private final OnNoteClickListener listener;

    Holder(@NonNull ItemNoteBinding binding, @NonNull DateTimeFormatter formatter,
        @NonNull OnNoteClickListener listener) {
      super(binding.getRoot());
      this.binding = binding;
      this.formatter = formatter;
      this.listener = listener;
    }

    void bind(int position, NoteWithImages note) {
      binding.title.setText(note.getTitle());
      String description = note.getDescription();
      binding.description.setText(description != null ? description : "");
      binding.created.setText(
          formatter.format(ZonedDateTime.ofInstant(note.getCreated(), ZoneId.systemDefault())));
      binding.thumbnail.setVisibility(View.GONE);
      // TODO: 6/17/25 Display thumbnail.
      binding
          .getRoot()
          .setOnClickListener((v) -> listener.onNoteClick(note, position));
    }

  }

  public interface OnNoteClickListener {

    void onNoteClick(NoteWithImages note, int position);

  }

}
