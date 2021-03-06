
/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.studio.core.perspectives;

import java.util.Properties;

import it.eng.spagobi.meta.editor.multi.wizards.SpagoBIModelEditorWizard;
import it.eng.spagobi.studio.core.util.Utilities;
import it.eng.spagobi.studio.core.views.ResourceNavigator;
import it.eng.spagobi.studio.core.wizards.NewSpagoBIProjectWizard;
import it.eng.spagobi.studio.utils.util.SpagoBIStudioConstants;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class SpagoBIPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "it.eng.spagobi.studio.core.perspectives.SpagoBIPerspective"; 


	private IPageLayout factory;

	public SpagoBIPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout layout) {
		//		String editorArea = layout.getEditorArea();
		//		layout.setEditorAreaVisible(false);
		//		layout.setFixed(true);
		// 
		//		layout.addStandaloneView("it.eng.spagobi.studio.core.views.ResourceNavigator",  true /* show title */, IPageLayout.LEFT, 0.25f, editorArea);
		this.factory = layout;

		SpagoBIModelEditorWizard ciao = new SpagoBIModelEditorWizard();
		
		addViews();
		addShortcuts();
		addActions();

//		IWorkbenchPage iworkbenchpage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		iworkbenchpage.hideActionSet("org.eclipse.debug.ui.launchActionSet");
//		iworkbenchpage.hideActionSet("org.eclipse.debug.ui.debugActionSet");
//
//		iworkbenchpage.hideActionSet("org.eclipse.debug.ui.profileActionSet");
//		WorkbenchWindow activeWorkbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
//		ToolBarManager toolBarManager = activeWorkbenchWindow.getToolBarManager();
//		activeWorkbenchWindow.setCoolBarVisible(false);

		
		
	}

	/**
	 *  SpaoBI Perspectve View are:
	 *  - spagobi resource navigator
	 *  - project explorer
	 *  - data source view
	 */
	
	private void addViews() {
		IFolderLayout topLeft = factory.createFolder("topLeft", IPageLayout.LEFT, 0.20f, factory.getEditorArea());
		// Add the SpagoBI Resource Navigator
		topLeft.addView(ResourceNavigator.VIEW_ID);
		// add the resource viewer
		topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);

		// Add data source property view only in Meta case when models are activated
		Properties properties = new Utilities().getStudioMetaProperties();	
		if(Utilities.readBooleanProperty(properties, SpagoBIStudioConstants.CONFIG_PROPERTY_FOLDER_BUSINESS_MODELS)){
			IFolderLayout bottomRight = factory.createFolder("bottomRight", IPageLayout.BOTTOM, 0.85f, factory.getEditorArea());
			bottomRight.addView(SpagoBIStudioConstants.DATA_SOURCE_EXPLORER_VIEW_ID);
			factory.addShowViewShortcut(SpagoBIStudioConstants.DATA_SOURCE_EXPLORER_VIEW_ID);
		}
		
	}


	private void addShortcuts() {
		factory.addPerspectiveShortcut(PERSPECTIVE_ID);
		factory.addShowViewShortcut(ResourceNavigator.VIEW_ID);
		factory.addNewWizardShortcut(NewSpagoBIProjectWizard.SPAGOBI_PROJECT_WIZARD_ID);
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
	}

	private void addActions() {
		// Add "new wizards".

	}




}


//IPageLayout.


//		// Creates the overall folder layout. 
//		// Note that each new Folder uses a percentage of the remaining EditorArea.
//		
//		IFolderLayout bottom =
//			factory.createFolder(
//				"bottomRight", //NON-NLS-1
//				IPageLayout.BOTTOM,
//				0.75f,
//				factory.getEditorArea());
//		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
//		bottom.addView("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
//		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
//
//		IFolderLayout topLeft =
//			factory.createFolder(
//				"topLeft", //NON-NLS-1
//				IPageLayout.LEFT,
//				0.25f,
//				factory.getEditorArea());
//		topLeft.addView(IPageLayout.ID_RES_NAV);
//		topLeft.addView("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
//		
//		factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
//		factory.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
//}

//	private void addActionSets() {
//		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
//		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); //NON-NLS-1
//		factory.addActionSet(JavaUI.ID_ACTION_SET);
//		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
//		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
//	}
//
//	private void addPerspectiveShortcuts() {
//		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //NON-NLS-1
//		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); //NON-NLS-1
//		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //NON-NLS-1
//	}
//
//	private void addNewWizardShortcuts() {
//		factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");//NON-NLS-1
//		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
//		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
//	}
//
//	private void addViewShortcuts() {
//		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
//		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
//		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
//		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
//		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
//		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
//		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
//		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
//		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
//		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
//	}





