package org.mian.gitnex.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.gitnex.tea4j.v2.models.CreateIssueOption;
import org.gitnex.tea4j.v2.models.Issue;
import org.gitnex.tea4j.v2.models.Label;
import org.gitnex.tea4j.v2.models.Milestone;
import org.gitnex.tea4j.v2.models.User;
import org.mian.gitnex.R;
import org.mian.gitnex.actions.AssigneesActions;
import org.mian.gitnex.actions.LabelsActions;
import org.mian.gitnex.adapters.AssigneesListAdapter;
import org.mian.gitnex.adapters.LabelsListAdapter;
import org.mian.gitnex.clients.RetrofitClient;
import org.mian.gitnex.databinding.ActivityCreateIssueBinding;
import org.mian.gitnex.databinding.CustomAssigneesSelectionDialogBinding;
import org.mian.gitnex.databinding.CustomLabelsSelectionDialogBinding;
import org.mian.gitnex.fragments.IssuesFragment;
import org.mian.gitnex.helpers.AlertDialogs;
import org.mian.gitnex.helpers.AppUtil;
import org.mian.gitnex.helpers.Constants;
import org.mian.gitnex.helpers.Markdown;
import org.mian.gitnex.helpers.Toasty;
import org.mian.gitnex.helpers.contexts.RepositoryContext;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author M M Arif
 */
