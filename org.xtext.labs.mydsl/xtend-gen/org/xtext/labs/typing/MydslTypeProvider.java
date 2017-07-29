package org.xtext.labs.typing;

import com.google.common.base.Objects;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.xtext.labs.mydsl.MydslPackage;
import org.xtext.labs.mydsl.StringRef;
import org.xtext.labs.mydsl.arrayRef;
import org.xtext.labs.mydsl.numRef;
import org.xtext.labs.mydsl.varAssignment;
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.varRef;
import org.xtext.labs.mydsl.varSymbol;

@SuppressWarnings("all")
public class MydslTypeProvider {
  private final MydslPackage ep = MydslPackage.eINSTANCE;
  
  public String expectedType(final varExpression e) {
    String _xblockexpression = null;
    {
      final EObject c = e.eContainer();
      final EStructuralFeature f = e.eContainingFeature();
      String _switchResult = null;
      boolean _matched = false;
      if (c instanceof varAssignment) {
        EReference _varAssignment_Right = this.ep.getvarAssignment_Right();
        boolean _equals = Objects.equal(f, _varAssignment_Right);
        if (_equals) {
          _matched=true;
          _switchResult = this.typeFor(((varAssignment)c).getLeft());
        }
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public String typeFor(final varExpression e) {
    String _switchResult = null;
    boolean _matched = false;
    if (e instanceof numRef) {
      _matched=true;
      _switchResult = "num";
    }
    if (!_matched) {
      if (e instanceof StringRef) {
        _matched=true;
        _switchResult = "string";
      }
    }
    if (!_matched) {
      if (e instanceof arrayRef) {
        _matched=true;
        varSymbol _varRef = ((arrayRef)e).getVarRef();
        String _type = null;
        if (_varRef!=null) {
          _type=_varRef.getType();
        }
        _switchResult = _type;
      }
    }
    if (!_matched) {
      if (e instanceof varRef) {
        _matched=true;
        varSymbol _varRef = ((varRef)e).getVarRef();
        String _type = null;
        if (_varRef!=null) {
          _type=_varRef.getType();
        }
        _switchResult = _type;
      }
    }
    return _switchResult;
  }
  
  public boolean isConformant(final String c1, final String c2) {
    return (((this.conformsToDigit(c1) && this.conformsToDigit(c2)) || (this.conformsToString(c1) && this.conformsToString(c2))) || (this.conformsToBool(c1) && this.conformsToBool(c2)));
  }
  
  public boolean conformsToString(final String c) {
    return c.equals("string");
  }
  
  public boolean conformsToDigit(final String c) {
    return c.equals("num");
  }
  
  public boolean conformsToBool(final String c) {
    return c.equals("bool");
  }
}
