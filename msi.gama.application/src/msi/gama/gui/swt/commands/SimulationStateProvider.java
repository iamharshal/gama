/*********************************************************************************************
 * 
 *
 * 'SimulationStateProvider.java', in plugin 'msi.gama.application', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.gui.swt.commands;

import java.util.*;
import msi.gama.runtime.*;
import org.eclipse.ui.*;
import org.eclipse.ui.services.IServiceLocator;

public class SimulationStateProvider extends AbstractSourceProvider implements ISimulationStateProvider {

	public final static String SIMULATION_RUNNING_STATE = "msi.gama.application.commands.SimulationRunningState";

	private final static Map<String, String> map = new HashMap<String, String>(1);

	@Override
	public void dispose() {}

	@Override
	public void initialize(final IServiceLocator locator) {
		GAMA.controller.state = this;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { SIMULATION_RUNNING_STATE };
	}

	@Override
	public Map<String, String> getCurrentState() {
		map.put(SIMULATION_RUNNING_STATE, GAMA.controller.getFrontmostSimulationState());
		return map;
	}

	/**
	 * Change the UI state based on the state of the simulation (none, stopped, running or notready)
	 */
	@Override
	public void updateStateTo(final String state) {
		fireSourceChanged(ISources.WORKBENCH, SIMULATION_RUNNING_STATE, state);
	}

}