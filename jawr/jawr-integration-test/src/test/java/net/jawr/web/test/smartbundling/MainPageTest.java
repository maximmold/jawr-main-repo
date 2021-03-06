/**
 * 
 */
package net.jawr.web.test.smartbundling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

import static org.junit.Assert.assertEquals;

import net.jawr.web.resource.bundle.IOUtils;
import net.jawr.web.test.JawrIntegrationServer;
import net.jawr.web.test.JawrTestConfigFiles;
import net.jawr.web.test.utils.Utils;

/**
 * Test case for standard page in production mode.
 * 
 * @author ibrahim Chaehoi
 */
@JawrTestConfigFiles(webXml = "net/jawr/web/smartbundling/config/web.xml", jawrConfig = "net/jawr/web/smartbundling/config/jawr.properties")
public class MainPageTest extends AbstractSmartBundlingPageTest {

	protected static String getTempFolder() {
		return "jawr-integration-smartbundling-test-1";
	}
	
	@Test
	public void testPageLoad() throws Exception {

		final List<String> expectedAlerts = Collections
				.singletonList("A little message retrieved from the message bundle : Hello $ world!");
		assertEquals(expectedAlerts, collectedAlerts);
		
		assertContentEquals("/net/jawr/web/smartbundling/resources/index-jsp-result-1-expected.txt", page);

		checkStandardGeneratedCssLinks();
		checkStandardGeneratedJsLinks();
		checkStandardJsBundleContent();
		checkStandardCssBundleContent();
		checkStandardGeneratedHtmlImageLinks();
		checkStandardGeneratedHtmlImageInputLinks();
		
		updateContent();
		page = webClient.getPage(getPageUrl());
		assertContentEquals("/net/jawr/web/smartbundling/resources/index-jsp-result-2-expected.txt", page);

		checkUpdatedGeneratedCssLinks();
		checkUpdatedGeneratedJsLinks();
		checkUpdatedJsBundleContent();
		checkUpdatedCssBundleContent();
		checkUpdatedGeneratedHtmlImageLinks();
		checkUpdatedGeneratedHtmlImageInputLinks();
		
	}

