package pillihuaman.com.pe.support.repository.system.dao;


import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqEmployee;
import pillihuaman.com.pe.support.dto.ReqPage;
import pillihuaman.com.pe.support.dto.RespPage;
import pillihuaman.com.pe.support.repository.BaseMongoRepository;
import pillihuaman.com.pe.support.repository.employee.Employee;
import pillihuaman.com.pe.support.repository.system.Page;

import java.util.List;

public interface PageDAO extends BaseMongoRepository<Page> {

    List<RespPage> listPages(ReqPage page); // Reusamos ReqEmployee para filtros genéricos

    RespPage savePage(ReqPage page, MyJsonWebToken token);

    List<RespPage> findBySystemId(String systemId);

    boolean deletePageById(String id, MyJsonWebToken token);
}