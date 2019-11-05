package ch.longstone.i18n;

import java.util.Objects;

public class I18nResourceValidatorResult {

	public enum State { ERROR, VALID};
	private State result;
	private String message;

	public I18nResourceValidatorResult(State result, String message) {
		this.result = result;
		this.message = message;
	}

	/**
	 * @return the result of the validation
	 */
	public State getResult() {
		return result;
	}

	/**
	 * In case of error, a string containing all errors
	 * @return the errors to be printed or logged
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof I18nResourceValidatorResult))
			return false;
		I18nResourceValidatorResult that = (I18nResourceValidatorResult) o;
		return getResult() == that.getResult() && Objects.equals(getMessage(), that.getMessage());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getResult(), getMessage());
	}
}
