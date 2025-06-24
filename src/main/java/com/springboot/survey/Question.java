package com.springboot.survey;

import java.util.List;

public class Question {
	private String id;
	private String description;
	private List<String> options;
	private String correctOption;

	public Question() {
		super();
	}

	public Question(String id, String description, List<String> options, String correctOption) {
		super();
		this.id = id;
		this.description = description;
		this.options = options;
		this.correctOption = correctOption;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public List<String> getOptions() {
		return options;
	}

	public String getCorrectOption() {
		return correctOption;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", description=" + description + ", options=" + options + ", correctOption="
				+ correctOption + "]";
	}

}
