/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.test.integration.customtype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.NotYetImplementedFor6Exception;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Custom type used to persist binary representation of Java object in the database.
 * Spans over two columns - one storing text representation of Java class name and the second one
 * containing binary data.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class ObjectUserType implements UserType {
	private static final int[] TYPES = new int[] {Types.VARCHAR, Types.BLOB};

	@Override
	public int[] sqlTypes() {
		return TYPES;
	}

	@Override
	public Class returnedClass() {
		return Object.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if ( x == y ) {
			return true;
		}
		if ( x == null || y == null ) {
			return false;
		}
		return x.equals( y );
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
		throw new NotYetImplementedFor6Exception(
				"See https://github.com/hibernate/hibernate-orm/discussions/3960"
		);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if ( value == null ) {
			st.setNull( index, TYPES[0] );
			st.setNull( index + 1, TYPES[1] );
		}
		else {
			st.setString( index, value.getClass().getName() );
			st.setBinaryStream( index + 1, convertObjectToInputStream( value ) );
		}
	}

	private InputStream convertObjectToInputStream(Object value) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
			objectOutputStream.writeObject( value );
			objectOutputStream.flush();
			return new ByteArrayInputStream( byteArrayOutputStream.toByteArray() );
		}
		catch (IOException e) {
			throw new RuntimeException( e );
		}
		finally {
			closeQuietly( objectOutputStream );
		}
	}

	private Object convertInputStreamToObject(InputStream inputStream) {
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream( inputStream );
			return objectInputStream.readObject();
		}
		catch (Exception e) {
			throw new RuntimeException( e );
		}
		finally {
			closeQuietly( objectInputStream );
		}
	}

	private void closeQuietly(OutputStream stream) {
		if ( stream != null ) {
			try {
				stream.close();
			}
			catch (IOException e) {
			}
		}
	}

	private void closeQuietly(InputStream stream) {
		if ( stream != null ) {
			try {
				stream.close();
			}
			catch (IOException e) {
			}
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value; // Persisting only immutable types.
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
