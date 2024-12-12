package com.continuum.vendor.service.model.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChargingSessionFilter {
    private List<String> cities;
    private Date startDate;
    private Date endDate;
    private List<Long> stationIds;
}
