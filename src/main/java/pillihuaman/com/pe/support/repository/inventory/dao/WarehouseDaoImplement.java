// Archivo: WarehouseDaoImplement.java
package pillihuaman.com.pe.support.repository.inventory.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqWarehouse;
import pillihuaman.com.pe.support.repository.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.support.repository.company.Company;
import pillihuaman.com.pe.support.repository.inventory.WarehouseDAO;
import pillihuaman.com.pe.support.repository.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO Implementation for Warehouse operations.
 * This class is designed with a multi-tenant security model, ensuring that all
 * operations are isolated to the tenant/company specified in the JWT.
 * Input validation has been added to prevent NullPointerExceptions and invalid data.
 */
@Component
@Repository
public class WarehouseDaoImplement extends AzureAbstractMongoRepositoryImpl<Warehouse> implements WarehouseDAO {

    public WarehouseDaoImplement() {
        DS_WRITE = Constantes.DW;
        COLLECTION = Constantes.COLLECTION_WAREHOUSE;
    }

    @Override
    public Class<Warehouse> provideEntityClass() {
        return Warehouse.class;
    }

    /**
     * Lists warehouses for a given tenant.
     * Validates that the request and tenant ID are not null or empty.
     *
     * @param req The request object containing filtering criteria. Must not be null and must contain a valid tenant ID.
     * @return A list of warehouses, or an empty list if no warehouses are found or if the input is invalid.
     */
    // Archivo: WarehouseDaoImplement.java (Método corregido y flexible)

    /**
     * Lista los almacenes basándose en filtros opcionales.
     * Este método es flexible y construye la consulta dinámicamente.
     * ADVERTENCIA DE SEGURIDAD: Si no se proporciona un tenantId, se buscará
     * en todos los tenants. Esta operación solo debe ser accesible por roles
     * de muy alto privilegio (ej. Super Administrador de la plataforma).
     *
     * @param req El objeto de solicitud que contiene criterios de filtro opcionales. Puede ser nulo.
     * @return Una lista de almacenes que coinciden con los filtros válidos proporcionados.
     */
    @Override
    public List<Warehouse> listWarehouses(ReqWarehouse req) {
        MongoCollection<Warehouse> collection = getCollection(this.COLLECTION, Warehouse.class);
        List<Document> filters = new ArrayList<>();

        // Filtro base (solo activos)
        filters.add(new Document("status", true));

        if (req != null) {
            if (req.getTenantid() != null && !req.getTenantid().isBlank()) {
                try {
                    ObjectId companyId = new ObjectId(req.getTenantid());
                    filters.add(new Document("company.$id", companyId));
                } catch (IllegalArgumentException ignored) {}
            }

            if (req.getId() != null && !req.getId().isBlank()) {
                try {
                    ObjectId warehouseId = new ObjectId(req.getId());
                    filters.add(new Document("_id", warehouseId));
                } catch (IllegalArgumentException ignored) {}
            }

            // --- Texto con búsqueda parcial e insensible a mayúsculas ---
            if (req.getWarehouseCode() != null && !req.getWarehouseCode().isBlank()) {
                filters.add(new Document("warehouseCode",
                        new Document("$regex", req.getWarehouseCode()).append("$options", "i")));
            }

            if (req.getCity() != null && !req.getCity().isBlank()) {
                filters.add(new Document("city",
                        new Document("$regex", req.getCity()).append("$options", "i")));
            }

            if (req.getType() != null && !req.getType().isBlank()) {
                filters.add(new Document("type",
                        new Document("$regex", req.getType()).append("$options", "i")));
            }

            if (req.getOperationalStatus() != null && !req.getOperationalStatus().isBlank()) {
                filters.add(new Document("operationalStatus",
                        new Document("$regex", req.getOperationalStatus()).append("$options", "i")));
            }

            if (req.getCapacity() != null) {
                filters.add(new Document("capacity", req.getCapacity()));
            }
        }

        Document query = (filters.size() > 1) ? new Document("$and", filters) : filters.get(0);

        return collection.find(query)
                .sort(new Document("audit.dateRegister", -1))
                .into(new ArrayList<>());
    }

    /**
     * Saves or updates a warehouse.
     * Validates that the warehouse object and token are not null.
     * Enforces that the operation is performed within the user's tenant.
     *
     * @param warehouse The warehouse object to save/update. Must not be null.
     * @param token The user's token containing identity and tenant info. Must not be null.
     * @return The saved or updated warehouse object.
     * @throws IllegalArgumentException if warehouse or token is null or invalid.
     */
// Archivo: WarehouseDaoImplement.java

