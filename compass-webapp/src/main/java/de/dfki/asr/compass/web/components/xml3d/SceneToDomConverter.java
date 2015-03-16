/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components.xml3d;

import de.dfki.asr.compass.web.exception.SceneToDomConversionException;
import de.dfki.asr.compass.model.SceneNode;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SceneToDomConverter implements Serializable {

	private static final long serialVersionUID = 4377020818289951599L;

	public SceneToDomConverter() {}

	protected Document document;

	public String convertToDomAsString(SceneNode node, Map<String, Object> attributes)
			throws SceneToDomConversionException {
		resetData();
		createNewDocument();
		fillDocument(node, attributes);
		return serializeDocumentToString();
	}

	protected void resetData() {
		document = null;
	}

	private void createNewDocument() throws SceneToDomConversionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new SceneToDomConversionException("Could not create DocumentBuilder", e);
		}
	}

	private void fillDocument(SceneNode node, Map<String, Object> attributes) {
		Element xml3dRoot = createXML3DTag(node, attributes);
		document.appendChild(xml3dRoot);
	}

	private Element createXML3DTag(SceneNode node, Map<String, Object> attributes) {
		String width = (String) attributes.get("width");
		String height = (String) attributes.get("height");
		String style = (String) attributes.get("style");
		Element xml3d = document.createElementNS("http://www.xml3d.org/2009/xml3d", "xml3d");
		xml3d.setAttribute("width", width);
		xml3d.setAttribute("height", height);
		xml3d.setAttribute("style", style);
		xml3d.setAttribute("id", "editor_scene");
		xml3d.setAttribute("root_scenenode_id", String.valueOf(node.getId()));
		return xml3d;
	}

	private String serializeDocumentToString() throws SceneToDomConversionException {
		StringWriter xmlResultWriter = new StringWriter();
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer xmlTransformer = factory.newTransformer();
			xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			xmlTransformer.transform(new DOMSource(document), new StreamResult(xmlResultWriter));
		} catch (TransformerConfigurationException e) {
			throw new SceneToDomConversionException("Could not create XML transformer", e);
		} catch (TransformerException e) {
			throw new SceneToDomConversionException("Could not transform document to string", e);
		}
		return xmlResultWriter.getBuffer().toString();
	}
}
