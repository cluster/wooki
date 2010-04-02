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

package com.wooki.services.security;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wooki.domain.biz.UserManager;
import com.wooki.domain.model.User;

/**
 * Custom implementation of use details service for spring security.
 * 
 * @author ccordenier
 */
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Inject @Autowired
    private UserManager userManager;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
            DataAccessException
    {
        User user = userManager.findByUsername(username);
        return user;
    }

}
