package org.mian.gitnex.viewmodels;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import org.gitnex.tea4j.v2.models.Cron;
import org.mian.gitnex.R;
import org.mian.gitnex.clients.RetrofitClient;
import org.mian.gitnex.helpers.AlertDialogs;
import org.mian.gitnex.helpers.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author M M Arif
 */
public class AdminCronTasksViewModel extends ViewModel {

	private MutableLiveData<List<Cron>> tasksList;

	public LiveData<List<Cron>> getCronTasksList(Context ctx, int page, int limit) {

		tasksList = new MutableLiveData<>();
		loadCronTasksList(ctx, page, limit);

		return tasksList;
	}

	public void loadCronTasksList(final Context ctx, int page, int limit) {

		Call<List<Cron>> call = RetrofitClient.getApiInterface(ctx).adminCronList(page, limit);

		call.enqueue(
				new Callback<>() {

					@Override
					public void onResponse(
							@NonNull Call<List<Cron>> call,
							@NonNull Response<List<Cron>> response) {

						if (response.isSuccessful()) {
							tasksList.postValue(response.body());
						} else if (response.code() == 401) {
							AlertDialogs.authorizationTokenRevokedDialog(ctx);
						} else if (response.code() == 403) {
							Toasty.error(ctx, ctx.getString(R.string.authorizeError));
						} else if (response.code() == 404) {
							Toasty.warning(ctx, ctx.getString(R.string.apiNotFound));
						} else {
							Toasty.error(ctx, ctx.getString(R.string.genericError));
						}
					}

					@Override
					public void onFailure(@NonNull Call<List<Cron>> call, @NonNull Throwable t) {

						Toasty.error(ctx, ctx.getString(R.string.genericServerResponseError));
					}
				});
	}
}
