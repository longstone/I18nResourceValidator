package ch.longstone.i18n;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class I18nResourceBundleValidationTest {

	private static boolean isMessageProperties(Path path) {
		String filename = path.getFileName().toString();
		return filename.startsWith("messages_") && filename.endsWith("properties");
	}

	@Test
	public void validateEscapingOfSingleQuotes_ignoreComment()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.ignoreComments()
				.scanPath(Paths.get(".","src", "test", "resources","samples","comment"))
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.VALID));
	}

	@Test
	public void validateEscapingOfSingleQuotes_notIgnoreComment()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.scanPath(Paths.get(".","src", "test", "resources","samples","comment"))
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.ERROR));
	}

	@Test
	public void validateEscapingOfSingleQuotes_validFiles()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.ignoreComments()
				.scanPath(Paths.get(".","src", "test", "resources","samples", "comment"))
				.scanPath(Paths.get(".","src", "test", "resources","samples", "valid"))
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.VALID));
	}

	@Test
	public void validateEscapingOfSingleQuotes_invalidLines()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.ignoreComments()
				.scanPath(Paths.get(".","src", "test", "resources","samples", "invalid"))
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.ERROR));
	}

	@Test
	public void validateEscapingOfSingleQuotes_ignoredKeys()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.ignoreComments()
				.scanPath(Paths.get(".","src", "test", "resources","samples", "invalid"))
				.excludeKey("i.do.some.fancy.js.stuff.here")
				.excludeKey("and.i.am.some.wrongly.escaped.stuff")
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.VALID));
	}
}
