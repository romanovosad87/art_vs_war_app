package com.example.artvswar.service.shipping;

import com.easypost.exception.EasyPostException;
import com.easypost.model.Address;
import com.easypost.model.CustomsInfo;
import com.easypost.model.CustomsItem;
import com.easypost.model.Rate;
import com.easypost.model.Shipment;
import com.easypost.service.EasyPostClient;
import com.example.artvswar.dto.request.shipping.AddressRequestDto;
import com.example.artvswar.dto.request.shipping.ShippingRequestDto;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.PaintingService;
import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EasyPostService {
    private static final float CM_IN_INCH = 2.54f;
    private static final float GRAM_IN_OUNCES = 28.3495231f;

    private final EasyPostClient easyPostClient;
    private final PaintingService paintingService;

    public Address validateAddress(AddressRequestDto dto) {
        System.out.println("I am in easyPosyValidate");
        List<CountryCode> codes = CountryCode.findByName(dto.getCountry());
        CountryCode countryCode = CountryCode.UNDEFINED;
        if (!codes.isEmpty()) {
            countryCode = codes.get(0);
        }
        Map<String, Object> stubAddress = Map.of(
//                "name", "ShipEngine",
//                "company", "Auctane",
                "phone", dto.getPhone(),
                "street1", dto.getAddressLine1(),
                "street2", dto.getAddressLine2(),
                "city", dto.getCity(),
                "state", dto.getState(),
                "zip", dto.getPostalCode(),
                "country", countryCode.name(),
                "verify", true
//                "address_residential_indicator", "unknown"
        );
        try {
            return easyPostClient.address.createAndVerify(stubAddress);
        } catch (EasyPostException e) {
            throw new RuntimeException("Can't create and verify address", e);
        }
    }

    public List<Rate> calculateRate(ShippingRequestDto dto,
                                    Set<Long> painingIds) {
        List<Shipment> shipment = createShipment(dto, painingIds);
        return getRates(shipment);
    }

    public List<Shipment> createShipment(ShippingRequestDto dto,
                                   Set<Long> painingIds) {
        List<Shipment> shipments = new ArrayList<>();
        for (Long id : painingIds) {
            Painting painting = paintingService.get(id);

            HashMap<String, Object> toAddressMap = getToAddress(dto);

            HashMap<String, Object> fromAddressMap = getFromAddress();

            HashMap<String, Object> parcelMap = getParcel(painting);

            HashMap<String, Object> customsInfoMap = new HashMap<>();
            CustomsInfo customInfo = createCustomInfo(id);
            customsInfoMap.put("id", customInfo.getId());

            HashMap<String, Object> params = new HashMap<>();
            params.put("to_address", toAddressMap);
            params.put("from_address", fromAddressMap);
            params.put("parcel", parcelMap);
            params.put("customs_info", customsInfoMap);
            params.put("carrier_accounts", "ca_fb31ca928b404cc1a94d309152a1142e");
            params.put("service", "ExpressWorldwideNonDoc");

            try {
                Shipment shipment = easyPostClient.shipment.create(params);
                System.out.println(shipment.getId());
                System.out.println(shipment.getParcel().getId());
                System.out.println(shipment.getCarrierAccounts());
                System.out.println(shipment.getService());
                shipments.add(shipment);
            } catch (EasyPostException e) {
                throw new RuntimeException("Can't create shimpment", e);
            }
        }
        return shipments;
    }

    public List<Rate> getRates(List<Shipment> shipments) {
        List<Rate> rates = new ArrayList<>();
        for (Shipment shipment : shipments) {
            String id = null;
            try {
                id = shipment.getId();
                List<Rate> rate = easyPostClient.shipment.newRates(id).getRates();
                rates.addAll(rate);
            } catch (EasyPostException e) {
                throw new RuntimeException(
                        String.format("Can't get rates for shipment id: '%s'", id), e);
            }
        }
        return rates;
    }

    @NotNull
    private HashMap<String, Object> getParcel(Painting painting) {
        HashMap<String, Object> parcelMap = new HashMap<>();
        parcelMap.put("length", painting.getHeight().floatValue() / CM_IN_INCH);
        parcelMap.put("width", painting.getWidth().floatValue() / CM_IN_INCH);
        parcelMap.put("height", painting.getDepth().floatValue() / CM_IN_INCH);
        parcelMap.put("weight", painting.getWeight().floatValue() / GRAM_IN_OUNCES);
        return parcelMap;
    }

    @NotNull
    private HashMap<String, Object> getFromAddress() {
        HashMap<String, Object> fromAddressMap = new HashMap<>();
        fromAddressMap.put("name", "Roman Novosad");
        fromAddressMap.put("street1", "Rue de la Regence 3");
        fromAddressMap.put("street2", "");
        fromAddressMap.put("city", "Bruxelles");
        fromAddressMap.put("state", "Bruxelles-Capitale");
        fromAddressMap.put("zip", "1000");
        fromAddressMap.put("country", "BE");
        fromAddressMap.put("phone", "111-111-1111");
        fromAddressMap.put("email", "hasToChange@easypost.com");
        return fromAddressMap;
    }

    @NotNull
    private HashMap<String, Object> getToAddress(ShippingRequestDto dto) {
        HashMap<String, Object> toAddressMap = new HashMap<>();
        toAddressMap.put("name", dto.getFirstName() + " " + dto.getLastName());
        toAddressMap.put("street1", dto.getAddressLine1());
        toAddressMap.put("street2", dto.getAddressLine2());
        toAddressMap.put("city", dto.getCity());
        toAddressMap.put("state", dto.getState());
        toAddressMap.put("country", CountryCode.findByName(dto.getCountry()).get(0).name());
        toAddressMap.put("phone", dto.getPhone());
        toAddressMap.put("email", "hasToChange@gmail.com");
        toAddressMap.put("zip", dto.getPostalCode());
        return toAddressMap;
    }

    private CustomsInfo createCustomInfo(Long id) {
        try {
            Painting painting = paintingService.get(id);
            HashMap<String, Object> customsItemMap = new HashMap<>();
            customsItemMap.put("description", "painting executed entirely by hand");
            customsItemMap.put("quantity", 1);
            customsItemMap.put("value", painting.getPrice().floatValue());
            customsItemMap.put("weight", painting.getWeight().floatValue());
            customsItemMap.put("origin_country", CountryCode.findByName(painting.getAuthor().getCountry()).get(0).name());
            customsItemMap.put("hs_tariff_number", "97019100");
            customsItemMap.put("currency", "eur");

            CustomsItem customsItem = easyPostClient.customsItem.create(customsItemMap);

            List<CustomsItem> customsItemsList = new ArrayList<>();
            customsItemsList.add(customsItem);

            HashMap<String, Object> params = new HashMap<>();
            params.put("customs_certify", true);
            params.put("customs_signer", "Steve Brule");
            params.put("contents_type", "merchandise");
            params.put("contents_explanation", "");
            params.put("eel_pfc", "NOEEI 30.37(a)");
            params.put("restriction_type", "none");
            params.put("non_delivery_option", "return");
            params.put("customs_items", customsItemsList);
            return easyPostClient.customsInfo.create(params);
        } catch (EasyPostException e) {
            throw new RuntimeException("Can't create customInfo", e);
        }
    }
}
