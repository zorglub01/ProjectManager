/**
 * 
 */
package com.lang.util;

/**
 * @author thomas
 *
 */
public aspect TestAOPCompliance {
	
	
	pointcut jsonwrapper() : call(String com.services.projects.bean.JsonProjectWrapper.getDescription());
	
	before() : jsonwrapper(){
		//System.out.println("COUCOU");
	}

}
