package org.mian.gitnex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.mian.gitnex.R;
import org.mian.gitnex.adapters.DiffFilesAdapter;
import org.mian.gitnex.clients.RetrofitClient;
import org.mian.gitnex.databinding.FragmentDiffFilesBinding;
import org.mian.gitnex.helpers.AlertDialogs;
import org.mian.gitnex.helpers.FileDiffView;
import org.mian.gitnex.helpers.ParseDiff;
import org.mian.gitnex.helpers.Toasty;
import org.mian.gitnex.helpers.contexts.IssueContext;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author opyale
 */
public class DiffFilesFragment extends Fragment {

	private FragmentDiffFilesBinding binding;
	private Context ctx;
	private List<FileDiffView> fileDiffViews = new ArrayList<>();
	private DiffFilesAdapter adapter;

	public DiffFilesFragment() {}

	public static DiffFilesFragment newInstance() {
		return new DiffFilesFragment();
	}

	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (binding != null) {
			ctx = requireContext();
			return binding.getRoot();
		}

		binding = FragmentDiffFilesBinding.inflate(inflater, container, false);
		ctx = requireContext();
		IssueContext issue = IssueContext.fromIntent(requireActivity().getIntent());

		binding.progressBar.setVisibility(View.VISIBLE);

		adapter = new DiffFilesAdapter(ctx, fileDiffViews, issue, "");

		binding.diffFiles.setHasFixedSize(true);
		binding.diffFiles.setLayoutManager(new LinearLayoutManager(ctx));
		binding.diffFiles.setAdapter(adapter);

		getPullDiffFiles(
				issue.getRepository().getOwner(),
				issue.getRepository().getName(),
				String.valueOf(issue.getIssueIndex()));

		return binding.getRoot();
	}

	private void getPullDiffFiles(String owner, String repo, String pullIndex) {

		Thread thread =
				new Thread(
						() -> {
							Call<String> call =
									RetrofitClient.getApiInterface(ctx)
											.repoDownloadPullDiffOrPatch(
													owner,
													repo,
													Long.valueOf(pullIndex),
													"diff",
													null);

							try {

								Response<String> response = call.execute();
								if (response.body() == null) {
									Toasty.error(
											requireContext(), getString(R.string.genericError));
									requireActivity().finish();
									return;
								}

								switch (response.code()) {
									case 200:
										fileDiffViews =
												ParseDiff.getFileDiffViewArray(response.body());

										int filesCount = fileDiffViews.size();

										String toolbarTitleText =
												(filesCount > 1)
														? getResources()
																.getString(
																		R.string.fileDiffViewHeader,
																		Integer.toString(
																				filesCount))
														: getResources()
																.getString(
																		R.string
																				.fileDiffViewHeaderSingle,
																		Integer.toString(
																				filesCount));

										requireActivity()
												.runOnUiThread(
														() -> {
															binding.toolbarTitle.setVisibility(
																	View.VISIBLE);
															binding.progressBar.setVisibility(
																	View.GONE);
															binding.toolbarTitle.setText(
																	toolbarTitleText);
															adapter.updateList(fileDiffViews);
															adapter.notifyDataChanged();
														});
										break;

									case 401:
										requireActivity()
												.runOnUiThread(
														() ->
																AlertDialogs
																		.authorizationTokenRevokedDialog(
																				ctx));
										break;

									case 403:
										requireActivity()
												.runOnUiThread(
														() ->
																Toasty.error(
																		ctx,
																		ctx.getString(
																				R.string
																						.authorizeError)));
										break;

									case 404:
										requireActivity()
												.runOnUiThread(
														() ->
																Toasty.warning(
																		ctx,
																		ctx.getString(
																				R.string
																						.apiNotFound)));
										break;

									default:
										requireActivity()
												.runOnUiThread(
														() ->
																Toasty.error(
																		ctx,
																		getString(
																				R.string
																						.genericError)));
								}
							} catch (IOException ignored) {
							}
						});

		thread.start();
	}
}
