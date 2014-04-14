/*********************************************************************************************
 * 
 * 
 * 'GamlLabelProvider.java', in plugin 'msi.gama.lang.gaml.ui', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.lang.gaml.ui.labeling;

import java.util.regex.*;
import msi.gama.lang.gaml.gaml.*;
import msi.gama.lang.utils.EGaml;
import msi.gaml.descriptions.IGamlDescription;
import msi.gaml.factories.DescriptionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.naming.*;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import com.google.inject.Inject;

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class GamlLabelProvider extends DefaultEObjectLabelProvider {

	@Inject
	private IQualifiedNameProvider nameProvider;

	@Inject
	public GamlLabelProvider(final AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	// Model : GAMA icon
	String image(final Model ele) {
		return "icon-16x16x32b.gif";
	}

	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	public String removeTags(final String string) {
		if ( string == null || string.length() == 0 ) { return string; }
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}

	// Import
	String text(final Import ele) {
		String display = ele.getImportURI();
		int index = display.lastIndexOf('/');
		if ( index >= 0 ) {
			display = display.substring(index + 1);
		}
		return "import " + display;
	}

	String text(final EObject ele) {
		String text;
		QualifiedName qn;
		try {
			qn = nameProvider.getFullyQualifiedName(ele);
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			return "";
		}
		String key = EGaml.getKeyOf(ele);
		if ( key == null ) {
			key = "";
		}
		if ( qn == null ) {
			text = key;
			if ( ele instanceof Statement ) {
				String name = EGaml.getNameOf((Statement) ele);
				if ( name == null ) {
					Expression expr = ((Statement) ele).getExpr();
					if ( expr != null ) {
						name = EGaml.getKeyOf(expr);
					}
				}
				text += " " + (name == null ? "" : name);
			}
		} else {
			text = key + " " + qn.toString();
		}

		IGamlDescription ed = DescriptionFactory.getGamlDocumentation(ele);
		if ( ed != null ) {
			text += " [" + removeTags(ed.getTitle()) + "]";
		}
		return text;
	}

	// String text(final Definition obj) {
	// IGamlDescription ed = EGaml.getGamlDescription(obj);
	// if ( ed != null ) { return removeTags(ed.getTitle()); }
	// String s = text((EObject) obj);
	// String n = obj.getName();
	// if ( n == null ) {
	// n = "";
	// }
	// return s + " " + obj.getName();
	// }

	// String text(final Statement obj) {
	// if ( !obj.getKey().equals(IKeyword.SPECIES) && !obj.getKey().equals(IKeyword.GRID) ) {
	// IGamlDescription ed = EGaml.getGamlDescription(obj);
	// if ( ed != null ) { return removeTags(ed.getTitle()); }
	// }
	// String s = EGaml.getKeyOf(obj);
	// QualifiedName qn = nameProvider.getFullyQualifiedName(obj);
	// String n = qn == null ? EGaml.getNameOf(obj) : qn.toString();
	// if ( n == null ) {
	// n = "";
	// }
	// return s + " " + n;
	// }

	String text(final Model obj) {
		return obj.getName();
	}

	String image(final Import ele) {
		return "_include.png";
	}

	//
	// String image(final SetEval ele) {
	// return "_set.png";
	// }
	//
	// String text(final SetEval ele) {
	// return "set";
	// }
	//
	// String image(final LoopEval ele) {
	// return "_loop.png";
	// }
	//
	// String text(final LoopEval ele) {
	// return "loop";
	// }
	//
	// String image(final IfEval ele) {
	// return "_if.png";
	// }
	//
	// String text(final IfEval ele) {
	// return "if";
	// }
	//
	// String image(final DoEval ele) {
	// return "_do.png";
	// }
	//
	// String text(final DoEval ele) {
	// return "do";
	// }
	//
	// String image(final ReturnEval ele) {
	// return "_return.png";
	// }
	//
	// String text(final ReturnEval ele) {
	// return "return";
	// }

	// Statement : keyword.value
	public String image(final/* Sub */Statement ele) {
		String kw = EGaml.getKeyOf(ele);
		if ( kw == null ) { return image((Facet) null); }
		if ( kw.equals("var") || kw.equals("const") ) {
			// for ( FacetExpr f : ele.getFacets() ) {
			for ( Facet f : EGaml.getFacetsOf(ele) ) {
				if ( EGaml.getKeyOf(f).equals("type") && f.getExpr() instanceof VariableRef ) {
					VariableRef type = (VariableRef) f.getExpr();
					return typeImage(EGaml.getKeyOf(type));
				}
			}
		}
		return "_" + kw + ".png";
	}

	public String typeImage(final String string) {
		return "_" + string + ".png";
	}

	// String text(final Evaluation ele) {
	// return ele.getKey().getRef().getName();
	// }

	// dirty image for now (debug purpose, proposal provider)
	// String image(final FacetExpr ele) { // FIXME
	// return "gaml_facet.png";
	// }

	String image(final Facet ele) { // FIXME
		return "gaml_facet.png";
	}

	// String image(final DefKeyword ele) {
	// return "gaml_keyword.png";
	// }

	// String image(final DefBinaryOp ele) {
	// return "gaml_binaryop.png";
	// }

	// String image(final DefReserved ele) {
	// return "gaml_reserved.png";
	// }

	// String image(final GamlUnitRef ele) {
	// return "gaml_unit.png";
	// }
}
