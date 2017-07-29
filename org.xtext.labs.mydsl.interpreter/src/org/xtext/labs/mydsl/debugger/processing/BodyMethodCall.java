package org.xtext.labs.mydsl.debugger.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.xtext.labs.mydsl.AbstractMethodCall;
import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.FuncCall;
import org.xtext.labs.mydsl.FuncDefinition;
import org.xtext.labs.mydsl.FuncParameter;
import org.xtext.labs.mydsl.StdFunction;
import org.xtext.labs.mydsl.Terminal;
import org.xtext.labs.mydsl.debugger.ModelGenerator;
import org.xtext.labs.mydsl.debugger.context.CallStack;
import org.xtext.labs.mydsl.debugger.context.CallStackItem;
import org.xtext.labs.mydsl.debugger.context.Symbol;

public class BodyMethodCall extends IStackHelper implements IBody {

	AbstractMethodCall e;
	IBodySwitcher exe;

	public BodyMethodCall(IBodySwitcher exe, AbstractMethodCall e) {
		this.e = e;
		this.exe = exe;
	}

	@Override
	public void execute(String funcId) {

		if (e instanceof FuncCall) {
			FuncDefinition fdef = ((FuncCall) e).getFunc();

			String functionname = fdef.getName();
			funcId = pushCallStack(functionname);

			EList<FuncParameter> args = fdef.getArgs();
			EList<Terminal> callargs = e.getArgs();
			addValueSymbolTable(args, callargs, funcId);

			for (BodyStatement b : fdef.getBody()) {
				if (isRtn == false) {
					exe.executor(b, funcId);
				} else {
					break;
				}
			}
		} else if (e instanceof StdFunction) {
			standardFunctionExecutor((StdFunction) e, funcId);
		}

		isRtn = false; // initialize
	}

	private void standardFunctionExecutor(StdFunction std, String funcId) {
		String functionname = std.getName();
		Symbol p = null;
		Symbol q = null;
		String val = "";
		String val1 = "";

		switch (functionname) {
		case "printstr":
			p = lookupSymbolByTerminal(std.getArgs().get(0), funcId);
			val = (String) p.getVarVal();
			System.out.println(val);
			break;
		case "strjoin":
			p = lookupSymbolByTerminal(std.getArgs().get(0), funcId);
			val = (String) p.getVarVal();

			q = lookupSymbolByTerminal(std.getArgs().get(1), funcId);
			val1 = (String) q.getVarVal();

			lastFunctionReturn = val + val1;
			break;
		case "strsplit":
			//TODO: ??
			p = lookupSymbolByTerminal(std.getArgs().get(0), funcId);
			val = (String) p.getVarVal();

			q = lookupSymbolByTerminal(std.getArgs().get(1), funcId);
			val1 = (String) q.getVarVal();
			
			Map<String, Object> dim = new HashMap<String, Object>();
			String[] sp = val.split(val1);
			int mkey = 0;
			for(String s : sp){
				dim.put(String.valueOf(mkey), s);
				++mkey;
			}
			lastFunctionReturn = dim;
			break;
		case "numtostr":
			p = lookupSymbolByTerminal(std.getArgs().get(0), funcId);
			int val2 = (int) p.getVarVal();
			lastFunctionReturn = String.valueOf(val2);
			break;
		case "getargs":
			p = lookupSymbolByTerminal(std.getArgs().get(0), funcId);
			int val3 = (int) p.getVarVal();

			String[] args = ModelGenerator.args;
			int key = 0;
			for(String arg : args){
				if(key == val3){
					lastFunctionReturn = arg;
					break;
				}
				++key;
			}
			
			break;
		}

	}

	private void addValueSymbolTable(EList<FuncParameter> args, EList<Terminal> callargs, String funcId) {
		// Terminal is caller, FuncParameter is callee.
		for (Terminal t : callargs) {
			Symbol s = lookupSymbolByTerminal(t, getCallerFuncId(funcId));

			int index = callargs.indexOf(t);
			FuncParameter p = args.get(index);

			Symbol sm = new Symbol();
			sm.setScope(s.getScope());
			sm.setVarName(p.getName());
			sm.setVarType(p.getType());
			sm.setVarVal(s.getVarVal());

			addCallStackBySymbol(sm, funcId);
		}

	}

	/**
	 * this function return caller's function id in stack
	 * 
	 * @return
	 */
	private String getCallerFuncId(String funcId) {
		int lastindex = CallStack.getStack().size() - 1;
		int callerIndex = lastindex - 1;

		CallStackItem itm = CallStack.getStack().get(callerIndex);

		return itm.getFuncId();
	}

	/**
	 * generate new function id and make callStackitem and add into callstack.
	 * 
	 * @param functionname
	 * @return
	 */
	private String pushCallStack(String functionname) {
		int index = CallStack.getStack().size();
		String funcId = functionname + "_" + String.valueOf(index);

		pushCallStackItem(funcId);

		return funcId;
	}

}
