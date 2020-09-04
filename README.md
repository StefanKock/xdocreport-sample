# xdocreport-sample

Showcase to bug report for xdocreport

Problem: Filled Merge fields with `SyntaxKind.Html` are filled with nested `<w:t><w:r><w:t>` structure with seems to be invalid. At least LibreOffice 6 drops partly document data.

1. `FilledInvalidTemplate.docx`: Filled with some merge fields with `SyntaxKind.Html`. Some text is lost when opened in LibreOffice.
2. `FilledInvalidTemplate.pdf`: Opened with LibreOffice and exported as PDF. The first text in the HTML field is lost.
3. `FilledFixedTemplate.docx`: Sanitized `word/document.xml`, no loss in LibreOffice any more.
4. `FilledFixedTemplate.pdf`: Opened with LibreOffice and exported as PDF. No loss.

`DocxTemplateWithHtmlFillerTest` generates the .docx files.
