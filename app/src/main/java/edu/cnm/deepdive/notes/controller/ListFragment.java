package edu.cnm.deepdive.notes.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.notes.databinding.FragmentListBinding;
import edu.cnm.deepdive.notes.view.adapter.NoteAdapter;
import edu.cnm.deepdive.notes.viewmodel.LoginViewModel;
import edu.cnm.deepdive.notes.viewmodel.NoteViewModel;
import javax.inject.Inject;

@AndroidEntryPoint
public class ListFragment extends Fragment implements MenuProvider {

  @Inject
  NoteAdapter adapter;

  private FragmentListBinding binding;
  private NoteViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentListBinding.inflate(inflater, container, false);
    binding.notes.setAdapter(adapter);
    adapter.setListener((note, position) -> Navigation.findNavController(binding.getRoot())
        .navigate(ListFragmentDirections.showDetails(note.getId())));
    binding.addNote.setOnClickListener((v) -> Navigation.findNavController(binding.getRoot())
        .navigate(ListFragmentDirections.showDetails(0)));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    ViewModelProvider provider = new ViewModelProvider(activity);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel = provider.get(NoteViewModel.class);
    viewModel
        .getNotes()
        .observe(owner, adapter::setNotes);
    LoginViewModel loginViewModel = provider.get(LoginViewModel.class);
    loginViewModel
        .getAccount()
            .observe(owner, (account) -> {
              if (account == null) {
                Navigation.findNavController(binding.getRoot())
                    .navigate(ListFragmentDirections.showPreLogin());
              }
            });
    activity.addMenuProvider(this, owner, State.RESUMED);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    // TODO: 6/16/25 Inflate a menu resource, attaching the inflated items to the specified menu.
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    // TODO: 6/16/25 Check the ID of menuItem, to see if it is of interest to us; if so, perform
    //  appropriate operations and return true; otherwise, return false.
    return false;
  }

}
