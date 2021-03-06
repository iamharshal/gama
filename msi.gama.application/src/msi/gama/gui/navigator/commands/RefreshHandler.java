/*********************************************************************************************
 * 
 * 
 * 'RefreshHandler.java', in plugin 'msi.gama.application', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.gui.navigator.commands;

import msi.gama.gui.navigator.GamaNavigator;
import org.eclipse.core.commands.*;
import org.eclipse.ui.*;
import org.eclipse.ui.navigator.CommonViewer;

public class RefreshHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		run();
		return null;
	}

	public static void run() {

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				final IViewPart view = page.findView("msi.gama.gui.view.GamaNavigator");
				if ( view == null ) { return; }
				CommonViewer cm = ((GamaNavigator) view).getCommonViewer();
				if ( cm != null ) {
					cm.refresh();
				}
			}
		});
	}
}
