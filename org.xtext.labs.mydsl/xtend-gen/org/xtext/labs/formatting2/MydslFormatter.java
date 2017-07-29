/**
 * generated by Xtext 2.12.0
 */
package org.xtext.labs.formatting2;

import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XBasicForLoopExpression;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XCastedExpression;
import org.eclipse.xtext.xbase.XClosure;
import org.eclipse.xtext.xbase.XCollectionLiteral;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XDoWhileExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XForLoopExpression;
import org.eclipse.xtext.xbase.XIfExpression;
import org.eclipse.xtext.xbase.XInstanceOfExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XPostfixOperation;
import org.eclipse.xtext.xbase.XReturnExpression;
import org.eclipse.xtext.xbase.XSwitchExpression;
import org.eclipse.xtext.xbase.XSynchronizedExpression;
import org.eclipse.xtext.xbase.XThrowExpression;
import org.eclipse.xtext.xbase.XTryCatchFinallyExpression;
import org.eclipse.xtext.xbase.XTypeLiteral;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.XWhileExpression;
import org.eclipse.xtext.xbase.formatting2.XbaseFormatter;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xtype.XFunctionTypeRef;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;
import org.xtext.labs.mydsl.BodyStatement;
import org.xtext.labs.mydsl.BrkStr;
import org.xtext.labs.mydsl.DSLProgram;
import org.xtext.labs.mydsl.DoWhileExpression;
import org.xtext.labs.mydsl.FuncCall;
import org.xtext.labs.mydsl.FuncDefinition;
import org.xtext.labs.mydsl.IfExpression;
import org.xtext.labs.mydsl.mainDeclared;
import org.xtext.labs.mydsl.varDeclared;
import org.xtext.labs.mydsl.varExpression;
import org.xtext.labs.mydsl.varReturn;
import org.xtext.labs.services.MydslGrammarAccess;

@SuppressWarnings("all")
public class MydslFormatter extends XbaseFormatter {
  @Inject
  @Extension
  private MydslGrammarAccess _mydslGrammarAccess;
  
  /**
   * @comment:kim
   * dispatch is override keyword in xtend
   */
  protected void _format(final DSLProgram dSLProgram, @Extension final IFormattableDocument document) {
    EList<varDeclared> _global = dSLProgram.getGlobal();
    for (final varDeclared varDec : _global) {
      document.<varDeclared>format(varDec);
    }
    EList<FuncDefinition> _func = dSLProgram.getFunc();
    for (final FuncDefinition funcDefinition : _func) {
      document.<FuncDefinition>format(funcDefinition);
    }
    document.<mainDeclared>format(dSLProgram.getMain());
  }
  
