package msi.gama.environment;

import java.util.*;
import msi.gama.interfaces.*;
import msi.gama.internal.types.Types;
import msi.gama.kernel.GAMA;
import msi.gama.kernel.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gama.util.graph.GamaPath;
import com.vividsolutions.jts.geom.Envelope;

public abstract class AbstractTopology implements ITopology {

	protected IScope scope;
	protected IGeometry environment;
	protected ISpatialIndex spatialIndex;
	protected IGamaContainer<?, IGeometry> places;
	protected double environmentWidth, environmentHeight;
	protected double environmentMinX, environmentMinY, environmentMaxX, environmentMaxY;
	protected double[] steps;

	public AbstractTopology(final IScope scope, final IGeometry env) {
		environment = env;
		this.scope = scope;
		setEnvironmentBounds();
		spatialIndex =
			scope.getSimulationScope().getModel().getModelEnvironment().getSpatialIndex();
	}

	protected boolean createAgents() {
		return false;
	}

	/**
	 * @see msi.gama.environment.ITopology#initialize(msi.gama.interfaces.IPopulation)
	 */
	@Override
	public void initialize(final IPopulation pop) throws GamaRuntimeException {
		// Create the population from the places of the topology
		if ( !createAgents() ) { return; }
		List<Map<String, Object>> initialValues = new GamaList(places.length());
		Map<String, Object> vars;
		for ( IGeometry g : places ) {
			vars = new HashMap();
			vars.put(ISymbol.SHAPE, g.getGeometry());
			initialValues.add(vars);
		}
		pop.createAgents(scope, places.length(), initialValues, false);

	}

	@Override
	public void removeAgent(final IAgent agent) {
		final GamaGeometry g = agent.getGeometry();
		if ( g == null ) { return; }
		if ( g.isPoint() ) {
			spatialIndex.remove(g.getLocation(), agent);
		} else {
			spatialIndex.remove(g.getEnvelope(), agent);
		}
	}

	/**
	 * @throws GamaRuntimeException
	 * @see msi.gama.environment.ITopology#pathBetween(msi.gama.interfaces.IGeometry,
	 *      msi.gama.interfaces.IGeometry)
	 */
	@Override
	public GamaPath pathBetween(final IGeometry source, final IGeometry target)
		throws GamaRuntimeException {
		return new GamaPath(this, GamaList.with(source.getLocation(), target.getLocation()));
	}

	private void setEnvironmentBounds() {
		Envelope environmentEnvelope = environment.getEnvelope();
		environmentWidth = environmentEnvelope.getWidth();
		environmentHeight = environmentEnvelope.getHeight();
		environmentMinX = environmentEnvelope.getMinX();
		environmentMinY = environmentEnvelope.getMinY();
		environmentMaxX = environmentEnvelope.getMaxX();
		environmentMaxY = environmentEnvelope.getMaxY();
		double biggest = Math.max(environmentWidth, environmentHeight);
		steps = new double[] { biggest / 20, biggest / 10, biggest / 2, biggest };
	}

	@Override
	public void updateAgent(final IAgent agent, final boolean previousShapeIsPoint,
		final GamaPoint previousLoc, final Envelope previousEnv) {
		if ( previousShapeIsPoint && previousLoc != null ) {
			spatialIndex.remove(previousLoc, agent);
		} else if ( !previousShapeIsPoint && previousEnv != null ) {
			spatialIndex.remove(previousEnv, agent);
		}

		GamaGeometry currentShape = agent.getGeometry();
		if ( currentShape == null || currentShape.isPoint() ) {
			spatialIndex.insert(agent.getLocation(), agent);
		} else {
			spatialIndex.insert(currentShape.getEnvelope(), agent);
		}
	}

	@Override
	public IGeometry getEnvironment() {
		return environment;
	}

