/*
 * GAMA - V1.4 http://gama-platform.googlecode.com
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

import msi.gama.gui.application.views.LayeredDisplayView;
import msi.gama.gui.graphics.IDisplaySurface;
import org.eclipse.core.commands.*;

public class ZoomInHandler extends AbstractGamaHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final LayeredDisplayView view =
			(LayeredDisplayView) getView(event, LayeredDisplayView.class);
		if ( view == null ) { return null; }
		new Thread(new Runnable() {

			@Override
			public void run() {
				IDisplaySurface imageCanvas = view.getDisplaySurface();
				while (!imageCanvas.canBeUpdated()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {

					}
				}
				imageCanvas.zoomIn();

			}
		}).start();
		return null;
	}
}
