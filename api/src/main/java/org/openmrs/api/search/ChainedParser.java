/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.api.search;

/**
 *
 * @author czakian
 */
public interface ChainedParser<S, T> {
	
	public S eval(T a);
}
