package pillihuaman.com.pe.support.foreing;

import org.springframework.web.multipart.MultipartFile;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;


import java.util.List;
import java.util.Map;

public interface NeuroIaFileStorageService {

    /**
     * Calls the external neuroIA microservice to upload files.
     *
     * @param files        Array of files to upload.
     * @param metadataJson JSON string with metadata for each file.
     * @param productId    The ID of the product associated with the files.
     * @param authToken    The authorization token (Bearer) to be forwarded.
     * @return A response containing the list of file metadata from the neuroIA service.
     */
    RespBase<List<RespFileMetadata>> uploadFilesToNeuroIA(
            MultipartFile[] files,
            String metadataJson,
            String productId,
            String authToken);
    /**
     * Llama al servicio neuroIA para eliminar un archivo por su ID.
     *
     * @param fileId El ID del metadato del archivo a eliminar.
     * @param authToken El token de autorización para la llamada.
     */
    void deleteFileFromNeuroIA(String fileId, String authToken);
    /**
     * =========================================================================================
     * NUEVO MÉTODO PARA OBTENER UNA URL PRE-FIRMADA
     * =========================================================================================
     * Llama al endpoint /generate-presigned-url del servicio neuroIA.
     *
     * @param s3Key La clave del objeto en S3.
     * @param typeFile El tipo de archivo para que neuroIA determine el bucket.
     * @param authToken El token de autorización para la llamada.
     * @return Un objeto RespBase que en su payload contiene un Map con la URL.
     */
    RespBase<Map<String, Object>> getPresignedUrl(String s3Key, String typeFile, String authToken);
}