package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqQuotation;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.RespQuotation;
import pillihuaman.com.pe.support.repository.bussiness.CustomerInfo;
import pillihuaman.com.pe.support.repository.bussiness.DesignDetails;
import pillihuaman.com.pe.support.repository.bussiness.Quotation;
import pillihuaman.com.pe.support.repository.bussiness.QuotationItem;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-20T23:12:40-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class QuotationMapperImpl implements QuotationMapper {

    @Override
    public Quotation toEntity(ReqQuotation req) {
        if ( req == null ) {
            return null;
        }

        Quotation.QuotationBuilder quotation = Quotation.builder();

        quotation.customerInfo( reqQuotationToCustomerInfo( req ) );
        quotation.designDetails( reqQuotationToDesignDetails( req ) );
        quotation.items( itemListToQuotationItemList( req.getItems() ) );
        quotation.aceptaTerminos( req.isAceptaTerminos() );

        return quotation.build();
    }

    @Override
    public void updateEntityFromDto(ReqQuotation dto, Quotation entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getCustomerInfo() == null ) {
            entity.setCustomerInfo( CustomerInfo.builder().build() );
        }
        reqQuotationToCustomerInfo1( dto, entity.getCustomerInfo() );
        if ( entity.getDesignDetails() == null ) {
            entity.setDesignDetails( DesignDetails.builder().build() );
        }
        reqQuotationToDesignDetails1( dto, entity.getDesignDetails() );
        if ( entity.getItems() != null ) {
            List<QuotationItem> list = itemListToQuotationItemList( dto.getItems() );
            if ( list != null ) {
                entity.getItems().clear();
                entity.getItems().addAll( list );
            }
            else {
                entity.setItems( null );
            }
        }
        else {
            List<QuotationItem> list = itemListToQuotationItemList( dto.getItems() );
            if ( list != null ) {
                entity.setItems( list );
            }
        }
        entity.setAceptaTerminos( dto.isAceptaTerminos() );
    }

    @Override
    public QuotationItem toQuotationItem(ReqQuotation.Item item) {
        if ( item == null ) {
            return null;
        }

        QuotationItem.QuotationItemBuilder quotationItem = QuotationItem.builder();

        quotationItem.playerName( item.getNombre() );
        quotationItem.shirtNumber( item.getNumeroCamisa() );
        quotationItem.size( item.getTalla() );
        quotationItem.quantity( item.getCantidad() );
        quotationItem.isFullSet( item.isEsConjuntoCompleto() );

        return quotationItem.build();
    }

    @Override
    public RespQuotation toDto(Quotation entity) {
        if ( entity == null ) {
            return null;
        }

        RespQuotation respQuotation = new RespQuotation();

        respQuotation.setId( objectIdToString( entity.getId() ) );
        respQuotation.setCreatedAt( dateToString( entityAuditDateRegister( entity ) ) );
        respQuotation.setUpdatedAt( dateToString( entityAuditDateUpdate( entity ) ) );
        respQuotation.setAceptaTerminos( entity.isAceptaTerminos() );
        respQuotation.setCustomerInfo( entity.getCustomerInfo() );
        List<QuotationItem> list = entity.getItems();
        if ( list != null ) {
            respQuotation.setItems( new ArrayList<QuotationItem>( list ) );
        }
        respQuotation.setDesignDetails( entity.getDesignDetails() );
        respQuotation.setTotals( entity.getTotals() );
        respQuotation.setStatus( entity.getStatus() );

        return respQuotation;
    }

    protected CustomerInfo reqQuotationToCustomerInfo(ReqQuotation reqQuotation) {
        if ( reqQuotation == null ) {
            return null;
        }

        CustomerInfo.CustomerInfoBuilder customerInfo = CustomerInfo.builder();

        customerInfo.contactName( reqQuotation.getClienteNombre() );
        customerInfo.contactEmail( reqQuotation.getClienteEmail() );
        customerInfo.contactPhone( reqQuotation.getClienteTelefono() );

        return customerInfo.build();
    }

    protected DesignDetails reqQuotationToDesignDetails(ReqQuotation reqQuotation) {
        if ( reqQuotation == null ) {
            return null;
        }

        DesignDetails.DesignDetailsBuilder designDetails = DesignDetails.builder();

        designDetails.detailedDescription( reqQuotation.getDescripcionDetallada() );

        return designDetails.build();
    }

    protected List<QuotationItem> itemListToQuotationItemList(List<ReqQuotation.Item> list) {
        if ( list == null ) {
            return null;
        }

        List<QuotationItem> list1 = new ArrayList<QuotationItem>( list.size() );
        for ( ReqQuotation.Item item : list ) {
            list1.add( toQuotationItem( item ) );
        }

        return list1;
    }

    protected void reqQuotationToCustomerInfo1(ReqQuotation reqQuotation, CustomerInfo mappingTarget) {
        if ( reqQuotation == null ) {
            return;
        }

        mappingTarget.setContactName( reqQuotation.getClienteNombre() );
        mappingTarget.setContactEmail( reqQuotation.getClienteEmail() );
        mappingTarget.setContactPhone( reqQuotation.getClienteTelefono() );
    }

    protected void reqQuotationToDesignDetails1(ReqQuotation reqQuotation, DesignDetails mappingTarget) {
        if ( reqQuotation == null ) {
            return;
        }

        mappingTarget.setDetailedDescription( reqQuotation.getDescripcionDetallada() );
    }

    private Date entityAuditDateRegister(Quotation quotation) {
        if ( quotation == null ) {
            return null;
        }
        AuditEntity audit = quotation.getAudit();
        if ( audit == null ) {
            return null;
        }
        Date dateRegister = audit.getDateRegister();
        if ( dateRegister == null ) {
            return null;
        }
        return dateRegister;
    }

    private Date entityAuditDateUpdate(Quotation quotation) {
        if ( quotation == null ) {
            return null;
        }
        AuditEntity audit = quotation.getAudit();
        if ( audit == null ) {
            return null;
        }
        Date dateUpdate = audit.getDateUpdate();
        if ( dateUpdate == null ) {
            return null;
        }
        return dateUpdate;
    }
}
