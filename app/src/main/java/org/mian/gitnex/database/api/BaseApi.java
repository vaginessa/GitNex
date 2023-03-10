package org.mian.gitnex.database.api;

import android.content.Context;
import androidx.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mian.gitnex.database.db.GitnexDatabase;

/**
 * @author opyale
 */
public abstract class BaseApi {

	protected static final ExecutorService executorService = Executors.newCachedThreadPool();
	private static final Map<Class<? extends BaseApi>, Object> instances = new HashMap<>();
	protected final GitnexDatabase gitnexDatabase;

	protected BaseApi(Context context) {
		gitnexDatabase = GitnexDatabase.getDatabaseInstance(context);
	}

	public static <T extends BaseApi> T getInstance(
			@NonNull Context context, @NonNull Class<T> clazz) {

		try {

			if (!instances.containsKey(clazz)) {
				synchronized (BaseApi.class) {
					if (!instances.containsKey(clazz)) {

						T instance =
								clazz.getDeclaredConstructor(Context.class).newInstance(context);

						instances.put(clazz, instance);
						return instance;
					}
				}
			}

			return (T) instances.get(clazz);

		} catch (NoSuchMethodException
				| IllegalAccessException
				| InvocationTargetException
				| InstantiationException ignored) {
		}

		return null;
	}
}
