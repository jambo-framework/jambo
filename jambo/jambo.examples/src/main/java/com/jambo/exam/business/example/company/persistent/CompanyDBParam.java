/**
* auto-generated code
* Sat Jul 29 16:54:43 CST 2006
*/
package com.jambo.exam.business.example.company.persistent;

import com.jambo.jop.infrastructure.db.DBQueryParam;


/**
 * <p>Title: CompanyListVO</p>
 * <p>Description: Query Params Object for CompanyDAO</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author 
 * @version 1.0
 */
public class CompanyDBParam extends DBQueryParam {
    private String _sk_companyname;
    private String _sk_shortname;

      /**
       * @return Returns the _sk_companyname.
       */
      public String get_sk_companyname() {
          return _sk_companyname;
      }

      /**
       * @param _sk_companyname The _sk_companyname to set.
       */
      public void set_sk_companyname(String _sk_companyname) {
          this._sk_companyname = _sk_companyname;
      }

      /**
       * @return Returns the _sk_shortname.
       */
      public String get_sk_shortname() {
          return _sk_shortname;
      }

      /**
       * @param _sk_shortname The _sk_shortname to set.
       */
      public void set_sk_shortname(String _sk_shortname) {
          this._sk_shortname = _sk_shortname;
      }
}
