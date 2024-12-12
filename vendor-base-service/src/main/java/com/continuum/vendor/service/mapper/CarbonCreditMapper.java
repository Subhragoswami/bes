package com.continuum.vendor.service.mapper;

import com.continuum.vendor.service.entity.cms.CarbonCreditsCMS;
import com.continuum.vendor.service.entity.vendor.CarbonCredits;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarbonCreditMapper {

    private final MapperFacade mapperFacade;

    public CarbonCreditMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(CarbonCredits.class, CarbonCreditsCMS.class)
                .byDefault()
                .register();
        this.mapperFacade = mapperFactory.getMapperFacade();
    }


    public List<CarbonCreditsCMS> mapCarbonCreditList(List<CarbonCredits> carbonCreditsList, String vendorCode) {
        List<CarbonCreditsCMS> carbonCreditsCMSList = mapperFacade.mapAsList(carbonCreditsList, CarbonCreditsCMS.class);
        carbonCreditsCMSList.forEach(data ->{
            data.setVendorCode(vendorCode);
        });
        return carbonCreditsCMSList;
    }
}
