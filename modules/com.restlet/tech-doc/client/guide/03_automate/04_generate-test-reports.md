When you run your tests, you sometimes want to preserve a trace of their results. That's why Restlet provides a few
ways to generate printable test reports - in HTML of PDF.

<a class="anchor" name="from-the-chrome-extension"></a>
## From the Chrome extension

When you have run a project/service/scenario in Restlet Client, you just need to open it and click on the button
`Print`.

![Print button](./images/print_report_button.png)

If you don't want to print it, you can just export it in PDF format by changing the printer.

![Save as pdf](./images/save_as_pdf.png)

The generated report will look like this:

![Extension report](./images/extension_report.png)

<a class="anchor" name="from-cli"></a>
## From CLI

You can generate HTML reports directly from the command line using surefire with the following commands:

<pre class="language-shell">
<code class="language-shell">
# Runs the API tests
mvn clean test

# Creates a nicely formatted Surefire Test Report in HTML format
# without running the tests as they have been run already
mvn surefire-report:report-only

# The Site Plugin is used to generate a site for the project. The generated
# site also includes the project&apos;s reports that were configured in the POM
mvn site -DgenerateReports=false
</code>
</pre>

The generated output displays a summary of all the test results at the top followed by a detailed view.

![Surefire summary](./images/surefire_summary.png)

![Surefire details](./images/surefire_details.png)

<a class="anchor" name="from-ci"></a>
## From CI

Our CLI tool generates JUnit-like XML files for each test. This means every reporting tool that integrates with this
type of files can generate reports for your API tests too.

Here is an example with Jenkins but most CI tools should have similar capabilities.

<!-- TODO: dodelidoo -->
> Let's rencarde with Antoine on that point
