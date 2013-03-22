/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gama.impl;

import gama.EGamaModel;
import gama.ELayerAspect;
import gama.GamaPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ELayer Aspect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gama.impl.ELayerAspectImpl#getGamlCode <em>Gaml Code</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getName <em>Name</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getModel <em>Model</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getShape <em>Shape</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getColor <em>Color</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getEmpty <em>Empty</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getRotate <em>Rotate</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getSize <em>Size</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getHeigth <em>Heigth</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getRadius <em>Radius</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getPath <em>Path</em>}</li>
 *   <li>{@link gama.impl.ELayerAspectImpl#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ELayerAspectImpl extends EObjectImpl implements ELayerAspect {
	/**
	 * The default value of the '{@link #getGamlCode() <em>Gaml Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGamlCode()
	 * @generated
	 * @ordered
	 */
	protected static final String GAML_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGamlCode() <em>Gaml Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGamlCode()
	 * @generated
	 * @ordered
	 */
	protected String gamlCode = GAML_CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected EGamaModel model;

	/**
	 * The default value of the '{@link #getShape() <em>Shape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShape()
	 * @generated
	 * @ordered
	 */
	protected static final String SHAPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShape() <em>Shape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShape()
	 * @generated
	 * @ordered
	 */
	protected String shape = SHAPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected static final String COLOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected String color = COLOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getEmpty() <em>Empty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmpty()
	 * @generated
	 * @ordered
	 */
	protected static final String EMPTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEmpty() <em>Empty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmpty()
	 * @generated
	 * @ordered
	 */
	protected String empty = EMPTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getRotate() <em>Rotate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRotate()
	 * @generated
	 * @ordered
	 */
	protected static final String ROTATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRotate() <em>Rotate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRotate()
	 * @generated
	 * @ordered
	 */
	protected String rotate = ROTATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final String SIZE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected String size = SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected static final String WIDTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected String width = WIDTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeigth() <em>Heigth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeigth()
	 * @generated
	 * @ordered
	 */
	protected static final String HEIGTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHeigth() <em>Heigth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeigth()
	 * @generated
	 * @ordered
	 */
	protected String heigth = HEIGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getRadius() <em>Radius</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRadius()
	 * @generated
	 * @ordered
	 */
	protected static final String RADIUS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRadius() <em>Radius</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRadius()
	 * @generated
	 * @ordered
	 */
	protected String radius = RADIUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ELayerAspectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GamaPackage.Literals.ELAYER_ASPECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGamlCode() {
		return gamlCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGamlCode(String newGamlCode) {
		String oldGamlCode = gamlCode;
		gamlCode = newGamlCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__GAML_CODE, oldGamlCode, gamlCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGamaModel getModel() {
		if (model != null && model.eIsProxy()) {
			InternalEObject oldModel = (InternalEObject)model;
			model = (EGamaModel)eResolveProxy(oldModel);
			if (model != oldModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GamaPackage.ELAYER_ASPECT__MODEL, oldModel, model));
			}
		}
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EGamaModel basicGetModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(EGamaModel newModel) {
		EGamaModel oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShape(String newShape) {
		String oldShape = shape;
		shape = newShape;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__SHAPE, oldShape, shape));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getColor() {
		return color;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColor(String newColor) {
		String oldColor = color;
		color = newColor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__COLOR, oldColor, color));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEmpty() {
		return empty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEmpty(String newEmpty) {
		String oldEmpty = empty;
		empty = newEmpty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__EMPTY, oldEmpty, empty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRotate() {
		return rotate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRotate(String newRotate) {
		String oldRotate = rotate;
		rotate = newRotate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__ROTATE, oldRotate, rotate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize(String newSize) {
		String oldSize = size;
		size = newSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__SIZE, oldSize, size));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWidth(String newWidth) {
		String oldWidth = width;
		width = newWidth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__WIDTH, oldWidth, width));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHeigth() {
		return heigth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeigth(String newHeigth) {
		String oldHeigth = heigth;
		heigth = newHeigth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__HEIGTH, oldHeigth, heigth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRadius() {
		return radius;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRadius(String newRadius) {
		String oldRadius = radius;
		radius = newRadius;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__RADIUS, oldRadius, radius));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__PATH, oldPath, path));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GamaPackage.ELAYER_ASPECT__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GamaPackage.ELAYER_ASPECT__GAML_CODE:
				return getGamlCode();
			case GamaPackage.ELAYER_ASPECT__NAME:
				return getName();
			case GamaPackage.ELAYER_ASPECT__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
			case GamaPackage.ELAYER_ASPECT__SHAPE:
				return getShape();
			case GamaPackage.ELAYER_ASPECT__COLOR:
				return getColor();
			case GamaPackage.ELAYER_ASPECT__EMPTY:
				return getEmpty();
			case GamaPackage.ELAYER_ASPECT__ROTATE:
				return getRotate();
			case GamaPackage.ELAYER_ASPECT__SIZE:
				return getSize();
			case GamaPackage.ELAYER_ASPECT__WIDTH:
				return getWidth();
			case GamaPackage.ELAYER_ASPECT__HEIGTH:
				return getHeigth();
			case GamaPackage.ELAYER_ASPECT__RADIUS:
				return getRadius();
			case GamaPackage.ELAYER_ASPECT__PATH:
				return getPath();
			case GamaPackage.ELAYER_ASPECT__TEXT:
				return getText();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GamaPackage.ELAYER_ASPECT__GAML_CODE:
				setGamlCode((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__NAME:
				setName((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__MODEL:
				setModel((EGamaModel)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__SHAPE:
				setShape((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__COLOR:
				setColor((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__EMPTY:
				setEmpty((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__ROTATE:
				setRotate((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__SIZE:
				setSize((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__WIDTH:
				setWidth((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__HEIGTH:
				setHeigth((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__RADIUS:
				setRadius((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__PATH:
				setPath((String)newValue);
				return;
			case GamaPackage.ELAYER_ASPECT__TEXT:
				setText((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GamaPackage.ELAYER_ASPECT__GAML_CODE:
				setGamlCode(GAML_CODE_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__MODEL:
				setModel((EGamaModel)null);
				return;
			case GamaPackage.ELAYER_ASPECT__SHAPE:
				setShape(SHAPE_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__COLOR:
				setColor(COLOR_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__EMPTY:
				setEmpty(EMPTY_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__ROTATE:
				setRotate(ROTATE_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__SIZE:
				setSize(SIZE_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__WIDTH:
				setWidth(WIDTH_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__HEIGTH:
				setHeigth(HEIGTH_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__RADIUS:
				setRadius(RADIUS_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case GamaPackage.ELAYER_ASPECT__TEXT:
				setText(TEXT_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GamaPackage.ELAYER_ASPECT__GAML_CODE:
				return GAML_CODE_EDEFAULT == null ? gamlCode != null : !GAML_CODE_EDEFAULT.equals(gamlCode);
			case GamaPackage.ELAYER_ASPECT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case GamaPackage.ELAYER_ASPECT__MODEL:
				return model != null;
			case GamaPackage.ELAYER_ASPECT__SHAPE:
				return SHAPE_EDEFAULT == null ? shape != null : !SHAPE_EDEFAULT.equals(shape);
			case GamaPackage.ELAYER_ASPECT__COLOR:
				return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals(color);
			case GamaPackage.ELAYER_ASPECT__EMPTY:
				return EMPTY_EDEFAULT == null ? empty != null : !EMPTY_EDEFAULT.equals(empty);
			case GamaPackage.ELAYER_ASPECT__ROTATE:
				return ROTATE_EDEFAULT == null ? rotate != null : !ROTATE_EDEFAULT.equals(rotate);
			case GamaPackage.ELAYER_ASPECT__SIZE:
				return SIZE_EDEFAULT == null ? size != null : !SIZE_EDEFAULT.equals(size);
			case GamaPackage.ELAYER_ASPECT__WIDTH:
				return WIDTH_EDEFAULT == null ? width != null : !WIDTH_EDEFAULT.equals(width);
			case GamaPackage.ELAYER_ASPECT__HEIGTH:
				return HEIGTH_EDEFAULT == null ? heigth != null : !HEIGTH_EDEFAULT.equals(heigth);
			case GamaPackage.ELAYER_ASPECT__RADIUS:
				return RADIUS_EDEFAULT == null ? radius != null : !RADIUS_EDEFAULT.equals(radius);
			case GamaPackage.ELAYER_ASPECT__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case GamaPackage.ELAYER_ASPECT__TEXT:
				return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (gamlCode: ");
		result.append(gamlCode);
		result.append(", name: ");
		result.append(name);
		result.append(", shape: ");
		result.append(shape);
		result.append(", color: ");
		result.append(color);
		result.append(", empty: ");
		result.append(empty);
		result.append(", rotate: ");
		result.append(rotate);
		result.append(", size: ");
		result.append(size);
		result.append(", width: ");
		result.append(width);
		result.append(", heigth: ");
		result.append(heigth);
		result.append(", radius: ");
		result.append(radius);
		result.append(", path: ");
		result.append(path);
		result.append(", text: ");
		result.append(text);
		result.append(')');
		return result.toString();
	}

} //ELayerAspectImpl