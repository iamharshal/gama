/*********************************************************************************************
 * 
 * 
 * 'StringEditor.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.gui.parameters;

import java.util.List;
import msi.gama.common.interfaces.EditorListener;
import msi.gama.kernel.experiment.IParameter;
import msi.gama.metamodel.agent.IAgent;
import msi.gaml.types.*;
import org.eclipse.swt.widgets.Composite;

public class StringEditor extends ExpressionBasedEditor<String> {

	StringEditor(final IAgent agent, final IParameter param, final EditorListener l) {
		super(agent, param, l);
	}

	StringEditor(final Composite parent, final String title, final Object value,
		final EditorListener<String> whenModified) {
		super(new InputParameter(title, value), whenModified);
		this.createComposite(parent);
	}

	StringEditor(final Composite parent, final String title, final String value, final List<String> among,
		final EditorListener<String> whenModified, final boolean asLabel) {
		super(new InputParameter(title, value, Types.STRING, among), whenModified);
		this.createComposite(parent);
	}

	@Override
	public IType getExpectedType() {
		return Types.STRING;
	}

	@Override
	protected int[] getToolItems() {
		return new int[] { REVERT };
	}

}
