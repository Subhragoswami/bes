package com.continuum.vendor.service.service;

import com.continuum.vendor.service.dao.CarbonCreditsDao;
import com.continuum.vendor.service.dao.ChargingStationDao;
import com.continuum.vendor.service.entity.vendor.CarbonCreditConfig;
import com.continuum.vendor.service.entity.vendor.ChargingStation;
import com.continuum.vendor.service.model.response.FileDataResponse;
import com.continuum.vendor.service.model.response.ResponseDto;
import com.continuum.vendor.service.repository.vendor.CarbonCreditConfigRepository;
import com.continuum.vendor.service.repository.vendor.VehicleEmissionFactorRepository;
import com.continuum.vendor.service.entity.vendor.VehicleEmissionFactor;
import com.continuum.vendor.service.util.enums.VehicleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.continuum.vendor.service.util.AppConstants.RESPONSE_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarbonCreditService {

    private final CarbonCreditConfigRepository carbonCreditConfigRepository;
    private final VehicleEmissionFactorRepository repository;

    private final ChargingStationDao chargingStationDao;
    private final CarbonCreditsDao carbonCreditsDao;

    private static Map<String, BigDecimal> configValues;
    private static Map<String, Map<String, BigDecimal>> emissionFactors;

    private static BigDecimal IR;
    private static BigDecimal GALLON_TO_LITRE;
    private static BigDecimal KM_TO_MILE;
    private static int CREDITING_PERIOD;
    private static BigDecimal NUMBER_OF_CARS;
    private static BigDecimal MILEAGE_GASOLINE_2W;
    private static BigDecimal MILEAGE_GASOLINE_4W;
    private static BigDecimal MILEAGE_GASOLINE_3W;
    private static BigDecimal KWH_PER_KM_TWO_WHEELER;
    private static BigDecimal KWH_PER_KM_FOUR_WHEELER;
    private static BigDecimal  KWH_PER_KM_THREE_WHEELER;
    private static BigDecimal CS_RENEWABLE;
    private static BigDecimal CS_NON_RENEWABLE;
    private static BigDecimal VEHICLE_EMISSION_FACTOR_2W;

    @PostConstruct
    public void init() {
        loadConfigurations();
        loadEmissionFactors();
        initializeConstants();
    }

    private void initializeConstants() {
        IR = configValues.get("IR");
        GALLON_TO_LITRE = configValues.get("GALLON_TO_LITRE");
        KM_TO_MILE = configValues.get("KM_TO_MILE");
        CREDITING_PERIOD = configValues.get("CREDITING_PERIOD").intValue();
        NUMBER_OF_CARS = configValues.get("NUMBER_OF_CARS");
        MILEAGE_GASOLINE_2W = configValues.get("MILEAGE_GASOLINE_2W");
        MILEAGE_GASOLINE_3W = configValues.get("MILEAGE_GASOLINE_3W");
        MILEAGE_GASOLINE_4W = configValues.get("MILEAGE_GASOLINE_4W");
        KWH_PER_KM_TWO_WHEELER = configValues.get("KWH_PER_KM_TWO_WHEELER");
        KWH_PER_KM_FOUR_WHEELER = configValues.get("KWH_PER_KM_FOUR_WHEELER");
        KWH_PER_KM_THREE_WHEELER = configValues.get("KWH_PER_KM_THREE_WHEELER");
        CS_RENEWABLE = configValues.get("CS_RENEWABLE");
        CS_NON_RENEWABLE = configValues.get("CS_NON_RENEWABLE");
        VEHICLE_EMISSION_FACTOR_2W = configValues.get("VEHICLE_EMISSION_FACTOR_2W");
    }

    private void loadEmissionFactors() {
        List<VehicleEmissionFactor> factors = repository.findAll();
        emissionFactors = new HashMap<>();
        for (VehicleEmissionFactor factor : factors) {
            emissionFactors.computeIfAbsent(factor.getCity().toUpperCase(), k -> new HashMap<>())
                    .put(factor.getVehicleType(), factor.getVehicleEmissionFactor());
        }
    }


    private void loadConfigurations() {
        configValues = new HashMap<>();
        List<CarbonCreditConfig> configs = carbonCreditConfigRepository.findAll();
        for (CarbonCreditConfig config : configs) {
            configValues.put(config.getKey(), config.getValue());
        }
    }

    private BigDecimal getConfigValue(String key) {
        return configValues.get(key);
    }

    private Optional<BigDecimal> getEmissionFactor(String city, String vehicleType) {
        return Optional.ofNullable(emissionFactors.getOrDefault(city.toUpperCase(), new HashMap<>()).get(vehicleType));
    }


    /******************** START: Carbon Credit Formula ********************/
    private static BigDecimal calculateKwhPer100Mile(BigDecimal kwhPerKmWheeler) {
        return kwhPerKmWheeler.multiply(BigDecimal.valueOf(100)).divide(KM_TO_MILE, 20, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateMileageMilesPerGallon(BigDecimal mileageGasoline) {
        return GALLON_TO_LITRE.multiply(KM_TO_MILE).multiply(mileageGasoline).setScale(20, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateTotalMilesPerGallon(BigDecimal mileageMilesPerGallon) {
        return mileageMilesPerGallon.multiply(NUMBER_OF_CARS).setScale(20, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateMileageMilesPerGallonDetermined(BigDecimal totalMilesPerGallon) {
        return totalMilesPerGallon.divide(NUMBER_OF_CARS, 20, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateBaselineEmission(VehicleType vehicleType, BigDecimal kwhConsumed, BigDecimal vehicleEmissionFactor) {
        BigDecimal kwhPer100Mile = getKWHPer100Mile(vehicleType);
        BigDecimal mileageMilesPerGallon = getMileageByVehicleType(vehicleType);

        BigDecimal totalMilesPerGallon = calculateTotalMilesPerGallon(mileageMilesPerGallon);
        BigDecimal mileageMilesPerGallonDetermined = calculateMileageMilesPerGallonDetermined(totalMilesPerGallon);

        return kwhConsumed.multiply(vehicleEmissionFactor)
                .multiply(BigDecimal.valueOf(100))
                .multiply(IR.pow(CREDITING_PERIOD - 1))
                .divide(kwhPer100Mile.multiply(mileageMilesPerGallonDetermined), 20, RoundingMode.HALF_UP)
                .setScale(10, RoundingMode.HALF_UP);
    }

    private static BigDecimal getKWHPer100Mile(VehicleType vehicleType) {
        switch (vehicleType) {
            case TWO_WHEELER:
                return calculateKwhPer100Mile(KWH_PER_KM_TWO_WHEELER);
            case THREE_WHEELER:
                return calculateKwhPer100Mile(KWH_PER_KM_THREE_WHEELER);
            case FOUR_WHEELER:
                return  calculateKwhPer100Mile(KWH_PER_KM_FOUR_WHEELER);
            case HEAVY:
                return  calculateKwhPer100Mile(KWH_PER_KM_FOUR_WHEELER);
            default:
                return calculateKwhPer100Mile(KWH_PER_KM_TWO_WHEELER);
        }

    }

    private static BigDecimal getMileageByVehicleType(VehicleType vehicleType) {
        switch (vehicleType) {
            case TWO_WHEELER:
                return calculateMileageMilesPerGallon(MILEAGE_GASOLINE_2W);
            case THREE_WHEELER:
                return calculateMileageMilesPerGallon(MILEAGE_GASOLINE_3W);
            case FOUR_WHEELER:
                return calculateMileageMilesPerGallon(MILEAGE_GASOLINE_4W);
            case HEAVY:
                return calculateMileageMilesPerGallon(MILEAGE_GASOLINE_4W);
            default:
                return calculateMileageMilesPerGallon(MILEAGE_GASOLINE_2W);
        }
    }

    private static BigDecimal calculatePowergridEmission(BigDecimal kwhConsumed) {
        return kwhConsumed.multiply(CS_NON_RENEWABLE).setScale(10, RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateCarbonCredit(BigDecimal baselineEmission, BigDecimal powerGridEmission) {
        return baselineEmission.subtract(powerGridEmission).setScale(10, RoundingMode.HALF_UP);
    }

    public BigDecimal compute(String vehicleTypeStr, String kwhConsumedStr, Long locationId) {

        double usedEnergy = Double.parseDouble(kwhConsumedStr);

        Optional<VehicleType> vehicleType = Optional.ofNullable(VehicleType.fromString(vehicleTypeStr));
        Optional<ChargingStation> chargingStation = chargingStationDao.findByLocatonId(locationId);

        if (usedEnergy >= 0.0 && vehicleType.isPresent() && chargingStation.isPresent()) {
            BigDecimal vehicleEmissionFactor = VehicleType.TWO_WHEELER.getName().equals(vehicleType.get().getName()) ? VEHICLE_EMISSION_FACTOR_2W :
                    getEmissionFactor(chargingStation.get().getCity(), vehicleType.get().getName()).orElse(BigDecimal.ZERO);

            if (vehicleEmissionFactor.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            }

            BigDecimal baselineEmission = calculateBaselineEmission(vehicleType.get(), BigDecimal.valueOf(usedEnergy), vehicleEmissionFactor);
            BigDecimal powerGridEmission = calculatePowergridEmission(BigDecimal.valueOf(usedEnergy));
            BigDecimal carbonCredit = calculateCarbonCredit(baselineEmission, powerGridEmission);

            return carbonCredit.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : carbonCredit;

        }
        return BigDecimal.ZERO;
    }

    public ResponseDto<BigDecimal> getTotalCarbonCredit() {
        return ResponseDto.<BigDecimal>builder().status(RESPONSE_SUCCESS).data(List.of(carbonCreditsDao.getTotalCarbonCredit())).build();
    }

    /******************** END: Carbon Credit Formula ********************/
}