  protected void _format(final mainDeclared d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getMainDeclaredAccess().getLeftCurlyBracketKeyword_4()), _function);
    EList<BodyStatement> _body = d.getBody();
    for (final BodyStatement bodyStatement : _body) {
      document.<BodyStatement>format(bodyStatement);
    }
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(
      this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getMainDeclaredAccess().getLeftCurlyBracketKeyword_4()), 
      this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getMainDeclaredAccess().getRightCurlyBracketKeyword_6()), _function_1);
  }
  
  protected void _format(final varDeclared d, @Extension final IFormattableDocument document) {
    EObject _eContainer = d.eContainer();
    if ((_eContainer instanceof DSLProgram)) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      document.<varDeclared>prepend(d, _function);
    }
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<varDeclared>append(d, _function_1);
  }
  
  protected void _format(final FuncDefinition d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.prepend(this.textRegionExtensions.regionFor(d).keyword("def"), _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword("{"), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(
      this.textRegionExtensions.regionFor(d).keyword("{"), 
      this.textRegionExtensions.regionFor(d).keyword("}"), _function_2);
    EList<BodyStatement> _body = d.getBody();
    for (final BodyStatement bd : _body) {
      document.<BodyStatement>format(bd);
    }
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword("}"), _function_3);
  }
  
  protected void _format(final BodyStatement p, @Extension final IFormattableDocument document) {
    boolean _matched = false;
    if (p instanceof IfExpression) {
      _matched=true;
      document.<IfExpression>format(((IfExpression)p));
    }
    if (!_matched) {
      if (p instanceof DoWhileExpression) {
        _matched=true;
        document.<DoWhileExpression>format(((DoWhileExpression)p));
      }
    }
    if (!_matched) {
      if (p instanceof varExpression) {
        _matched=true;
        document.<varExpression>format(((varExpression)p));
      }
    }
    if (!_matched) {
      if (p instanceof BrkStr) {
        _matched=true;
        document.<BrkStr>format(((BrkStr)p));
      }
    }
    if (!_matched) {
      if (p instanceof FuncCall) {
        _matched=true;
        document.<FuncCall>format(((FuncCall)p));
      }
    }
    if (!_matched) {
      if (p instanceof varReturn) {
        _matched=true;
        document.<varReturn>format(((varReturn)p));
      }
    }
    if (!_matched) {
      if (p instanceof varDeclared) {
        _matched=true;
        document.<varDeclared>format(((varDeclared)p));
      }
    }
  }
  
  protected void _format(final varExpression d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<varExpression>prepend(d, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<varExpression>append(d, _function_1);
  }
  
  protected void _format(final BrkStr d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<BrkStr>prepend(d, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<BrkStr>append(d, _function_1);
  }
  
  protected void _format(final varReturn d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<varReturn>prepend(d, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<varReturn>append(d, _function_1);
  }
  
  protected void _format(final FuncCall d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<FuncCall>prepend(d, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.<FuncCall>append(d, _function_1);
  }
  
  protected void _format(final IfExpression d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.prepend(document.prepend(this.textRegionExtensions.regionFor(d).keyword("if"), _function), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getLeftCurlyBracketKeyword_5()), _function_2);
    EList<BodyStatement> _then = d.getThen();
    for (final BodyStatement bodypart : _then) {
      document.<BodyStatement>format(bodypart);
    }
    if (((d.getElse() != null) && (d.getElse().size() != 0))) {
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<ISemanticRegion, ISemanticRegion>interior(
        this.textRegionExtensions.regionFor(d).keyword("if"), 
        this.textRegionExtensions.regionFor(d).keyword("else"), _function_3);
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<ISemanticRegion, ISemanticRegion>interior(
        this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getElseKeyword_7_1()), 
        this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getRightCurlyBracketKeyword_8()), _function_4);
      final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
        it.setNewLines(1, 1, 2);
      };
      document.prepend(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getRightCurlyBracketKeyword_7_0()), _function_5);
      final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
        it.setNewLines(1, 1, 2);
      };
      document.append(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getLeftCurlyBracketKeyword_7_2()), _function_6);
      EList<BodyStatement> _else = d.getElse();
      for (final BodyStatement bodypart_1 : _else) {
        document.<BodyStatement>format(bodypart_1);
      }
    } else {
      final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      document.<ISemanticRegion, ISemanticRegion>interior(
        this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getIfKeyword_1()), 
        this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getRightCurlyBracketKeyword_8()), _function_7);
    }
    final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_9 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.prepend(document.prepend(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getRightCurlyBracketKeyword_8()), _function_8), _function_9);
    final Procedure1<IHiddenRegionFormatter> _function_10 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getIfExpressionAccess().getRightCurlyBracketKeyword_8()), _function_10);
  }
  
  protected void _format(final DoWhileExpression d, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.prepend(document.prepend(this.textRegionExtensions.regionFor(d).keyword("while"), _function), _function_1);
    EList<BodyStatement> _body = d.getBody();
    for (final BodyStatement bodypart : _body) {
      document.<BodyStatement>format(bodypart);
    }
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.prepend(document.prepend(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getDoWhileExpressionAccess().getRightCurlyBracketKeyword_7()), _function_2), _function_3);
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    document.<ISemanticRegion, ISemanticRegion>interior(
      this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getDoWhileExpressionAccess().getWhileKeyword_1()), 
      this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getDoWhileExpressionAccess().getRightCurlyBracketKeyword_7()), _function_4);
    final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
      it.setNewLines(1, 1, 2);
    };
    document.append(this.textRegionExtensions.regionFor(d).keyword(this._mydslGrammarAccess.getDoWhileExpressionAccess().getRightCurlyBracketKeyword_7()), _function_5);
  }
  
  public void format(final Object d, final IFormattableDocument document) {
    if (d instanceof JvmTypeParameter) {
      _format((JvmTypeParameter)d, document);
      return;
    } else if (d instanceof JvmFormalParameter) {
      _format((JvmFormalParameter)d, document);
      return;
    } else if (d instanceof XtextResource) {
      _format((XtextResource)d, document);
      return;
    } else if (d instanceof XAssignment) {
      _format((XAssignment)d, document);
      return;
    } else if (d instanceof XBinaryOperation) {
      _format((XBinaryOperation)d, document);
      return;
    } else if (d instanceof XDoWhileExpression) {
      _format((XDoWhileExpression)d, document);
      return;
    } else if (d instanceof XFeatureCall) {
      _format((XFeatureCall)d, document);
      return;
    } else if (d instanceof XMemberFeatureCall) {
      _format((XMemberFeatureCall)d, document);
      return;
    } else if (d instanceof XPostfixOperation) {
      _format((XPostfixOperation)d, document);
      return;
    } else if (d instanceof XWhileExpression) {
      _format((XWhileExpression)d, document);
      return;
    } else if (d instanceof XFunctionTypeRef) {
      _format((XFunctionTypeRef)d, document);
      return;
    } else if (d instanceof FuncCall) {
      _format((FuncCall)d, document);
      return;
    } else if (d instanceof JvmGenericArrayTypeReference) {
      _format((JvmGenericArrayTypeReference)d, document);
      return;
    } else if (d instanceof JvmParameterizedTypeReference) {
      _format((JvmParameterizedTypeReference)d, document);
      return;
    } else if (d instanceof JvmWildcardTypeReference) {
      _format((JvmWildcardTypeReference)d, document);
      return;
    } else if (d instanceof XBasicForLoopExpression) {
      _format((XBasicForLoopExpression)d, document);
      return;
    } else if (d instanceof XBlockExpression) {
      _format((XBlockExpression)d, document);
      return;
    } else if (d instanceof XCastedExpression) {
      _format((XCastedExpression)d, document);
      return;
    } else if (d instanceof XClosure) {
      _format((XClosure)d, document);
      return;
    } else if (d instanceof XCollectionLiteral) {
      _format((XCollectionLiteral)d, document);
      return;
    } else if (d instanceof XConstructorCall) {
      _format((XConstructorCall)d, document);
      return;
    } else if (d instanceof XForLoopExpression) {
      _format((XForLoopExpression)d, document);
      return;
    } else if (d instanceof XIfExpression) {
      _format((XIfExpression)d, document);
      return;
    } else if (d instanceof XInstanceOfExpression) {
      _format((XInstanceOfExpression)d, document);
      return;
    } else if (d instanceof XReturnExpression) {
      _format((XReturnExpression)d, document);
      return;
    } else if (d instanceof XSwitchExpression) {
      _format((XSwitchExpression)d, document);
      return;
    } else if (d instanceof XSynchronizedExpression) {
      _format((XSynchronizedExpression)d, document);
      return;
    } else if (d instanceof XThrowExpression) {
      _format((XThrowExpression)d, document);
      return;
    } else if (d instanceof XTryCatchFinallyExpression) {
      _format((XTryCatchFinallyExpression)d, document);
      return;
    } else if (d instanceof XTypeLiteral) {
      _format((XTypeLiteral)d, document);
      return;
    } else if (d instanceof XVariableDeclaration) {
      _format((XVariableDeclaration)d, document);
      return;
    } else if (d instanceof BrkStr) {
      _format((BrkStr)d, document);
      return;
    } else if (d instanceof DoWhileExpression) {
      _format((DoWhileExpression)d, document);
      return;
    } else if (d instanceof IfExpression) {
      _format((IfExpression)d, document);
      return;
    } else if (d instanceof varDeclared) {
      _format((varDeclared)d, document);
      return;
    } else if (d instanceof varExpression) {
      _format((varExpression)d, document);
      return;
    } else if (d instanceof varReturn) {
      _format((varReturn)d, document);
      return;
    } else if (d instanceof JvmTypeConstraint) {
      _format((JvmTypeConstraint)d, document);
      return;
    } else if (d instanceof XExpression) {
      _format((XExpression)d, document);
      return;
    } else if (d instanceof XImportDeclaration) {
      _format((XImportDeclaration)d, document);
      return;
    } else if (d instanceof XImportSection) {
      _format((XImportSection)d, document);
      return;
    } else if (d instanceof BodyStatement) {
      _format((BodyStatement)d, document);
      return;
    } else if (d instanceof DSLProgram) {
      _format((DSLProgram)d, document);
      return;
    } else if (d instanceof FuncDefinition) {
      _format((FuncDefinition)d, document);
      return;
    } else if (d instanceof mainDeclared) {
      _format((mainDeclared)d, document);
      return;
    } else if (d instanceof EObject) {
      _format((EObject)d, document);
      return;
    } else if (d == null) {
      _format((Void)null, document);
      return;
    } else if (d != null) {
      _format(d, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(d, document).toString());
    }
  }
}