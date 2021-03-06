/*********************************************************************************************
 * 
 * 
 * 'GamlMarkOccurrenceActionContributor.java', in plugin 'msi.gama.lang.gaml.ui', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gama.lang.gaml.ui.editor;

import msi.gama.common.*;
import msi.gama.common.GamaPreferences.Entry;
import msi.gama.common.GamaPreferences.IPreferenceChangeListener;
import msi.gaml.types.IType;
import org.eclipse.xtext.ui.editor.occurrences.MarkOccurrenceActionContributor;
import org.eclipse.xtext.ui.editor.preferences.*;
import com.google.inject.Singleton;

/**
 * The class GamlMarkOccurrenceActionContributor.
 * 
 * @author drogoul
 * @since 12 sept. 2013
 * 
 */
@Singleton
public class GamlMarkOccurrenceActionContributor extends MarkOccurrenceActionContributor implements IPreferenceStoreInitializer {

	IPreferenceStoreAccess access;

	// Preference here is an instance variable, but only one will be created as this class is a singleton.
	public final Entry<Boolean> EDITOR_MARK_OCCURENCES = GamaPreferences
		.create("editor.mark.occurences", "Mark occurences of symbols in models", true, IType.BOOL)
		.in(GamaPreferences.EDITOR).group("Presentation").addChangeListener(new IPreferenceChangeListener<Boolean>() {

			@Override
			public boolean beforeValueChange(final Boolean newValue) {
				return true;
			}

			@Override
			public void afterValueChange(final Boolean newValue) {
				stateChanged(newValue);
			}
		});

	@Override
	protected void stateChanged(final boolean newState) {
		super.stateChanged(newState);
	}

	@Override
	public void initialize(final IPreferenceStoreAccess preferenceStoreAccess) {
		access = preferenceStoreAccess;
		preferenceStoreAccess.getWritablePreferenceStore().setDefault(getPreferenceKey(),
			EDITOR_MARK_OCCURENCES.getValue());
		preferenceStoreAccess.getWritablePreferenceStore().setValue(getPreferenceKey(),
			EDITOR_MARK_OCCURENCES.getValue());
	}
}
