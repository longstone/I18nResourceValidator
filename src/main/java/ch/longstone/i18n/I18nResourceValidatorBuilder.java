package ch.longstone.i18n;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class I18nResourceValidatorBuilder {
	private static I18nValidatorPattern singleSingleQuotePattern = new  I18nValidatorPattern("validate that all ' are escaped", Pattern.compile("[^']'[^']"));

	private List<I18nValidatorPattern> validatePatterns = new LinkedList<>();
	private List<Path> scanPaths = new LinkedList<>();
	private List<String> ignoreKeys = new LinkedList<>();
	private boolean ignoreComments;
	private Predicate<Path> messagePropertiesFilter;

	public static I18nValidatorPattern singleQuotePattern() {
		return singleSingleQuotePattern;
	}

	public I18nResourceValidatorBuilder validatePattern(I18nValidatorPattern pattern) {
		validatePatterns.add(pattern);
		return this;
	}

	/**
	 * ignore lines starting with #
	 */
	public I18nResourceValidatorBuilder ignoreComments() {
		this.ignoreComments = true;
		return this;
	}

	/**
	 * path to be scanned for files
	 * @param src
	 */
	public I18nResourceValidatorBuilder scanPath(Path src) {
		this.scanPaths.add(src);
		return this;
	}

	/**
	 * ignore some values in the files, ex. to ignore this=asdf you'll pass "this"
	 * @param keyToIgnore
	 */
	public I18nResourceValidatorBuilder excludeKey(String keyToIgnore) {
		this.ignoreKeys.add(keyToIgnore);
		return this;
	}

	/**
	 * the matcher to include all the needed properties files
	 * @param messagePropertiesFilter
	 */
	public I18nResourceValidatorBuilder messagePropertyMatcher(Predicate<Path> messagePropertiesFilter) {
		this.messagePropertiesFilter = messagePropertiesFilter;
		return this;
	}

	public I18nResourceValidator build() {
		return new I18nResourceValidator(scanPaths, validatePatterns, ignoreKeys, ignoreComments, messagePropertiesFilter);
	}
}
