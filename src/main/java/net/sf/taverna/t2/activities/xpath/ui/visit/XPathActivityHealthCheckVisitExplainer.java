package net.sf.taverna.t2.activities.xpath.ui.visit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.taverna.t2.activities.xpath.XPathActivityHealthCheck;
import net.sf.taverna.t2.lang.ui.ReadOnlyTextArea;
import net.sf.taverna.t2.visit.VisitKind;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.workbench.report.explainer.VisitExplainer;
import net.sf.taverna.t2.workbench.report.view.ReportViewConfigureAction;
import net.sf.taverna.t2.workflowmodel.Processor;
import static java.awt.Component.LEFT_ALIGNMENT;
import static javax.swing.BoxLayout.Y_AXIS;
// import status constants
import static net.sf.taverna.t2.activities.xpath.XPathActivityHealthCheck.*;

/**
 * 
 * @author Sergejs Aleksejevs
 */
public class XPathActivityHealthCheckVisitExplainer implements VisitExplainer {
	@Override
	public boolean canExplain(VisitKind vk, int resultId) {
		return vk instanceof XPathActivityHealthCheck;
	}

	/**
	 * This class only handles {@link VisitReport} instances that are of
	 * {@link XPathActivityHealthCheck} kind. Therefore, decisions on the
	 * explanations / solutions are made solely by visit result IDs.
	 */
	@Override
	public JComponent getExplanation(VisitReport vr) {
		return new ReadOnlyTextArea(getExplanationText(vr));
	}

	/**
	 * This class only handles {@link VisitReport} instances that are of
	 * {@link XPathActivityHealthCheck} kind. Therefore, decisions on the
	 * explanations / solutions are made solely by visit result IDs.
	 */
	@Override
	public JComponent getSolution(VisitReport vr) {
		boolean includeConfigButton = (vr.getResultId() != CORRECTLY_CONFIGURED);
		String explanation = getSolutionText(vr);

		JPanel jpSolution = new JPanel();
		jpSolution.setLayout(new BoxLayout(jpSolution, Y_AXIS));

		ReadOnlyTextArea taExplanation = new ReadOnlyTextArea(explanation);
		taExplanation.setAlignmentX(LEFT_ALIGNMENT);
		jpSolution.add(taExplanation);

		if (includeConfigButton) {
			JButton button = new JButton(
					"Open XPath Activity configuration dialog");
			button.setAction(new ReportViewConfigureAction((Processor) vr
					.getSubject()));
			button.setAlignmentX(LEFT_ALIGNMENT);
			jpSolution.add(button);
		}

		return jpSolution;
	}

	/**
	 * Produce a human-readable explanation of what is wrong with an XPath
	 * activity, as discovered by the health checker.
	 */
	public String getExplanationText(VisitReport vr) {
		switch (vr.getResultId()) {
		case CORRECTLY_CONFIGURED:
			return "No problem found";
		case EMPTY_XPATH_EXPRESSION:
			return "XPath expression that this activity would apply to "
					+ "the XML document at its input is not set";
		case INVALID_XPATH_EXPRESSION:
			return "XPath expression that this activity would apply to "
					+ "the XML document at its input is invalid or ill-formed";
		case GENERAL_CONFIG_PROBLEM:
			return "Configuration of this XPath activity is not valid";
		case NO_EXAMPLE_DOCUMENT:
			return "Current configuration of this XPath activity does not "
					+ "contain an example XML document, from the tree structure "
					+ "of which the XPath expression could be generated "
					+ "automatically.\n\n"
					+ "This means that you have manually added the XPath "
					+ "expression - this is fine, but semantical mistakes can "
					+ "be easily introduced into the XPath expression in this "
					+ "case.";
		case MISSING_NAMESPACE_MAPPINGS:
			return "Current configuration of this XPath activity has some "
					+ "missing namespace mappings for the specified XPath "
					+ "expression. This is a severe problem and prevents from "
					+ "execution of the XPath activity.\n\n"
					+ "This may have happened if you have typed in (or "
					+ "pasted) the XPath expression, but did not manually add "
					+ "the required XML namespace mappings.\n\n"
					+ "However, if the XPath expression was generated "
					+ "automatically (by selecting desired element from the "
					+ "example XML tree structure), this may indicate a "
					+ "problem with the XPath Activity plugin itself.";
		default:
			return "Unknown issue - no expalanation available";
		}
	}

	/**
	 * Produce a human-readable explanation of what to do about a problem with
	 * an XPath activity.
	 */
	public String getSolutionText(VisitReport vr) {
		switch (vr.getResultId()) {
		case CORRECTLY_CONFIGURED:
			return "No change necessary";
		case EMPTY_XPATH_EXPRESSION:
			return "Enter the XPath expression manually or paste an example "
					+ "XML document and select desired element from the "
					+ "automatically-generated tree structure:";
		case INVALID_XPATH_EXPRESSION:
			return "Please check correctness of the XPath expression:";
		case GENERAL_CONFIG_PROBLEM:
			return "Please check configuration of the XPath activity:";
		case NO_EXAMPLE_DOCUMENT:
			return "Example XML document can be added in the configuration "
					+ "panel:";
		case MISSING_NAMESPACE_MAPPINGS:
			return "Please try to re-select the desired node from the XML "
					+ "tree structure of the example XML document or manually "
					+ "add missing namespace mappings in the configuration "
					+ "panel:";
		default:
			return "Unknown issue - no expalanation available";
		}
	}
}
