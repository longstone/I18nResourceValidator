# I18nResourceValidator
Validates java i18n porperty files, so that you don't have to

## Sample

``` java
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class I18nResourceBundleValidationTest {

	private static boolean isMessageProperties(Path path) {
		String filename = path.getFileName().toString();
		return filename.startsWith("messages_") && filename.endsWith("properties");
	}

	@Test
	public void validateEscapingOfSingleQuotes()  {
		I18nResourceValidator validator = new I18nResourceValidatorBuilder()
				.validatePattern(I18nResourceValidatorBuilder.singleQuotePattern())
				.ignoreComments()
				.scanPath(Paths.get(".", "src"))
				.excludeKey("ignore.me.please")
				.messagePropertyMatcher(I18nResourceBundleValidationTest::isMessageProperties)
				.build();

		I18nResourceValidatorResult validate = validator.validate();

		assertThat(validate.getMessage(), validate.getResult(), is( I18nResourceValidatorResult.State.VALID));

	}
}
```