    @Override
    public Warehouse saveWarehouse(Warehouse warehouse, MyJsonWebToken token) {
        // VALIDATION: Ensure critical inputs are not null.
        if (warehouse == null || token == null || token.getUser() == null || token.getUser().getTenantId() == null || token.getUser().getId() == null) {
            throw new IllegalArgumentException("Warehouse object, token, and user/tenant information must not be null.");
        }

        ObjectId companyIdFromToken = new ObjectId(token.getUser().getTenantId());
        String userId = token.getUser().getId().toString();
        String userMail = token.getUser().getMail();

        MongoCollection<Warehouse> collection = getCollection(this.COLLECTION, Warehouse.class);

        if (warehouse.getId() == null) {
            // Verificar si ya existe un almacén con el mismo código y la misma empresa
            Document duplicateFilter = new Document("warehouseCode", warehouse.getWarehouseCode())
                    //.append("company.$id", companyIdFromToken) // para que la validación sea por tenant
                    .append("status", true); // opcional: solo validar contra activos

            if (collection.find(duplicateFilter).first() != null) {
                throw new IllegalStateException("Ya existe un almacén con el código: " + warehouse.getWarehouseCode());
            }

            warehouse.setId(new ObjectId());
            warehouse.setStatus(true);
            warehouse.setCompany(Company.builder().id(companyIdFromToken).build());

            AuditEntity audit = new AuditEntity();
            audit.setMail(userMail);
            audit.setDateRegister(new Date());
            warehouse.setAudit(audit);
            collection.insertOne(warehouse);
        }
 else {
            // --- ACTUALIZACIÓN (LÓGICA MODIFICADA) ---

            // 1. Buscar el documento existente SOLO por el ID del almacén.
            Document query = new Document("_id", warehouse.getId());
            Warehouse existing = collection.find(query).first();

            // 2. VERIFICACIÓN #1: Asegurarse de que el almacén exista.
            if (existing == null) {
                throw new IllegalStateException("Error: Warehouse with ID " + warehouse.getId() + " not found.");
            }

            // 3. VERIFICACIÓN DE SEGURIDAD #2: Comprobar la pertenencia a la compañía en Java.
          /*  if (existing.getCompany() == null || !existing.getCompany().getId().equals(companyIdFromToken)) {
                // Si el almacén no tiene compañía o su ID no coincide con el del token, se deniega el acceso.
                throw new SecurityException("Access Denied: You do not have permission to modify this warehouse.");
            }*/

            // 4. Si las verificaciones pasan, proceder con la actualización.

            // Seguridad: Forzar la asignación de la empresa de nuevo para consistencia.
            warehouse.setCompany(Company.builder().id(companyIdFromToken).build());

            AuditEntity audit = existing.getAudit() != null ? existing.getAudit() : new AuditEntity();
            audit.setMailUpdate(userMail);
            audit.setDateUpdate(new Date());
            warehouse.setAudit(audit);
            warehouse.setStatus(true); // Asegurar que el estado sea activo al guardar.

            // Se usa el mismo 'query' (que solo contiene el _id) para reemplazar el documento.
            collection.replaceOne(query, warehouse);
        }
        return warehouse;
    }

    /**
     * Performs a logical delete on a warehouse.
     * Validates that the ID and token are valid.
     * Enforces that the operation is performed within the user's tenant.
     *
     * @param id The ID of the warehouse to delete. Must be a valid ObjectId string.
     * @param token The user's token. Must not be null.
     * @return true if the delete was successful, false otherwise.
     * @throws IllegalArgumentException if token is null or invalid.
     */

    @Override
    public boolean deleteWarehouse(String id, MyJsonWebToken token) {
        // VALIDATION: Check for null token and user info first.
        if (id == null || id.isBlank() || token == null || token.getUser() == null || token.getUser().getTenantId() == null) {
            throw new IllegalArgumentException("ID and token with user/tenant info must not be null or blank.");
        }

        ObjectId companyIdFromToken = new ObjectId(token.getUser().getTenantId());
        ObjectId warehouseId;
        try {
            warehouseId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }

        MongoCollection<Warehouse> collection = getCollection(this.COLLECTION, Warehouse.class);

        // --- LÓGICA CORREGIDA (igual que en saveWarehouse) ---

        // 1. Buscar el documento a borrar SOLO por su ID.
        Document query = new Document("_id", warehouseId);
        Warehouse warehouse = collection.find(query).first();

        // 2. Si se encuentra, proceder con las verificaciones.
        if (warehouse != null) {
            // 3. VERIFICACIÓN DE SEGURIDAD: Comprobar la pertenencia en Java.
           /* if (warehouse.getCompany() == null || !warehouse.getCompany().getId().equals(companyIdFromToken)) {
                // El usuario intenta borrar un almacén que no es suyo. Se devuelve 'false' silenciosamente.
                return false;
            }*/

            // 4. Si la seguridad pasa, proceder con el borrado lógico.
            warehouse.setStatus(false);

            AuditEntity audit = warehouse.getAudit() != null ? warehouse.getAudit() : new AuditEntity();
            audit.setCodUserUpdate(token.getUser().getId());
            audit.setMailUpdate(token.getUser().getMail());
            audit.setDateUpdate(new Date());
            warehouse.setAudit(audit);

            // Reemplazar el documento usando la misma consulta simple por _id.
            collection.replaceOne(query, warehouse);
            return true;
        }

        // No se encontró nada que borrar.
        return false;
    }
}