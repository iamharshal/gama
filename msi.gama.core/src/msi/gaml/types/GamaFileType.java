/*********************************************************************************************
 * 
 * 
 * 'GamaFileType.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.types;

import gnu.trove.map.hash.THashMap;
import java.io.File;
import java.util.*;
import msi.gama.common.GamaPreferences;
import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.operator;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.precompiler.*;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.*;
import msi.gama.util.file.*;
import msi.gaml.compilation.GamaHelper;
import org.eclipse.core.runtime.*;

/**
 * Written by drogoul
 * Modified on 1st Aug. 2010
 * Modified on 30 Dec. 2013
 * 
 */
@type(name = IKeyword.FILE, id = IType.FILE, wraps = { IGamaFile.class }, kind = ISymbolKind.Variable.CONTAINER)
public class GamaFileType extends GamaContainerType<IGamaFile> {

	static Map<String, Set<String>> typesExtensions = new THashMap();
	static Map<Class, Set<String>> classExtensions = new THashMap();
	static Map<String, GamaHelper<IGamaFile>> extensionsToFiles = new THashMap();

	/**
	 * Adds a new file type definition.
	 * 
	 * @param string a string representin the type of the file in GAML
	 * @param clazz the class that supports this file type
	 * @param s an array of allowed extensions for files of this type
	 */
	public static void addFileTypeDefinition(final String string, final Class clazz,
		final GamaHelper<IGamaFile> builder, final String[] extensions) {
		// GuiUtils.debug("GamaFileFactory registering file type " + string + " with extensions " +
		// Arrays.toString(extensions));
		Set<String> exts = new HashSet(Arrays.asList(extensions));
		typesExtensions.put(string, exts);
		classExtensions.put(clazz, exts);
		// Added to ensure that extensions do not begin with a "." or contain blank characters
		for ( String s : extensions ) {
			String ext = s.trim();
			if ( ext.startsWith(".") ) {
				ext = s.substring(1);
			}
			extensionsToFiles.put(s, builder);
		}
	}

	/**
	 * Verifies if the path has the correct extension with respect to the type of the file.
	 * 
	 * @param type a string representing the type of the file
	 * @param path an absolute or relative file path
	 * @return true if the extension of the path belongs to the extensions of the file type, false if the type is
	 *         unknown or if the extension does not belong to its extensions
	 */

	public static boolean verifyExtension(final String type, final String path) {
		IPath p = new Path(path);
		String ext = p.getFileExtension();
		Set<String> extensions = typesExtensions.get(type);
		if ( extensions == null ) { return false; }
		for ( String s : extensions ) {
			if ( s.equalsIgnoreCase(ext) ) { return true; }
		}
		return false;
	}

	public static boolean verifyExtension(final IGamaFile file, final String path) {
		IPath p = new Path(path);
		String ext = p.getFileExtension();
		Class type = file.getClass();
		Set<String> extensions = classExtensions.get(type);
		if ( extensions == null ) { return false; }
		for ( String s : extensions ) {
			if ( s.equalsIgnoreCase(ext) ) { return true; }
		}
		return false;
	}

	public static IGamaFile createFile(final IScope scope, final String path) {
		if ( new File(path).isDirectory() ) { return new GamaFolderFile(scope, path); }
		IPath p = new Path(path);
		String ext = p.getFileExtension();
		GamaHelper<IGamaFile> builder = getHelper(ext);
		if ( builder != null ) { return builder.run(scope, path); }
		return new GamaPreferences.GenericFile(path);
	}

	private static GamaHelper<IGamaFile> getHelper(final String extension) {
		for ( String s : extensionsToFiles.keySet() ) {
			if ( s.equalsIgnoreCase(extension) ) { return extensionsToFiles.get(s); }
		}
		return null;
	}

	public static IGamaFile createFile(final IScope scope, final String path, final IModifiableContainer contents) {

		// TODO USE THE BUILDER INSTEAD (NEED TO REGISTER IT TO TAKE TWO PARAMETERS INTO ACCOUNT)

		IGamaFile f = createFile(scope, path);
		if ( f == null ) { return null; }
		f.setWritable(true);
		f.setContents(contents);
		return f;
	}

	@Override
	public IGamaFile cast(final IScope scope, final Object obj, final Object param, final IType keyType,
		final IType contentType) throws GamaRuntimeException {
		if ( obj == null ) { return getDefault(); }
		// 04/03/14 Problem of initialization of files. See if it works or not. No copy of the file is done.
		if ( obj instanceof IGamaFile ) { return (IGamaFile) obj; }
		// if ( obj instanceof IGamaFile ) { return createFile(scope, ((IGamaFile) obj).getPath(), (IGamaFile) obj); }
		if ( obj instanceof String ) {
			if ( param == null ) { return createFile(scope, (String) obj); }
			if ( param instanceof IContainer.Modifiable ) { return createFile(scope, (String) obj,
				(IModifiableContainer) param); }
		}
		return getDefault();
	}

	// @doc(deprecated = "use 'is_property' instead", value =
	// "the operator tests whether the operand represents the name of a supported properties file", comment =
	// "cf. file type definition for supported (espacially image) file extensions.", examples = {
	// "is_properties(\"../includes/Stupid_Cell.Data\")    --:  false;",
	// "is_properties(\"../includes/test.png\")            --:  false;",
	// "is_properties(\"../includes/test.properties\")     --:  true;",
	// "is_properties(\"../includes/test.shp\")            --:  false;" }, see = { "properties", "is_text",
	// "is_shape", "is_image" })
	@operator(value = "is_properties")
	@doc(deprecated = "use 'is_property' instead")
	@Deprecated
	public static Boolean isProperties(final String f) {
		return verifyExtension("property", f);
	}

	// @doc(deprecated = "use 'is_gaml' instead", value =
	// "the operator tests whether the operand represents the name of a supported gamlfile", comment =
	// "cf. file type definition for supported (espacially model) file extensions.", examples = {
	// "is_shape(\"../includes/Stupid_Cell.Data\")    --:  false;",
	// "is_shape(\"../includes/test.png\")            --:  false;",
	// "is_shape(\"../includes/test.properties\")     --:  false;",
	// "is_shape(\"../includes/test.gaml\")            --:  true;" }, see = { "image", "is_text", "is_properties",
	// "is_image" })
	@operator(value = "is_GAML")
	@doc(deprecated = "use 'is_gaml' instead")
	@Deprecated
	public static Boolean isGAML(final String f) {
		return verifyExtension("gaml", f);
	}

	@Override
	public boolean canCastToConst() {
		return false;
	}

}
