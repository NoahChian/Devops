package org.iii.ideas.catering_service.util;

import org.hibernate.dialect.MySQLInnoDBDialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;

public class RegisterFunction extends MySQLInnoDBDialect{
	public RegisterFunction() {
		super();
		registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
		registerFunction("bitwise_andTwo", new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 & ?2)"));
	}
}
