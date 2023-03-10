package org.mian.gitnex.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.mian.gitnex.activities.MyProfileEmailActivity;
import org.mian.gitnex.databinding.BottomSheetProfileBinding;

/**
 * @author M M Arif
 */
public class BottomSheetMyProfileFragment extends BottomSheetDialogFragment {

	@Nullable @Override
	public View onCreateView(
			@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {

		BottomSheetProfileBinding bottomSheetProfileBinding =
				BottomSheetProfileBinding.inflate(inflater, container, false);

		bottomSheetProfileBinding.addNewEmailAddress.setOnClickListener(
				v1 -> {
					startActivity(new Intent(getContext(), MyProfileEmailActivity.class));
					dismiss();
				});

		return bottomSheetProfileBinding.getRoot();
	}
}
