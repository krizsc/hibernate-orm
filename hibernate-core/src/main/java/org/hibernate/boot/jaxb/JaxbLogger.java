/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.boot.jaxb;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.MessageLogger;
import org.jboss.logging.annotations.ValidIdRange;

/**
 * @author Steve Ebersole
 */
@MessageLogger( projectCode = "HHH" )
@ValidIdRange( min = 90005501, max = 90005600 )
public interface JaxbLogger extends BasicLogger {
	String LOGGER_NAME = "org.hibernate.orm.boot.jaxb";

	JaxbLogger JAXB_LOGGER = Logger.getMessageLogger(
			JaxbLogger.class,
			LOGGER_NAME
	);

	boolean TRACE_ENABLED = JAXB_LOGGER.isTraceEnabled();
	boolean DEBUG_ENABLED = JAXB_LOGGER.isDebugEnabled();
}
