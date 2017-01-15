package com.jambo.exam.comm;

import com.jambo.exam.business.example.company.persistent.CompanyVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 用于测试哪个类库复制Bean比较快
 * User: jinbo
 * Date: 13-7-27
 * Time: 下午8:46
 */
public class BeanCopyTest {
    public static void main(String[] args) {
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);

        CompanyVO cardPayOrderModel = new CompanyVO();
        cardPayOrderModel.setCompanyname("HS");
        cardPayOrderModel.setShortname("a1231241241awdasdf");
        cardPayOrderModel.setAccount("123124dzvsds");
        cardPayOrderModel.setId(2l);
        cardPayOrderModel.setState(Byte.valueOf("1"));

        CompanyVO companyModel = new CompanyVO();

        /**
         * 10W次
         * BeanUtils.copyProperties：718
         * beanCopier.copy：56
         */

        /**
         * 100W次
         * BeanUtils.copyProperties：5673
         * beanCopier.copy：70
         */
        int times = 1000000;

        //BeanUtils性能
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < times; i++) {
                BeanUtils.copyProperties(cardPayOrderModel, companyModel);
            }
            long end = System.currentTimeMillis();
            System.out.println("BeanUtils.copyProperties:" + (end - start));

            //BeanCopier性能
            long start1 = System.currentTimeMillis();
            BeanCopier beanCopier = BeanCopier.create(CompanyVO.class, CompanyVO.class,
                    false);
            for (int i = 0; i < times; i++) {
                beanCopier.copy(cardPayOrderModel, companyModel, null);
            }
            long end1 = System.currentTimeMillis();
            System.out.println("beanCopier.copy:" + (end1 - start1));

            //BeanCopier性能
            long start2 = System.currentTimeMillis();
            for (int i = 0; i < times; i++) {
                beanCopier = BeanCopier.create(CompanyVO.class, CompanyVO.class, false);
                beanCopier.copy(cardPayOrderModel, companyModel, null);
            }
            long end2 = System.currentTimeMillis();
            System.out.println("bad beanCopier.copy:" + (end2 - start2));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
