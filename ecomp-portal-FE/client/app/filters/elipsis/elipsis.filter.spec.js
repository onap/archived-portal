/*-
 * ================================================================================
 * eCOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
describe('Filter: elipsis', function () {
    'use strict';
    let filter;

    beforeEach(function () {
        module('ecompApp');
    });

    beforeEach(inject((_CacheFactory_)=> {
        _CacheFactory_.destroyAll();

    }));

    it('should trim the text and return it with ... when the text length is greater than the limit"', inject(function (_$filter_) {
        filter = _$filter_;
        // given
        let text = `Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
        et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex
        ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat
        nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim
        id est laborum`;
        let limit = 50;

        var result = filter('elipsis')(text, limit);

        expect(result).toBe(text.substr(0, 50) + '...');
    }));

    it("should return the exact string where there's no limit", inject(function (_$filter_) {
        filter = _$filter_;

        let text = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore';

        let result = filter('elipsis')(text);

        expect(result).toEqual(text);
    }));

});
