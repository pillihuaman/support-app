package pillihuaman.com.pe.support.Service;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.request.CorouselImage;
import pillihuaman.com.pe.lib.request.ReqImagenByProduct;
import pillihuaman.com.pe.lib.response.RespBase;
import pillihuaman.com.pe.lib.response.RespImagenGeneral;

import java.util.List;

public interface ImagenService {
	RespBase<List<RespImagenGeneral>> getTopImagen(int page , int perage);
	RespBase<List<RespImagenGeneral>> getImagenHome(int page, int perage);
	Boolean saveClickCountImagen(CorouselImage imFile);
	Boolean saveImagenByProduct(MyJsonWebToken jwt, ReqImagenByProduct imFile) throws Exception;
}
