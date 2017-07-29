package org.xtext.labs.typing

import org.xtext.labs.mydsl.MydslPackage
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.varAssignment;
import org.xtext.labs.mydsl.arrayRef;
import org.xtext.labs.mydsl.varRef;
import org.xtext.labs.mydsl.numRef;
import org.xtext.labs.mydsl.StringRef;

class MydslTypeProvider {
	val ep = MydslPackage::eINSTANCE

	def expectedType(varExpression e) {
		val c = e.eContainer
		val f = e.eContainingFeature

		switch (c) {
			varAssignment case f == ep.getvarAssignment_Right:
				c.left.typeFor
		}
	}

	def typeFor(varExpression e) {
		switch (e) {
			numRef: "num"
			StringRef: "string"
			arrayRef: e.varRef?.type
			varRef: e.varRef?.type
		}
	}
	
	def isConformant(String c1, String c2) {
		(c1.conformsToDigit && c2.conformsToDigit) || (c1.conformsToString && c2.conformsToString) 
		|| (c1.conformsToBool && c2.conformsToBool) 
	}
	
	def conformsToString(String c) {
		c.equals("string")
	}

	def conformsToDigit(String c) {
		c.equals("num") 
	}
	
	def conformsToBool(String c) {
		c.equals("bool") 
	}

}

//http://stackoverflow.com/questions/33362041/check-type-in-xtend-validator
// not work in my case
/**
 * import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider
 * import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider.IParserErrorContext
 * import org.eclipse.xtext.parser.antlr.ISyntaxErrorMessageProvider.IValueConverterErrorContext
 * import org.antlr.runtime.MismatchedTokenException
 * import org.eclipse.xtext.nodemodel.SyntaxErrorMessage

 * class DSL2TypeProvider implements ISyntaxErrorMessageProvider {
 * 	val FALSE_PARAMETER_TYPE = "FalseParameterType";
 * 	
 * 	override getSyntaxErrorMessage(IParserErrorContext context) {
 * 		if (context.getRecognitionException() instanceof MismatchedTokenException) {
 * 			val exception = context.getRecognitionException() as MismatchedTokenException;
 * 			val value = exception.token.getText();
 * 			return new SyntaxErrorMessage("The type of " + value + " is wrong.", FALSE_PARAMETER_TYPE);
 * 		}
 * 		return null;
 * 	}

 * 	override getSyntaxErrorMessage(IValueConverterErrorContext context) {
 * 		throw new UnsupportedOperationException("TODO: auto-generated method stub")
 * 	}
 * }
 */