	/**
	 * @throws Exception
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void updateContent() throws Exception, InterruptedException, FileNotFoundException, IOException {
		JawrIntegrationServer.getInstance().getJettyWebAppContext().stop();
		
		// Sleep
		Thread.sleep(3000);
		
		// Update CSS and JS file
		updateResources();
		
		JawrIntegrationServer.getInstance().getJettyWebAppContext().start();
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void updateResources() throws FileNotFoundException, IOException {
		InputStream is = getClass().getResourceAsStream("/net/jawr/web/smartbundling/resources/css/three.css");
		OutputStream out = new FileOutputStream(JawrIntegrationServer.getInstance().getWebAppRootDir()+"/smartbundling/css/basic/three.css");
		IOUtils.copy(is, out, true);
		
		is = getClass().getResourceAsStream("/net/jawr/web/smartbundling/resources/js/script.js");
		out = new FileOutputStream(JawrIntegrationServer.getInstance().getWebAppRootDir()+"/smartbundling/js/script.js");
		IOUtils.copy(is, out, true);
	}

	public void checkStandardGeneratedJsLinks() {
		// Test generated Script link
		final List<HtmlScript> scripts = getJsScriptTags();
		assertEquals(1, scripts.size());
		final HtmlScript script = scripts.get(0);
		assertEquals(
				getUrlPrefix()+"/839854056.en_US/js/bundle/msg.js",
				script.getSrcAttribute());
	}

	public void checkStandardJsBundleContent() throws Exception {

		final List<HtmlScript> scripts = getJsScriptTags();
		final HtmlScript script = scripts.get(0);
		final JavaScriptPage page = getJavascriptPage(script);
		assertContentEquals("/net/jawr/web/smartbundling/resources/msg-bundle.js", page);
	}

	public void checkStandardGeneratedCssLinks() {
		// Test generated Css link
		final List<HtmlLink> styleSheets = getHtmlLinkTags();
		assertEquals(1, styleSheets.size());
		final HtmlLink css = styleSheets.get(0);
		assertEquals(
				getUrlPrefix()+"/N2053553881/fwk/core/component.css",
				css.getHrefAttribute());

	}

	public void checkStandardCssBundleContent() throws Exception {

		final List<HtmlLink> styleSheets = getHtmlLinkTags();
		final HtmlLink css = styleSheets.get(0);
		final TextPage page = getCssPage(css);
		assertContentEquals("/net/jawr/web/smartbundling/resources/component-expected.css", page);
	}

	public void checkStandardGeneratedHtmlImageLinks() {
		// Test generated HTML image link
		final List<?> images = getHtmlImageTags();
		assertEquals(1, images.size());
		final HtmlImage img = (HtmlImage) images.get(0);
		Utils.assertGeneratedLinkEquals(getUrlPrefix()+"/cbfc517da02d6a64a68e5fea9a5de472f1/img/appIcons/application.png",
				img.getSrcAttribute());

	}

	public void checkStandardGeneratedHtmlImageInputLinks() {
		// Test generated HTML image link
		final List<HtmlImageInput> images = getHtmlImageInputTags();
		assertEquals(1, images.size());
		final HtmlImageInput img = images.get(0);
		Utils.assertGeneratedLinkEquals(getUrlPrefix()+"/cb30a18063ef42b090194a7e936086960f/img/cog.png", 
				img.getSrcAttribute());

	}
	
	public void checkUpdatedGeneratedJsLinks() {
		// Test generated Script link
		final List<HtmlScript> scripts = getJsScriptTags();
		assertEquals(1, scripts.size());
		final HtmlScript script = scripts.get(0);
		assertEquals(
				getUrlPrefix()+"/995562889.en_US/js/bundle/msg.js",
				script.getSrcAttribute());
	}

	public void checkUpdatedJsBundleContent() throws Exception {

		final List<HtmlScript> scripts = getJsScriptTags();
		final HtmlScript script = scripts.get(0);
		final JavaScriptPage page = getJavascriptPage(script);
		assertContentEquals("/net/jawr/web/smartbundling/resources/msg-bundle-2.js", page);
	}

	public void checkUpdatedGeneratedCssLinks() {
		// Test generated Css link
		final List<HtmlLink> styleSheets = getHtmlLinkTags();
		assertEquals(1, styleSheets.size());
		final HtmlLink css = styleSheets.get(0);
		assertEquals(
				getUrlPrefix()+"/1701415561/fwk/core/component.css",
				css.getHrefAttribute());

	}

	public void checkUpdatedCssBundleContent() throws Exception {

		final List<HtmlLink> styleSheets = getHtmlLinkTags();
		final HtmlLink css = styleSheets.get(0);
		final TextPage page = getCssPage(css);
		assertContentEquals("/net/jawr/web/smartbundling/resources/component-expected-2.css", page);
	}

	public void checkUpdatedGeneratedHtmlImageLinks() {
		// Test generated HTML image link
		final List<?> images = getHtmlImageTags();
		assertEquals(1, images.size());
		final HtmlImage img = (HtmlImage) images.get(0);
		Utils.assertGeneratedLinkEquals(getUrlPrefix()+"/cbfc517da02d6a64a68e5fea9a5de472f1/img/appIcons/application.png",
				img.getSrcAttribute());

	}

	public void checkUpdatedGeneratedHtmlImageInputLinks() {
		// Test generated HTML image link
		final List<HtmlImageInput> images = getHtmlImageInputTags();
		assertEquals(1, images.size());
		final HtmlImageInput img = images.get(0);
		Utils.assertGeneratedLinkEquals(getUrlPrefix()+"/cb30a18063ef42b090194a7e936086960f/img/cog.png", 
				img.getSrcAttribute());

	}
	
}
