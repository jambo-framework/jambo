package com.jambo.exam.web.example.dubbo1;

import com.jambo.exam.business.example.company.persistent.CompanyDBParam;
import com.jambo.exam.business.example.company.persistent.CompanyVO;
import com.jambo.exam.service.ExampleService;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.infrastructure.service.BaseResponse;
import com.jambo.jop.infrastructure.service.BaseServiceBO;

import java.io.Serializable;

/**
 * 为了适配MVC框架，把DUBBO服务的基础功能包装成BO
 * 如果不需要这些功能，不需要使用这个类，可以直接把方法写到action里
 * LoginedUser: jinbo
 */
public class CompanyServiceBO extends BaseServiceBO {
    private ExampleService demoService;
    private DBAccessUser user;

    public CompanyServiceBO(ExampleService demoService, DBAccessUser user) {
        this.user = user;
        this.demoService = demoService;
    }

    public BaseVO doCreate(BaseVO vo) throws Exception {
        CompanyDBParam param = new CompanyDBParam();
        param.setOperator(user);
        param.setOperateType(DBQueryParam.OPERATE_TYPE_CREATE);
        BaseResponse<CompanyVO> response = demoService.manager(param, (CompanyVO) vo);
        return response.getVo();
    }

    public void doRemoveByVO(BaseVO vo) throws Exception {
        CompanyDBParam param = new CompanyDBParam();
        param.setOperator(user);
        param.setOperateType(DBQueryParam.OPERATE_TYPE_DELETE);
        demoService.manager(param, (CompanyVO) vo);
    }

    public void doRemoveByPK(Serializable pk) throws Exception {
        CompanyVO vo = new CompanyVO();
        vo.setId((Long) pk);
        doRemoveByVO(vo);
    }

    public BaseVO doUpdate(BaseVO vo) throws Exception {
        CompanyDBParam param = new CompanyDBParam();
        param.setOperator(user);
        param.setOperateType(DBQueryParam.OPERATE_TYPE_UPDATE);
        BaseResponse<CompanyVO> response = demoService.manager(param, (CompanyVO) vo);
        return response.getVo();
    }

    public BaseVO findByPk(Serializable pk) throws Exception {
        CompanyDBParam param = new CompanyDBParam();
        param.setOperator(user);

//        if (pk instanceof BaseVO){
//            param.setOperateType(DBQueryParam.OPERATE_TYPE_FINDBYVO);
//        } else {
//            param.set_pk((String) pk);
//            param.setOperateType(DBQueryParam.OPERATE_TYPE_FINDBYPK);
//        }
//
//        CompanyVO vo = new CompanyVO();
//        vo.setId((Long) pk);

        BaseResponse<CompanyVO> response = demoService.findByPk(param, pk);
        return response.getVo();
    }

    public DataPackage query(DBQueryParam param) throws Exception {
        param.setOperator(user);
        return demoService.query((CompanyDBParam) param);
    }

}
