package edu.cnm.deepdive.notes.controller;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.databinding.FragmentDetailsBinding;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import edu.cnm.deepdive.notes.viewmodel.NoteViewModel;

public class DetailsFragment extends Fragment {

  private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
      registerForActivityResult(new ActivityResultContracts.RequestPermission(), (granted) -> {
        if (granted) {
          // TODO: 6/17/25 Make camera capture control visible.
        } else {
          // TODO: 6/17/25 Make camera capture control gone.
        }
      });

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
    checkCameraPermission();
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void checkCameraPermission() {
    if (!hasCameraPermission()) {
      if (shouldExplainCameraPermission()) {
        explainCameraPermission();
      } else {
        requestCameraPermission();
      }
    } else {
      // TODO: 6/17/25 Make camera capture control visible.
    }
  }

  private void requestCameraPermission() {
    requestCameraPermissionLauncher.launch(permission.CAMERA);
  }

  private boolean hasCameraPermission() {
    return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED;
  }

  private boolean shouldExplainCameraPermission() {
    return ActivityCompat
        .shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA);
  }

  private void explainCameraPermission() {
    Snackbar.make(
        binding.getRoot(), R.string.camera_permission_explanation, Snackbar.LENGTH_INDEFINITE)
        .setAction(android.R.string.ok, (v) -> requestCameraPermission())
        .show();
  }

}
