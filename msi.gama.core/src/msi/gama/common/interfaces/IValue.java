/*********************************************************************************************
 * 
 * 
 * 'IValue.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.common.interfaces;

import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;

/**
 * Written by drogoul Modified on 19 nov. 2008
 * 
 * @todo Description
 * 
 */
public interface IValue extends IGamlable, ITyped {

	// public abstract IType type();

	// @operator(value = IType.STRING, can_be_const = true)
	public abstract String stringValue(IScope scope) throws GamaRuntimeException;

	// @operator(value = "to_java")
	// TODO To be done later
	// public abstract String toJava();

	// @operator(value = "copy", can_be_const = true, type = ITypeProvider.FIRST_TYPE, content_type
	// = ITypeProvider.FIRST_CONTENT_TYPE)
	public abstract IValue copy(IScope scope) throws GamaRuntimeException;

}
