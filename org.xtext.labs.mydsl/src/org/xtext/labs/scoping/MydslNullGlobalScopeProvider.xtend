package org.xtext.labs.scoping

import org.eclipse.xtext.scoping.IGlobalScopeProvider
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.EReference
import com.google.common.base.Predicate
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope

class MydslNullGlobalScopeProvider implements IGlobalScopeProvider {
	
	//limit scope to the active DSL file only.
	//http://xtextcasts.org/episodes/17-restricting-scope?view=comments
	override getScope(Resource context, EReference reference, Predicate<IEObjectDescription> filter) {
		return IScope.NULLSCOPE;
	}
}
