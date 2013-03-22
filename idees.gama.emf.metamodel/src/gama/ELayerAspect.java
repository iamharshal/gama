/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gama;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ELayer Aspect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gama.ELayerAspect#getGamlCode <em>Gaml Code</em>}</li>
 *   <li>{@link gama.ELayerAspect#getName <em>Name</em>}</li>
 *   <li>{@link gama.ELayerAspect#getModel <em>Model</em>}</li>
 *   <li>{@link gama.ELayerAspect#getShape <em>Shape</em>}</li>
 *   <li>{@link gama.ELayerAspect#getColor <em>Color</em>}</li>
 *   <li>{@link gama.ELayerAspect#getEmpty <em>Empty</em>}</li>
 *   <li>{@link gama.ELayerAspect#getRotate <em>Rotate</em>}</li>
 *   <li>{@link gama.ELayerAspect#getSize <em>Size</em>}</li>
 *   <li>{@link gama.ELayerAspect#getWidth <em>Width</em>}</li>
 *   <li>{@link gama.ELayerAspect#getHeigth <em>Heigth</em>}</li>
 *   <li>{@link gama.ELayerAspect#getRadius <em>Radius</em>}</li>
 *   <li>{@link gama.ELayerAspect#getPath <em>Path</em>}</li>
 *   <li>{@link gama.ELayerAspect#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see gama.GamaPackage#getELayerAspect()
 * @model
 * @generated
 */
public interface ELayerAspect extends EObject {
	/**
	 * Returns the value of the '<em><b>Gaml Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gaml Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Gaml Code</em>' attribute.
	 * @see #setGamlCode(String)
	 * @see gama.GamaPackage#getELayerAspect_GamlCode()
	 * @model derived="true"
	 * @generated
	 */
	String getGamlCode();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getGamlCode <em>Gaml Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Gaml Code</em>' attribute.
	 * @see #getGamlCode()
	 * @generated
	 */
	void setGamlCode(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gama.GamaPackage#getELayerAspect_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(EGamaModel)
	 * @see gama.GamaPackage#getELayerAspect_Model()
	 * @model required="true"
	 * @generated
	 */
	EGamaModel getModel();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(EGamaModel value);

	/**
	 * Returns the value of the '<em><b>Shape</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shape</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shape</em>' attribute.
	 * @see #setShape(String)
	 * @see gama.GamaPackage#getELayerAspect_Shape()
	 * @model
	 * @generated
	 */
	String getShape();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getShape <em>Shape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shape</em>' attribute.
	 * @see #getShape()
	 * @generated
	 */
	void setShape(String value);

	/**
	 * Returns the value of the '<em><b>Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Color</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Color</em>' attribute.
	 * @see #setColor(String)
	 * @see gama.GamaPackage#getELayerAspect_Color()
	 * @model
	 * @generated
	 */
	String getColor();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getColor <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Color</em>' attribute.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(String value);

	/**
	 * Returns the value of the '<em><b>Empty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Empty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Empty</em>' attribute.
	 * @see #setEmpty(String)
	 * @see gama.GamaPackage#getELayerAspect_Empty()
	 * @model
	 * @generated
	 */
	String getEmpty();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getEmpty <em>Empty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Empty</em>' attribute.
	 * @see #getEmpty()
	 * @generated
	 */
	void setEmpty(String value);

	/**
	 * Returns the value of the '<em><b>Rotate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rotate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rotate</em>' attribute.
	 * @see #setRotate(String)
	 * @see gama.GamaPackage#getELayerAspect_Rotate()
	 * @model
	 * @generated
	 */
	String getRotate();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getRotate <em>Rotate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rotate</em>' attribute.
	 * @see #getRotate()
	 * @generated
	 */
	void setRotate(String value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #setSize(String)
	 * @see gama.GamaPackage#getELayerAspect_Size()
	 * @model
	 * @generated
	 */
	String getSize();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(String value);

	/**
	 * Returns the value of the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see #setWidth(String)
	 * @see gama.GamaPackage#getELayerAspect_Width()
	 * @model
	 * @generated
	 */
	String getWidth();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getWidth <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #getWidth()
	 * @generated
	 */
	void setWidth(String value);

	/**
	 * Returns the value of the '<em><b>Heigth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heigth</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heigth</em>' attribute.
	 * @see #setHeigth(String)
	 * @see gama.GamaPackage#getELayerAspect_Heigth()
	 * @model
	 * @generated
	 */
	String getHeigth();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getHeigth <em>Heigth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heigth</em>' attribute.
	 * @see #getHeigth()
	 * @generated
	 */
	void setHeigth(String value);

	/**
	 * Returns the value of the '<em><b>Radius</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Radius</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Radius</em>' attribute.
	 * @see #setRadius(String)
	 * @see gama.GamaPackage#getELayerAspect_Radius()
	 * @model
	 * @generated
	 */
	String getRadius();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getRadius <em>Radius</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Radius</em>' attribute.
	 * @see #getRadius()
	 * @generated
	 */
	void setRadius(String value);

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see gama.GamaPackage#getELayerAspect_Path()
	 * @model
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see gama.GamaPackage#getELayerAspect_Text()
	 * @model
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link gama.ELayerAspect#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

} // ELayerAspect