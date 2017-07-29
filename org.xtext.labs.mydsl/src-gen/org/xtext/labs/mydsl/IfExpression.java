/**
 * generated by Xtext 2.12.0
 */
package org.xtext.labs.mydsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.labs.mydsl.IfExpression#getIfconditon <em>Ifconditon</em>}</li>
 *   <li>{@link org.xtext.labs.mydsl.IfExpression#getThen <em>Then</em>}</li>
 *   <li>{@link org.xtext.labs.mydsl.IfExpression#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @see org.xtext.labs.mydsl.MydslPackage#getIfExpression()
 * @model
 * @generated
 */
public interface IfExpression extends BodyStatement
{
  /**
   * Returns the value of the '<em><b>Ifconditon</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ifconditon</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ifconditon</em>' containment reference.
   * @see #setIfconditon(varExpression)
   * @see org.xtext.labs.mydsl.MydslPackage#getIfExpression_Ifconditon()
   * @model containment="true"
   * @generated
   */
  varExpression getIfconditon();

  /**
   * Sets the value of the '{@link org.xtext.labs.mydsl.IfExpression#getIfconditon <em>Ifconditon</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ifconditon</em>' containment reference.
   * @see #getIfconditon()
   * @generated
   */
  void setIfconditon(varExpression value);

  /**
   * Returns the value of the '<em><b>Then</b></em>' containment reference list.
   * The list contents are of type {@link org.xtext.labs.mydsl.BodyStatement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Then</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Then</em>' containment reference list.
   * @see org.xtext.labs.mydsl.MydslPackage#getIfExpression_Then()
   * @model containment="true"
   * @generated
   */
  EList<BodyStatement> getThen();

  /**
   * Returns the value of the '<em><b>Else</b></em>' containment reference list.
   * The list contents are of type {@link org.xtext.labs.mydsl.BodyStatement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Else</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else</em>' containment reference list.
   * @see org.xtext.labs.mydsl.MydslPackage#getIfExpression_Else()
   * @model containment="true"
   * @generated
   */
  EList<BodyStatement> getElse();

} // IfExpression