/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.studio.core.services.modelTemplate;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.studio.utils.bo.Document;
import it.eng.spagobi.studio.utils.bo.DocumentParameter;
import it.eng.spagobi.studio.utils.bo.Engine;
import it.eng.spagobi.studio.utils.bo.Template;
import it.eng.spagobi.studio.utils.exceptions.AlreadyPresentException;
import it.eng.spagobi.studio.utils.exceptions.NoActiveServerException;
import it.eng.spagobi.studio.utils.exceptions.NoDocumentException;
import it.eng.spagobi.studio.utils.services.SpagoBIServerObjectsFactory;
import it.eng.spagobi.studio.utils.util.SpagoBIStudioConstants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Called by action of model refresh
 * @author Antonella Giachino (antonella.giachino@eng.it)
 *
 */

public class RefreshModelService {

	private final Template template = new Template();

	Document document=null;
	ISelection selection;
	String projectName = null;
	String modelName = null;

	// fields to retrieve only once
	String[] roles=null;
	DocumentParameter[] parameters=null;
	Engine engine=null;
	
	AlreadyPresentException alreadyPresentException = new AlreadyPresentException();
	private static Logger logger = LoggerFactory.getLogger(RefreshModelService.class);


	public RefreshModelService(ISelection _selection) {
		selection = _selection;	
	}



	public void refreshModelTemplate() {
		
		IStructuredSelection sel = (IStructuredSelection)selection;

		// go on only if you selected a document (a file)
		Object objSel = sel.toList().get(0);
		File fileSel = null;
		try{
			fileSel = (File)objSel;
			projectName = fileSel.getProject().getName();
			modelName = fileSel.getName();
		}
		catch (Exception e) {
			logger.error("No file selected",e);		

			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Not a file", "You must select a file to refresh");		
			return;
		}

		logger.debug("get datamart.jar of model file name "+fileSel.getName());

		EmfXmiSerializer emfXmiSerializer = new EmfXmiSerializer();

		Model root = null;
		BusinessModel businessModel= null;
		try{
			root = emfXmiSerializer.deserialize(fileSel.getContents(true));
			logger.debug("Model root is [{}] ",root );
			businessModel = root.getBusinessModels().get(0);
			logger.debug("model "+businessModel.getName());	
		}
		catch (Exception e) {
			logger.error("error in retrieving business model; try refreshing model folder ",e);
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Warning",
			"error in retrieving business model: try refreshing model folder");				
			return;
		}
		final BusinessModel finalBusinessModel = businessModel;
		
		ProgressMonitorPart monitor;
		monitor = new ProgressMonitorPart(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), null);
		logger.debug("Do the model template refresh, model with name  " + modelName);
		
		final org.eclipse.core.internal.resources.File fileSel2 = fileSel ;
		final NoDocumentException documentException = new NoDocumentException();
		final NoActiveServerException noActiveServerException = new NoActiveServerException();

		IRunnableWithProgress op = new IRunnableWithProgress() {			
			public void run(IProgressMonitor monitor) throws InvocationTargetException {

				monitor.beginTask("Template Refresh for model " + modelName, IProgressMonitor.UNKNOWN);

				// document associated, upload the template
				SpagoBIServerObjectsFactory spagoBIServerObjects = null;
				try{
					spagoBIServerObjects = new SpagoBIServerObjectsFactory(projectName);
					
					documentException.setNoDocument(false);
					Template mytemplate  = spagoBIServerObjects.getServerDocuments().downloadDatamartFile(finalBusinessModel.getName(), modelName);
					if (mytemplate == null){
						logger.error("The download operation has returned a null object!");			
						documentException.setNoDocument(true) ;
						return;
					}
					template.setContent(mytemplate.getContent());
					template.setFileName(mytemplate.getFileName());						
					overwriteTemplate(template, fileSel2);					
				}

				 
				catch (NoActiveServerException e1) {
					noActiveServerException.setNoServer(true);
					return;
				}
				catch (RemoteException re) {
					logger.error("Error comunicating with server",re);			
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							"Error comunicating with server", "Error while uploading the template: missing comunication with server");	
					return;
				}
				catch (CoreException ec) {
					logger.error("Error in fie creation",ec);		
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							"Error in file creation", "Error in file creation");	
					return;
				}


				monitor.done();
				if (monitor.isCanceled())
					logger.error("The long running operation was cancelled");		
			}
		};


		ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());		
		try {
			dialog.run(true, true, op);
		} catch (InvocationTargetException e1) {
			logger.error("Error comunicating with server", e1);			
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Missing comunication with server; check server definition and if service is avalaible");	
			dialog.close();
			return;
		} catch (InterruptedException e1) {
			logger.error("Error comunicating with server");		
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Missing comunication with server; check server definition and if service is avalaible");	
			dialog.close();
			return;
		} 
		if(noActiveServerException.isNoServer()){
			logger.error("No server is defined active");			
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "No server is defined active");	
			return;
		}				
		// check if document has been found (could have been deleted) 
		if(documentException.isNoDocument() || template.getContent()==null){
			logger.warn("Document no more present on server or no permission " + modelName);					
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error upload", "Document not retrieved; check it is still on server and you have enough permission to reach it. Make a new Upload.");	
			return;
		}		
		
		dialog.close();

		String succesfullMessage="Succesfully replaced with the last model template (" + modelName + ")" ;
		
		logger.debug(succesfullMessage);					
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Refresh succesfull", succesfullMessage);		
		
	}


	public void overwriteTemplate(Template template, File fileSel) 
			throws CoreException{
		try {
			// get the directory
			Folder folder = (Folder)fileSel.getParent();
			String projectName = folder.getProject().getName();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			IPath pathFolder = folder.getProjectRelativePath(); 
			String templateFileName = template.getFileName();
			
			DataHandler dh = template.getContent(); 
			InputStream is = dh.getInputStream();
			
			IPath pathNewFile = pathFolder.append(templateFileName); 
			IFile newFile = project.getFile(pathNewFile);			
			
			//Rename the actual in backup file into a dedicated bck folder
			IFolder bckFolder = project.getFolder(pathFolder.append(SpagoBIStudioConstants.FOLDER_METADATA_MODEL_BCK));
			if(bckFolder == null || !bckFolder.exists()){
				bckFolder.create(true, true, null);
			}
			IPath pathOldFile = pathFolder.append(SpagoBIStudioConstants.FOLDER_METADATA_MODEL_BCK);
			pathOldFile = pathOldFile.append(templateFileName + "." + SpagoBIStudioConstants.BACKUP_EXTENSION);
			IFile oldFile = project.getFile(pathOldFile);
			if (oldFile.exists()){
				oldFile.delete(true, null);
			}
			oldFile.create(fileSel.getContents(), true, null);

			// Delete the local old file 
			if (fileSel.exists()){
				fileSel.delete(true, null);
			}
			// create new File
			newFile.create(is, true, null);
			//set the dirty property to true 
			newFile.setPersistentProperty(SpagoBIStudioConstants.DIRTY_MODEL, "true");
		} catch (IOException e1) {

			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Error in writing the file");				
			logger.error("Error in writing the file", e1);		

			return ;
		}

	}
	
}