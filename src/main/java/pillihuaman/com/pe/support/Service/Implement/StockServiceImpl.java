package pillihuaman.com.pe.support.Service.Implement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.basebd.product.Product;
import pillihuaman.com.pe.basebd.product.dao.ProductSupportDAO;
import pillihuaman.com.pe.basebd.product.dao.StockSupportDAO;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.ReqBase;
import pillihuaman.com.pe.lib.request.ReqProduct;
import pillihuaman.com.pe.lib.request.ReqStock;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespProduct;
import pillihuaman.com.pe.lib.response.ResponseStock;
import pillihuaman.com.pe.support.Service.StockService;
import pillihuaman.com.pe.basebd.common.ProductStock;

import java.util.List;

@Component
public class StockServiceImpl implements StockService {
	@Autowired
	private ProductSupportDAO productSupportDAO;
	@Autowired
	private StockSupportDAO stockSupportDAO;

	protected final Log log = LogFactory.getLog(getClass());

	@Override
	public RespBase<RespProduct> SaveProduct(MyJsonWebToken token, ReqBase<ReqProduct> request) {
		RespBase<RespProduct> response = new RespBase<RespProduct>();
		try {
			request.getData();
			Product tblproduct = new Product();
		//	tblproduct = ConvertClass.ProductDtoToProductTbl(request.getData());
			productSupportDAO.SaveProduct(tblproduct);
			response.getStatus().setSuccess(Boolean.TRUE);
			response.setPayload(new RespProduct());
		} catch (Exception e) {

			response.getStatus().setSuccess(Boolean.FALSE);
			throw e;
		}

		return response;

	}

	@Override
	public RespBase<ResponseStock> saveStock(MyJsonWebToken token, ReqBase<ReqStock> request) {
		RespBase<ResponseStock> response = new RespBase<ResponseStock>();
		try {
			request.getData();
			ProductStock tblproductStock = new ProductStock();
			//tblproductStock = ConvertClass.ProductStockRequestDtoToProductStock(request.getData());
			stockSupportDAO.saveStock(tblproductStock);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;

	}

	@Override
	public RespBase<List<RespProduct>> listProductbyUser(MyJsonWebToken token, ReqBase<ReqProduct> request) {
		Product pro= new Product();
		//pro=ConvertClass.ProductDtoToProductTbl(request.getData());
		RespBase<List<RespProduct>> res = new RespBase<>();
		//res.setPayload(ConvertClass.listProductoRespProduct(productSupportDAO.getallProductbyUser(pro)));
		return res;
	}


}
