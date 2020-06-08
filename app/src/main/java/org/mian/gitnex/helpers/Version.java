package org.mian.gitnex.helpers;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author 6543
 */

public class Version {

	// the raw String
	private String raw;
	// the version numbers in its order (dot separated)
	private List<Integer> values;

	public Version(String value) {

		raw = value;
		this.init();

	}

	/**
	 * init parse and store values for other functions of an Version instance
	 * it use the raw variable as base
	 *
	 * @return if parse was successfully
	 */
	private void init() {

		final Pattern pattern_valid = Pattern.compile("^[v,V]?(\\d+)+(\\.(\\d+))*([_,\\-,+][\\w,\\d,_,\\-,+]*)?$");
		final Pattern pattern_number_dot_number = Pattern.compile("^\\d+(\\.(\\d)+)*");

		if(!pattern_valid.matcher(raw).find()) {
			throw new IllegalArgumentException("Invalid version format");
		}

		if(raw.charAt(0) == 'v' || raw.charAt(0) == 'V') {
			raw = raw.substring(1);
		}

		values = new ArrayList<Integer>();
		Matcher match = pattern_number_dot_number.matcher(raw);
		match.find();
		for(String i : match.group().split("\\.")) {
			values.add(Integer.parseInt(i));
		}

	}

	/**
	 * equal return true if version is the same
	 *
	 * @param value
	 * @return true/false
	 */
	public boolean equal(String value) {

		return this.equal(new Version(value));

	}

	/**
	 * equal return true if version is the same
	 *
	 * @param v
	 * @return
	 */
	public boolean equal(@NotNull Version v) {

		int rounds = Math.min(this.values.size(), v.values.size());
		for(int i = 0; i < rounds; i++) {
			if(this.values.get(i) != v.values.get(i)) {
				return false;
			}
		}
		return true;

	}


	/**
	 * less return true if version is less
	 *
	 * @param value
	 * @return true/false
	 */
	public boolean less(String value) {

		return this.less(new Version(value));

	}

	/**
	 * less return true if version is less
	 *
	 * @param v
	 * @return
	 */
	public boolean less(@NotNull Version v) {

		int rounds = Math.min(this.values.size(), v.values.size());
		for(int i = 0; i < rounds; i++) {
			if(i + 1 == rounds) {
				if(this.values.get(i) >= v.values.get(i)) {
					return false;
				}
			}
			else {
				if(this.values.get(i) > v.values.get(i)) {
					return false;
				}
				else if(this.values.get(i) < v.values.get(i)) {
					return true;
				}
			}
		}
		return true;

	}


	/**
	 * higher return true if version is higher
	 *
	 * @param value
	 * @return true/false
	 */
	public boolean higher(String value) {

		return this.higher(new Version(value));

	}

	/**
	 * higher return true if version is higher
	 *
	 * @param v
	 * @return
	 */
	public boolean higher(@NotNull Version v) {

		int rounds = Math.min(this.values.size(), v.values.size());
		for(int i = 0; i < rounds; i++) {
			if(i + 1 == rounds) {
				if(this.values.get(i) <= v.values.get(i)) {
					return false;
				}
			}
			else {
				if(this.values.get(i) < v.values.get(i)) {
					return false;
				}
				else if(this.values.get(i) > v.values.get(i)) {
					return true;
				}
			}
		}
		return true;

	}

	/**
	 * lessOrEqual return true if version is less or equal
	 *
	 * @param value
	 * @return true/false
	 */
	public boolean lessOrEqual(String value) {

		return this.lessOrEqual(new Version(value));

	}

	/**
	 * lessOrEqual return true if version is less or equal
	 *
	 * @param v
	 * @return
	 */
	public boolean lessOrEqual(@NotNull Version v) {

		int rounds = Math.min(this.values.size(), v.values.size());
		for(int i = 0; i < rounds; i++) {
			if(this.values.get(i) > v.values.get(i)) {
				return false;
			}
		}
		return true;

	}


	/**
	 * higherOrEqual return true if version is higher or equal
	 *
	 * @param value
	 * @return true/false
	 */
	public boolean higherOrEqual(String value) {

		return this.higherOrEqual(new Version(value));

	}

	/**
	 * higherOrEqual return true if version is higher or equal
	 *
	 * @param v
	 * @return
	 */
	public boolean higherOrEqual(@NotNull Version v) {

		int rounds = Math.min(this.values.size(), v.values.size());
		for(int i = 0; i < rounds; i++) {
			if(this.values.get(i) < v.values.get(i)) {
				return false;
			}
		}
		return true;

	}

}