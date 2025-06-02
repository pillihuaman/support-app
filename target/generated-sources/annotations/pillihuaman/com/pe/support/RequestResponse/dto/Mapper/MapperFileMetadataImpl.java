package pillihuaman.com.pe.support.RequestResponse.dto.Mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.support.RequestResponse.dto.RespFileMetadata;
import pillihuaman.com.pe.support.RequestResponse.dto.RespSizeStock;
import pillihuaman.com.pe.support.repository.product.FileMetadata;
import pillihuaman.com.pe.support.repository.product.SizeStock;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T05:50:57-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (JetBrains s.r.o.)"
)
@Component
public class MapperFileMetadataImpl implements MapperFileMetadata {

    @Override
    public RespFileMetadata toDto(FileMetadata entity) {
        if ( entity == null ) {
            return null;
        }

        RespFileMetadata.RespFileMetadataBuilder respFileMetadata = RespFileMetadata.builder();

        respFileMetadata.id( entity.getId() );
        respFileMetadata.filename( entity.getFilename() );
        respFileMetadata.contentType( entity.getContentType() );
        respFileMetadata.size( entity.getSize() );
        respFileMetadata.hashCode( entity.getHashCode() );
        respFileMetadata.dimension( entity.getDimension() );
        respFileMetadata.userId( entity.getUserId() );
        respFileMetadata.uploadTimestamp( entity.getUploadTimestamp() );
        respFileMetadata.status( entity.getStatus() );
        respFileMetadata.order( entity.getOrder() );
        respFileMetadata.typeFile( entity.getTypeFile() );
        respFileMetadata.url( entity.getUrl() );
        respFileMetadata.position( entity.getPosition() );
        respFileMetadata.productId( entity.getProductId() );
        respFileMetadata.sizeStock( sizeStockListToRespSizeStockList( entity.getSizeStock() ) );

        return respFileMetadata.build();
    }

    @Override
    public FileMetadata toEntity(RespFileMetadata dto) {
        if ( dto == null ) {
            return null;
        }

        FileMetadata.FileMetadataBuilder fileMetadata = FileMetadata.builder();

        fileMetadata.id( dto.getId() );
        fileMetadata.filename( dto.getFilename() );
        fileMetadata.contentType( dto.getContentType() );
        fileMetadata.size( dto.getSize() );
        fileMetadata.hashCode( dto.getHashCode() );
        fileMetadata.dimension( dto.getDimension() );
        fileMetadata.userId( dto.getUserId() );
        fileMetadata.uploadTimestamp( dto.getUploadTimestamp() );
        fileMetadata.status( dto.getStatus() );
        fileMetadata.order( dto.getOrder() );
        fileMetadata.typeFile( dto.getTypeFile() );
        fileMetadata.url( dto.getUrl() );
        fileMetadata.position( dto.getPosition() );
        fileMetadata.productId( dto.getProductId() );
        fileMetadata.sizeStock( respSizeStockListToSizeStockList( dto.getSizeStock() ) );

        return fileMetadata.build();
    }

    protected RespSizeStock sizeStockToRespSizeStock(SizeStock sizeStock) {
        if ( sizeStock == null ) {
            return null;
        }

        RespSizeStock respSizeStock = new RespSizeStock();

        respSizeStock.setSize( sizeStock.getSize() );
        respSizeStock.setStock( sizeStock.getStock() );

        return respSizeStock;
    }

    protected List<RespSizeStock> sizeStockListToRespSizeStockList(List<SizeStock> list) {
        if ( list == null ) {
            return null;
        }

        List<RespSizeStock> list1 = new ArrayList<RespSizeStock>( list.size() );
        for ( SizeStock sizeStock : list ) {
            list1.add( sizeStockToRespSizeStock( sizeStock ) );
        }

        return list1;
    }

    protected SizeStock respSizeStockToSizeStock(RespSizeStock respSizeStock) {
        if ( respSizeStock == null ) {
            return null;
        }

        SizeStock sizeStock = new SizeStock();

        sizeStock.setSize( respSizeStock.getSize() );
        sizeStock.setStock( respSizeStock.getStock() );

        return sizeStock;
    }

    protected List<SizeStock> respSizeStockListToSizeStockList(List<RespSizeStock> list) {
        if ( list == null ) {
            return null;
        }

        List<SizeStock> list1 = new ArrayList<SizeStock>( list.size() );
        for ( RespSizeStock respSizeStock : list ) {
            list1.add( respSizeStockToSizeStock( respSizeStock ) );
        }

        return list1;
    }
}
