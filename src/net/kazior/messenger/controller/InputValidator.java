package net.kazior.messenger.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

	public boolean isDateValid(String date, String format) {
		if (date.length() != format.length())
			return false;
		try {
			DateFormat df = new SimpleDateFormat(format);
			df.setLenient(false);

			df.parse(date); // exception is not thrown if day and month is
			// correct AND the first char of year is a digit

			// so if we have correct day and correct month
			// and we know the year has 4 chars we can try to parse it
			Integer year = Integer.parseInt(date.substring(6, 10));

			if (year >= 1900 && year <= 2015) // here we know that the year is 4
				// digit integer
				return true; // and we can limit it
			else
				return false;

		} catch (ParseException e) {
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isPasswordValid(String password) {
		if (password.length() > 50)
			return false;
		String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{5,10}";
		
//		(?=.*[0-9]) a digit must occur at least once
//		(?=.*[a-z]) a lower case letter must occur at least once
//		(?=.*[A-Z]) an upper case letter must occur at least once
//		(?=.*[!@#$%^&+=]) a special character must occur at least once
//		(?=\\S+$) no whitespace allowed in the entire string
//		.{8,} at least 8 characters
		
		Pattern pattern = Pattern.compile(passwordPattern);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();

	}

	public boolean isEmailValid(final String email) {
		if (email.length() > 50)
			return false;
		String emailPattern =
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
//		^			#start of the line
//		  [_A-Za-z0-9-\\+]+	#  must start with string in the bracket [ ], must contains one or more (+)
//		  (			#   start of group #1
//		    \\.[_A-Za-z0-9-]+	#     follow by a dot "." and string in the bracket [ ], must contains one or more (+)
//		  )*			#   end of group #1, this group is optional (*)
//		    @			#     must contains a "@" symbol
//		     [A-Za-z0-9-]+      #       follow by string in the bracket [ ], must contains one or more (+)
//		      (			#         start of group #2 - first level TLD checking
//		       \\.[A-Za-z0-9]+  #           follow by a dot "." and string in the bracket [ ], must contains one or more (+)
//		      )*		#         end of group #2, this group is optional (*)
//		      (			#         start of group #3 - second level TLD checking
//		       \\.[A-Za-z]{2,}  #           follow by a dot "." and string in the bracket [ ], with minimum length of 2
//		      )			#         end of group #3
//		$			#end of the line
		
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean isNameValid(String firstName) {
		if (firstName.length() < 2 || firstName.length() > 50)
			return false;
		else {
			char[] array = firstName.toCharArray();
			if (!(array[0] >= 'A' && array[0] <= 'Z'))
				return false;
			for (int i=1; i < array.length; i++)
				if (!(array[i] >= 'a' && array[i] <= 'z'))
					return false;
			return true;
		}
	}
}
