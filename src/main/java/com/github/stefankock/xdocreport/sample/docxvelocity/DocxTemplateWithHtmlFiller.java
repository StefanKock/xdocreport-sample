package com.github.stefankock.xdocreport.sample.docxvelocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DocxTemplateWithHtmlFiller {

	private static final XDocReportRegistry REPORT_REGISTRY = new XDocReportRegistry() {

		private static final long serialVersionUID = 1477457033772462428L;

		protected void onStartInitialization() {
			registerInstance(new FixedDocxReportFactoryDiscovery());
		}
	};

	private final Map<String, Object> xdocBeans;
	private final Map<String, SyntaxKind> syntaxKinds;

	public DocxTemplateWithHtmlFiller() {

		xdocBeans = new LinkedHashMap<>();
		syntaxKinds = new LinkedHashMap<>();
	}

	/**
	 * Data to bind to template.
	 * 
	 * @return
	 */
	public Map<String, Object> getXdocBeans() {
		return xdocBeans;
	}

	/**
	 * To {@link #getXdocBeans()} belonging {@link SyntaxKind}s (optional).
	 */
	public Map<String, SyntaxKind> getSyntaxKinds() {
		return syntaxKinds;
	}

	/**
	 * Merge field key examples:<br />
	 * <i>$key.valueAttribute1</i><br />
	 * <i>$key.valueAttribute2</i><br />
	 * <i>$key.valueAttribute3.subattribute1</i>
	 */
	public void putXdocBean(String key, Object value) {
		putXdocBean(key, value, null);
	}

	/**
	 * Merge field key examples:<br />
	 * <i>$key.valueAttribute1</i><br />
	 * <i>$key.valueAttribute2</i><br />
	 * <i>$key.valueAttribute3.subattribute1</i>
	 */
	public void putXdocBean(String key, Object value, SyntaxKind syntaxKind) {
		xdocBeans.put(key, value);
		if (syntaxKind != null) {
			syntaxKinds.put(key, syntaxKind);
		}
	}

	public void process(InputStream templateDocument, OutputStream targetDocument, boolean fixedReport) throws IOException {

		try (InputStream template = templateDocument; OutputStream target = targetDocument) {

			// Load Template
			XDocReportRegistry reportRegistry = fixedReport ? REPORT_REGISTRY : XDocReportRegistry.getRegistry();
			IXDocReport report = reportRegistry.loadReport(template, TemplateEngineKind.Velocity, false);

			// Configure Syntax
			if (!getSyntaxKinds().isEmpty()) {
				FieldsMetadata metadata = report.createFieldsMetadata();
				for (Map.Entry<String, SyntaxKind> entry : getSyntaxKinds().entrySet()) {
					metadata.addFieldAsTextStyling(entry.getKey(), entry.getValue());
				}
			}

			// Put Data 
			IContext context = report.createContext();
			for (Map.Entry<String, Object> xdocBean : getXdocBeans().entrySet()) {
				context.put(xdocBean.getKey(), xdocBean.getValue());
			}

			// Generate report by merging Java model with template
			report.process(context, target);
		} catch (XDocReportException e) {
			throw new RuntimeException(e);
		}
	}
}
