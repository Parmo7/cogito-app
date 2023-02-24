package uk.ac.aston.cogito.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.databinding.FragmentSavedBinding;
import uk.ac.aston.cogito.model.ConfigsViewModel;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    private RecyclerView recyclerView;
    private SavedListAdapter adapter;
    private ConfigsViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseModel();

        // Get a handle to the RecyclerView.
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // Create an adapter and supply the data to be displayed.
        adapter = new SavedListAdapter(requireActivity(), getContext(), model.getAllConfigs().getValue());
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(adapter);
        // Give the RecyclerView a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        binding.savedAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedFragmentDirections.ActionNavigationSavedToConfigSettingsFragment action =
                        SavedFragmentDirections.actionNavigationSavedToConfigSettingsFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initialiseModel() {
        model = new ViewModelProvider(requireActivity()).get(ConfigsViewModel.class);

        final Observer<List<SessionConfig>> latestConfigObserver = new Observer<List<SessionConfig>>() {
            @Override
            public void onChanged(@Nullable final List<SessionConfig> allConfigs) {
                adapter.updateData(allConfigs);
            }
        };
        model.getAllConfigs().observe(getViewLifecycleOwner(), latestConfigObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}