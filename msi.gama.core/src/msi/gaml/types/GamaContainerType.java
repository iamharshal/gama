/*********************************************************************************************
 * 
 * 
 * 'GamaContainerType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.types;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.IContainer;
import msi.gaml.expressions.IExpression;

/**
 * Written by drogoul
 * Modified on 11 nov. 2011
 * 
 * A generic type for containers. Tentative.
 * 
 */
@type(name = IKeyword.CONTAINER, id = IType.CONTAINER, wraps = { IContainer.class }, kind = ISymbolKind.Variable.CONTAINER)
public class GamaContainerType<T extends IContainer> extends GamaType<T> implements IContainerType<T> {

	@Override
	public T cast(final IScope scope, final Object obj, final Object param) throws GamaRuntimeException {
		return cast(scope, obj, param, getKeyType(), getContentType());
		// return (T) (obj instanceof IContainer ? (IContainer) obj : Types.get(LIST).cast(scope, obj, null,
		// Types.NO_TYPE, Types.NO_TYPE));
	}

	@Override
	public T cast(final IScope scope, final Object obj, final Object param, final IType keyType, final IType contentType)
		throws GamaRuntimeException {
		// by default
		return (T) (obj instanceof IContainer ? (IContainer) obj : Types.get(LIST).cast(scope, obj, null,
			Types.NO_TYPE, Types.NO_TYPE));
	}

	@Override
	public T getDefault() {
		return null;
	}

	@Override
	public GamaContainerType getType() {
		return this;
	}

	@Override
	public IType getContentType() {
		return Types.NO_TYPE;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public boolean isFixedLength() {
		return false;
	}

	@Override
	public IType contentsTypeIfCasting(final IExpression exp) {
		IType itemType = exp.getType();
		if ( itemType.isContainer() || itemType.isAgentType() ) { return itemType.getContentType(); }
		return itemType;
	}

	@Override
	public IContainerType typeIfCasting(final IExpression exp) {
		return (IContainerType) super.typeIfCasting(exp);
	}

	@Override
	public boolean canCastToConst() {
		return false;
	}

}
