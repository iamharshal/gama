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
package msi.gama.internal.types;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.*;
import msi.gama.interfaces.*;
import msi.gama.internal.expressions.IExpressionParser;
import msi.gama.kernel.exceptions.GamaRuntimeException;
import msi.gama.precompiler.GamlAnnotations.type;
import msi.gama.util.*;

/**
 * 
 * A Utility class for String related operations
 * 
 * Written by drogoul Modified on 3 juin 2010
 * 
 * @todo Description
 * 
 */
@type(value = IType.STRING_STR, id = IType.STRING, wraps = String.class)
public class GamaStringType extends GamaType<String> {

	@Override
	public String cast(final IScope scope, final Object obj, final Object param)
		throws GamaRuntimeException {
		return staticCast(obj, param);
	}

	public static String staticCast(final Object obj, final Object param)
		throws GamaRuntimeException {
		if ( obj == null ) { return null; }
		if ( obj instanceof IValue ) { return ((IValue) obj).stringValue(); }
		return obj.toString();
	}

	@Override
	public String getDefault() {
		return null;
	}

	// // ----------------------------------
	// // STRING CONVERSIONS
	// // ----------------------------------

	static public String toGamlString(final String s) {
		if ( s == null ) { return null; }
		StringBuilder sb = new StringBuilder();
		sb.append('\'');
		sb.append(s);
		sb.append('\'');
		return sb.toString();
	}

	static public String toJavaString(final String s) {
		if ( s == null ) { return null; }
		String t = s.trim();
		if ( !isGamaString(t) ) { return s; }
		if ( t.length() >= 2 ) { return t.substring(1, t.length() - 1); }
		return s;
	}

	static public String stringArrayToString(final String[] array) {
		StringBuilder sb = new StringBuilder();
		for ( String s : array ) {
			sb.append(s);
			sb.append(' ');
		}
		return sb.toString().trim();
	}

	// // ----------------------------------
	// // STRING MANIPULATIONS
	// // ----------------------------------

	/**
	 * tokenize Created 31 août 07 by drogoul.
	 * 
	 * @param string the expression
	 * 
	 * @return the list of tokens found in the expression
	 */

	final static String operators = "::|<>|!=|>=|<=|//|";
	final static String ponctuation = "\\p{Punct}";
	final static String literals =
		"\\w+\\$\\w+|'[^'\\\r\n]*(?:\\.[^'\\\r\n]*)*'|\\#\\w+|\\d+\\.\\d+|\\w+\\.\\w+|\\w+|"; // TODO
																								// remove
																								// "$"
																								// support
	final static String regex = literals + operators + ponctuation;
	final static Pattern p = Pattern.compile(regex);

	public static GamaList<String> tokenize(final String expression) {
		final GamaList<String> tokens = new GamaList<String>();
		final Matcher m = p.matcher(expression);
		while (m.find()) {
			String tmp = "";
			tmp = expression.substring(m.start(), m.end());
			if ( tmp != null && MathUtils.UNITS.containsKey(tmp) ) {
				if ( !IExpressionParser.BINARIES.containsKey(tokens.get(tokens.size() - 1)) ) {
					tokens.add("*");
				}
				tokens.add(String.valueOf(MathUtils.UNITS.get(tmp)));
			} else {
				if ( !IExpressionParser.IGNORED.contains(tmp) ) {
					tokens.add(tmp);
				}
			}
		}
		// OutputManager.debug("Tokens : " + tokens);
		return tokens;
	}

	/**
	 * Inserts a string after the specified position. If the position is beyond the length of the
	 * original string, the result string is padded with the specified pad character.
	 * 
	 * @param s the input string
	 * @param ins the string to be inserted
	 * @param start the starting position for the insert
	 * @param pad the pad character
	 * 
	 * @return a string with the input string inserted after the specified position.
	 */
	static public String insert(final String s, final String ins, final int start, final char pad) {
		int ls = s.length();
		int li = ins.length();
		int newlen;

		if ( start < 0 ) { return ""; }

		if ( start > ls ) {
			newlen = li + start;
		} else {
			newlen = ls + li;
		}

		int sPos = start > ls ? ls : start;
		int sRest = sPos;

		char[] buf = new char[newlen];

		s.getChars(0, sPos, buf, 0);

		while (sPos < start) {
			buf[sPos++] = pad;
		}

		ins.getChars(0, li, buf, sPos);
		s.getChars(sRest, ls, buf, sPos + li);

		return new String(buf);
	}

