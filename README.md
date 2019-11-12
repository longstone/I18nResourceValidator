# I18nResourceValidator
Validates java i18n property files, so that you don't have to...

Build status: [![Build Status](https://travis-ci.com/longstone/I18nResourceValidator.svg?branch=master)](https://travis-ci.com/longstone/I18nResourceValidator)

## Example

Import:
```
<dependency>
  <groupId>com.github.longstone</groupId>
  <artifactId>i18n-validator</artifactId>
  <version>2.0</version>
</dependency>
```

A file with the entry `deal.with.it=C'est la vie` will lead to a broken test.

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
### Development

## Release

Create a release in Github on the master branch and Travis will take care of the deployment. A release became visible in a few (-15) minutes on https://oss.sonatype.org/#nexus-search;gav~com.github.longstone~i18n-validator~~~ . A few hours later on maven central https://mvnrepository.com/artifact/com.github.longstone/i18n-validator.

## Contribution

Feel free to open merge requests (covered with unit tests ;) )!
