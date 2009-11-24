package com.wooki.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.wooki.domain.model.User;
import com.wooki.pages.book.IndexDesign;
import com.wooki.services.UserManager;

/**
 * Signup page for new authors.
 * 
 * @author ccordenier
 * 
 */
public class Signup {

	@InjectComponent
	private Form signupForm;

	@Inject
	private ApplicationContext context;

	private UserManager authorManager;

	@InjectPage
	private Index successPage;

	@Property
	@Validate("required")
	private String username;

	@Property
	@Validate("required")
	private String password;

	@Property
	@Validate("required")
	private String confirmPassword;

	@Property
	@Validate("required,email")
	private String email;

	@OnEvent(value = EventConstants.PREPARE, component = "signupForm")
	public void prepareSubmit() {
		authorManager = (UserManager) context.getBean("authorManager");
	}

	@OnEvent(value = EventConstants.VALIDATE_FORM, component = "signupForm")
	public void onValidate() {
		if (!password.equals(confirmPassword)) {
			signupForm.recordError("Password do not match");
		}
		if (authorManager.findByUsername(username) != null) {
			signupForm.recordError("User already exists");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "signupForm")
	public Object onSignupSuccess() {
		User author = new User();
		author.setUsername(username);
		author.setEmail(email);
<<<<<<< HEAD
		author.setPassword(password+"{DEADBEEF}");
		authorManager.addUser(author);
		
=======
		author.setPassword(password);
		authorManager.addAuthor(author);

>>>>>>> e6a2e915e7983956cf275a6591eb928c4278b7df
		// Alert spring security that an author has logged in
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(author, author
						.getAuthorities()));
		successPage.setUsername(username);
		return successPage;
	}

}
