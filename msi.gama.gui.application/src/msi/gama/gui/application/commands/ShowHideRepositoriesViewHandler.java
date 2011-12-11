/*
 * GAMA - V1.4  http://gama-platform.googlecode.com
 * 
 * (c) 2007-2011 UMI 209 UMMISCO IRD/UPMC 
 * 
 * Developers :
 * 
 * - Alexis Drogoul, IRD (Kernel, Metamodel, XML-based GAML), 2007-2011
 * - Vo Duc An, IRD & AUF (SWT integration, multi-level architecture), 2008-2011
 * - Patrick Taillandier, AUF & CNRS (batch framework, GeoTools & JTS integration), 2009-2011
 * - Pierrick Koch, IRD (XText-based GAML environment), 2010-2011
 * - Romain Lavaud, IRD (project-based environment), 2010
 * - Francois Sempe, IRD & AUF (EMF behavioral model, batch framework), 2007-2009
 * - Edouard Amouroux, IRD (C++ initial porting), 2007-2008
 * - Chu Thanh Quang, IRD (OpenMap integration), 2007-2008
 */
package msi.gama.gui.application.commands;

import msi.gama.gui.application.GUI;
import org.eclipse.core.commands.*;
import org.eclipse.ui.*;

public class ShowHideRepositoriesViewHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final String REPOSITORIES_VIEW_ID = "msi.gama.gui.application.view.RepositoriesView";
		IWorkbenchPage activePage =
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkbenchPart part = activePage.findView(REPOSITORIES_VIEW_ID);

		try {
			if ( !activePage.isPartVisible(part) ) {
				if ( !GUI.isModelingPerspective() ) {
					GUI.openModelingPerspective();
				}
				activePage.showView(REPOSITORIES_VIEW_ID);
			} else {
				if ( GUI.isModelingPerspective() ) {
					activePage.hideView((IViewPart) part);
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
}
