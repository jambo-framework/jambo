package com.jambo.jop.business.base.dictitem;

import com.jambo.jop.infrastructure.db.AbstractDAO;

/**
 * <p>Title: DictitemDAO</p>
 * <p>Description: Data Access Object for CompanyVO</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: jambo-framework Tech Ltd.</p>
 * @author Yedaoe
 * @version 1.0
 */
public class DictitemDAO extends AbstractDAO implements IDictitemDAO{

    /**
     * default constructor
     */
    public DictitemDAO(){
        super(DictitemVO.class);
    }
}
