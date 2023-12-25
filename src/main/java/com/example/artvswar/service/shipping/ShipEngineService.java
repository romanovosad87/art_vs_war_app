package com.example.artvswar.service.shipping;

import com.example.artvswar.dto.request.shipping.AddressRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRequestDto;
import com.example.artvswar.dto.response.shipping.ShippingRateResponseDto;
import com.example.artvswar.exception.ShippingNotProcessingException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorShippingAddress;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.PaintingService;
import com.neovisionaries.i18n.CountryCode;
import com.shipengine.ShipEngine;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ShipEngineService {
    private static final String UPS_ID = "se-121179";
    private static final String STAMPS_ID = "se-121178";
    private final ShipEngine shipEngine;
    private final PaintingService paintingService;


    public Map<String, String> listCarriers() {
        Map<String, String> result = shipEngine.listCarriers();
        System.out.println("result = " + result);
        return result;
    }

    public List<Map<String, String>> validateAddresses(AddressRequestDto dto) {
        List<CountryCode> codes = CountryCode.findByName(dto.getCountry());
        CountryCode countryCode = CountryCode.UNDEFINED;
        if (!codes.isEmpty()) {
            countryCode = codes.get(0);
        }
        Map<String, String> stubAddress = Map.of(
//                "name", "ShipEngine",
//                "company", "Auctane",
                "phone", dto.getPhone(),
                "address_line1", dto.getAddressLine1(),
                "address_line2", dto.getAddressLine2(),
                "city_locality", dto.getCity(),
                "state_province", dto.getState(),
                "postal_code", dto.getPostalCode(),
                "country_code", countryCode.name()
//                "address_residential_indicator", "unknown"
        );

        List<Map<String, String>> unvalidatedAddress = List.of(stubAddress);

        List<Map<String, String>> result = shipEngine.validateAddresses(unvalidatedAddress);
        System.out.println("result = " + result);
        return result;
    }

    public List<ShippingRateResponseDto> getRatesWithShipmentDetails(ShippingRequestDto dto,
                                                                     Set<Long> painingIds) {
        List<ShippingRateResponseDto> dtos = new ArrayList<>();
        for (Long id : painingIds) {

            Map<String, Object> shipmentDetails = getShippingDetails(dto, id);
            Map<String, String> result = shipEngine.getRatesWithShipmentDetails(shipmentDetails);

            if (result.isEmpty()) {
                throw new ShippingNotProcessingException(String.format("It is not possible to "
                        + "provide shipping rates for address: %s", formAddress(dto)));
            }

            ShippingRateResponseDto responseDto = parseJsonResponse(result);
            responseDto.setPaintingId(id);
            dtos.add(responseDto);
        }
        return dtos;
    }

    public Map<String, String> createLabelFromShipmentDetails(ShippingRequestDto dto) {
        Map<String, Object> shipmentDetails = getShipmentDetailsForLabels(dto);
        Map<String, String> result = shipEngine.createLabelFromShipmentDetails(shipmentDetails);
        System.out.println("result = " + result);
        return result;
    }

    public Map<String, String> createLabelFromRateId(String rateId) {
        Map<String, Object> labelParams = Map.of(
                "label_layout", "4x6",
                "label_format", "pdf",
                "label_download_type", "url"
        );

        Map<String, String> result = shipEngine.createLabelFromRateId(rateId, labelParams);
        System.out.println("result = " + result);
        return result;
    }

    public Map<String, String> trackUsingLabelId(String labelId) {
        Map<String, String> result = shipEngine.trackUsingLabelId(labelId);
        System.out.println("result = " + result);
        return result;
    }

    public Map<String, String> voidLabel(String labelId) {
        Map<String, String> result = shipEngine.voidLabelWithLabelId(labelId);
        System.out.println("result = " + result);
        return result;
    }

    private Map<String, Object> getShippingDetails(ShippingRequestDto dto, Long id) {
        Painting painting = paintingService.get(id);
        Author author = painting.getAuthor();
        AuthorShippingAddress authorShippingAddress = author.getAuthorShippingAddress();
        return Map.ofEntries(
                Map.entry("shipment", Map.of(
                                "validate_address", "validate_and_clean",
                                "carrier_id", STAMPS_ID,
                                "service_code", "usps_priority_mail_express_international",
//                                "package_type", "package",
                                "customs", Map.of(
                                        "contents", "merchandise",
                                        "non_delivery", "return_to_sender",
                                        "customs_items", List.of(
                                                Map.of(
                                                        "quantity", 1,
                                                        "value", Map.ofEntries(
                                                                Map.entry("currency", "eur"),
                                                                Map.entry("amount", painting.getPrice())
                                                        ),
                                                        "harmonized_tariff_code", "97019100",
                                                        "country_of_origin", CountryCode.findByName(painting.getAuthor().getCountry()).get(0).name(),
                                                        "description", "painting executed entirely by hand"
//                                                "sku", "",
//                                                "sku_description", ""
                                                ))),
//                                "external_order_id", "string",
//                                "item", List.of(),
                                "tax_identifiers", List.of(
                                        Map.of(
                                                "taxable_entity_type", "shipper",
                                                "identifier_type", "eori",
                                                "issuing_authority", "GB",
                                                "value", "GB987654312000"
                                        )
                                ),
//                                "external_shipment_id", "string",
//                                "ship_date", "2018-09-23T00:00:00.000Z",
                                "packages", List.of(
                                        Map.of(
                                                "package_code", "package",
                                                "weight", Map.ofEntries(
                                                        Map.entry("value", painting.getWeight()),
                                                        Map.entry("unit", "gram")
                                                ),
                                                "dimensions", Map.ofEntries(
                                                        Map.entry("width", "10"),
                                                        Map.entry("height", "75"),
                                                        Map.entry("length", "100"),
                                                        Map.entry("unit", "centimeter")
                                                ),
                                                "label_messages", Map.ofEntries(
                                                        Map.entry("reference1", "Thank you for helping Ukraine!"),
                                                        Map.entry("reference2", "Artvswar Team :)")
                                                )
                                        )),
                                "ship_to", Map.ofEntries(
                                        Map.entry("name", dto.getFirstName() + " " + dto.getLastName()),
                                        Map.entry("phone", dto.getPhone()),
//                                        Map.entry("company_name", dto.getCompanyName()),
                                        Map.entry("address_line1", dto.getAddressLine1()),
                                        Map.entry("address_line2", dto.getAddressLine2()),
//                                        Map.entry("address_line3", "Building #7"),
                                        Map.entry("city_locality", dto.getCity()),
                                        Map.entry("state_province", dto.getState()),
                                        Map.entry("postal_code", dto.getPostalCode()),
                                        Map.entry("country_code", CountryCode.findByName(dto.getCountry()).get(0).name()),
                                        Map.entry("address_residential_indicator", "no")
                                ),
                                "ship_from", Map.ofEntries(
                                        Map.entry("name", author.getFullName()),
                                        Map.entry("phone", authorShippingAddress.getPhone()),
//                                        Map.entry("company_name", ""),
                                        Map.entry("address_line1", authorShippingAddress.getAddressLine1()),
//                                        Map.entry("", ""),
//                                        Map.entry("address_line3", "Building #7"),
                                        Map.entry("city_locality", authorShippingAddress.getCity()),
                                        Map.entry("state_province", authorShippingAddress.getState()),
                                        Map.entry("postal_code", authorShippingAddress.getPostalCode()),
                                        Map.entry("country_code", authorShippingAddress.getCountryCode()),
                                        Map.entry("address_residential_indicator", "no")
                                )
                        )
                ));
    }

    private Map<String, Object> getShipmentDetailsForLabels(ShippingRequestDto dto) {
        return Map.ofEntries(
                Map.entry("shipment", Map.of(
                                "validate_address", "validate_and_clean",
                                "carrier_id", STAMPS_ID,
                                "service_code", "usps_priority_mail_express_international",
                                "customs", Map.of(
                                        "contents", "merchandise",
                                        "non_delivery", "return_to_sender",
                                        "customs_items", List.of(
                                                Map.of(
                                                        "quantity", 1,
                                                        "value", Map.ofEntries(
                                                                Map.entry("currency", "eur"),
                                                                Map.entry("amount", 800)
                                                        ),
                                                        "harmonized_tariff_code", "97019100",
                                                        "country_of_origin", "BE",
                                                        "description", "painting executed entirely by hand"
//                                                "sku", "",
//                                                "sku_description", ""
                                                ))),
//                                "external_order_id", "string",
//                                "item", List.of(),
                                "tax_identifiers", List.of(
                                        Map.of(
                                                "taxable_entity_type", "shipper",
                                                "identifier_type", "eori",
                                                "issuing_authority", "GB",
                                                "value", "GB987654312000"
                                        )
                                ),
//                                "external_shipment_id", "string",
//                                "ship_date", "2018-09-23T00:00:00.000Z",
                                "packages", List.of(
                                        Map.of("package_code", "package",
                                                "weight", Map.ofEntries(
                                                        Map.entry("value", "1500"),
                                                        Map.entry("unit", "gram")
                                                ),
                                                "dimensions", Map.ofEntries(
                                                        Map.entry("width", "70"),
                                                        Map.entry("height", "50"),
                                                        Map.entry("length", "10"),
                                                        Map.entry("unit", "centimeter")
                                                ),
                                                "label_messages", Map.ofEntries(
                                                        Map.entry("reference1", "Thank you for helping Ukraine!"),
                                                        Map.entry("reference2", "Artvswar Team :)")
                                                )
                                        )),
                                "ship_to", Map.ofEntries(
                                        Map.entry("name", dto.getFirstName() + " " + dto.getLastName()),
                                        Map.entry("phone", dto.getPhone()),
//                                        Map.entry("company_name", dto.getCompanyName()),
                                        Map.entry("address_line1", dto.getAddressLine1()),
                                        Map.entry("address_line2", dto.getAddressLine2()),
//                                        Map.entry("address_line3", "Building #7"),
                                        Map.entry("city_locality", dto.getCity()),
                                        Map.entry("state_province", dto.getState()),
                                        Map.entry("postal_code", dto.getPostalCode()),
                                        Map.entry("country_code", CountryCode.findByName(dto.getCountry()).get(0).name()),
                                        Map.entry("address_residential_indicator", "no")
                                ),
                                "ship_from", Map.ofEntries(
                                        Map.entry("name", "Roman Novosad"),
                                        Map.entry("phone", "111-111-1111"),
                                        Map.entry("company_name", "Royal Museum of Fine Art"),
                                        Map.entry("address_line1", "Rue de la Regence 3"),
//                                        Map.entry("", ""),
//                                        Map.entry("address_line3", "Building #7"),
                                        Map.entry("city_locality", "Bruxelles"),
                                        Map.entry("state_province", "Bruxelles-Capitale"),
                                        Map.entry("postal_code", "1000"),
                                        Map.entry("country_code", "BE"),
                                        Map.entry("address_residential_indicator", "no")
                                )
                        )
                ));
    }

    private ShippingRateResponseDto parseJsonResponse(Map<String, String> result) {
        try {
            ShippingRateResponseDto responseDto = new ShippingRateResponseDto();
            JSONObject object = new JSONObject(result);
            JSONObject rateResponse = object.getJSONObject("rate_response");
            String status = rateResponse.getString("status");

            if (status.equals("error")) {
//                JSONArray errors = rateResponse.getJSONArray("errors");
//                JSONObject jsonObject = errors.getJSONObject(0);
//                String message = jsonObject.getString("message");
//                throw new ShippingNotProcessingException(message);
              responseDto.setShippingPrice(74L);
              responseDto.setDeliveryMinDays(3L);
              responseDto.setDeliveryMaxDays(5L);
              return  responseDto;
            }

            JSONArray rates = rateResponse.getJSONArray("rates");
            JSONObject jsonObject = rates.getJSONObject(0);
            JSONObject shippingAmount = jsonObject.getJSONObject("shipping_amount");
            String amount = shippingAmount.getString("amount");

            long shippingPrice = Math.round(Double.parseDouble(amount));
            String carrierDeliveryDays = jsonObject.getString("carrier_delivery_days");

            String transformedCarrierDeliveryDays = carrierDeliveryDays.replaceAll(" ", "");
            responseDto.setShippingPrice(shippingPrice);
            responseDto.setDeliveryMinDays(Long.parseLong(transformedCarrierDeliveryDays.split("-")[0]));
            responseDto.setDeliveryMaxDays(Long.parseLong(transformedCarrierDeliveryDays.split("-")[1]));
            return responseDto;
        } catch (JSONException e) {
            throw new RuntimeException(String.format("Can't parse result %s", result), e);
        }
    }

    private String formAddress(ShippingRequestDto dto) {
        StringBuilder builder = new StringBuilder();
        builder.append("addressLine1").append(" - ").append(dto.getAddressLine1()).append(", ")
                .append("addressLine2").append(" - ").append(dto.getAddressLine2()).append(", ")
                .append("city").append(" - ").append(dto.getCity()).append(", ")
                .append("state").append(" - ").append(dto.getState()).append(", ")
                .append("country").append(" - ").append(dto.getCountry()).append(", ")
                .append("postalCode").append(" - ").append(dto.getPostalCode());
        return builder.toString();
    }
}
