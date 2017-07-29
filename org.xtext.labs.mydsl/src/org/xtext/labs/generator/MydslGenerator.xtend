/*
 * generated by Xtext 2.11.0
 */
package org.xtext.labs.generator

import com.google.inject.Inject
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.xtext.labs.mydsl.AbstractMethodCall
import org.xtext.labs.mydsl.BodyStatement
import org.xtext.labs.mydsl.BrkStr
import org.xtext.labs.mydsl.DSLProgram
import org.xtext.labs.mydsl.DoWhileExpression
import org.xtext.labs.mydsl.FuncCall
import org.xtext.labs.mydsl.FuncParameter
import org.xtext.labs.mydsl.IfExpression
import org.xtext.labs.mydsl.StdFunction
import org.xtext.labs.mydsl.StringRef
import org.xtext.labs.mydsl.Terminal
import org.xtext.labs.mydsl.arrayRef
import org.xtext.labs.mydsl.numRef
import org.xtext.labs.mydsl.varAssignment
import org.xtext.labs.mydsl.varDeclared
import org.xtext.labs.mydsl.varExpression
import org.xtext.labs.mydsl.varRef
import org.xtext.labs.mydsl.varReturn
import org.xtext.labs.mydsl.FuncDefinition
import org.xtext.labs.mydsl.boolRef

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class MydslGenerator extends AbstractGenerator {

	@Inject MydslGeneratorCS csgen

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {

		val path = input.getURI.path
		val filepath = path.toString.substring(path.lastIndexOf('/') + 1);
		val filename = filepath.replace(".dsl", "")

		var java = input.allContents.filter(typeof(DSLProgram))
		java.forEach[item|fsa.generateFile(filename + '.java', compileJava(item, filename))]
		var cs = input.allContents.filter(typeof(DSLProgram))
		cs.forEach[item|fsa.generateFile(filename + '.cs', csgen.compileCsharp(item, filename))]
	}

	/**
	 * @comment: kim
	 * Tip: 
	 * 1. when xtext attr name has var, hat^ can be used as a wild card ex) var+=varDeclared* => dsl.^var
	 * 2. xtend doesn't have break and continue
	 * 3. xtend can't return Object.
	 */
	def compileJava(DSLProgram dsl, String fileName) {
		'''
			import java.util.Map;
			import java.util.HashMap;
			
			/**
			*
			this java file is generated from «toTitleCase(fileName)».dsl. the generator was written by kim.
			*/
			
			class «fileName.replace("-","")»{	
			
				static Map<String, String> map = new HashMap<>();
			
				«IF dsl.global !== null»
					«FOR param : dsl.global» 
						«compilevarDeclaredGlobal(param)»
					«ENDFOR»
				«ENDIF»
			
				«FOR func : dsl.func»
					private static «rtnType(func)» «func.name»(«compileArgDeclaration2(func.args)»){
					«FOR body : func.body»						
						«compileBodyStatement(body)»
					«ENDFOR»
					}
				«ENDFOR»
				
				@SuppressWarnings("unused")
				public static void main(String args[]){
				init(args);
								
				«FOR body : dsl.main.body»
					«compileBodyStatement(body)»
				«ENDFOR»
				}
			
				«stdfunctionModelBuilder»
			}
			
		'''
	}

	def rtnType(FuncDefinition d) {
		var buf = "void"

		for (p : d.body) {
			if (p instanceof varReturn) {
				val t = TerminalTypeResolver(p.rtn)
				buf = typeName(t)
			}
		}

		return buf
	}

	def TerminalTypeResolver(Terminal t) {
		var buf = ""

		if (t instanceof varRef) {
			val sb = t.varRef
			buf = sb.type
		}

		if (t instanceof arrayRef) {
			val sb = t.varRef
			buf = sb.type
		}

		return buf
	}

	def compilevarDeclaredGlobal(varDeclared p) {
		var buf = "static "
		var isDim = false
		if (p.dim.length != 0) {
			isDim = true
		}
		buf += typeName(p.type)
		buf += " " + p.name

		// String[] array = new String[10];
		// array index to be int
		if (isDim) {
			for (d : p.dim) {
				buf += "[]"
			}
			buf += " = new "
			buf += typeName(p.type)

			for (d : p.dim) {
				buf += "[" + d.size.toString + "]"

				if (p.dim.length == (p.dim.lastIndexOf(d) + 1)) {
					buf += ';'
				}
			}
		}

		if (!isDim) {
			if (p.type == "string") {
				buf += " = \"\";"
			} else if (p.type == "bool") {
				buf += " = false;"
			} else {
				buf += " = 0;"
			}
		}

		return buf
	}

	def compileArgDeclaration2(EList<FuncParameter> pl) {
		var buf = ""

		for (p : pl) {
			buf += typeName(p.type)

			buf += " " + p.name

			if (pl.length != (pl.lastIndexOf(p) + 1)) {
				buf += ','
			}
		}

		return buf
	}

	def compileArgDeclaration(EList<varDeclared> pl) {
		var buf = ""

		for (p : pl) {
			buf = typeName(p.type)

			if (p.dim.length != 0) {
				for (d : p.dim) {
					buf += "[]"
				}
			}
			buf += " " + p.name

			if (pl.length != (pl.lastIndexOf(p) + 1)) {
				buf += ','
			}
		}

		return buf
	}

	def compilevarDeclared(varDeclared p) {
		var buf = ""
		var isDim = false
		if (p.dim.length != 0) {
			isDim = true
		}
		buf = typeName(p.type)
		buf += " " + p.name

		// String[] array = new String[10];
		// array index to be int
		if (isDim) {
			for (d : p.dim) {
				buf += "[]"
			}
			buf += " = new "
			buf += typeName(p.type)

			for (d : p.dim) {
				buf += "[" + d.size.toString + "]"

				if (p.dim.length == (p.dim.lastIndexOf(d) + 1)) {
					buf += ';'
				}
			}
		}

		if (!isDim) {
			if (p.type == "string") {
				buf += " = \"\";"
			} else if (p.type == "bool") {
				buf += " = false;"
			} else {
				buf += " = 0;"
			}
		}

		return buf
	}

	def compileBodyStatement(BodyStatement b) {
		switch (b) {
			varExpression: compileVarExpression(b)
			IfExpression: compileIfExpression(b)
			DoWhileExpression: compileDoWhileExpression(b)
			BrkStr: brkStr(b)
			AbstractMethodCall: compileAbstractMethod(b)
			varDeclared: compilevarDeclared(b)
			varReturn: returnStr(b)
		}
	}

	def returnStr(varReturn b) {
		var buf = 'return '

		buf += TerminalMatchFinder(b.rtn)
		buf += ';'

		return buf
	}

	def compileVarExpression(varExpression r) {
		var buf = new StringBuilder()

		if (r instanceof varAssignment) {
			buf.append(TerminalMatchFinder(r.left))

			for (op : r.op) {
				var opStr = op
				
				if(op.equals("and")){
					opStr = '&'
				}else if(op.equals("or")){
					opStr = '|'
				}
				
				buf.append(" " + opStr + " ")

				val a = r.right.get(r.op.indexOf(op))

				if (a instanceof Terminal) {
					buf.append(TerminalMatchFinder(a))
				} else if (a instanceof AbstractMethodCall) {

					var func = compileAbstractMethod(a)
					if (r.eContainer instanceof IfExpression) {
						func = func.replace(";", "")
					}
					buf.append(func)
				}

			}
		}

		if (isColon(r)) {
			buf.append(";")
		}

		return buf.toString.replace(";;", ";")
	}

	def compileFunction(FuncCall s) {
		var buf = ""
		buf = '''«funcName(s)»('''

		for (p : s.args) {
			if (p instanceof varRef) {
				buf += p.varRef.name

				if (s.args.length != (s.args.lastIndexOf(p) + 1)) {
					buf += ','
				}
			}
		// Is it need to develop for ArrayRef? 
		}
		buf += ')'

		if (isColon(s)) {
			buf += ";"
		}

		return buf
	}

	def TerminalMatchFinder(Terminal t) {
		var buf = ""

		if (t instanceof numRef) {
			buf = t.value.toString()
		}
		if (t instanceof arrayRef) {
			buf = t.varRef.name

			if (t.dim.length != 0) {
				for (d : t.dim) {
					if (d.index !== null) {
						buf += "[" + d.index.name + "]"
					} else {
						buf += "[" + d.size.toString + "]"
					}
				}
			}
		}
		if (t instanceof varRef) {
			buf = t.varRef.name
		}

		if (t instanceof StringRef) {
			buf = '\"' + t.value + '\"'
		}
		
		if (t instanceof boolRef) {
			buf = t.varRef
		}

		return buf
	}

	def compileAbstractMethod(AbstractMethodCall r) {
		switch (r) {
			StdFunction: compileStdFunction(r)
			FuncCall: compileFuncCall(r)
		}
	}

	def String compileFuncCall(FuncCall s) {
		var buf = ""
		buf = '''«funcName(s)»('''

		for (p : s.args) {
			buf += compileArgResolver(p)

			if (s.args.length != (s.args.lastIndexOf(p) + 1)) {
				buf += ','
			}
		// Is it need to develop for ArrayRef? 
		}
		buf += ')'

		if (isColon(s)) {
			buf += ";"
		}

		return buf
	}

	def compileArgResolver(Terminal p) {
		var buf = ""

		if (p instanceof varRef) {
			val vsym = p.varRef
			if (vsym instanceof varDeclared) {
				buf = vsym.name
			}

			if (vsym instanceof FuncParameter) {
				buf = vsym.name
			}
		}

		if (p instanceof StringRef) {
			buf = "\"" + p.value + "\""
		}

		if (p instanceof numRef) {
			buf = p.value.toString
		}

		return buf
	}

	def compileStdFunction(StdFunction s) {
		var buf = ""
		buf = '''«s.name»('''

		for (p : s.args) {
			buf += compileArgResolver(p)

			if (s.args.length != (s.args.lastIndexOf(p) + 1)) {
				buf += ','
			}
		// Is it need to develop for ArrayRef? 
		}
		buf += ')'

		if (isColon(s)) {
			buf += ";"
		}

		return buf
	}

	def compileIfExpression(IfExpression r) {
		val buf = new StringBuilder()

		buf.append("if(")
		buf.append(compileVarExpression(r.ifconditon))
		buf.append("){\n")
		for (t : r.then) {
			buf.append(compileBodyStatement(t))
		}

		if (r.^else.length !== 0) {
			buf.append("}else{\n")
			for (t : r.^else) {
				buf.append(compileBodyStatement(t))
			}
		}

		buf.append("}\n")

		return buf.toString
	}

	def compileDoWhileExpression(DoWhileExpression r) {
		val buf = new StringBuilder()

		buf.append("while(")
		buf.append(compileVarExpression(r.loopConditon))
		buf.append("){\n")

		for (t : r.body) {
			buf.append(compileBodyStatement(t))
		}

		buf.append("}\n")

		return buf.toString
	}

	/**
	 * Util Area
	 */
	def String typeName(String s) {
		var buf = ""
		switch (s) {
			case "num": buf = '''int'''
			case "bool": buf = '''boolean'''
			case "string": buf = '''String'''
		}
		return buf
	}

	def String brkStr(BrkStr r) {
		"break;"
	}

	def funcName(FuncCall s) {
		val funcCrossRef = s.func
		var funcname = funcCrossRef.name
		return funcname
	}

	def Boolean isColon(EObject s) {
		var isColon = true

		if (s.eContainer instanceof IfExpression || s.eContainer instanceof DoWhileExpression ||
			s.eContainer instanceof varAssignment) {
			if (s.eContainingFeature.name.equals("ifconditon") || s.eContainingFeature.name.equals("loopConditon")) {
				isColon = false
			}
			if (s.eContainer.eContainer instanceof IfExpression ||
				s.eContainer.eContainer instanceof DoWhileExpression) {
				if (s.eContainer.eContainingFeature.name.equals("ifconditon") ||
					s.eContainer.eContainingFeature.name.equals("loopConditon")) {
					isColon = false
				}
			}
		}
		return isColon
	}

	def String toTitleCase(String input) {
		val titleCase = new StringBuilder()
		var isFirst = true

		for (c : input.toCharArray()) {
			var ch = c
			if (isFirst) {
				ch = Character.toTitleCase(ch);
				isFirst = false;
			} else {
				ch = Character.toLowerCase(ch);
			}

			titleCase.append(ch);
		}
		return titleCase.toString();
	}

	def String stdfunctionModelBuilder() {
		val buf = new StringBuilder()

		buf.append("\n//Standard Function ======================================================\n")
		buf.append("private static void printstr(String a){ System.out.println(a); }\n")
		buf.append("private static String strjoin(String a, String b){ a = a.concat(b); return a; }\n")
		buf.append("private static String[] strsplit(String a, String b){ String[] rtn = a.split(b); return rtn; }\n")
		buf.append("private static String numtostr(int a){ String rtn = String.valueOf(a); return rtn; }\n")

		// main String arg[] to Map 
		buf.append("private static void init(String[] args) {\n")
		buf.append("for (String arg : args) {\n")
		buf.append("String[] ag = arg.split(\"/\");\n")
		buf.append("map.put(ag[0], ag[1]);}}\n")

		return buf.toString()
	}

}