	/**
	 * Inserts a string after the specified position. If the position is beyond the length of the
	 * original string, the result string is padded with blanks.
	 * 
	 * @param s the input string
	 * @param ins the string to be inserted
	 * @param start the starting position for the insert
	 * 
	 * @return a string with the input string inserted after the specified position.
	 */
	static public String insert(final String s, final String ins, final int start) {
		return insert(s, ins, start, ' ');
	}

	/**
	 * Overlay with.
	 * 
	 * @param s the s
	 * @param o the o
	 * @param start the start
	 * @param pad the pad
	 * 
	 * @return the string
	 */
	static public String overlayWith(final String s, final String o, final int start, final char pad) {
		int ls = s.length();
		int lo = o.length();

		if ( start < 0 ) { return ""; }

		int ln = start + lo;
		int pos = start >= ls ? ls : start;
		int newlen = ln < ls ? ls : ln;

		char[] buf = new char[newlen];

		s.getChars(0, pos, buf, 0);

		for ( int i = ls; i < start; i++ ) {
			buf[i] = pad;
		}

		o.getChars(0, lo, buf, start);

		if ( ln < ls ) {
			s.getChars(ln, ls, buf, ln);
		}

		return new String(buf);
	}

	/**
	 * Replaces part of the string with the specified string, starting at a specified position. If
	 * the starting position is beyond the end of the string, it is padded with blanks.
	 * 
	 * @param s the original string
	 * @param o the string to be overlayed over the original string
	 * @param start the starting position for the overlay
	 * 
	 * @return a string with part of it replaced by the overlay string.
	 */
	static public String overlayWith(final String s, final String o, final int start) {
		return overlayWith(s, o, start, ' ');
	}

	/**
	 * Unescape java.
	 * 
	 * @param str the str
	 * 
	 * @return the string
	 */
	static public String unescapeJava(final String str) {
		if ( str == null ) { return null; }

		final StringWriter writer = new StringWriter(str.length());
		unescapeJava(writer, str);
		return writer.toString();

	}

	/**
	 * Unescape java.
	 * 
	 * @param out the out
	 * @param str the str
	 */
	static private void unescapeJava(final StringWriter out, final String str) {
		if ( str == null ) { return; }
		final int sz = str.length();
		final StringBuffer unicode = new StringBuffer(4);
		boolean hadSlash = false;
		boolean inUnicode = false;
		for ( int i = 0; i < sz; i++ ) {
			final char ch = str.charAt(i);
			if ( inUnicode ) {
				// if in unicode, then we're reading unicode
				// values in somehow
				unicode.append(ch);
				if ( unicode.length() == 4 ) {
					// digits
					// which represents our unicode character
					try {
						final int value = Integer.parseInt(unicode.toString(), 16);
						out.write((char) value);
						unicode.setLength(0);
						inUnicode = false;
						hadSlash = false;
					} catch (final NumberFormatException nfe) {
						nfe.printStackTrace();
					}
				}
				continue;
			}
			if ( hadSlash ) {
				// handle an escaped value
				hadSlash = false;
				switch (ch) {
					case '\\':
						out.write('\\');
					break;
					case '\'':
						out.write('\'');
					break;
					case '\"':
						out.write('"');
					break;
					case 'r':
						out.write('\r');
					break;
					case 'f':
						out.write('\f');
					break;
					case 't':
						out.write('\t');
					break;
					case 'n':
						out.write('\n');
					break;
					case 'b':
						out.write('\b');
					break;
					case 'u': {
						// uh-oh, we're in unicode country....
						inUnicode = true;
						break;
					}
					default:
						out.write(ch);
					break;
				}
				continue;
			} else if ( ch == '\\' ) {
				hadSlash = true;
				continue;
			}
			out.write(ch);
		}
		if ( hadSlash ) {
			// string, let's output it anyway.
			out.write('\\');
		}
	}

	/**
	 * Gets the time in string.
	 * 
	 * @return the time in string
	 */
	public static String getTimeInString() {
		final Calendar cal = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
		return sdf.format(cal.getTime());
	}

	static public boolean isGamaString(final String s) {
		if ( s == null ) { return false; }
		int n = s.length();
		if ( n == 0 || n == 1 ) { return false; }
		if ( s.charAt(0) != '\'' ) { return false; }
		if ( s.charAt(n - 1) != '\'' ) { return false; }
		return true;
	}

}
