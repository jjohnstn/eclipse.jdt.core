/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.model;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.ICompilationUnit;

import junit.framework.*;

public class ResolveTests_1_5 extends AbstractJavaModelTests {
Hashtable oldOptions;

public static Test suite() {
	TestSuite suite = new Suite(ResolveTests_1_5.class.getName());		

	if (true) {
		Class c = ResolveTests_1_5.class;
		Method[] methods = c.getMethods();
		for (int i = 0, max = methods.length; i < max; i++) {
			if (methods[i].getName().startsWith("test")) { //$NON-NLS-1$
				suite.addTest(new ResolveTests_1_5(methods[i].getName()));
			}
		}
		return suite;
	}
	suite.addTest(new ResolveTests_1_5("test0028"));			
	return suite;
}

public ResolveTests_1_5(String name) {
	super(name);
}
public void setUpSuite() throws Exception {
	super.setUpSuite();
	
	setUpJavaProject("Resolve");
	
	this.oldOptions = JavaCore.getOptions();
	Hashtable options = new Hashtable(this.oldOptions);
	options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
	JavaCore.setOptions(options);
	
	waitUntilIndexesReady();
}

public void tearDownSuite() throws Exception {
	deleteProject("Resolve");
	
	JavaCore.setOptions(oldOptions);
	
	super.tearDownSuite();
}

public void test0001() throws JavaModelException {
	ICompilationUnit cu = getCompilationUnit("Resolve", "src2", "test0001", "Test.java");
	
	String str = cu.getSource();
	String selection = "iii";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"iii [in foo(Iterable) [in Test [in Test.java [in test0001 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0002() throws JavaModelException {
	ICompilationUnit cu = getCompilationUnit("Resolve", "src2", "test0002", "Test.java");
	
	String str = cu.getSource();
	String selection = "Y";
	int start = str.indexOf(selection);
	int length = selection.length();
	
	IJavaElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Y [in X [in Test [in Test.java [in test0002 [in src2 [in Resolve]]]]]]",
		elements
	);
}
public void test0003() throws JavaModelException {
	ICompilationUnit cu = getCompilationUnit("Resolve", "src2", "test0003", "Test.java");
	
	String str = cu.getSource();
	String selection = "X";
	int start = str.indexOf(selection);
	int length = selection.length();
	
	IJavaElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"X [in Test [in Test.java [in test0003 [in src2 [in Resolve]]]]]",
		elements
	);
}
public void test0004() throws JavaModelException {
	ICompilationUnit cu = getCompilationUnit("Resolve", "src2", "test0004", "Test.java");
	
	String str = cu.getSource();
	String selection = "Test";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"Test [in Test.java [in test0004 [in src2 [in Resolve]]]]",
		elements
	);
}
public void test0005() throws JavaModelException {
	ICompilationUnit cu = getCompilationUnit("Resolve", "src2", "test0005", "Test.java");
	
	String str = cu.getSource();
	String selection = "test0005";
	int start = str.lastIndexOf(selection);
	int length = selection.length();
	
	IJavaElement[] elements = cu.codeSelect(start, length);
	assertElementsEqual(
		"Unexpected elements",
		"test0005 [in src2 [in Resolve]]",
		elements
	);
}
}
