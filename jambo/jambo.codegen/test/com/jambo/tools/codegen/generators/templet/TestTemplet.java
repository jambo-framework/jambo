package com.jambo.tools.codegen.generators.templet;

import java.sql.Types;

import org.apache.velocity.app.Velocity;
//import org.junit.Before;
//import org.junit.Test;

public class TestTemplet {
//	@Before
	public void setUp() {
        try {
			Velocity.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
	public void testReadFile() throws Exception {
		new ResolveTemplet("Struts2FormTemplet.vm") ;
//		new ResolveTemplet("DaoTemplet.templets") ;
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.200.5.201:1521:BOSS15TEST", "test", "test");
//        DatabaseMetaData dmd = conn.getMetaData();
//        
//        ResultSet rs = null;
//
//        try {
//            rs = dmd.getPrimaryKeys(null, null, "EXAMPLE1");
//
//            List<Object> columns = new LinkedList<Object>();
//
//            while (rs.next()) {
//                JDBCUtil.Column aCol = new JDBCUtil.Column();
//                aCol.name = rs.getString(4);
//                System.out.println(aCol.name) ;
//                aCol.sqlType = rs.getShort(5);
//                aCol.sqlColumnLength = rs.getInt(7);
//                aCol.sqlDecimalLength = rs.getInt(9);
//                aCol.sqlNotNull = ("NO".equals(rs.getString(18)));
//                aCol.javaType = getJavaType(aCol.sqlType, aCol.sqlColumnLength,
//                        aCol.sqlDecimalLength);
//                columns.add(aCol);
//            }
//
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//        }
	}
	

    public static Class getJavaType(int sqlType, int columnSize,
        int decimalDigits) {

        Class rv = String.class;

        if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            rv = String.class;
        } else if ((sqlType == Types.FLOAT) || (sqlType == Types.REAL)) {
            rv = Float.class;
        } else if (sqlType == Types.INTEGER) {
            rv = Integer.class;
        } else if (sqlType == Types.DOUBLE) {
            rv = Double.class;
        } else if (sqlType == Types.DATE) {
            rv = java.util.Date.class;
        } else if (sqlType == Types.TIMESTAMP) {
            rv = java.util.Date.class;
        } else if (sqlType == Types.TIME) {
            rv = java.util.Date.class;
        }
        // commented to support JDK version < 1.4
        /*      else if (sqlType == Types.BOOLEAN) {
                rv = Boolean.class;
        } */
        else if (sqlType == Types.SMALLINT) {
            rv = Short.class;
        } else if (sqlType == Types.BIT) {
            rv = Byte.class;
        } else if (sqlType == Types.BIGINT) {
            rv = Long.class;
        } else if ((sqlType == Types.NUMERIC) || (sqlType == Types.DECIMAL)) {
            if (decimalDigits == 0) {
                if (columnSize == 1) {
                    rv = Byte.class;
                } else if (columnSize < 5) {
                    rv = Short.class;
                } else if (columnSize < 10) {
                    rv = Integer.class;
                } else {
                    rv = Long.class;
                }
            } else {
                if (columnSize < 9) {
                    rv = Float.class;
                } else {
                    rv = Double.class;
                }
            }
        }

        return rv;
    }

}
