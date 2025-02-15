/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: RowIdType.java 6477 2005-04-21 07:39:21Z oneovthafew $
package org.hibernate.orm.test.rowid;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * @author Gavin King
 */
public class RowIdType implements UserType {

	public int[] sqlTypes() {
		return new int[] { Types.JAVA_OBJECT };
	}

	public Class returnedClass() {
		return Object.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
		return rs.getObject( position );
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		throw new UnsupportedOperationException();
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return null;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return null;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return null;
	}

}
