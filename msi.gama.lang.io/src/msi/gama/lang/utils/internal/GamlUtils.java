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
package msi.gama.lang.utils.internal;

import java.io.File;
import java.net.URL;
import java.util.*;
import msi.gama.lang.gaml.gaml.*;
import msi.gama.lang.utils.ISyntacticElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class GamlUtils {

	public static Map<String, ISyntacticElement> getDocMap(final Resource r) throws Exception {
		return getDocMap(r, r.getResourceSet());
	}

	private static Map<String, ISyntacticElement> getDocMap(final Resource r, final ResourceSet rs)
		throws Exception {
		if ( !r.getErrors().isEmpty() ) {
			StringBuilder sb =
				new StringBuilder("The Resource contains errors! ").append(r.getURI());
			for ( Diagnostic e : r.getErrors() ) {
				sb.append('\n').append(e.getMessage());
			}
			throw new Exception(sb.toString());
		}
		Map<String, ISyntacticElement> docs = new HashMap();
		// xrs.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		// EcoreUtil.resolveAll(r);
		// get include from 'r'
		Model m = getModel(r);
		// convert 'r' + include in 'rs'
		for ( Import imp : m.getImports() ) {
			String importUri = imp.getImportURI();
			if ( !importUri.startsWith("platform:") ) {
				URI iu = URI.createURI(importUri).resolve(r.getURI());
				Resource ir = rs.getResource(iu, true);
				// EcoreUtil.resolveAll(ir);
				docs.putAll(getDocMap(ir, rs)); // (!) recursive
			}
		}
		docs.put(getPath(r), parse(m));
		return docs;
	}

	public static String getPath(final Resource r) {
		return getPath(r.getURI());
	}

	// because of GAMA kernel (based on FileSystem resources)
	public static String getPath(final URI uri) {
		try {
			URL url = FileLocator.resolve(new URL(uri.toString()));
			return new File(url.getFile()).getAbsolutePath();
		} catch (Exception e) {}
		return null;
	}

	private static Model getModel(final Resource resource) {
		return (Model) EcoreUtil.copy(resource.getContents().get(0));
	}

	public static ISyntacticElement parse(final Model m) {
		return Gaml2JDOM.doConvert(m);
	}

	public static Map<String, ISyntacticElement> getDocMap(final IFile f, final ResourceSet rs)
		throws Exception {
		String p = f.getFullPath().toString();
		URI u = URI.createPlatformResourceURI(p, true);
		Resource r = rs.getResource(u, true);
		return getDocMap(r, rs);
	}
}
