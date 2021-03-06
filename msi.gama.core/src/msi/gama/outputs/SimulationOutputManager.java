/*********************************************************************************************
 * 
 * 
 * 'SimulationOutputManager.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.outputs;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.common.util.GuiUtils;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.GamlAnnotations.usage;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gaml.descriptions.IDescription;

/**
 * The Class OutputManager.
 * 
 * @author Alexis Drogoul modified by Romain Lavaud 05.07.2010
 */
@symbol(name = IKeyword.OUTPUT, kind = ISymbolKind.OUTPUT, with_sequence = true)
@inside(kinds = { ISymbolKind.MODEL, ISymbolKind.EXPERIMENT })
@doc(value = "`output` blocks define how to visualize a simulation (with one or more display blocks that define separate windows). It will include a set of displays, monitors and files statements. It will be taken into account only if the experiment type is `gui`.",
	usages = { @usage(value = "Its basic syntax is: ", examples = {
		@example(value = "experiment exp_name type: gui {", isExecutable = false),
		@example(value = "   // [inputs]", isExecutable = false),
		@example(value = "   output {", isExecutable = false),
		@example(value = "      // [display, file or monitor statements]", isExecutable = false),
		@example(value = "   }", isExecutable = false), @example(value = "}", isExecutable = false) }) },
	see = { IKeyword.DISPLAY, IKeyword.MONITOR, IKeyword.INSPECT, IKeyword.OUTPUT_FILE })
public class SimulationOutputManager extends AbstractOutputManager {

	public SimulationOutputManager(final IDescription desc) {
		super(desc);
	}

	@Override
	public boolean init(final IScope scope) {
		// GuiUtils.prepareForSimulation((SimulationAgent) scope.getSimulationScope());
		GuiUtils.waitStatus(" Building outputs ");
		return super.init(scope);
	}

	@Override
	public boolean step(final IScope scope) {
		boolean result = super.step(scope);
		if ( !GuiUtils.isInHeadLessMode() && scope.getExperiment() != null &&
			!scope.getExperiment().getSpecies().isBatch() ) {
			GuiUtils.informStatus(scope.getClock().getInfo());
		}
		return result;
	}

}
