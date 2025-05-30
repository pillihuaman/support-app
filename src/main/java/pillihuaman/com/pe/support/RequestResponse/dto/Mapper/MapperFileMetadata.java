package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import org.mapstruct.Mapper;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.repository.product.FileMetadata;

@Mapper(componentModel = "spring")
public interface MapperFileMetadata {
    RespFileMetadata toDto(FileMetadata entity);
    FileMetadata toEntity(RespFileMetadata dto);
}
