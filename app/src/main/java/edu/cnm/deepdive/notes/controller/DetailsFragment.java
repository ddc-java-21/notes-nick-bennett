package edu.cnm.deepdive.notes.controller;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.TakePicture;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.databinding.FragmentDetailsBinding;
import edu.cnm.deepdive.notes.model.pojo.NoteWithImages;
import edu.cnm.deepdive.notes.service.ImageFileProvider;
import edu.cnm.deepdive.notes.view.adapter.ImageAdapter;
import edu.cnm.deepdive.notes.viewmodel.NoteViewModel;
import edu.cnm.deepdive.notes.viewmodel.NoteViewModel.VisibilityFlags;
import java.io.File;
import java.util.UUID;

public class DetailsFragment extends Fragment {

  private static final String TAG = DetailsFragment.class.getSimpleName();
  private static final String AUTHORITY = ImageFileProvider.class.getName().toLowerCase();

  private FragmentDetailsBinding binding;
  private NoteViewModel viewModel;
  private long noteId;
  private NoteWithImages note;
  private ActivityResultLauncher<String> requestCameraPermissionLauncher;
  private ActivityResultLauncher<Uri> takePictureLauncher;

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
    binding.edit.setOnClickListener((v) -> viewModel.setEditing(true));
    binding.save.setOnClickListener((v) -> {
      boolean addObserver = (note.getId() == 0);
      note.setTitle(binding.titleEditable.getText().toString().strip());
      String description = binding.descriptionEditable.getText().toString().strip();
      note.setDescription(description.isEmpty() ? null : description);
      viewModel.save(note);
      viewModel.setEditing(false);
      if (addObserver) {
        viewModel
            .getNote()
            .observe(getViewLifecycleOwner(), this::handleNote);
      }
    });
    binding.cancel.setOnClickListener((v) -> {
      // TODO: 6/18/25 Discard changes, return note field to its original state.
      viewModel.setEditing(false);
    });
    binding.addPhoto.setOnClickListener((v) -> capture());
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
          .observe(owner, this::handleNote);
    } else {
      note = new NoteWithImages();
      handleNote(note);
      viewModel.setEditing(true);
    }
    viewModel
        .getImages()
        .observe(owner, (images) -> {
          ImageAdapter adapter = new ImageAdapter(requireContext(), images);
          binding.images.setAdapter(adapter);
        });
    viewModel
        .getVisibilityFlags()
        .observe(owner, this::handleVisibilityFlags);
    requestCameraPermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        viewModel::setCameraPermission);
    takePictureLauncher = registerForActivityResult(new TakePicture(), viewModel::confirmCapture);
    checkCameraPermission();
  }

  private void handleVisibilityFlags(VisibilityFlags flags) {
    if (flags.editing()) {
      binding.staticContent.setVisibility(View.GONE);
      binding.editableContent.setVisibility(View.VISIBLE);
      binding.edit.setVisibility(View.GONE);
      binding.addPhoto.setVisibility(flags.cameraPermission() ? View.VISIBLE : View.GONE);
      binding.save.setVisibility(View.VISIBLE);
      binding.cancel.setVisibility(View.VISIBLE);
    } else {
      binding.staticContent.setVisibility(View.VISIBLE);
      binding.editableContent.setVisibility(View.GONE);
      binding.edit.setVisibility(View.VISIBLE);
      binding.addPhoto.setVisibility(View.GONE);
      binding.save.setVisibility(View.GONE);
      binding.cancel.setVisibility(View.GONE);
    }
  }

  private void handleNote(NoteWithImages note) {
    this.note = note;
    noteId = note.getId();
    binding.titleStatic.setText(note.getTitle());
    binding.titleEditable.setText(note.getTitle());
    binding.descriptionStatic.setText(note.getDescription());
    binding.descriptionEditable.setText(note.getDescription());
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
      viewModel.setCameraPermission(true);
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

  private void capture() {
    Context context = requireContext();
    File captureDir = new File(context.getFilesDir(), getString(R.string.capture_directory));
    //noinspection ResultOfMethodCallIgnored
    captureDir.mkdir();
    File captureFile;
    do {
      captureFile = new File(captureDir, UUID.randomUUID().toString());
    } while (captureFile.exists());
    Uri uri = FileProvider.getUriForFile(context, AUTHORITY, captureFile);
    viewModel.setPendingCaptureUri(uri);
    takePictureLauncher.launch(uri);
  }

}
