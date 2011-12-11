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
package msi.gaml.commands;

import msi.gama.interfaces.*;
import msi.gama.internal.compilation.*;
import msi.gama.kernel.exceptions.*;
import msi.gama.precompiler.GamlAnnotations.combination;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.*;
import msi.gama.util.ExecutionStatus;

/**
 * Written by drogoul Modified on 6 févr. 2010
 * 
 * @todo Description
 * 
 */

@facets(value = { @facet(name = ISymbol.VAR, type = IType.NEW_TEMP_ID, optional = true),
	@facet(name = ISymbol.NAME, type = IType.NEW_TEMP_ID, optional = true),
	@facet(name = ISymbol.VALUE, type = { IType.NONE_STR }, optional = true),
	@facet(name = ISymbol.OF, type = { IType.TYPE_ID }, optional = true),
	@facet(name = ISymbol.TYPE, type = { IType.TYPE_ID }, optional = true) },

combinations = { @combination({ ISymbol.VAR, ISymbol.VALUE }),
	@combination({ ISymbol.NAME, ISymbol.VALUE }), })
@symbol(name = { ISymbol.LET }, kind = ISymbolKind.SINGLE_COMMAND)
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_COMMAND })
public class LetCommand extends SetCommand {

	public LetCommand(final IDescription desc) throws GamlException {
		super(desc);
		setName(ISymbol.LET + " " + varExpr.literalValue());
		varExpr.setType(hasFacet(ISymbol.TYPE) ? desc.getTypeOf(getLiteral(ISymbol.TYPE)) : value
			.type());
		varExpr.setContentType(hasFacet(ISymbol.OF) ? desc.getTypeOf(getLiteral(ISymbol.OF))
			: value.getContentType());
	}

	@Override
	protected Object privateExecuteIn(final IScope stack) throws GamaRuntimeException {
		Object val = value.value(stack);
		varExpr.setVal(stack, val, true);
		stack.setStatus(ExecutionStatus.skipped);
		return val;
	}

}
