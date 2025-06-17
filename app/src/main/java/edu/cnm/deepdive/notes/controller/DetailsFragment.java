package edu.cnm.deepdive.notes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.notes.databinding.FragmentDetailsBinding;
import edu.cnm.deepdive.notes.model.entity.Note;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import edu.cnm.deepdive.notes.viewmodel.NoteViewModel;

public class DetailsFragment extends Fragment {

  private FragmentDetailsBinding binding;
  private NoteViewModel viewModel;
  private long noteId;
  private NoteWithImages note;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    noteId = DetailsFragmentArgs.fromBundle(getArguments()).getNoteId();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentDetailsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LifecycleOwner owner = getViewLifecycleOwner();
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    viewModel = provider.get(NoteViewModel.class);
    if (noteId != 0) {
      viewModel.setNoteId(noteId);
      viewModel
          .getNote()
          .observe(owner, (note) -> {
            this.note = note;
            // TODO: 6/17/25 Set contents of view widgets based on note.
          });
    } else {
      note = new NoteWithImages();
    }
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
