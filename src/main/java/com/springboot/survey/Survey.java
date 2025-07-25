package com.springboot.survey;

import java.util.List;

public class Survey {
	private String id;
	private String title;
	private String description;
	private List<Question> questions;

	public Survey() {
		super();
	}

	public Survey(String id, String title, String description, List<Question> questions) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.questions = questions;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public List<Question> getQuestions() {
		return questions;
	}
	
	public List<Question> addQuestion(Question question) {
		questions.add(question);
		return questions;
	}

	@Override
	public String toString() {
		return "Survey [id=" + id + ", title=" + title + ", description=" + description + ", questions=" + questions
				+ "]";
	}
}
