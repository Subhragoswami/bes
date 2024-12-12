package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.cms.VendorCustomerCMS;
import com.continuum.vendor.service.entity.vendor.VendorCustomer;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendorCustomerMapper {
    private final MapperFactory mapperFactory;
    private final MapperFacade mapperFacade;

    @Autowired
    public VendorCustomerMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFacade = mapperFactory.getMapperFacade();
        configureMapper();
    }

    private void configureMapper() {
        mapperFactory.classMap(VendorCustomer.class, VendorCustomerCMS.class)
                .byDefault()
                .mapNulls(false)
                .register();
    }

    public List<VendorCustomerCMS> mapToVendorCustomerListCMS(List<VendorCustomer> vendorCustomerList, String vendorCode) {
        List<VendorCustomerCMS> vendorCustomerCMSList = mapperFacade.mapAsList(vendorCustomerList, VendorCustomerCMS.class);
        vendorCustomerCMSList.forEach(vendorCustomerCMS -> {
            vendorCustomerCMS.setVendorCode(vendorCode);
        });
        return vendorCustomerCMSList;
    }
}


