package pillihuaman.com.pe.support.Service;



import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqProduct;
import pillihuaman.com.pe.lib.request.ReqStock;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespProduct;
import pillihuaman.com.pe.lib.response.ResponseStock;

import java.util.List;

public interface StockService {
	RespBase<RespProduct> SaveProduct(MyJsonWebToken token, ReqBase<ReqProduct> request);
	RespBase<List<RespProduct>> listProductbyUser(MyJsonWebToken token, ReqBase<ReqProduct> request);

	RespBase<ResponseStock> saveStock(MyJsonWebToken token, ReqBase<ReqStock> request) ;
}



