package org.mian.gitnex.viewmodels;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.gitnex.tea4j.v2.models.ContentsResponse;
import org.mian.gitnex.R;
import org.mian.gitnex.clients.RetrofitClient;
import org.mian.gitnex.helpers.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author M M Arif
 */
public class FilesViewModel extends ViewModel {

	private MutableLiveData<List<ContentsResponse>> filesList;
	private MutableLiveData<List<ContentsResponse>> filesList2;

	public LiveData<List<ContentsResponse>> getFilesList(
			String owner,
			String repo,
			String ref,
			Context ctx,
			ProgressBar progressBar,
			TextView noDataFiles) {

		filesList = new MutableLiveData<>();
		loadFilesList(owner, repo, ref, ctx, progressBar, noDataFiles);

		return filesList;
	}

	private void loadFilesList(
			String owner,
			String repo,
			String ref,
			final Context ctx,
			ProgressBar progressBar,
			TextView noDataFiles) {

		Call<List<ContentsResponse>> call =
				RetrofitClient.getApiInterface(ctx).repoGetContentsList(owner, repo, ref);

		call.enqueue(
				new Callback<>() {
					@Override
					public void onResponse(
							@NonNull Call<List<ContentsResponse>> call,
							@NonNull Response<List<ContentsResponse>> response) {

						if (response.isSuccessful()
								&& response.body() != null
								&& !response.body().isEmpty()) {
							Collections.sort(
									response.body(),
									Comparator.comparing(ContentsResponse::getType));
							filesList.postValue(response.body());
						} else {
							progressBar.setVisibility(View.GONE);
							noDataFiles.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(
							@NonNull Call<List<ContentsResponse>> call, @NonNull Throwable t) {

						Toasty.error(ctx, ctx.getString(R.string.genericServerResponseError));
					}
				});
	}

	public LiveData<List<ContentsResponse>> getFilesList2(
			String owner,
			String repo,
			String filesDir,
			String ref,
			Context ctx,
			ProgressBar progressBar,
			TextView noDataFiles) {

		filesList2 = new MutableLiveData<>();
		loadFilesList2(owner, repo, filesDir, ref, ctx, progressBar, noDataFiles);

		return filesList2;
	}

	private void loadFilesList2(
			String owner,
			String repo,
			String filesDir,
			String ref,
			final Context ctx,
			ProgressBar progressBar,
			TextView noDataFiles) {

		Call<List<ContentsResponse>> call =
				RetrofitClient.getApiInterface(ctx).repoGetContentsList(owner, repo, filesDir, ref);

		call.enqueue(
				new Callback<>() {
					@Override
					public void onResponse(
							@NonNull Call<List<ContentsResponse>> call,
							@NonNull Response<List<ContentsResponse>> response) {

						if (response.isSuccessful()
								&& response.body() != null
								&& !response.body().isEmpty()) {
							Collections.sort(
									response.body(),
									Comparator.comparing(ContentsResponse::getType));
							filesList2.postValue(response.body());
						} else {
							progressBar.setVisibility(View.GONE);
							noDataFiles.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(
							@NonNull Call<List<ContentsResponse>> call, @NonNull Throwable t) {

						Toasty.error(ctx, ctx.getString(R.string.genericServerResponseError));
					}
				});
	}
}
