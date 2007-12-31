package org.openmrs.layout.web;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LayoutSupport<T extends LayoutTemplate> {

	private Log log = LogFactory.getLog(getClass());
	
	protected String defaultLayoutFormat;
	protected List<T> layoutTemplates;
	protected List<String> specialTokens;

	/**
	 * @return Returns the layoutTemplates.
	 */
	public List<T> getLayoutTemplates() {
		return layoutTemplates;
	}

	/**
	 * @param layoutTemplates The layoutTemplates to set.
	 */
	public void setLayoutTemplates(List<T> layoutTemplates) {
		this.layoutTemplates = layoutTemplates;
	}

	/**
	 * @return Returns the defaultLayoutTemplate.
	 */
	public T getDefaultLayoutTemplate() {
		return getLayoutTemplateByName(getDefaultLayoutFormat());
	}

	public T getLayoutTemplateByName(String templateName) {
		log.debug("looking for template name: " + templateName);
		
		if ( this.layoutTemplates != null && templateName != null ) {
			T ret = null;
			
			for ( T at : this.layoutTemplates ) {
				if ( at != null ) {
					if ( templateName.equalsIgnoreCase(at.getDisplayName())
							|| templateName.equalsIgnoreCase(at.getCodeName())
							|| templateName.equalsIgnoreCase(at.getCountry())) {
						ret = at;
						log.debug("Found Layout Template named " + at.getDisplayName());
					}
				}
			}
			
			return ret;
		} else {
			log.debug("No Layout Templates defined");
			return null;
		}
	}

	public T getLayoutTemplateByCodeName(String templateName) {
		if ( this.layoutTemplates != null && templateName != null ) {
			T ret = null;
			
			for ( T at : this.layoutTemplates ) {
				if ( at != null ) {
					if ( templateName.equalsIgnoreCase(at.getCodeName())) {
						ret = at;
						log.debug("Found Layout Template named " + at.getDisplayName());
					}
				}
			}
			
			return ret;
		} else {
			log.debug("No Layout Templates defined");
			return null;
		}
	}

	public T getLayoutTemplateByCountry(String templateName) {
		if ( this.layoutTemplates != null && templateName != null ) {
			T ret = null;
			
			for ( T at : this.layoutTemplates ) {
				if ( at != null ) {
					if ( templateName.equalsIgnoreCase(at.getCountry())) {
						ret = at;
						log.debug("Found Layout Template named " + at.getDisplayName());
					}
				}
			}
			
			return ret;
		} else {
			log.debug("No Layout Templates defined");
			return null;
		}
	}

	public T getLayoutTemplateByDisplayName(String templateName) {
		if ( this.layoutTemplates != null && templateName != null ) {
			T ret = null;
			
			for ( T at : this.layoutTemplates ) {
				if ( at != null ) {
					if ( templateName.equalsIgnoreCase(at.getDisplayName())) {
						ret = at;
						log.debug("Found Layout Template named " + at.getDisplayName());
					}
				}
			}
			
			return ret;
		} else {
			log.debug("No Layout Templates defined");
			return null;
		}
	}

	/**
	 * @return Returns the specialTokens.
	 */
	public List<String> getSpecialTokens() {
		return specialTokens;
	}

	/**
	 * @param specialTokens The specialTokens to set.
	 */
	public void setSpecialTokens(List<String> specialTokens) {
		this.specialTokens = specialTokens;
	}

	/**
	 * @return Returns the defaultLayoutFormat.
	 */
	public abstract String getDefaultLayoutFormat();

	/**
	 * @param defaultLayoutFormat The defaultLayoutFormat to set.
	 */
	public void setDefaultLayoutFormat(String defaultLayoutFormat) {
		this.defaultLayoutFormat = defaultLayoutFormat;
	}

}