/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public abstract class AbstractGraphServlet extends HttpServlet {
	
	public static final long serialVersionUID = 1231231L;
	
	private Log log = LogFactory.getLog(AbstractGraphServlet.class);
	
	// Supported mime types
	public static final String PNG_MIME_TYPE = "image/png";
	
	public static final String JPG_MIME_TYPE = "image/jpeg";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// Set default values
			Integer width = new Integer(500);
			Integer height = new Integer(300);
			String mimeType = PNG_MIME_TYPE;
			
			// Retrieve custom values
			try {
				width = Integer.parseInt(request.getParameter("width"));
			}
			catch (Exception e) {}
			
			try {
				height = Integer.parseInt(request.getParameter("height"));
			}
			catch (Exception e) {}
			
			if (request.getParameter("mimeType") != null) {
				mimeType = request.getParameter("mimeType");
			}
			
			JFreeChart chart = createChart(request, response);
			
			// Modify response to disable caching
			response.setHeader("Pragma", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			
			// Write chart out to response as image 
			try {
				if (JPG_MIME_TYPE.equalsIgnoreCase(mimeType)) {
					response.setContentType(JPG_MIME_TYPE);
					ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, width, height);
				} else if (PNG_MIME_TYPE.equalsIgnoreCase(mimeType)) {
					response.setContentType(PNG_MIME_TYPE);
					ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
				} else {
					// Throw exception: unsupported mime type
				}
			}
			catch (IOException e) {
				log.error(e);
			}
			
		}
		// Add error handling above and remove this try/catch 
		catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * Override this method for each graph
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract JFreeChart createChart(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
