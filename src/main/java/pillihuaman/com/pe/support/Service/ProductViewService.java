package pillihuaman.com.pe.support.Service;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProductView;
import pillihuaman.com.pe.support.RequestResponse.dto.RespImagenProductRank;
import pillihuaman.com.pe.support.RequestResponse.dto.RespProductView;

import java.util.List;

public interface ProductViewService {

    RespBase<RespProductView> saveView(MyJsonWebToken jwt, ReqBase<ReqProductView> request);

    RespBase<List<RespImagenProductRank>> getViews(MyJsonWebToken jwt);

    RespBase<List<RespProductView>> getViewsByUserId(MyJsonWebToken jwt, String userId);

    RespBase<List<RespProductView>> getTopViewedProducts(MyJsonWebToken jwt, int limit);
    RespBase<RespImagenProductRank> getViewsByIdProduct(String idProduct);
}

