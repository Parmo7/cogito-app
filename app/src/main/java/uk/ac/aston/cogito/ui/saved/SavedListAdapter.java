package uk.ac.aston.cogito.ui.saved;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import uk.ac.aston.cogito.R;
import uk.ac.aston.cogito.model.ConfigsViewModel;
import uk.ac.aston.cogito.model.entities.AudioResource;
import uk.ac.aston.cogito.model.entities.SessionConfig;
import uk.ac.aston.cogito.ui.home.HomeFragmentDirections;
import uk.ac.aston.cogito.ui.home.dialogs.BottomDialogListener;
import uk.ac.aston.cogito.ui.home.dialogs.EnterNameDialog;
import uk.ac.aston.cogito.ui.home.dialogs.FormBottomDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectDurationDialog;
import uk.ac.aston.cogito.ui.home.dialogs.SelectMusicDialog;

public class SavedListAdapter extends RecyclerView.Adapter<SavedListAdapter.SavedViewHolder> implements BottomDialogListener {

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

            holder.configDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setLatestConfig(config);

                    Navigation.findNavController(view).navigate(SavedFragmentDirections.actionNavigationSavedToNavigationHome());
                }
            });

            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setLatestConfig(config);

                    SavedFragmentDirections.ActionNavigationSavedToSession action = SavedFragmentDirections.actionNavigationSavedToSession(config);
                    Navigation.findNavController(view)
                            .navigate(action);
                }
            });

            holder.threeDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Create a popup menu
                    PopupMenu popup = new PopupMenu(view.getContext(), holder.threeDots);
                    //Inflate menu from xml resource
                    popup.inflate(R.menu.config_item_options);
                    displayIcons(popup);


                    //Add a click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.edit:
                                    //handle menu1 click
                                    SavedFragmentDirections.ActionNavigationSavedToConfigSettingsFragment action =
                                            SavedFragmentDirections.actionNavigationSavedToConfigSettingsFragment().setConfig(config);
                                    Navigation.findNavController(view).navigate(action);
                                    return true;

                                case R.id.rename:
                                    EnterNameDialog enterNameDialog = new EnterNameDialog(SavedListAdapter.this, view.getContext(), config);
                                    enterNameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            String name = ((EnterNameDialog) dialog).getValue();
                                            if (name != null && !name.isEmpty()) {
                                                config.setName(name);
                                                model.updateConfig(config);

                                                Navigation.findNavController(view).navigate(R.id.navigation_saved);
                                            }
                                        }
                                    });
                                    enterNameDialog.show();
                                    return true;

                                case R.id.delete:
                                    showDeleteConfirmationDialog(view, config);
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();

                }
            });
        }


    private void showDeleteConfirmationDialog(View view, SessionConfig config) {
        // Setup the alert dialog for the delete FAB
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle(R.string.delete_confirmation_title);
        builder.setMessage("Configuration '" + config.getName() + "' will be permanently deleted.");
        builder.setPositiveButton(R.string.delete_confirmation_proceed, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Get the model to actually remove the place
                model.removeConfig(config.getId());
                // And, finally, let the user know.
                Toast.makeText(inflater.getContext(), "Configuration successfully deleted.", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                Navigation.findNavController(view).navigate(R.id.navigation_saved);
            }
        });
        builder.setNegativeButton(R.string.delete_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If the user selects "Cancel", dismiss the dialog.
                dialog.cancel();
            }
        });

        // Build the AlertDialog
        AlertDialog deleteConfirmationDialog = builder.create();
        deleteConfirmationDialog.show();
    }

        @Override
        public void onDoneBtnPressed(FormBottomDialog dialog) {
            // DO NOTHING;
        }

        private void displayIcons(PopupMenu popup) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

            public LinearLayout configDetails;
            public final TextView nameTextView;
            public final TextView durationTextView;
            public final FrameLayout playBtn;
            public final ImageView threeDots;

            public SavedViewHolder(@NonNull View itemView, SavedListAdapter adapter) {
                super(itemView);

                configDetails = itemView.findViewById(R.id.config_details);
                nameTextView = itemView.findViewById(R.id.config_item_name);
                durationTextView = itemView.findViewById(R.id.config_item_duration);
                playBtn = itemView.findViewById(R.id.play_btn);
                threeDots = itemView.findViewById(R.id.three_dots);

                this.adapter = adapter;
            }
        }
}
