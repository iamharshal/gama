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
package msi.gama.internal.descriptions;

import java.util.*;
import msi.gama.interfaces.*;
import msi.gama.internal.compilation.*;
import msi.gama.internal.expressions.*;
import msi.gama.kernel.exceptions.GamlException;
import msi.gama.lang.utils.ISyntacticElement;
import msi.gama.skills.Skill;
import msi.gama.util.GamaList;
import msi.gaml.control.*;

public abstract class ExecutionContextDescription extends SymbolDescription {

	protected Map<String, CommandDescription> behaviors;
	protected Map<String, CommandDescription> aspects;
	protected Map<String, CommandDescription> actions;
	protected Map<String, VariableDescription> variables;
	protected final List<String> sortedVariableNames;
	protected final List<String> updatableVariableNames;
	protected Set<Class> skillsClasses;
	protected Map<String, Class> skillsMethods;
	protected final Map<Class, ISkill> skillInstancesByClass;
	protected final Map<String, ISkill> skillInstancesByMethod;

	protected Class javaBase;
	protected IAgentConstructor agentConstructor;

	protected IControl control;

	protected int varCount = 0;
	protected SpeciesDescription macroSpecies;
	protected SpeciesDescription parentSpecies;

	/** Indicate that this micro-species is copied from the parent species or not. */
	protected boolean isCopy = false;

	public ExecutionContextDescription(final String keyword, final IDescription superDesc,
		final Facets facets, final List<IDescription> children, final ISyntacticElement source)
		throws GamlException {
		super(keyword, superDesc, facets, children, source);
		skillInstancesByClass = new HashMap();
		skillInstancesByMethod = new HashMap();
		sortedVariableNames = new ArrayList();
		updatableVariableNames = new ArrayList();

		// "world_species" has ModelDescription as superDesc
		if ( superDesc instanceof SpeciesDescription ) {
			macroSpecies = (SpeciesDescription) superDesc;
		}
	}

	@Override
	protected void initFields() {
		super.initFields();

		behaviors = new HashMap<String, CommandDescription>();
		aspects = new HashMap<String, CommandDescription>();
		actions = new HashMap<String, CommandDescription>();
		variables = new HashMap<String, VariableDescription>();
		skillsClasses = new HashSet();
		skillsMethods = new HashMap();
	}

	protected void setSkills(final ExpressionDescription s) {
		if ( s != null ) {
			final String[] skillNames = s.getString().split(",");
			for ( final String skill : skillNames ) {
				final Class c = Skill.getSkillClassFor(skill.trim());
				if ( c != null ) {
					addSkill(c);
				}
			}
		}
		addSkill(Skill.getSkillClassFor(facets.getString(ISpecies.CONTROL)));
		// ADDED HERE, BUT SHOULD BE MOVED ELSEWHERE
	}

	/**
	 * Finalizes the description:
	 * + Copy the behaviors, attributes from parent;
	 * + Creates the control if necessary;
	 * + Verifies the spatial level value.
	 * 
	 * @throws GamlException
	 */
	public void finalizeDescription() throws GamlException {
		copyItemsFromParent();
		createControl();
		buildSharedSkills();
	}

	protected abstract void copyItemsFromParent() throws GamlException;

	/**
	 * Returns the level of this species.
	 * "world" species is the top level species having 0 as level.
	 * level of a species is equal to level of its direct macro-species plus 1.
	 * 
	 * @return
	 */
	public int getLevel() {
		// "world_species" has ModelDescription as enclosing description.
		if ( enclosing instanceof ModelDescription ) { return 0; }

		return ((ExecutionContextDescription) enclosing).getLevel() + 1;
	}

	public String getControlName() {
		String controlName = facets.getString(ISpecies.CONTROL);

		// if the "control" is not explicitly declared then inherit it from the parent species.
		if ( controlName == null && parentSpecies != null ) {
			controlName = parentSpecies.getControlName();
		}

		return controlName;
	}

	protected void createControl() {
		String keyword = getControlName();
		control =
			ISpecies.FSM.equals(keyword) ? new FsmBehavior() : ISpecies.EMF.equals(keyword)
				? new EmfBehavior() : new ReflexControl();
	}

	public ISkill getSharedSkill(final Class c) {
		return skillInstancesByClass.get(c);
	}

	public ISkill getSkillFor(final String methodName) {
		return skillInstancesByMethod.get(methodName);
	}

	public Class getSkillClassFor(final String getterName) {
		return skillsMethods.get(getterName);
	}

