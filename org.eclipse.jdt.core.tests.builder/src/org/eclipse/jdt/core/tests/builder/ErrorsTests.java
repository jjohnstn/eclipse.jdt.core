/*******************************************************************************
 * Copyright (c) 2002 IBM Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.core.tests.builder;

import junit.framework.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.tests.util.Util;


/**
 * Basic errors tests of the image builder.
 */
public class ErrorsTests extends Tests {
	public ErrorsTests(String name) {
		super(name);
	}
	
	public static Test suite() {
		return new TestSuite(ErrorsTests.class);
	}
	
	public void testErrors() {
		IPath projectPath = env.addProject("Project");
		env.addExternalJar(projectPath, Util.getJavaClassLib());
		fullBuild(projectPath);
		
		// remove old package fragment root so that names don't collide
		env.removePackageFragmentRoot(projectPath,"");
		
		IPath root = env.addPackageFragmentRoot(projectPath, "src");
		env.setOutputFolder(projectPath, "bin");
		
		env.addClass(root, "p1", "Indicted",
			"package p1;\n"+
			"public abstract class Indicted {\n"+
			"}\n"
			);
			
		IPath collaboratorPath =  env.addClass(root, "p2", "Collaborator",
			"package p2;\n"+
			"import p1.*;\n"+
			"public class Collaborator extends Indicted{\n"+
			"}\n"
			);
		
		fullBuild(projectPath);
		expectingNoProblems();
		
		env.addClass(root, "p1", "Indicted",
			"package p1;\n"+
			"public abstract class Indicted {\n"+
			"   public abstract void foo();\n"+
			"}\n"
			);
			
		incrementalBuild(projectPath);

		expectingOnlyProblemsFor(collaboratorPath);
		expectingOnlySpecificProblemFor(collaboratorPath, new Problem("Collaborator", "Class must implement the inherited abstract method Indicted.foo()", collaboratorPath));
	}
	
	/*
	 * Regression test for bug 2857 Renaming .java class with errors to .txt leaves errors in Task list (1GK06R3)	 */
	public void testRenameToNonJava() {
		IPath projectPath = env.addProject("Project");
		env.addExternalJar(projectPath, Util.getJavaClassLib());
		
		// remove old package fragment root so that names don't collide
		env.removePackageFragmentRoot(projectPath,"");
		
		IPath root = env.addPackageFragmentRoot(projectPath, "src");
		env.setOutputFolder(projectPath, "bin");
		
		IPath cuPath = env.addClass(root, "p1", "X",
			"package p1;\n"+
			"public class X extends Y {\n"+
			"}\n"
			);
			
		fullBuild(projectPath);
		expectingOnlyProblemsFor(cuPath);
		expectingOnlySpecificProblemFor(cuPath, new Problem("X", "Y cannot be resolved or is not a valid superclass", cuPath));
		
		
		env.renameCU(root.append("p1"), "X.java", "X.txt");
		incrementalBuild(projectPath);
		expectingNoProblems();
	}

}