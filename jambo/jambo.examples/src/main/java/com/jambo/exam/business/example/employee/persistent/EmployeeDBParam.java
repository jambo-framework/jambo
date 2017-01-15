/**
* auto-generated code
* Sat Jul 29 16:54:43 CST 2006
*/
package com.jambo.exam.business.example.employee.persistent;

import com.jambo.jop.infrastructure.db.DBQueryParam;


/**
 * <p>Title: CompanyListVO</p>
 * <p>Description: Query Params Object for CompanyDAO</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author 
 * @version 1.0
 */
public class EmployeeDBParam extends DBQueryParam {
    private String _sk_empname;
    private Long _ne_companyid;

      /**
       * @return Returns the _sk_companyname.
       */
      public String get_sk_empname() {
          return _sk_empname;
      }

      /**
       * @param _sk_companyname The _sk_companyname to set.
       */
      public void set_sk_empname(String _sk_empname) {
          this._sk_empname = _sk_empname;
      }

      /**
       * @return Returns the _sk_shortname.
       */
      public Long get_ne_companyid() {
          return _ne_companyid;
      }

      /**
       * @param _sk_shortname The _sk_shortname to set.
       */
      public void set_ne_companyid(Long _sk_shortname) {
          this._ne_companyid = _sk_shortname;
      }
}
