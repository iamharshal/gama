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
package msi.gama.factories;

import java.util.*;
import msi.gama.factories.SpeciesFactory.SpeciesStructure;
import msi.gama.gui.application.*;
import msi.gama.interfaces.*;
import msi.gama.internal.compilation.ISymbolConstructor;
import msi.gama.internal.descriptions.*;
import msi.gama.kernel.ModelFileManager;
import msi.gama.kernel.exceptions.*;
import msi.gama.lang.utils.ISyntacticElement;
import msi.gama.precompiler.*;
import msi.gama.precompiler.GamlAnnotations.handles;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.uses;
import msi.gama.skills.Skill;

/**
 * Written by drogoul Modified on 27 oct. 2009
 * 
 * @todo Description
 */
@handles({ ISymbolKind.MODEL })
@uses({ ISymbolKind.EXPERIMENT, ISymbolKind.SPECIES, ISymbolKind.ENVIRONMENT, ISymbolKind.OUTPUT,
	ISymbolKind.GAML_LANGUAGE })
// Batch and Outputs are here for the moment, before being moved to the SimulationFactory
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ModelFactory extends SymbolFactory {

	public static class ModelStructure {

		private String							name	= "";

		private final List<SpeciesStructure>	species;

		private List<ISyntacticElement>			globalNodes;

		private List<ISyntacticElement>			modelNodes;

		public ModelStructure() {
			species = new ArrayList<SpeciesStructure>();
		}

		public void setName(final String name) {
			if ( name == null ) { return; }

			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void addSpecies(final SpeciesStructure s) {
			this.species.add(s);
		}

		public List<SpeciesStructure> getSpecies() {
			return species;
		}

		public void setGlobalNodes(final List<ISyntacticElement> globalNodes) {
			this.globalNodes = globalNodes;
		}

		public List<ISyntacticElement> getGlobalNodes() {
			return globalNodes;
		}

		public void setModelNodes(final List<ISyntacticElement> modelNodes) {
			this.modelNodes = modelNodes;
		}

		public List<ISyntacticElement> getModelNodes() {
			return modelNodes;
		}

		@Override
		public String toString() {
			return "model " + name;
		}
	}

	private static final Map<String, Class>	BUILT_IN_SPECIES_CLASSES	= new HashMap();
	// private static final Map<String, SpeciesDescription> BUILT_IN_SPECIES = new HashMap();

	static {
		MultiProperties mp = new MultiProperties();
		try {
			mp = Activator.getGamaProperties(GamaProcessor.SPECIES);
		} catch (GamlException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		}
		for ( String className : mp.keySet() ) {
			try {
				BUILT_IN_SPECIES_CLASSES.put(mp.getFirst(className), Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static boolean isBuiltIn(final String name) {
		return BUILT_IN_SPECIES_CLASSES.containsKey(name);
	}

	/**
	 * Recursively adds micro-species (built from SpeciesStructure) to a species.
	 * 
	 * @param macroSpecies
	 * @param microSpecies
	 * @throws GamlException
	 */
	private void addMicroSpecies(final SpeciesDescription macroSpecies,
		final SpeciesStructure microSpecies) throws GamlException {
		// lose the SourceCodeInformation
		// in fact, the information of (line, column, row) in the org.jdom.Element object is not
		// correct
		SpeciesDescription microSpeciesDesc =
			(SpeciesDescription) createDescription(macroSpecies, null,
				convertTags(microSpecies.getNode()));
		macroSpecies.addChild(microSpeciesDesc);

		for ( SpeciesStructure microSpecStructure : microSpecies.getMicroSpecies() ) {
			addMicroSpecies(microSpeciesDesc, microSpecStructure);
		}
	}

	/**
	 * Recursively complements a species with its micro-species.
	 * Add variables, behaviors (actions, reflex, task, states, ...), aspects to species.
	 * 
	 * @param macroSpecies the macro-species
	 * @param microSpeciesStructure the structure of micro-species
	 */
	private void complementSpecies(final SpeciesDescription macroSpecies,
		final SpeciesStructure microSpeciesStructure) throws GamlException {
		ISyntacticElement microSpeciesNode = microSpeciesStructure.getNode();
		SpeciesDescription speciesDesc =
			macroSpecies.getMicroSpecies(microSpeciesNode.getAttribute(ISymbol.NAME));
		String keyword = getKeyword(microSpeciesNode);
		String context = speciesDesc.getKeyword();
		ISymbolFactory f = chooseFactoryFor(keyword, context);
		List<ISyntacticElement> children = microSpeciesNode.getChildren();

		Set<String> userRedefinedOrNewVars = new HashSet<String>();
		List<IDescription> subDescs = new ArrayList<IDescription>();
		for ( ISyntacticElement child : children ) {
			// if micro-species were already added, no need to re-add them
			if ( !ModelFileManager.SPECIES_NODES.contains(child.getName()) ) {
				IDescription desc = f.createDescription(child, speciesDesc);
				subDescs.add(desc);

				if ( desc instanceof VariableDescription ) {
					userRedefinedOrNewVars.add(desc.getName());
				}
			}
		}
		speciesDesc.addChildren(subDescs);
		speciesDesc.setUserRedefinedAndNewVars(userRedefinedOrNewVars);

		// recursively complement micro-species
		for ( SpeciesStructure microSpec : microSpeciesStructure.getMicroSpecies() ) {
			complementSpecies(speciesDesc, microSpec);
		}
	}

	private synchronized ModelDescription parse(final String fileName) throws GamlException {
		ModelDescription model = new ModelDescription(fileName);

		ModelStructure modelStructure =
			ModelFileManager.getInstance().getModelStructureFrom(fileName);
		model.getFacets().putAsLabel(ISymbol.NAME, modelStructure.getName());

		// Collecting built-in species & species
		Set<SpeciesDescription> builtIn = computeBuiltInSpecies(model);
		SpeciesDescription worldSpeciesDesc = null;

		// Add "world_species" to ModelDescription
		for ( final SpeciesDescription spd : builtIn ) {
			if ( ISymbol.WORLD_SPECIES_NAME.equals(spd.getName()) ) {
				worldSpeciesDesc = spd;
				model.addChild(spd);
				break;
			}
		}

		// Add built-in species to "world_species"
		for ( final SpeciesDescription spd : builtIn ) {
			if ( !ISymbol.WORLD_SPECIES_NAME.equals(spd.getName()) ) {
				worldSpeciesDesc.addChild(spd);
			}
		}

		// recursively add user-defined species to world species and down on to the species hierarchy
		for ( SpeciesStructure speciesStructure : modelStructure.getSpecies() ) {
			addMicroSpecies(worldSpeciesDesc, speciesStructure);
		}

		// Complementing the world
		ISymbolFactory f = chooseFactoryFor(ISymbol.GLOBAL, null);
		List<IDescription> subDescs = new ArrayList();
		Set<String> userRedefinedOrNewVars = new HashSet<String>();
		for ( final ISyntacticElement e : modelStructure.getGlobalNodes() ) {
			for ( ISyntacticElement child : e.getChildren() ) {
				IDescription desc = f.createDescription(child, worldSpeciesDesc);
				subDescs.add(desc);

				if ( desc instanceof VariableDescription ) {
					userRedefinedOrNewVars.add(desc.getName());
				}
			}
		}
		worldSpeciesDesc.addChildren(subDescs);
		worldSpeciesDesc.setUserRedefinedAndNewVars(userRedefinedOrNewVars);

		// Complementing species
		for ( SpeciesStructure specStructure : modelStructure.getSpecies() ) {
			complementSpecies(worldSpeciesDesc, specStructure);
		}

		// Inheritance (of attributes, actions, primitives, control, ... ) between parent-species & sub-species
		worldSpeciesDesc.finalizeDescription();

		// Inheritance of micro-species between parent-species & sub-species
		worldSpeciesDesc.inheritMicroSpecies();

		// Parse the other definitions (output, environment, batch...)
		for ( final ISyntacticElement e : modelStructure.getModelNodes() ) {
			model.addChild(createDescription(e, model));
		}

		return model;
	}

	public static Set<SpeciesDescription> computeBuiltInSpecies(final ModelDescription model)
		throws GamlException {
		Set<SpeciesDescription> builtInSpecies = new HashSet();

		// Firstly, create "world_species" (defined in WorldSkill) SpeciesDescription with
		// ModelDescription as SuperDescription
		String facets[] = new String[0];
		Class worldSkill = BUILT_IN_SPECIES_CLASSES.get(ISymbol.WORLD_SPECIES_NAME);
		skill s = (skill) worldSkill.getAnnotation(skill.class);
		if ( s != null ) {
			String[] names = s.value();
			String skillName = names[0];
			facets =
				new String[] { /* ISymbol.SPECIES, */ISymbol.NAME, ISymbol.WORLD_SPECIES_NAME,
					ISpecies.BASE,
					BUILT_IN_SPECIES_CLASSES.get(ISymbol.DEFAULT).getCanonicalName(),
					ISpecies.SKILLS, skillName };
		}
		SpeciesDescription worldSpeciesDescription =
			(SpeciesDescription) DescriptionFactory.createDescription(ISymbol.SPECIES, model,
				facets);
		builtInSpecies.add(worldSpeciesDescription);

		// Secondly, create other built-in SpeciesDescriptions with worldSpeciesDescription as
		// SuperDescription
		for ( String speciesName : BUILT_IN_SPECIES_CLASSES.keySet() ) {
			if ( !ISymbol.WORLD_SPECIES_NAME.equals(speciesName) ) {
				Class c = BUILT_IN_SPECIES_CLASSES.get(speciesName);
				facets =
					new String[] { /* ISymbol.SPECIES, */ISymbol.NAME, speciesName, ISpecies.BASE,
						c.getCanonicalName() };
				if ( Skill.class.isAssignableFrom(c) ) {
					s = (skill) c.getAnnotation(skill.class);
					if ( s != null ) {
						String[] names = s.value();
						String skillName = names[0];
						facets =
							new String[] { /* ISymbol.SPECIES, */ISymbol.NAME, speciesName,
								ISpecies.BASE,
								BUILT_IN_SPECIES_CLASSES.get(ISymbol.DEFAULT).getCanonicalName(),
								ISpecies.SKILLS, skillName };
					}
				}
				SpeciesDescription sd =
					(SpeciesDescription) DescriptionFactory.createDescription(ISymbol.SPECIES,
						worldSpeciesDescription, facets);
				builtInSpecies.add(sd);
				// OutputManager.debug("Built-in species " + speciesName +
				// " created with Java support in " + c.getSimpleName());
			}
		}
		return builtInSpecies;
	}

	@Override
	protected ISymbol compileSymbol(final IDescription desc, final ISymbolConstructor c)
		throws GamlException, GamaRuntimeException {
		ISymbol cs = super.compileSymbol(desc, c);
		return cs;
	}

	@Override
	public synchronized ISymbol compileFile(final String modelFileName) throws GamlException,
		GamaRuntimeException, InterruptedException {
		IModel m = null;
		// long startTime = System.nanoTime();
		ModelDescription md = parse(modelFileName);
		GUI.stopIfCancelled();
		if ( !md.hasExperiment(IModel.DEFAULT_EXPERIMENT) ) {
			IDescription sim =
				DescriptionFactory.createDescription(ISymbol.EXPERIMENT, ISymbol.NAME,
					IModel.DEFAULT_EXPERIMENT, ISymbol.TYPE, ISymbol.GUI_);
			md.addChild(sim);
		}
		GUI.stopIfCancelled();
		m =
			(IModel) compileDescription(md, DescriptionFactory.getModelFactory()
				.getDefaultExpressionFactory());
		GUI.stopIfCancelled();
		// long endTime = System.nanoTime();
		// GUI.debug("#### Parsing + compile time : " + (endTime - startTime) / 1000000000d);
		return m;
	}
}
