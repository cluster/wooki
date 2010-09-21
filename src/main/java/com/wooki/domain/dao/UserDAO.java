//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.dao;

import java.math.BigInteger;

import com.wooki.domain.model.User;

/**
 * Implements handling of Wooki Authors.
 * 
 * @author ccordenier
 */
public interface UserDAO extends WookiGenericDAO<User, Long>
{

    /**
     * Find an author by its username, case is insensitive.
     * 
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * List usernames
     * 
     * @param prefix
     * @return
     */
    String[] listUserNames(String prefix);
    
    /**
     * Update user sid in case of username update
     *
     * @param sidId
     * @param username
     */
    void updateSid(BigInteger sidId, String username);
    
    /**
     * Find sid associated to the user
     *
     * @param username
     * @return
     */
    BigInteger findSid(String username);

}
