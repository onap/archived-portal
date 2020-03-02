/*
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 * Modifications Copyright (c) 2019 Samsung
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

package org.onap.portal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.onap.portal.domain.db.fn.FnUser;
import org.onap.portal.domain.dto.transport.UserWithNameSurnameTitle;
import org.onap.portal.service.user.FnUserService;
import org.onap.portal.utils.EcompPortalUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SearchService.class);

    private static final int maxSizeOfSearchResult = 100;

    private final FnUserService userService;

    @Autowired
    public SearchService(FnUserService userService) {
        this.userService = userService;
    }

    public String searchUsersInPhoneBook(final String searchString) {
        List<String> tokens = EcompPortalUtils.parsingByRegularExpression(searchString, " ");
        while (tokens.size() > 2) { // we use no more then first 2 tokens (userId is removed, see above)
            tokens.remove(tokens.size() - 1);
        }
        FnUser attrUser = new FnUser();
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

    public List<UserWithNameSurnameTitle> searchUsersByUserId(FnUser attrUser) {
        List<UserWithNameSurnameTitle> foundUsers = new ArrayList<UserWithNameSurnameTitle>();
        try {
            List<FnUser> searchResult = this.userService.getUserByUserId(attrUser.getOrgUserId());
            for (FnUser user : searchResult) {
                UserWithNameSurnameTitle foundUser = new UserWithNameSurnameTitle(user.getOrgUserId(), user.getFirstName(), user.getLastName(), user.getJobTitle());
                foundUsers.add(foundUser);
            }
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "searchUsersByUserId failed", e);
        }
        return foundUsers;
    }

    public List<UserWithNameSurnameTitle> searchUsersByName(FnUser attrUser) {
        List<UserWithNameSurnameTitle> foundUsers = new ArrayList<UserWithNameSurnameTitle>();
        try {
            List<FnUser> searchResult = this.userService.getUserByFirstLastName(attrUser.getFirstName(),attrUser.getLastName());
            for (Object obj : searchResult) {
                FnUser user = (FnUser) obj;
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
            if ((user.getFirstName() == null) || !user.getFirstName().toUpperCase().startsWith(firstName)) {
                resultOfSearch.remove(i);
            }
        }
        return resultOfSearch;
    }

    private List<UserWithNameSurnameTitle> removeWrongLastNames(List<UserWithNameSurnameTitle> resultOfSearch, String lastName) {
        lastName = lastName.toUpperCase();
        for (int i = resultOfSearch.size() - 1; i >= 0; i--) {
            UserWithNameSurnameTitle user = resultOfSearch.get(i);
            if ((user.getLastName() == null) || !user.getLastName().toUpperCase().startsWith(lastName)) {
                resultOfSearch.remove(i);
            }
        }
        return resultOfSearch;
    }

    private List<UserWithNameSurnameTitle> cutSearchResultToMaximumSize(List<UserWithNameSurnameTitle> resultOfSearch) {
        if (resultOfSearch.size() > maxSizeOfSearchResult) {
            resultOfSearch.subList(maxSizeOfSearchResult, resultOfSearch.size()).clear();
        }
        return resultOfSearch;
    }
}
