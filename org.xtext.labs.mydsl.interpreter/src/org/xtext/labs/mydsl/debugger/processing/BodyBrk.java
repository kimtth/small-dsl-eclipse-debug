package org.xtext.labs.mydsl.debugger.processing;

import org.xtext.labs.mydsl.BrkStr;

public class BodyBrk extends IStackHelper implements IBody {

	BrkStr e;
	
	public BodyBrk(BrkStr v) {
		this.e = v;
	}

	@Override
	public void execute(String functionName) {
		isBrk = true;
	}

}
