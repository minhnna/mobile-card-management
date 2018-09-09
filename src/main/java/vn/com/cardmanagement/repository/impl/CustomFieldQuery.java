package vn.com.cardmanagement.repository.impl;

import org.springframework.data.mongodb.core.query.Query;

public class CustomFieldQuery extends Query {

	public void excludeKeys(String... keys) {
		for (String key : keys)
			this.fields().exclude(key);
	}

	public void includeKeys(String... keys) {
		for (String key : keys)
			this.fields().include(key);
	}

}
