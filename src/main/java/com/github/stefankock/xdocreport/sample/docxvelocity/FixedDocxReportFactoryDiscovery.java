package com.github.stefankock.xdocreport.sample.docxvelocity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.docx.DocxConstants;
import fr.opensagres.xdocreport.document.docx.DocxReport;
import fr.opensagres.xdocreport.document.docx.discovery.DocxReportFactoryDiscovery;

public class FixedDocxReportFactoryDiscovery extends DocxReportFactoryDiscovery {

	@Override
	public IXDocReport createReport() {
		return new FixedDocxReport();
	}

	/**
	 * Fixes invalid structure when merge fields are filled with XHTML.
	 */
	public static class FixedDocxReport extends DocxReport {

		private static final long serialVersionUID = -6155041405485955581L;

		@Override
		protected void doPostprocessIfNeeded(XDocArchive outputArchive) {

			super.doPostprocessIfNeeded(outputArchive);

			ByteArrayInputStream entryInputStream = (ByteArrayInputStream) outputArchive.getEntryInputStream(DocxConstants.WORD_DOCUMENT_XML_ENTRY);
			try {

				// XXX Remove invalid nesting "w:t w:r w:t": Remove the outer nodes (open and close)
				String originalString = IOUtils.toString(entryInputStream, StandardCharsets.UTF_8);
				String cleanedString = originalString.replace("<w:t><w:r>", "").replace("</w:r></w:t>", "");

				XDocArchive.setEntry(
					outputArchive,
					DocxConstants.WORD_DOCUMENT_XML_ENTRY,
					new ByteArrayInputStream(cleanedString.getBytes(StandardCharsets.UTF_8)));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
}
