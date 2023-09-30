package com.example.artvswar.controller;

import com.example.artvswar.dto.request.shipping.AddressRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingRateResponseDto;
import com.example.artvswar.service.shipping.ShipEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipping")
public class ShippingController {
    private final ShipEngineService shipEngineService;

    @GetMapping("/getCarriers")
    public ResponseEntity<Map<String, String>> listCarriers() {
        Map<String, String> stringStringMap = shipEngineService.listCarriers();
        return new ResponseEntity<>(stringStringMap, HttpStatus.OK) ;
    }

    @PostMapping("/validateAddress")
    public List<Map<String, String>> validateAddress(@RequestBody @Valid AddressRequestDto dto) {
        return shipEngineService.validateAddresses(dto);
    }

    @PostMapping("/getRates")
    public List<ShippingRateResponseDto> getRates(@RequestParam Set<Long> paintingIds, @RequestBody @Valid ShippingRequestDto dto) {
        return shipEngineService.getRatesWithShipmentDetails(dto, paintingIds);
    }

    @PostMapping("/createLabel")
    public Map<String, String> getLabelFromShipmentDetails(@RequestBody @Valid ShippingRequestDto dto) {
        return shipEngineService.createLabelFromShipmentDetails(dto);
    }

    @GetMapping("/createLabel/{rateId}")
    public Map<String, String> getLabelFromShipmentDetails(@PathVariable String rateId) {
        return shipEngineService.createLabelFromRateId(rateId);
    }

    @GetMapping("/track/{labelId}")
    public Map<String, String> trackUsingLabelId(@PathVariable String labelId) {
        return shipEngineService.trackUsingLabelId(labelId);
    }

    @PutMapping("/voidLabel/{labelId}")
    public Map<String, String> voidLabel(@PathVariable String labelId) {
        return shipEngineService.voidLabel(labelId);
    }
}
