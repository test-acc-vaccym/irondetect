package de.hshannover.f4.trust.irondetect.policy.publisher.model.identifier.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.IdentifierHandler;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers.Helpers;
import de.hshannover.f4.trust.ifmapj.identifier.Identity;
import de.hshannover.f4.trust.ifmapj.log.IfmapJLog;
import de.hshannover.f4.trust.irondetect.policy.publisher.model.identifier.ExtendedIdentifier;
import de.hshannover.f4.trust.irondetect.policy.publisher.util.PolicyStrings;

/**
 * An {@link ExtendedIdentifierHandler} transforms an {@link ExtendedIdentifier} to a XML {@link Element}. This XML
 * {@link Element} is represented by an {@link Identity} identifier with IdentityType = 'other' and other-type =
 * 'extended'.
 * 
 * @author Marcel Reichenbach
 */
public abstract class ExtendedIdentifierHandler<T extends ExtendedIdentifier> implements IdentifierHandler<T> {

	private static DocumentBuilder mDocumentBuilder;

	static {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			mDocumentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			IfmapJLog.error("Could not get DocumentBuilder instance [" + e.getMessage() + "]");
			throw new RuntimeException(e);
		}
	}

	public abstract Element toExtendedElement(Identifier i, Document doc) throws MarshalException;

	@Override
	public Element toElement(Identifier i, Document doc) throws MarshalException {
		Helpers.checkIdentifierType(i, this);

		Document extendetDocument = mDocumentBuilder.newDocument();

		Element extendetElement = toExtendedElement(i, extendetDocument);
		extendetDocument.appendChild(extendetElement);

		Identity identity = Identifiers.createExtendedIdentity(extendetDocument);

		return Identifiers.toElement(identity, doc);
	}

	@Override
	public T fromElement(Element el) {
		// TODO fromElement (Element el) not implemented

		// return null is necessary for ifmapJ
		return null;
	}

	protected void appendListAsChild(Element parent, List<Element> elementList) {
		for (Element e : elementList) {
			parent.appendChild(e);
		}
	}

	protected Element buildElement(String elementQualifiedName, String textContent, Document doc) {
		Element featureExpressionElement = doc.createElementNS(null, elementQualifiedName);
		featureExpressionElement.setTextContent(textContent);
		return featureExpressionElement;
	}

	protected Element buildParameterExpression(String expression, Document doc) {
		return buildElement(PolicyStrings.PARAMETER_EXPRESSION_EL_NAME, escapeXml(expression), doc);
	}

	protected static String escapeXml(String input) {

		String ret = input;

		String[] unwanted = { "&", "<", ">", "\"", "'" };
		String[] replaceBy = { "&amp;", "&lt;", "&gt;", "&quot;", "&apos;" };

		for (int i = 0; i < unwanted.length; i++) {
			ret = ret.replace(unwanted[i], replaceBy[i]);
		}

		return ret;
	}

	protected Element buildContextElement(String id, List<Element> parameterExpressionElements, Document doc) {
		Element contextElement = doc.createElementNS(null, PolicyStrings.CONTEXT_EL_NAME);
		Element idElement = buildElement(PolicyStrings.ID_EL_NAME, id, doc);

		contextElement.appendChild(idElement);

		for (Element e : parameterExpressionElements) {
			contextElement.appendChild(e);
		}

		return contextElement;
	}

	protected List<Element> buildContextElements(Map<String, List<String>> context, Document doc) {
		List<Element> contextElements = new ArrayList<Element>();

		for (Entry<String, List<String>> entry : context.entrySet()) {
			List<Element> parameterExpressions = new ArrayList<Element>();
			for (String parameterExpression : entry.getValue()) {
				parameterExpressions.add(buildParameterExpression(parameterExpression, doc));
			}
			contextElements.add(buildContextElement(entry.getKey(), parameterExpressions, doc));
		}

		return contextElements;
	}

}