	@Override
	public GamaPoint normalizeLocation(final GamaPoint point, final boolean nullIfOutside) {
		/* localized for a bit more efficiency */

		// TODO Subclass (or rewrite) this naive implementation to take care of irregular
		// geometries.

		// TODO Take into account the fact that some topologies may consider invalid locations.

		double nil = Double.MAX_VALUE;
		double xx = point.x;
		double yy = point.y;
		double envMinX = this.environmentMinX;
		double envMinY = this.environmentMinY;
		double envMaxX = this.environmentMaxX;
		double envMaxY = this.environmentMaxY;
		double envWidth = this.environmentWidth;
		double envHeight = this.environmentHeight;

		// See if rounding errors of double do not interfere with the computation.
		// In which case, the use of Maths.approxEquals(value1, value2, tolerance) could help.

		if ( envWidth == 0.0 ) {
			xx = xx != envMinX ? nullIfOutside ? nil : envMinX : xx;
		} else if ( xx < envMinX /* && xx > hostMinX - precision */) {
			xx = /* !isTorus ? */nullIfOutside ? nil : envMinX /* : xx % envWidth + envWidth */;
		} else if ( xx >= envMaxX /*- precision*/) {
			xx = /* !isTorus ? */nullIfOutside ? nil : envMaxX /* : xx % envWidth */;
		}
		if ( xx == nil ) { return null; }
		if ( envHeight == 0.0 ) {
			yy = yy != envMinY ? nullIfOutside ? nil : envMinY : yy;
		} else if ( yy < envMinY/* && yy > hostMinY - precision */) {
			yy = /* !isTorus ? */nullIfOutside ? nil : envMinY /* : yy % envHeight + envHeight */;
		} else if ( yy >= envMaxY /*- precision*/) {
			yy = /* !isTorus ? */nullIfOutside ? nil : envMaxY /* : yy % envHeight */;
		}
		if ( yy == nil ) { return null; }
		point.x = xx;
		point.y = yy;
		if ( environment.getGeometry().covers(point) ) { return point; }
		return null;
	}

	@Override
	public GamaPoint getDestination(final GamaPoint source, final int direction,
		final double distance, final boolean nullIfOutside) {
		double cos = distance * MathUtils.cos(direction);
		double sin = distance * MathUtils.sin(direction);
		return normalizeLocation(new GamaPoint(source.x + cos, source.y + sin), nullIfOutside);
	}

	@Override
	public ITopology copy() {
		return _copy();
	}

	@Override
	public String toJava() {
		return "";
	}

	@Override
	public String toGaml() {
		return _toGaml();
	}

	/**
	 * @return a gaml description of the construction of this topology.
	 */
	protected abstract String _toGaml();

	/**
	 * @return a copy of this topology
	 */
	protected abstract ITopology _copy();

	@Override
	public GamaPoint getRandomLocation() {
		// IGeometry g = getRandomPlace();
		// return GeometricFunctions.pointInGeom(g.getInnerGeometry(), GAMA.getRandom());
		// FIXME temporary restriction as places can evolve (since they are agents).
		return GeometricFunctions.pointInGeom(environment.getInnerGeometry(), GAMA.getRandom());
	}

	@Override
	public IGamaContainer<?, IGeometry> getPlaces() {
		return places;
	}

	@Override
	public IAgent getAgentClosestTo(final IGeometry source, final IAgentFilter filter) {
		for ( int i = 0; i < steps.length; i++ ) {
			IAgent min_neighbour = spatialIndex.firstAtDistance(source, steps[i], filter);
			if ( min_neighbour != null ) { return min_neighbour; }
		}
		return null;
	}

	@Override
	public GamaList<IAgent> getNeighboursOf(final IGeometry source, final Double distance,
		final IAgentFilter filter) throws GamaRuntimeException {
		return spatialIndex.allAtDistance(source, distance, filter);
	}

	@Override
	public double getWidth() {
		return environmentWidth;
	}

	@Override
	public double getHeight() {
		return environmentHeight;
	}

	@Override
	public void shapeChanged(final IPopulation pop) {
		setEnvironmentBounds();
		// TODO CHANGE THIS
		for ( IAgent a : pop.getAgentsList() ) {
			a.hostChangesShape();
		}
	}

	@Override
	public void dispose() {
		// host = null;
		spatialIndex = null;
		scope = null;
	}

	/**
	 * @see msi.gama.interfaces.IValue#type()
	 */
	@Override
	public IType type() {
		return Types.get(IType.TOPOLOGY);
	}

	/**
	 * @see msi.gama.environment.ITopology#getAgentsIn(msi.gama.interfaces.IGeometry,
	 *      msi.gama.environment.IAgentFilter, boolean)
	 */
	@Override
	public GamaList<IAgent> getAgentsIn(final IGeometry source, final IAgentFilter f,
		final boolean covered) {
		GamaList<IAgent> result = new GamaList();
		if ( !isValidGeometry(source) ) { return result; }
		Envelope envelope = source.getEnvelope().intersection(environment.getEnvelope());
		GamaList<IAgent> agents = spatialIndex.allInEnvelope(source, envelope, f, covered);
		GamaGeometry sg =
			new GamaGeometry(source.getInnerGeometry().intersection(environment.getInnerGeometry()));
		for ( IAgent ag : agents ) {
			// Geometry g = ag.getInnerGeometry();
			if ( covered ? sg.covers(ag) : sg.intersects(ag) ) {
				result.add(ag);
			}
		}
		return result;
	}

}
