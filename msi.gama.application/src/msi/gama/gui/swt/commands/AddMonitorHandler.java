/*********************************************************************************************
 * 
 *
 * 'AddMonitorHandler.java', in plugin 'msi.gama.application', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.gui.swt.commands;

import msi.gama.gui.views.MonitorView;
import org.eclipse.core.commands.*;

public class AddMonitorHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		MonitorView.createNewMonitor();
		return null;
	}

}
