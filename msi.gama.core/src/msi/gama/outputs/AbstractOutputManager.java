/**
 * Created by drogoul, 9 juin 2013
 * 
 */
package msi.gama.outputs;

import java.util.*;
import msi.gama.common.util.GuiUtils;
import msi.gama.runtime.IScope;
import msi.gaml.compilation.*;
import msi.gaml.descriptions.IDescription;
import com.google.common.collect.*;

/**
 * Class AbstractOutputManager.
 * 
 * @author drogoul
 * @since 9 juin 2013
 * 
 */
public abstract class AbstractOutputManager extends Symbol implements IOutputManager {

	protected final Map<String, IOutput> outputs = new LinkedHashMap<String, IOutput>();

	/**
	 * @param desc
	 */
	public AbstractOutputManager(final IDescription desc) {
		super(desc);
	}

	@Override
	public IOutput getOutput(final String id) {
		return outputs.get(id);
	}

	public IOutput getOutputWithName(final String name) {
		for ( final IOutput output : outputs.values() ) {
			if ( output.getName().equals(name) ) { return output; }
		}
		return null;
	}

	@Override
	public void addOutput(final IOutput output) {
		if ( output == null || outputs.containsValue(output) ) { return; }
		outputs.put(output.getId(), output);
	}

	@Override
	public synchronized void dispose() {
		super.dispose();
		try {
			for ( final IOutput output : outputs.values() ) {
				output.dispose();
			}
			outputs.clear();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeOutput(final IOutput o) {
		if ( o == null ) { return; }
		if ( o.isUserCreated() ) {
			o.dispose();
			outputs.values().remove(o);
		} else {
			o.pause();
		}
	}

	@Override
	public void setChildren(final List<? extends ISymbol> commands) {
		for ( final ISymbol s : commands ) {
			if ( s instanceof IOutput ) {
				IOutput o = (IOutput) s;
				addOutput(o);
				o.setUserCreated(false);
			}
		}
	}

	@Override
	public void forceUpdateOutputs() {
		for ( final IDisplayOutput o : getDisplayOutputs() ) {
			o.update();
		}
	}

	private Iterable<IDisplayOutput> getDisplayOutputs() {
		return Iterables.filter(outputs.values(), IDisplayOutput.class);
	}

	public Map<String, IOutput> getOutputs() {
		return outputs;
	}

	@Override
	public boolean init(final IScope scope) {
		for ( final IOutput output : outputs.values() ) {
			if ( scope.init(output) ) {
				output.resume();
				if ( scope.step(output) ) {
					try {
						output.open();
					} catch (RuntimeException e) {
						GuiUtils.debug("AbstractOutputManager.step " + e.getMessage());
						return false;
					}
				}
			}
		}
		OutputSynchronizer.waitForViewsToBeInitialized();
		GuiUtils.informStatus("Simulation ready");
		return true;

	}

	@Override
	public boolean step(final IScope scope) {
		final int cycle = scope.getClock().getCycle();
		for ( final IOutput o : ImmutableList.copyOf(outputs.values()) ) {
			if ( !o.isPaused() && o.isOpen() ) {
				final long ii = o.getNextTime();
				if ( cycle >= ii ) {
					if ( scope.step(o) ) {
						try {
							o.update();
						} catch (RuntimeException e) {
							GuiUtils.debug("AbstractOutputManager.step " + e.getMessage());
							continue;
							// return false;
						}
						o.setNextTime(cycle + o.getRefreshRate());
					}
					// else {
					// return false;
					// }
				}
			}
		}
		return true;
	}

}