public class CreateIssueActivity extends BaseActivity
		implements View.OnClickListener,
				LabelsListAdapter.LabelsListAdapterListener,
				AssigneesListAdapter.AssigneesListAdapterListener {

	private final List<Label> labelsList = new ArrayList<>();
	private final LinkedHashMap<String, Milestone> milestonesList = new LinkedHashMap<>();
	private final List<User> assigneesList = new ArrayList<>();
	private ActivityCreateIssueBinding viewBinding;
	private View.OnClickListener onClickListener;
	private int milestoneId;
	private Date currentDate = null;
	private RepositoryContext repository;
	private LabelsListAdapter labelsAdapter;
	private AssigneesListAdapter assigneesAdapter;
	private MaterialAlertDialogBuilder materialAlertDialogBuilder;
	private List<Integer> labelsIds = new ArrayList<>();
	private List<String> assigneesListData = new ArrayList<>();
	private boolean renderMd = false;
	private RepositoryContext repositoryContext;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		viewBinding = ActivityCreateIssueBinding.inflate(getLayoutInflater());
		setContentView(viewBinding.getRoot());
		setSupportActionBar(viewBinding.toolbar);

		repositoryContext = RepositoryContext.fromIntent(getIntent());

		boolean connToInternet = AppUtil.hasNetworkConnection(appCtx);

		InputMethodManager imm =
				(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		materialAlertDialogBuilder =
				new MaterialAlertDialogBuilder(ctx, R.style.ThemeOverlay_Material3_Dialog_Alert);

		repository = RepositoryContext.fromIntent(getIntent());

		int resultLimit = Constants.getCurrentResultLimit(ctx);

		viewBinding.newIssueTitle.requestFocus();
		assert imm != null;
		imm.showSoftInput(viewBinding.newIssueTitle, InputMethodManager.SHOW_IMPLICIT);

		viewBinding.newIssueDescription.setOnTouchListener(
				(touchView, motionEvent) -> {
					touchView.getParent().requestDisallowInterceptTouchEvent(true);

					if ((motionEvent.getAction() & MotionEvent.ACTION_UP) != 0
							&& (motionEvent.getActionMasked() & MotionEvent.ACTION_UP) != 0) {

						touchView.getParent().requestDisallowInterceptTouchEvent(false);
					}
					return false;
				});

		labelsAdapter = new LabelsListAdapter(labelsList, CreateIssueActivity.this, labelsIds);
		assigneesAdapter =
				new AssigneesListAdapter(
						ctx, assigneesList, CreateIssueActivity.this, assigneesListData);

		initCloseListener();
		viewBinding.close.setOnClickListener(onClickListener);

		viewBinding.newIssueAssigneesList.setOnClickListener(this);
		viewBinding.newIssueLabels.setOnClickListener(this);
		viewBinding.newIssueDueDate.setOnClickListener(this);

		getMilestones(repository.getOwner(), repository.getName(), resultLimit);

		disableProcessButton();

		viewBinding.newIssueLabels.setOnClickListener(newIssueLabels -> showLabels());

		viewBinding.newIssueAssigneesList.setOnClickListener(
				newIssueAssigneesList -> showAssignees());

		if (!connToInternet) {

			viewBinding.createNewIssueButton.setEnabled(false);
		} else {

			viewBinding.createNewIssueButton.setOnClickListener(this);
		}

		if (!repository.getPermissions().isPush()) {
			viewBinding.newIssueAssigneesListLayout.setVisibility(View.GONE);
			viewBinding.newIssueMilestoneSpinnerLayout.setVisibility(View.GONE);
			viewBinding.newIssueLabelsLayout.setVisibility(View.GONE);
			viewBinding.newIssueDueDateLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(@NonNull Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.markdown_switcher, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.markdown) {

			if (!renderMd) {
				Markdown.render(
						ctx,
						EmojiParser.parseToUnicode(
								Objects.requireNonNull(viewBinding.newIssueDescription.getText())
										.toString()),
						viewBinding.markdownPreview,
						repositoryContext);

				viewBinding.markdownPreview.setVisibility(View.VISIBLE);
				viewBinding.newIssueDescriptionLayout.setVisibility(View.GONE);
				renderMd = true;
			} else {
				viewBinding.markdownPreview.setVisibility(View.GONE);
				viewBinding.newIssueDescriptionLayout.setVisibility(View.VISIBLE);
				renderMd = false;
			}

			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void assigneesInterface(List<String> data) {

		String assigneesSetter = String.valueOf(data);
		viewBinding.newIssueAssigneesList.setText(
				assigneesSetter.replace("]", "").replace("[", ""));
		assigneesListData = data;
	}

	@Override
	public void labelsInterface(List<String> data) {

		String labelsSetter = String.valueOf(data);
		viewBinding.newIssueLabels.setText(labelsSetter.replace("]", "").replace("[", ""));
	}

	@Override
	public void labelsIdsInterface(List<Integer> data) {

		labelsIds = data;
	}

	private void showAssignees() {

		viewBinding.progressBar.setVisibility(View.VISIBLE);

		CustomAssigneesSelectionDialogBinding assigneesBinding =
				CustomAssigneesSelectionDialogBinding.inflate(LayoutInflater.from(ctx));
		View view = assigneesBinding.getRoot();
		materialAlertDialogBuilder.setView(view);

		materialAlertDialogBuilder.setNeutralButton(R.string.close, null);

		AssigneesActions.getRepositoryAssignees(
				ctx,
				repository.getOwner(),
				repository.getName(),
				assigneesList,
				materialAlertDialogBuilder,
				assigneesAdapter,
				assigneesBinding,
				viewBinding.progressBar);
	}

	private void showLabels() {

		viewBinding.progressBar.setVisibility(View.VISIBLE);

		CustomLabelsSelectionDialogBinding labelsBinding =
				CustomLabelsSelectionDialogBinding.inflate(LayoutInflater.from(ctx));
		View view = labelsBinding.getRoot();
		materialAlertDialogBuilder.setView(view);

		materialAlertDialogBuilder.setNeutralButton(R.string.close, null);

		LabelsActions.getRepositoryLabels(
				ctx,
				repository.getOwner(),
				repository.getName(),
				labelsList,
				materialAlertDialogBuilder,
				labelsAdapter,
				labelsBinding,
				viewBinding.progressBar);
	}

	private void processNewIssue() {

		boolean connToInternet = AppUtil.hasNetworkConnection(appCtx);

		String newIssueTitleForm =
				Objects.requireNonNull(viewBinding.newIssueTitle.getText()).toString();
		String newIssueDescriptionForm =
				Objects.requireNonNull(viewBinding.newIssueDescription.getText()).toString();
		String newIssueDueDateForm =
				Objects.requireNonNull(viewBinding.newIssueDueDate.getText()).toString();

		if (!connToInternet) {

			Toasty.error(ctx, getResources().getString(R.string.checkNetConnection));
			return;
		}

		if (newIssueTitleForm.equals("")) {

			Toasty.error(ctx, getString(R.string.issueTitleEmpty));
			return;
		}

		disableProcessButton();
		createNewIssueFunc(
				repository.getOwner(),
				repository.getName(),
				newIssueDescriptionForm,
				milestoneId,
				newIssueTitleForm);
	}

	private void createNewIssueFunc(
			String repoOwner,
			String repoName,
			String newIssueDescriptionForm,
			int newIssueMilestoneIdForm,
			String newIssueTitleForm) {

		ArrayList<Long> labelIds = new ArrayList<>();
		for (Integer i : labelsIds) {
			labelIds.add((long) i);
		}

		CreateIssueOption createNewIssueJson = new CreateIssueOption();
		createNewIssueJson.setBody(newIssueDescriptionForm);
		createNewIssueJson.setMilestone((long) newIssueMilestoneIdForm);
		createNewIssueJson.setDueDate(currentDate);
		createNewIssueJson.setTitle(newIssueTitleForm);
		createNewIssueJson.setAssignees(assigneesListData);
		createNewIssueJson.setLabels(labelIds);

		Call<Issue> call3 =
				RetrofitClient.getApiInterface(ctx)
						.issueCreateIssue(repoOwner, repoName, createNewIssueJson);

		call3.enqueue(
				new Callback<>() {

					@Override
					public void onResponse(
							@NonNull Call<Issue> call,
							@NonNull retrofit2.Response<Issue> response2) {

						if (response2.code() == 201) {

							IssuesFragment.resumeIssues = true;

							Toasty.success(ctx, getString(R.string.issueCreated));
							enableProcessButton();
							RepoDetailActivity.updateRepo = true;
							MainActivity.reloadRepos = true;
							finish();
						} else if (response2.code() == 401) {

							enableProcessButton();
							AlertDialogs.authorizationTokenRevokedDialog(ctx);
						} else {

							Toasty.error(ctx, getString(R.string.genericError));
							enableProcessButton();
						}
					}

					@Override
					public void onFailure(@NonNull Call<Issue> call, @NonNull Throwable t) {

						Toasty.error(ctx, getString(R.string.genericServerResponseError));
						enableProcessButton();
					}
				});
	}

	private void initCloseListener() {

		onClickListener = view -> finish();
	}

	private void getMilestones(String repoOwner, String repoName, int resultLimit) {

		String msState = "open";
		Call<List<Milestone>> call =
				RetrofitClient.getApiInterface(ctx)
						.issueGetMilestonesList(repoOwner, repoName, msState, null, 1, resultLimit);

		call.enqueue(
				new Callback<>() {

					@Override
					public void onResponse(
							@NonNull Call<List<Milestone>> call,
							@NonNull retrofit2.Response<List<Milestone>> response) {

						if (response.isSuccessful()) {

							if (response.code() == 200) {

								List<Milestone> milestonesList_ = response.body();

								Milestone ms = new Milestone();
								ms.setId(0L);
								ms.setTitle(getString(R.string.issueCreatedNoMilestone));
								milestonesList.put(ms.getTitle(), ms);
								assert milestonesList_ != null;

								if (milestonesList_.size() > 0) {

									for (Milestone milestone : milestonesList_) {

										// Don't translate "open" is a enum
										if (milestone.getState().equals("open")) {
											milestonesList.put(milestone.getTitle(), milestone);
										}
									}
								}

								ArrayAdapter<String> adapter =
										new ArrayAdapter<>(
												CreateIssueActivity.this,
												R.layout.list_spinner_items,
												new ArrayList<>(milestonesList.keySet()));

								viewBinding.newIssueMilestoneSpinner.setAdapter(adapter);
								enableProcessButton();

								viewBinding.newIssueMilestoneSpinner.setOnItemClickListener(
										(parent, view, position, id) -> {
											if (position == 0) {
												milestoneId = 0;
											} else if (view instanceof TextView) {
												milestoneId =
														Math.toIntExact(
																Objects.requireNonNull(
																				milestonesList.get(
																						((TextView)
																										view)
																								.getText()
																								.toString()))
																		.getId());
											}
										});
							}
						}
					}

					@Override
					public void onFailure(
							@NonNull Call<List<Milestone>> call, @NonNull Throwable t) {

						Toasty.error(ctx, getString(R.string.genericServerResponseError));
					}
				});
	}

	@Override
	public void onClick(View v) {

		if (v == viewBinding.newIssueDueDate) {

			final Calendar c = Calendar.getInstance();
			int mYear = c.get(Calendar.YEAR);
			final int mMonth = c.get(Calendar.MONTH);
			final int mDay = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog datePickerDialog =
					new DatePickerDialog(
							this,
							(view, year, monthOfYear, dayOfMonth) -> {
								viewBinding.newIssueDueDate.setText(
										getString(
												R.string.setDueDate,
												year,
												(monthOfYear + 1),
												dayOfMonth));
								currentDate = new Date(year - 1900, monthOfYear, dayOfMonth);
							},
							mYear,
							mMonth,
							mDay);
			datePickerDialog.show();
		} else if (v == viewBinding.createNewIssueButton) {

			processNewIssue();
		}
	}

	private void disableProcessButton() {

		viewBinding.createNewIssueButton.setEnabled(false);
	}

	private void enableProcessButton() {

		viewBinding.createNewIssueButton.setEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		repository.checkAccountSwitch(this);
	}
}
