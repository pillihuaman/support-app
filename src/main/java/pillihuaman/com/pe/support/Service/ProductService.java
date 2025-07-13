package pillihuaman.com.pe.support.Service;


import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.ReqBase;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.RespProduct;
import pillihuaman.com.pe.support.RequestResponse.dto.ReqProduct;

import java.util.List;

public interface ProductService {

	// Método para guardar o actualizar un empleado
	RespBase<RespProduct> saveProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request);

	// Método para obtener un empleado específico
	RespBase<List<RespProduct>> getProduct(MyJsonWebToken jwt, ReqBase<ReqProduct> request);

	// Método para listar empleados asociados a un usuario
	RespBase<RespProduct> listProductsByUser(MyJsonWebToken jwt, ReqBase<ReqProduct> request);
	RespBase<Boolean> deleteProduct(MyJsonWebToken jwt,  String id);
	/**
	 * Busca productos por palabras clave y envuelve el resultado en un RespBase.
	 * @param keywordsString Una cadena de texto con una o más palabras clave.
	 * @param limit El número máximo de productos a devolver.
	 * @return Un objeto RespBase que contiene la lista de productos encontrados.
	 */
	RespBase<List<RespProduct>> searchProductsByKeywords(String keywordsString, int limit);

}
