/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package org.onap.portalapp.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;

public class SecurityXssValidator {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SecurityXssValidator.class);

	private static final String MYSQL_DB = "mysql";
	private static final String ORACLE_DB = "oracle";
	private static final String MARIA_DB = "mariadb";
	private static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;
	static SecurityXssValidator validator = null;
	private static Codec instance;
	private static final Lock lock = new ReentrantLock();

	public static SecurityXssValidator getInstance() {

		if (validator == null) {
			lock.lock();
			try {
				if (validator == null)
					validator = new SecurityXssValidator();
			} finally {
				lock.unlock();
			}
		}

		return validator;
	}

	private SecurityXssValidator() {
		// Avoid anything between script tags
		XSS_INPUT_PATTERNS.add(Pattern.compile("<script>(.*?)</script>", FLAGS));

		// avoid iframes
		XSS_INPUT_PATTERNS.add(Pattern.compile("<iframe(.*?)>(.*?)</iframe>", FLAGS));

		// Avoid anything in a src='...' type of expression
		XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", FLAGS));

		XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", FLAGS));

		XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*([^>]+)", FLAGS));

		// Remove any lonesome </script> tag
		XSS_INPUT_PATTERNS.add(Pattern.compile("</script>", FLAGS));

		XSS_INPUT_PATTERNS.add(Pattern.compile(".*(<script>|</script>).*", FLAGS));

		XSS_INPUT_PATTERNS.add(Pattern.compile(".*(<iframe>|</iframe>).*", FLAGS));

		// Remove any lonesome <script ...> tag
		XSS_INPUT_PATTERNS.add(Pattern.compile("<script(.*?)>", FLAGS));

		// Avoid eval(...) expressions
		XSS_INPUT_PATTERNS.add(Pattern.compile("eval\\((.*?)\\)", FLAGS));

		// Avoid expression(...) expressions
		XSS_INPUT_PATTERNS.add(Pattern.compile("expression\\((.*?)\\)", FLAGS));

		// Avoid javascript:... expressions
		XSS_INPUT_PATTERNS.add(Pattern.compile(".*(javascript:|vbscript:).*", FLAGS));

		// Avoid onload= expressions
		XSS_INPUT_PATTERNS.add(Pattern.compile(".*(onload(.*?)=).*", FLAGS));
	}

	private List<Pattern> XSS_INPUT_PATTERNS = new ArrayList<Pattern>();

	/**
	 * * This method takes a string and strips out any potential script injections.
	 * 
	 * @param value
	 * @return String - the new "sanitized" string.
	 */
	public String stripXSS(String value) {

		try {

			if (StringUtils.isNotBlank(value)) {

				value = StringEscapeUtils.escapeHtml4(value);

				value = ESAPI.encoder().canonicalize(value);

				// Avoid null characters
				value = value.replaceAll("\0", "");

				for (Pattern xssInputPattern : XSS_INPUT_PATTERNS) {
					value = xssInputPattern.matcher(value).replaceAll("");
				}
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "stripXSS() failed", e);
		}

		return value;
	}

	public Boolean denyXSS(String value) {
		Boolean flag = Boolean.FALSE;
		try {
			if (StringUtils.isNotBlank(value)) {
				if (value.contains("&timeseclgn"))
				{
					logger.info(EELFLoggerDelegate.applicationLogger, "denyXSS() replacing &timeseclgn with empty string for request value : " + value);
					value=value.replaceAll("&timeseclgn", "");
				}
				while(value.contains("%25")) {
					value = value.replaceAll("%25", "%");
				}
				value = ESAPI.encoder().canonicalize(value);
				for (Pattern xssInputPattern : XSS_INPUT_PATTERNS) {
					if (xssInputPattern.matcher(value).matches()) {
						flag = Boolean.TRUE;
						break;
					}
				}
			}

		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "denyXSS() failed for request with value : " + e.getMessage());
			logger.debug(EELFLoggerDelegate.debugLogger, "denyXSS() failed for request with value : " + value, e);
		}

		return flag;
	}

	public Codec getCodec() {
		try {
			if (null == instance) {
				if (StringUtils.containsIgnoreCase(SystemProperties.getProperty(SystemProperties.DB_DRIVER), MYSQL_DB)
						|| StringUtils.containsIgnoreCase(SystemProperties.getProperty(SystemProperties.DB_DRIVER),
								MARIA_DB)) {
					instance = new MySQLCodec(Mode.STANDARD);

				} else if (StringUtils.containsIgnoreCase(SystemProperties.getProperty(SystemProperties.DB_DRIVER),
						ORACLE_DB)) {
					instance = new OracleCodec();
				} else {
					throw new NotImplementedException("Handling for data base \""
							+ SystemProperties.getProperty(SystemProperties.DB_DRIVER) + "\" not yet implemented.");
				}
			}

		} catch (Exception ex) {
			logger.error(EELFLoggerDelegate.errorLogger, "getCodec() failed", ex);
		}
		return instance;

	}

	public List<Pattern> getXSS_INPUT_PATTERNS() {
		return XSS_INPUT_PATTERNS;
	}

	public void setXSS_INPUT_PATTERNS(List<Pattern> xSS_INPUT_PATTERNS) {
		XSS_INPUT_PATTERNS = xSS_INPUT_PATTERNS;
	}

}