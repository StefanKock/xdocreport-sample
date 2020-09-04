package com.github.stefankock.xdocreport.sample.docxvelocity;

import java.util.ArrayList;
import java.util.List;

public class TemplateRoot {

	private String plainAttribute;
	private String htmlAttribute;
	private List<TemplateEntry> entries = new ArrayList<>();

	public String getPlainAttribute() {
		return plainAttribute;
	}

	public void setPlainAttribute(String plainAttribute) {
		this.plainAttribute = plainAttribute;
	}

	public String getHtmlAttribute() {
		return htmlAttribute;
	}

	public void setHtmlAttribute(String htmlAttribute) {
		this.htmlAttribute = htmlAttribute;
	}

	public List<TemplateEntry> getEntries() {
		return entries;
	}
}
