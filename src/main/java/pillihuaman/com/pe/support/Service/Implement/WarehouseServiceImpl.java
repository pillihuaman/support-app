package pillihuaman.com.pe.support.Service.Implement;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespWarehouse;
import pillihuaman.com.pe.support.Service.WarehouseService;
import pillihuaman.com.pe.support.Service.mapper.WarehouseMapper;
import pillihuaman.com.pe.support.repository.common.Address;
import pillihuaman.com.pe.support.repository.employee.Employee;
import pillihuaman.com.pe.support.repository.inventory.WarehouseDAO;
import pillihuaman.com.pe.support.repository.warehouse.ContactInfo;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

import java.util.List;

@Component
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseDAO warehouseDAO;

    private final WarehouseMapper mapper = WarehouseMapper.INSTANCE;

    @Override
    public List<RespWarehouse> listWarehouses(ReqWarehouse req) {
        List<Warehouse> warehouses = warehouseDAO.listWarehouses(req);

        return mapper.toRespWarehouseList(warehouses);
    }

    @Override
    public RespWarehouse saveWarehouse(ReqWarehouse req, MyJsonWebToken token) {
        // 1. Mapeo inicial de los campos simples
        Warehouse warehouse = mapper.toWarehouseEntity(req);

        // 2. LÓGICA DE ENSAMBLAJE
        assembleComplexObjects(warehouse, req);

        // 3. Guardar la entidad completamente ensamblada
        Warehouse saved = warehouseDAO.saveWarehouse(warehouse, token);

        // 4. Mapear la entidad guardada a la respuesta
        return mapper.toRespWarehouse(saved);
    }
    private void assembleComplexObjects(Warehouse warehouse, ReqWarehouse req) {
        // Ensamblar el objeto Address
        if (req.getStreet() != null || req.getCity() != null || req.getCountry() != null) {
            Address address = Address.builder()
                    .street(req.getStreet())
                    .addressLine2(req.getAddressLine2())
                    .city(req.getCity())
                    .state(req.getState())
                    .postalCode(req.getPostalCode())
                    .country(req.getCountry())
                    .description(req.getAddressDescription())
                    .build();
            // NOTA: Si la dirección debe ser reutilizable, aquí iría una lógica
            // para buscarla por sus campos o guardarla en su propia colección.
            // Por ahora, la creamos y la asignamos.
            warehouse.setAddress(address);
        }

        // Ensamblar el objeto ContactInfo
        if (req.getMainPhoneNumber() != null || req.getMainEmail() != null) {
            ContactInfo contactInfo = ContactInfo.builder()
                    .mainPhoneNumber(req.getMainPhoneNumber())
                    .mainEmail(req.getMainEmail())
                    .build();
            warehouse.setContactInfo(contactInfo);
        }

        // Ensamblar la referencia al Manager (Employee)
        if (req.getManagerId() != null && !req.getManagerId().isBlank()) {
            try {
                Employee manager = new Employee();
                manager.setId(new ObjectId(req.getManagerId()));
                warehouse.setManager(manager);
            } catch (IllegalArgumentException e) {
                // El managerId no es un ObjectId válido, se ignora.
            }
        }
    }
    @Override
    public boolean deleteWarehouse(String id, MyJsonWebToken token) {
        return warehouseDAO.deleteWarehouse(id, token);
    }
}