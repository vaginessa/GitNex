package org.mian.gitnex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.mian.gitnex.databinding.BottomSheetAdminUsersBinding;
import org.mian.gitnex.structs.BottomSheetListener;

/**
 * @author M M Arif
 */
public class BottomSheetAdminUsersFragment extends BottomSheetDialogFragment {

	private BottomSheetListener bmListener;

	@Nullable @Override
	public View onCreateView(
			@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {

		BottomSheetAdminUsersBinding bottomSheetAdminUsersBinding =
				BottomSheetAdminUsersBinding.inflate(inflater, container, false);

		bottomSheetAdminUsersBinding.createNewUser.setOnClickListener(
				v1 -> {
					bmListener.onButtonClicked("newUser");
					dismiss();
				});

		return bottomSheetAdminUsersBinding.getRoot();
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);

		try {
			bmListener = (BottomSheetListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context + " must implement BottomSheetListener");
		}
	}
}
