package pillihuaman.com.pe.support.repository.system.dao;

import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqSystemEntities;
import pillihuaman.com.pe.support.RequestResponse.dto.RespMenuTree;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSystemEntities;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.system.System;

import java.util.List;

public interface SystemDAO extends BaseMongoRepository<System> {

    /**
     * Lista todos los sistemas activos ordenados por su campo 'order'.
     *
     * @return Lista de objetos System activos.
     */
    List<RespSystemEntities> listSystems(ReqSystemEntities red);

    /**
     * Guarda o actualiza un sistema.
     *
     * @param system Objeto System a guardar o actualizar.
     * @param token  Token JWT con datos de auditoría del usuario.
     * @return Objeto System guardado o actualizado.
     */
    RespSystemEntities saveSystem(ReqSystemEntities system, MyJsonWebToken token);

    /**
     * Elimina lógicamente un sistema (status=false).
     *
     * @param id    ID del sistema a eliminar.
     * @param token Token JWT con datos del usuario.
     * @return true si se eliminó exitosamente, false en caso contrario.
     */
    boolean deleteSystemById(String id, MyJsonWebToken token);


    List<RespSystemEntities> searchSystemEntitiesLineal(ReqSystemEntities req);

    public List<RespMenuTree> listSystemRespMenuTree();
}
