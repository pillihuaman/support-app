package pillihuaman.com.pe.support.Service.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.SaveCommonDataReq;
import pillihuaman.com.pe.support.Service.CommonService;
import pillihuaman.com.pe.support.repository.common.CommonDataDocument;
import pillihuaman.com.pe.support.repository.common.dao.CommonDAO;

import java.util.Optional;

@Component
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonDAO commonDAO;
    @Override
    public Optional<CommonDataDocument> findById(String id) {
        return commonDAO.findById(id);
    }
    @Override
    public CommonDataDocument saveOrUpdate(SaveCommonDataReq req) {
        CommonDataDocument docToSave = new CommonDataDocument();
        docToSave.setId(req.getId());
        docToSave.setConfigType(req.getConfigType());
        docToSave.getData().putAll(req.getData()); // Copia todos los datos del mapa

        return commonDAO.save(docToSave);
    }
}

