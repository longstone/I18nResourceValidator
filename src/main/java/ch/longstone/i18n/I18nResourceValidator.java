package ch.longstone.i18n;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class I18nResourceValidator {

	private static final String KEY_VALUE_SEPARATOR = "=";
	private final Predicate<Path> messagePropertiesFilter;
	private final List<I18nValidatorPattern> validatePatterns;
	private final List<Path> scanPaths;
	private final List<String> ignoreKeys;
	private final boolean ignoreComments;

	public I18nResourceValidator(List<Path> scanPaths, List<I18nValidatorPattern> validatePatterns,
			List<String> ignoreKeys, boolean ignoreComments, Predicate<Path> messagePropertiesFilter) {
		this.validatePatterns = validatePatterns.stream().distinct().collect(Collectors.toList());
		this.scanPaths = scanPaths.stream().distinct().collect(Collectors.toList());
		this.ignoreKeys = ignoreKeys.stream().distinct().collect(Collectors.toList());
		this.ignoreComments = ignoreComments;
		this.messagePropertiesFilter = messagePropertiesFilter;
	}

	/**
	 * Will trigger the validation of all files.
	 * @return a validation result
	 */
	public I18nResourceValidatorResult validate() {
		List<String> errors = scanPaths.stream().map(rootPath -> {
			try {
				return Files.walk(rootPath)
						.filter(Files::isRegularFile)
						.filter(messagePropertiesFilter)
						.map(this::validateSingleFile)
						.flatMap(List::stream)
						.collect(Collectors.toList());
			} catch (IOException e) {
				return Collections.singletonList(e.toString());
			}
		}).flatMap(List::stream)
		.collect(Collectors.toList());

		if (!errors.isEmpty()) {
			createErrorHeadline(errors);
			String message = String.join(System.lineSeparator(), errors);
			return new I18nResourceValidatorResult(I18nResourceValidatorResult.State.ERROR,message);
		}

		return new I18nResourceValidatorResult(I18nResourceValidatorResult.State.VALID, "");
	}

	private void createErrorHeadline(List<String> errors) {
		errors.add(0, "Following errors occurred: " + System.lineSeparator());
	}

	private List<String> validateSingleFile(Path path) {
		List<String> singleFileErrors = new LinkedList<>();
		try (InputStream is = new FileInputStream(path.toFile());
				InputStreamReader reader = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
				BufferedReader bufferedReader = new BufferedReader(reader)) {
			int countLines = 0;
			List<String> allLines = bufferedReader.lines().collect(Collectors.toList());

			for (String line : allLines) {
				countLines++;
				if (shouldValidate(line)) {
					validatePatterns.forEach(patternValidator(path, singleFileErrors, countLines, line));
				}
			}
		} catch (IOException ex) {
			singleFileErrors.add(String
					.format("Error while validating resource bundle %s: %s", path.toString(), ex.getMessage()));
		}
		return singleFileErrors;
	}

	private Consumer<I18nValidatorPattern> patternValidator(Path path, List<String> singleFileErrors, int countLines,
			String line) {
		return pattern -> {
			if (isInvalid(line, pattern)) {
				if (keyIsNotIgnored(line)) {
					singleFileErrors
							.add("unmatched validator " + pattern.getDescription() + ": " + path + ":" + countLines
									+ ", line:" + line);
				}
			}
		};
	}

	private boolean isInvalid(String line, I18nValidatorPattern pattern) {
		return pattern.getPattern().matcher(line).find();
	}

	private boolean shouldValidate(String line) {
		boolean isComment = line.startsWith("#");
		boolean ignore = isComment && ignoreComments;
		return !isNullOrEmpty(line) && !ignore;
	}

	private boolean isNullOrEmpty(String line) {
		return line == null || line.isEmpty();
	}

	private boolean keyIsNotIgnored(String line) {
		return !ignoreKeys.contains(keyOf(line));
	}

	private String keyOf(String line) {
		if (line.contains(KEY_VALUE_SEPARATOR)) {
			String[] splittedLine = line.split(KEY_VALUE_SEPARATOR);
			return splittedLine[0];
		}
		return line;
	}
}
