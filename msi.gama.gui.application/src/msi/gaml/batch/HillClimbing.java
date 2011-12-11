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
package msi.gaml.batch;

import java.util.*;
import msi.gama.interfaces.*;
import msi.gama.kernel.GAMA;
import msi.gama.kernel.exceptions.GamaRuntimeException;
import msi.gama.kernel.experiment.*;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.*;
import msi.gama.util.Cast;

@symbol(name = IBatch.HILL_CLIMBING, kind = ISymbolKind.BATCH_METHOD)
@inside(kinds = { ISymbolKind.EXPERIMENT })
@facets({
	@facet(name = ISymbol.NAME, type = IType.ID, optional = false),
	@facet(name = HillClimbing.ITER_MAX, type = IType.INT_STR, optional = true),
	@facet(name = ISymbol.MAXIMIZE, type = IType.FLOAT_STR, optional = true),
	@facet(name = ISymbol.MINIMIZE, type = IType.FLOAT_STR, optional = true),
	@facet(name = ISymbol.AGGREGATION, type = IType.LABEL, optional = true, values = { ISymbol.MIN,
		ISymbol.MAX }) })
public class HillClimbing extends LocalSearchAlgorithm {

	protected static final String ITER_MAX = "iter_max";
	private StoppingCriterion stoppingCriterion = null;
	private int maxIt;

	public HillClimbing(final IDescription species) {
		super(species);
	}

	@Override
	public Solution findBestSolution() throws GamaRuntimeException {
		bestSolution = this.solutionInit;
		double currentFitness = currentExperiment.launchSimulationsWithSolution(bestSolution);
		testedSolutions = new Hashtable<Solution, Double>();
		testedSolutions.put(bestSolution, new Double(currentFitness));
		int nbIt = 0;

		final Map<String, Object> endingCritParams = new Hashtable<String, Object>();
		endingCritParams.put("Iteration", Integer.valueOf(nbIt));
		while (stoppingCriterion == null || !stoppingCriterion.stopSearchProcess(endingCritParams)) {
			final List<Solution> neighbors = neighborhood.neighbor(bestSolution);
			if ( neighbors.isEmpty() ) {
				break;
			}
			bestFitness = currentFitness;
			Solution bestNeighbor = null;

			for ( final Solution neighborSol : neighbors ) {
				if ( neighborSol == null ) {
					continue;
				}
				Double neighborFitness = testedSolutions.get(neighborSol);
				if ( neighborFitness == null ) {
					neighborFitness =
						Double
							.valueOf(currentExperiment.launchSimulationsWithSolution(neighborSol));
				}
				testedSolutions.put(neighborSol, neighborFitness);

				if ( isMaximize() && neighborFitness.doubleValue() > bestFitness || !isMaximize() &&
					neighborFitness.doubleValue() < bestFitness ) {
					bestNeighbor = neighborSol;
					bestFitness = neighborFitness.doubleValue();
				}
			}
			if ( bestNeighbor != null ) {
				bestSolution = bestNeighbor;
				currentFitness = bestFitness;
			} else {
				break;
			}
			nbIt++;
			endingCritParams.put("Iteration", Integer.valueOf(nbIt));
		}
		// System.out.println("Best solution : " + currentSol + "  fitness : "
		// + currentFitness);
		return bestSolution;
	}

	@Override
	public void initializeFor(final BatchExperiment f) throws GamaRuntimeException {
		super.initializeFor(f);
		final IExpression maxItExp = getFacet("iter_max");
		if ( maxItExp != null ) {
			maxIt = Cast.asInt(maxItExp.value(GAMA.getDefaultScope()));
			stoppingCriterion = new StoppingCriterionMaxIt(maxIt);
		}

	}

	@Override
	public void addParametersTo(final BatchExperiment exp) {
		super.addParametersTo(exp);
		exp.addMethodParameter(new ParameterAdapter("Maximum number of iterations",
			IExperiment.BATCH_CATEGORY_NAME, IType.INT) {

			@Override
			public Object value() {
				return maxIt;
			}

		});
	}

}
