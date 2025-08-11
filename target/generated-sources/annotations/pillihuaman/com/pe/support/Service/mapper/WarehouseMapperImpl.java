package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespWarehouse;
import pillihuaman.com.pe.support.repository.common.Address;
import pillihuaman.com.pe.support.repository.company.Company;
import pillihuaman.com.pe.support.repository.employee.Employee;
import pillihuaman.com.pe.support.repository.warehouse.ContactInfo;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-11T14:44:24-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class WarehouseMapperImpl implements WarehouseMapper {

    @Override
    public RespWarehouse toRespWarehouse(Warehouse entity) {
        if ( entity == null ) {
            return null;
        }

        RespWarehouse.RespWarehouseBuilder respWarehouse = RespWarehouse.builder();

        respWarehouse.id( objectIdToString( entity.getId() ) );
        respWarehouse.companyId( objectIdToString( entityCompanyId( entity ) ) );
        respWarehouse.companyName( entityCompanyLegalName( entity ) );
        respWarehouse.addressId( objectIdToString( entityAddressId( entity ) ) );
        respWarehouse.street( entityAddressStreet( entity ) );
        respWarehouse.addressLine2( entityAddressAddressLine2( entity ) );
        respWarehouse.city( entityAddressCity( entity ) );
        respWarehouse.state( entityAddressState( entity ) );
        respWarehouse.postalCode( entityAddressPostalCode( entity ) );
        respWarehouse.country( entityAddressCountry( entity ) );
        List<Double> coordinates = entityAddressCoordinates( entity );
        List<Double> list = coordinates;
        if ( list != null ) {
            respWarehouse.coordinates( new ArrayList<Double>( list ) );
        }
        respWarehouse.addressDescription( entityAddressDescription( entity ) );
        respWarehouse.mainPhoneNumber( entityContactInfoMainPhoneNumber( entity ) );
        respWarehouse.mainEmail( entityContactInfoMainEmail( entity ) );
        respWarehouse.managerId( objectIdToString( entityManagerId( entity ) ) );
        respWarehouse.warehouseCode( entity.getWarehouseCode() );
        respWarehouse.type( entity.getType() );
        respWarehouse.operationalStatus( entity.getOperationalStatus() );
        respWarehouse.capacity( entity.getCapacity() );
        respWarehouse.capacityUnit( entity.getCapacityUnit() );
        respWarehouse.dockDoors( entity.getDockDoors() );

        return respWarehouse.build();
    }

    @Override
    public List<RespWarehouse> toRespWarehouseList(List<Warehouse> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RespWarehouse> list = new ArrayList<RespWarehouse>( entities.size() );
        for ( Warehouse warehouse : entities ) {
            list.add( toRespWarehouse( warehouse ) );
        }

        return list;
    }

    @Override
    public Warehouse toWarehouseEntity(ReqWarehouse req) {
        if ( req == null ) {
            return null;
        }

        Warehouse.WarehouseBuilder warehouse = Warehouse.builder();

        warehouse.id( stringToObjectId( req.getId() ) );
        warehouse.warehouseCode( req.getWarehouseCode() );
        warehouse.type( req.getType() );
        warehouse.operationalStatus( req.getOperationalStatus() );
        warehouse.capacity( req.getCapacity() );
        warehouse.capacityUnit( req.getCapacityUnit() );
        warehouse.dockDoors( req.getDockDoors() );

        return warehouse.build();
    }

    private ObjectId entityCompanyId(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Company company = warehouse.getCompany();
        if ( company == null ) {
            return null;
        }
        ObjectId id = company.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCompanyLegalName(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Company company = warehouse.getCompany();
        if ( company == null ) {
            return null;
        }
        String legalName = company.getLegalName();
        if ( legalName == null ) {
            return null;
        }
        return legalName;
    }

    private ObjectId entityAddressId(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        ObjectId id = address.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityAddressStreet(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String street = address.getStreet();
        if ( street == null ) {
            return null;
        }
        return street;
    }

    private String entityAddressAddressLine2(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String addressLine2 = address.getAddressLine2();
        if ( addressLine2 == null ) {
            return null;
        }
        return addressLine2;
    }

    private String entityAddressCity(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String city = address.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private String entityAddressState(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String state = address.getState();
        if ( state == null ) {
            return null;
        }
        return state;
    }

    private String entityAddressPostalCode(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String postalCode = address.getPostalCode();
        if ( postalCode == null ) {
            return null;
        }
        return postalCode;
    }

    private String entityAddressCountry(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String country = address.getCountry();
        if ( country == null ) {
            return null;
        }
        return country;
    }

    private List<Double> entityAddressCoordinates(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        List<Double> coordinates = address.getCoordinates();
        if ( coordinates == null ) {
            return null;
        }
        return coordinates;
    }

    private String entityAddressDescription(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Address address = warehouse.getAddress();
        if ( address == null ) {
            return null;
        }
        String description = address.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private String entityContactInfoMainPhoneNumber(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        ContactInfo contactInfo = warehouse.getContactInfo();
        if ( contactInfo == null ) {
            return null;
        }
        String mainPhoneNumber = contactInfo.getMainPhoneNumber();
        if ( mainPhoneNumber == null ) {
            return null;
        }
        return mainPhoneNumber;
    }

    private String entityContactInfoMainEmail(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        ContactInfo contactInfo = warehouse.getContactInfo();
        if ( contactInfo == null ) {
            return null;
        }
        String mainEmail = contactInfo.getMainEmail();
        if ( mainEmail == null ) {
            return null;
        }
        return mainEmail;
    }

    private ObjectId entityManagerId(Warehouse warehouse) {
        if ( warehouse == null ) {
            return null;
        }
        Employee manager = warehouse.getManager();
        if ( manager == null ) {
            return null;
        }
        ObjectId id = manager.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
