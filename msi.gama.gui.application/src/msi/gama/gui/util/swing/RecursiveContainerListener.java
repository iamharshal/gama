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
package msi.gama.gui.util.swing;

import java.awt.*;
import java.awt.event.*;

/**
 * The listener interface for receiving recursiveContainer events. The class
 * that is interested in processing a recursiveContainer event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addRecursiveContainerListener<code> method. When
 * the recursiveContainer event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see RecursiveContainerEvent
 */
class RecursiveContainerListener implements ContainerListener {

	/** The listener. */
	private final ContainerListener listener;

	/**
	 * Instantiates a new recursive container listener.
	 * 
	 * @param listener
	 *            the listener
	 */
	RecursiveContainerListener(final ContainerListener listener) {
		assert listener != null;

		this.listener = listener;
	}

	/**
	 * Handle add.
	 * 
	 * @param source
	 *            the source
	 * @param c
	 *            the c
	 */
	private void handleAdd(final Container source, final Component c) {
		assert source != null;
		assert c != null;
		assert listener != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		// System.out.println("Listening to: " + c);
		listener.componentAdded(new ContainerEvent(source,
				ContainerEvent.COMPONENT_ADDED, c));
		if (c instanceof Container) ((Container) c).addContainerListener(this);
	}

	/**
	 * Handle remove.
	 * 
	 * @param source
	 *            the source
	 * @param c
	 *            the c
	 */
	private void handleRemove(final Container source, final Component c) {
		assert source != null;
		assert c != null;
		assert listener != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		// System.out.println("Stopped Listening to: " + c);
		listener.componentRemoved(new ContainerEvent(source,
				ContainerEvent.COMPONENT_REMOVED, c));
		if (c instanceof Container)
			((Container) c).removeContainerListener(this);
	}

	/**
	 * Handle all adds.
	 * 
	 * @param source
	 *            the source
	 * @param child
	 *            the child
	 */
	private void handleAllAdds(final Container source, final Component child) {
		assert source != null;
		assert child != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		if (child instanceof Container) {
			final Container container = (Container) child;
			final Component[] children = container.getComponents();
			for (int i = 0; i < children.length; i++)
				handleAllAdds(container, children[i]);
		}
		handleAdd(source, child);
	}

	/**
	 * Handle all removes.
	 * 
	 * @param source
	 *            the source
	 * @param child
	 *            the child
	 */
	private void handleAllRemoves(final Container source, final Component child) {
		assert source != null;
		assert child != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		if (child instanceof Container) {
			final Container container = (Container) child;
			final Component[] children = container.getComponents();
			for (int i = 0; i < children.length; i++)
				handleAllRemoves(container, children[i]);
		}
		handleRemove(source, child);

	}

	/**
	 * 
	 * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
	 */
	@Override
	public void componentAdded(final ContainerEvent e) {
		assert e != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		final Container source = (Container) e.getSource();
		handleAllAdds(source, e.getChild());
	}

	/**
	 * 
	 * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
	 */
	@Override
	public void componentRemoved(final ContainerEvent e) {
		assert e != null;
		assert EventQueue.isDispatchThread(); // On AWT event thread

		final Container source = (Container) e.getSource();
		handleAllRemoves(source, e.getChild());
	}
}
