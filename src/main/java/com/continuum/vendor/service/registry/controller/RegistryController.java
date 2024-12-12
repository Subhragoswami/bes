package com.continuum.vendor.service.registry.controller;

import com.continuum.vendor.service.registry.service.RegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class RegistryController {

    private final RegistryService registryService;

}
