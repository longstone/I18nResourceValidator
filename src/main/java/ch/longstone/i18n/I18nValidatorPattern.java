package ch.longstone.i18n;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represent a single validation pattern
 */
public class I18nValidatorPattern {
	private final String description;
	private final Pattern pattern;

	/**
	 * creates a new validation pattern
	 * @param description a speaking name for this pattern (used for error reports)
	 * @param pattern the regex pattern itself
	 */
	public I18nValidatorPattern(String description, Pattern pattern) {
		this.description = description;
		this.pattern = pattern;
	}

	public String getDescription() {
		return description;
	}

	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof I18nValidatorPattern))
			return false;
		I18nValidatorPattern that = (I18nValidatorPattern) o;
		return Objects.equals(getDescription(), that.getDescription()) && Objects
				.equals(getPattern(), that.getPattern());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDescription(), getPattern().toString());
	}
}