	private void buildSharedSkills() {
		for ( final Class c : new HashSet<Class>(skillsMethods.values()) ) {
			if ( Skill.class.isAssignableFrom(c) ) {
				ISkill skill;
				if ( IControl.class.isAssignableFrom(c) && control != null ) {
					// In order to avoid having two objects of the same class
					skill = control;
				} else {
					skill = Skill.createSharedSkillFor(c);
				}
				skillInstancesByClass.put(c, skill);
				// skill.initializeFor(scope);
			} else {
				skillInstancesByClass.put(c, null);
			}
		}
		for ( final String s : skillsMethods.keySet() ) {
			final Class c = skillsMethods.get(s);
			addSkill(s, skillInstancesByClass.get(c));
		}
	}

	public void addSkill(final String methodName, final ISkill skill) {
		skillInstancesByMethod.put(methodName, skill);
	}

	@Override
	public IDescription addChild(final IDescription child) throws GamlException {
		IDescription desc = super.addChild(child);

		if ( desc.getKeyword().equals(ISymbol.REFLEX) ) {
			addBehavior((CommandDescription) desc);
		} else if ( desc.getKeyword().equals(ISymbol.ACTION) ) {
			addAction((CommandDescription) desc);
		} else if ( desc.getKeyword().equals(ISymbol.ASPECT) ) {
			addAspect((CommandDescription) desc);
		} else if ( desc.getKeyword().equals(ISymbol.PRIMITIVE) ) {
			addAction((CommandDescription) desc);
		} else if ( desc instanceof CommandDescription && !ISymbol.INIT.equals(desc.getKeyword()) ) {
			addBehavior((CommandDescription) desc);
		} else if ( desc instanceof VariableDescription ) {
			addVariable((VariableDescription) desc);
		}

		return desc;
	}

	private void addBehavior(final CommandDescription r) throws GamlException {
		String behaviorName = r.getName();
		if ( hasBehavior(behaviorName) ) { throw new GamlException(r.getKeyword() +
			" name already declared : " + behaviorName); }
		behaviors.put(behaviorName, r);
	}

	public boolean hasBehavior(final String a) {
		return behaviors.containsKey(a);
	}

	private void addAction(final CommandDescription ce) throws GamlException {
		String actionName = ce.getName();
		if ( hasAction(actionName) ) {
			CommandDescription existing = getAction(actionName);
			if ( existing.getKeyword().equals(ISymbol.PRIMITIVE) &&
				ce.getKeyword().equals(ISymbol.PRIMITIVE) ) {
				return;
			} else if ( existing.getKeyword().equals(ISymbol.PRIMITIVE) &&
				ce.getKeyword().equals(ISymbol.ACTION) ) { throw new GamlException(
				"action name already declared as a primitive : " + actionName); }
		}
		actions.put(actionName, ce);
		GamaCompiler.registerFunction(actionName, this);
	}

	private void addAspect(final CommandDescription ce) throws GamlException {
		String aspectName = ce.getName();
		if ( aspectName == null ) {
			aspectName = ISymbol.DEFAULT;
			ce.getFacets().putAsLabel(ISymbol.NAME, aspectName);
		}
		if ( !aspectName.equals(ISymbol.DEFAULT) && hasAspect(aspectName) ) { throw new GamlException(
			"aspect name already declared : " + aspectName); }
		aspects.put(aspectName, ce);
	}

	public Set<String> getAspectsNames() {
		return aspects.keySet();
	}

	public CommandDescription getAspect(final String aName) {
		return aspects.get(aName);
	}

	public CommandDescription getAction(final String aName) {
		return actions.get(aName);
	}

	@Override
	protected boolean hasAction(final String a) {
		return actions.containsKey(a);
	}

	public Collection<CommandDescription> getActions() {
		return actions.values();
	}

	public Set<String> getActionsNames() {
		return actions.keySet();
	}

	protected void addVariable(final VariableDescription v) throws GamlException {
		String vName = v.getName();
		if ( hasVar(vName) ) {
			IDescription builtIn = removeChild(variables.get(vName));
			IType bType = builtIn.getTypeOf(builtIn.getFacets().getString(ISymbol.TYPE));
			IType vType = v.getTypeOf(v.getFacets().getString(ISymbol.TYPE));
			if ( bType != vType ) {
				String builtInType = bType.toString();
				String varType = vType.toString();
				throw new GamlException("variable " + vName + " is of type " + builtInType +
					" and cannot be redefined as a " + varType);
			}
			v.copyFrom((VariableDescription) builtIn);
		}
		v.setDefinitionOrder(varCount++);
		variables.put(vName, v);
	}

	protected IDescription removeChild(final IDescription builtIn) {
		children.remove(builtIn);
		return builtIn;
	}

	public IControl getControl() {
		return control;
	}

	public VariableDescription getVariable(final String name) {
		return variables.get(name);
	}

	public Map<String, VariableDescription> getVariables() {
		return variables;
	}

