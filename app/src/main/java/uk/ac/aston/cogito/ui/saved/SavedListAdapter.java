package uk.ac.aston.cogito.ui.saved;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.ConfigsViewModel;
import uk.ac.aston.cogito.model.entities.SessionConfig;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.SavedViewHolder> {

        private List<SessionConfig> savedList;
        private final LayoutInflater inflater;
        private ConfigsViewModel model;

        public SavedListAdapter(FragmentActivity callingActivity, Context context, List<SessionConfig> savedList) {
            inflater = LayoutInflater.from(context);
            this.savedList = savedList;

            model = new ViewModelProvider(callingActivity).get(ConfigsViewModel.class);
        }

        @NonNull
        @Override
        public SavedListAdapter.SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mItemView = inflater.inflate(R.layout.config_item, parent, false);
            return new SavedViewHolder(mItemView, this);
        }

        @Override
        public void onBindViewHolder(@NonNull SavedListAdapter.SavedViewHolder holder, int position) {
            SessionConfig config = savedList.get(position);

            holder.config = config;
            holder.nameTextView.setText(config.getName());
            holder.durationTextView.setText(String.valueOf(config.getDuration()) + " min");

            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setLatestConfig(config);

                    SavedFragmentDirections.ActionNavigationSavedToSession action = SavedFragmentDirections.actionNavigationSavedToSession(config);
                    Navigation.findNavController(view)
                            .navigate(action);
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.savedList.size();
        }

        public void updateData(List<SessionConfig> list) {
            this.savedList = list;
            notifyDataSetChanged();
        }

        class SavedViewHolder extends RecyclerView.ViewHolder {

            final SavedListAdapter adapter;
            public SessionConfig config;

            public final TextView nameTextView;
            public final TextView durationTextView;
            public final FrameLayout playBtn;

            public SavedViewHolder(@NonNull View itemView, SavedListAdapter adapter) {
                super(itemView);

                nameTextView = itemView.findViewById(R.id.config_item_name);
                durationTextView = itemView.findViewById(R.id.config_item_duration);
                playBtn = itemView.findViewById(R.id.play_btn);

                this.adapter = adapter;
            }
        }
}
