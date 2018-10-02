/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017-2018 AT&T Intellectual Property. All rights reserved.
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
package org.onap.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.logging.aop.EPMetricsLog;
import org.onap.portalapp.portal.transport.UserWithNameSurnameTitle;
import org.onap.portalapp.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("searchService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class SearchServiceImpl implements SearchService {
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SearchServiceImpl.class);
	
	// TODO: the values below should be defined in other place
	private static final int maxSizeOfSearchResult = 100;

	@Autowired
	UserService userService;

	@Override
	public String searchUsersInPhoneBook(String searchString) {
		return searchUsersInFnTable(searchString);
	}
	
	
	@Override
	public String searchUsersInFnTable(String searchString) {
		List<String> tokens = EcompPortalUtils.parsingByRegularExpression(searchString, " ");
		while (tokens.size() > 2) { // we use no more then first 2 tokens (userId is removed, see above)
			tokens.remove(tokens.size() - 1);
		}
		EPUser attrUser = new EPUser();
		List<UserWithNameSurnameTitle> resultOfSearch = new ArrayList<UserWithNameSurnameTitle>(), resultOfAdditionalSearch = null,
				resultOfSearchUserId = new ArrayList<UserWithNameSurnameTitle>();
		if (tokens.size() == 2) {
			attrUser.setFirstName(tokens.get(0));
			attrUser.setLastName(tokens.get(1));
			resultOfSearch = this.searchUsersByName(attrUser);
			resultOfSearch = this.removeWrongFirstNames(resultOfSearch, tokens.get(0));
			resultOfSearch = this.removeWrongLastNames(resultOfSearch, tokens.get(1));
			if (resultOfSearch.size() < maxSizeOfSearchResult) {
				attrUser.setFirstName(tokens.get(1));
				attrUser.setLastName(tokens.get(0));
				resultOfAdditionalSearch = this.searchUsersByName(attrUser);
				resultOfAdditionalSearch = this.removeWrongFirstNames(resultOfAdditionalSearch, tokens.get(1));
				resultOfAdditionalSearch = this.removeWrongLastNames(resultOfAdditionalSearch, tokens.get(0));
			}
		} else if (tokens.size() == 1) {
			attrUser.setFirstName(tokens.get(0));
			attrUser.setOrgUserId(tokens.get(0));
			resultOfSearch = this.searchUsersByName(attrUser);
			resultOfSearchUserId = this.searchUsersByUserId(attrUser);
			resultOfSearch = this.removeWrongFirstNames(resultOfSearch, tokens.get(0));
			if (resultOfSearch.size() < maxSizeOfSearchResult) {
				attrUser.setFirstName(null);
				attrUser.setLastName(tokens.get(0));
				resultOfAdditionalSearch = this.searchUsersByName(attrUser);
				resultOfAdditionalSearch = this.removeWrongLastNames(resultOfAdditionalSearch, tokens.get(0));
			}
		}
		if (resultOfAdditionalSearch != null) {
			resultOfSearch.addAll(resultOfAdditionalSearch);
		}
		resultOfSearch.addAll(resultOfSearchUserId);
		resultOfSearch.stream().distinct().collect(Collectors.toList());
		resultOfSearch = this.cutSearchResultToMaximumSize(resultOfSearch);
		ObjectMapper mapper = new ObjectMapper();
		String result = "[]";
		try {
			result = mapper.writeValueAsString(resultOfSearch);
		} catch (JsonProcessingException e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchUsersInFnTable failed", e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List<UserWithNameSurnameTitle> searchUsersByUserId(EPUser attrUser) {
		List<UserWithNameSurnameTitle> foundUsers = new ArrayList<UserWithNameSurnameTitle>();
		try {
			List searchResult = this.userService.getUserByUserId(attrUser.getOrgUserId());
			for (Object obj : searchResult) {
				EPUser user = (EPUser) obj;
				UserWithNameSurnameTitle foundUser = new UserWithNameSurnameTitle(user.getOrgUserId(), user.getFirstName(), user.getLastName(), user.getJobTitle());
				foundUsers.add(foundUser);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchUsersByUserId failed", e);
		}
		return foundUsers;
	}
	
	@SuppressWarnings("rawtypes")
	public List<UserWithNameSurnameTitle> searchUsersByName(EPUser attrUser) {
		List<UserWithNameSurnameTitle> foundUsers = new ArrayList<UserWithNameSurnameTitle>();
		try {
			List searchResult = this.userService.getUserByFirstLastName(attrUser.getFirstName(),attrUser.getLastName());
			for (Object obj : searchResult) {
				EPUser user = (EPUser) obj;
				UserWithNameSurnameTitle foundUser = new UserWithNameSurnameTitle(user.getOrgUserId(), user.getFirstName(), user.getLastName(), user.getJobTitle());
				foundUsers.add(foundUser);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchUsersByName failed", e);
		}
		return foundUsers;
	}

	private List<UserWithNameSurnameTitle> removeWrongFirstNames(List<UserWithNameSurnameTitle> resultOfSearch, String firstName) {
		firstName = firstName.toUpperCase();
		for (int i = resultOfSearch.size() - 1; i >= 0; i--) {
			UserWithNameSurnameTitle user = resultOfSearch.get(i);
			if ((user.firstName == null) || !user.firstName.toUpperCase().startsWith(firstName)) {
				resultOfSearch.remove(i);
			}
		}
		return resultOfSearch;
	}

	private List<UserWithNameSurnameTitle> removeWrongLastNames(List<UserWithNameSurnameTitle> resultOfSearch, String lastName) {
		lastName = lastName.toUpperCase();
		for (int i = resultOfSearch.size() - 1; i >= 0; i--) {
			UserWithNameSurnameTitle user = resultOfSearch.get(i);
			if ((user.lastName == null) || !user.lastName.toUpperCase().startsWith(lastName)) {
				resultOfSearch.remove(i);
			}
		}
		return resultOfSearch;
	}

	private List<UserWithNameSurnameTitle> cutSearchResultToMaximumSize(List<UserWithNameSurnameTitle> resultOfSearch) {
		for (int i = resultOfSearch.size() - 1; i >= maxSizeOfSearchResult; i--) {
			resultOfSearch.remove(i);
		}
		return resultOfSearch;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public EPUser searchUserByUserId(String orgUserId) {
		List<EPUser> foundUsers = new ArrayList<EPUser>();
		try {
			List searchResult = this.userService.getUserByUserId(orgUserId);
			for (Object obj : searchResult) {
				EPUser user = (EPUser) obj;
				foundUsers.add(user);
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "searchUserByUserId failed", e);
			return null;
		}
		return foundUsers.get(0);
	}

}
