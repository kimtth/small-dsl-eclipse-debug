/*
 * generated by Xtext 2.12.0
 */
package org.xtext.labs.ui

import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverProvider
import org.xtext.labs.ui.contentassist.MydslHoverProvider

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class MydslUiModule extends AbstractMydslUiModule {
	
	def Class<? extends XbaseHoverProvider> bindXbaseHoverProvider() {
		MydslHoverProvider
	}
}
