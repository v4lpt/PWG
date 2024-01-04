package v4lpt.vpt.pwg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import v4lpt.vpt.pwg.R;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        Button backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Replace the InfoFragment with the original MainActivity fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return rootView;
    }
}