/*
 * GAMA - V1.4 http://gama-platform.googlecode.com
 * 
 * (c) 2007-2011 UMI 209 UMMISCO IRD/UPMC & Partners (see below)
 * 
 * Developers :
 * 
 * - Alexis Drogoul, UMI 209 UMMISCO, IRD/UPMC (Kernel, Metamodel, GAML), 2007-2012
 * - Vo Duc An, UMI 209 UMMISCO, IRD/UPMC (SWT, multi-level architecture), 2008-2012
 * - Patrick Taillandier, UMR 6228 IDEES, CNRS/Univ. Rouen (Batch, GeoTools & JTS), 2009-2012
 * - Beno�t Gaudou, UMR 5505 IRIT, CNRS/Univ. Toulouse 1 (Documentation, Tests), 2010-2012
 * - Phan Huy Cuong, DREAM team, Univ. Can Tho (XText-based GAML), 2012
 * - Pierrick Koch, UMI 209 UMMISCO, IRD/UPMC (XText-based GAML), 2010-2011
 * - Romain Lavaud, UMI 209 UMMISCO, IRD/UPMC (RCP environment), 2010
 * - Francois Sempe, UMI 209 UMMISCO, IRD/UPMC (EMF model, Batch), 2007-2009
 * - Edouard Amouroux, UMI 209 UMMISCO, IRD/UPMC (C++ initial porting), 2007-2008
 * - Chu Thanh Quang, UMI 209 UMMISCO, IRD/UPMC (OpenMap integration), 2007-2008
 */
package idees.gama.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorFactory;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;



public class NewFileWizard  extends Wizard implements INewWizard {

	private NewFileWizardPage page;
	private ISelection selection;

	public NewFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/** Adding pages to the wizard. */
	@Override
	public void addPages() {
		page = new NewFileWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create
	 * an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String typeOfModel = page.getTypeOfModel();
		final String fileName = page.getFileName();
		final String author = page.getAuthor();
		final String title = page.getModelName();
		final String desc = page.getDescription();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, typeOfModel, fileName, author, title, desc,monitor);
				} catch (CoreException e) {
					e.printStackTrace();
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
					
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just
	 * replace its contents, and open the editor on the newly created file.
	 */
	private void doFinish(final String containerName, final String typeOfModel,
		final String fileName, final String author, final String title, final String desc,
		final IProgressMonitor monitor) throws CoreException {

		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));

		
		IContainer container = resource.getProject();
		
		/* Add the models folder */
		final IFolder modelFolder = container.getFolder(new Path("diagrams"));
		if ( !modelFolder.exists() ) {
			modelFolder.create(true, true, monitor);
		}

		final IFile file = container.getFile(new Path("diagrams/" + fileName));
		createDiagramEditor(file, title, monitor);
	}
	
	private void createDiagramEditor (IFile file, String diagramName, final IProgressMonitor monitor) {
		// Create the diagram
		Diagram diagram = Graphiti.getPeCreateService().createDiagram("gamaDiagram", diagramName, true);
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		TransactionalEditingDomain domain = createEmfFileForDiagram(uri, diagram);

		final DiagramEditorInput editorInput = new DiagramEditorInput(EcoreUtil.getURI(diagram), domain, "idees.gama.diagram.MyGamaDiagramTypeProvider");
		
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IEditorPart ep = page.openEditor(editorInput, "org.eclipse.graphiti.ui.editor.DiagramEditor");
					ep.doSave(monitor);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		monitor.worked(1);
	} 
	
	public static TransactionalEditingDomain createEmfFileForDiagram(URI uri, final Diagram diagram) {
		 // Create a resource set and EditingDomain
		 TransactionalEditingDomain editingDomain = DiagramEditorFactory.createResourceSetAndEditingDomain();
		 ResourceSet resourceSet = editingDomain.getResourceSet();

		 // Create a resource for this file.
		 final Resource resource = resourceSet.createResource(uri);
		  
		 editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
		  @Override
		  protected void doExecute() {
		   resource.setTrackingModification(true);
		   resource.getContents().add(diagram);
		  }
		 });

		 return editingDomain;
		}


	/** We will accept the selection in the workbench to see if we can initialize from it. */
	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		this.selection = selection;
	}
}