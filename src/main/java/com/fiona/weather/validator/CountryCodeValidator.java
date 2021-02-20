package com.fiona.weather.validator;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;

public class CountryCodeValidator implements ConstraintValidator<ISO3166CountryCode, String> {

	private static final Set<String> ISO_COUNTRY_CODES = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);

	@Override
	public boolean isValid(String countryCode, ConstraintValidatorContext context) {
		if (Strings.isEmpty(countryCode)) {
			return false;
		}
		return ISO_COUNTRY_CODES.contains(countryCode.toUpperCase());
	}
}
