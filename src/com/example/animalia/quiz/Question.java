package com.example.animalia.quiz;

import java.util.ArrayList;

public class Question {
	private int number;
	private String question;
	private ArrayList<String> options;
	private int answer;
	
	public Question(int number, String question, ArrayList<String> options,
			int answer) {
		this.number = number;
		this.question = question;
		this.options = new ArrayList<String>(options);
		this.answer = answer;
	}

	public int getNumber() {
		return number;
	}

	public String getQuestion() {
		return question;
	}

	public ArrayList<String> getAnswers() {
		return options;
	}

	public int getAnswer() {
		return answer;
	}
	
	

}