	@Override
	public boolean hasVar(final String a) {
		return variables.containsKey(a);
	}

	@Override
	public IExpression getVarExpr(final String n, final IExpressionFactory f) {
		VariableDescription vd = getVariable(n);
		if ( vd == null ) { return null; }
		return vd.getVarExpr(f);
	}

	public List<String> getVarNames() {
		return sortedVariableNames;
	}

	public List<String> getUpdatableVarNames() {
		return updatableVariableNames;
	}

	protected void sortVars() throws GamlException {
		// OutputManager.debug("***** Sorting variables of " + getNameFacetValue());
		final List<VariableDescription> result = new GamaList();
		final Collection<VariableDescription> vars = variables.values();
		for ( final VariableDescription var : vars ) {
			var.usedVariables(this);
		}
		for ( final VariableDescription var : vars ) {
			var.expandDependencies(new GamaList());
		}
		for ( final VariableDescription toBePlaced : vars ) {
			boolean found = false;
			int i = 0;
			while (!found && i < result.size()) {
				final VariableDescription alreadyInPlace = result.get(i);
				if ( alreadyInPlace.getDependencies().contains(toBePlaced) ) {
					found = true;
				} else {
					i += 1;
				}
			}
			if ( found ) {
				result.add(i, toBePlaced);
			} else {
				result.add(toBePlaced);
			}
		}

		for ( int i = 0; i < result.size(); i++ ) {
			VariableDescription v = result.get(i);
			String s = v.getName();
			sortedVariableNames.add(s);
			if ( v.isUpdatable() ) {
				updatableVariableNames.add(s);
			}
		}

		// GUI.debug("Sorted variable names of " + facets.get(ISymbol.NAME).literalValue() + " are "
		// +
		// sortedVariableNames);
	}

	public Set<String> getBehaviorsNames() {
		return behaviors.keySet();
	}

	public Collection<CommandDescription> getBehaviors() {
		return behaviors.values();
	}

	@Override
	public String toString() {
		return "Description of " + getName();
	}

	public void setJavaBase(final Class c) throws GamlException {
		if ( javaBase != null ) { return; }
		javaBase = c;
		final List<Class> classes =
			GamaCompiler.collectImplementationClasses(javaBase, getSkillClasses());
		final List<IDescription> children = new ArrayList();
		for ( final Class c1 : classes ) {
			children.addAll(GamlCompiler.getVarDescriptions(c1));
			children.addAll(GamlCompiler.getCommandDescriptions(c1));
			List<String> sss = GamlCompiler.getSkillMethods(c1);
			for ( String s : sss ) {
				addSkillMethod(c1, s);
			}

		}
		for ( IDescription v : children ) {
			v.setSuperDescription(this);
			addChild(v);
		}
		cleanDeclarationClasses();
		agentConstructor = GamlCompiler.getAgentConstructor(javaBase);

		if ( agentConstructor == null ) { throw new GamlException("The base class " +
			getJavaBase().getName() + " cannot be used as an agent class"); }
	}

	public IAgentConstructor getAgentConstructor() {
		return agentConstructor;
	}

	public void cleanDeclarationClasses() {
		final Set<Class> allClasses = new HashSet();
		int changes = 0;
		allClasses.addAll(skillsMethods.values());
		for ( final Class c : allClasses ) {
			for ( final String s : skillsMethods.keySet() ) {
				final Class old = skillsMethods.get(s);
				if ( old != c && old.isAssignableFrom(c) ) {
					changes++;
					// OutputManager.debug("Change: " + old.getSimpleName() + " replaced by " +
					// c.getSimpleName() + " in the definition of " + s);
					skillsMethods.put(s, c);
				}
			}
		}
	}

	public void addSkill(final Class c) {
		if ( c != null && ISkill.class.isAssignableFrom(c) && !c.isInterface() ) {
			skillsClasses.add(c);
		}
	}

	public void addSkillMethod(final Class c, final String m) {
		if ( !ISkill.class.isAssignableFrom(c) ) { return; }
		final Class clazz =
		/* c.isInterface() || !Skill.class.isAssignableFrom(c) ? getJavaBase() : */c;
		addSkill(clazz);
		Class old = skillsMethods.get(m);
		if ( old == null || old.isAssignableFrom(clazz) ) {
			skillsMethods.put(m, clazz);
			return;
		}
	}

	public Set<Class> getSkillClasses() {
		return skillsClasses;
	}

	public Class getJavaBase() {
		return javaBase;
	}

	@Override
	public IType getType() {
		return getTypeOf(this.getName());
	}

	public SpeciesDescription getMacroSpecies() {
		return macroSpecies;
	}

	/**
	 * @return
	 */
	public abstract String getParentName();

}
