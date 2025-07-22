package pillihuaman.com.pe.support.Service;



import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;

import java.util.List;

public interface QuotationService {

	RespBase<RespQuotation> createQuotation(
			ReqQuotation reqQuotation,
			MultipartFile logoFile,
			List<MultipartFile> referenceImages,
			MyJsonWebToken jwt,String rawAuthToken
	);

	/**
	 * Actualiza una cotización existente, manejando la adición y eliminación de archivos.
	 */
	RespBase<RespQuotation> updateQuotation(
			String id,
			ReqQuotation reqDto,
			MultipartFile logoFile,
			List<MultipartFile> referenceImages,
			List<String> filesToDelete, // IDs de archivos a eliminar
			MyJsonWebToken jwt,
			String rawAuthToken
	);
	RespBase<RespQuotation> getQuotationById(String id,String token);

	RespBase<List<RespQuotation>> getAllQuotations(String token);

	RespBase<String> deleteQuotation(String id, MyJsonWebToken jwt, String rawAuthToken);
}