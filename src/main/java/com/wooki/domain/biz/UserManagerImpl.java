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

package com.wooki.domain.biz;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.Calendar;
import com.wooki.domain.dao.ActivityDAO;
import com.wooki.domain.dao.UserDAO;
import com.wooki.domain.exception.UserAlreadyException;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.AccountActivity;
import com.wooki.domain.model.activity.AccountEventType;
import com.wooki.services.WookiModule;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("userManager")
public class UserManagerImpl implements UserManager {

	@Autowired
	private UserDAO authorDao;

	@Autowired
	private ActivityDAO activityDao;

	@Transactional(readOnly = false, rollbackFor = UserAlreadyException.class)
	public void addUser(User author) throws UserAlreadyException {

		if (findByUsername(author.getUsername()) != null) {
			throw new UserAlreadyException();
		}

		// Encode password into database
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String pass = author.getPassword();
		author.setCreationDate(new Date());
		author.setPassword(encoder.encodePassword(pass, WookiModule.SALT));
		authorDao.create(author);

		AccountActivity aa = new AccountActivity();
		aa.setCreationDate(Calendar.getInstance().getTime());
		aa.setType(AccountEventType.JOIN);
		aa.setUser(author);
		this.activityDao.create(aa);

	}

	public User findByUsername(String username) {
		return authorDao.findByUsername(username);
	}

	public String[] listUserNames(String prefix) {
		return authorDao.listUserNames(prefix);
	}

}
