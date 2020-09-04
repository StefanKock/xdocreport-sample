package com.github.stefankock.xdocreport.sample.docxvelocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import fr.opensagres.xdocreport.core.document.SyntaxKind;

public class DocxTemplateWithHtmlFillerTest {

	@Test
	public void testFillTemplate() throws IOException, URISyntaxException {

		DocxTemplateWithHtmlFiller filler = new DocxTemplateWithHtmlFiller();
		filler.getSyntaxKinds().put("Template.htmlAttribute", SyntaxKind.Html);
		filler.getSyntaxKinds().put("entry.htmlAttribute", SyntaxKind.Html);

		TemplateRoot root = new TemplateRoot();
		root.setPlainAttribute("some example test\n2nd line");
		root.setHtmlAttribute(formatContent("Some <u>underlined text</u><ul><li>1st</li><li>2nd</li></ul>"));
		{
			TemplateEntry entry = new TemplateEntry();
			entry.setPlainAttribute("First entry\nwith text");
			entry.setHtmlAttribute(formatContent("Some <u>bold text</u><ul><li>3rd</li><li>4th</li></ul>"));
			root.getEntries().add(entry);
		}
		{
			TemplateEntry entry = new TemplateEntry();
			entry.setPlainAttribute("Second entry");
			entry.setHtmlAttribute(
				formatContent("Some <i>italic text</i></u><br>with second line<ul><li></b>5th</b></li><li>6th</li><li>7th</li></ul>"));
			root.getEntries().add(entry);
		}

		filler.putXdocBean("Template", root);

		try (FileOutputStream targetDocument = new FileOutputStream(new File("FilledInvalidTemplate.docx"))) {
			filler.process(getClass().getClassLoader().getResourceAsStream("Template.docx"), targetDocument, false);
		}
		try (FileOutputStream targetDocument = new FileOutputStream(new File("FilledFixedTemplate.docx"))) {
			filler.process(getClass().getClassLoader().getResourceAsStream("Template.docx"), targetDocument, true);
		}
	}

	private static String formatContent(String content) {

		// Force to a valid XHTML structure (for example <br> to <br />)
		final Document document = Jsoup.parseBodyFragment(content);
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

		final String sanitizedContent = document.body().html();
		return sanitizedContent;
	}
}
