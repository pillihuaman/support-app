package pillihuaman.com.pe.support.Service.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pillihuaman.com.pe.support.dto.ReqControl;
import pillihuaman.com.pe.support.dto.RespControl;
import pillihuaman.com.pe.support.repository.control.Control;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-30T07:20:25-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (JetBrains s.r.o.)"
)
public class ControlMapperImpl implements ControlMapper {

    @Override
    public RespControl toRespControl(Control control) {
        if ( control == null ) {
            return null;
        }

        RespControl.RespControlBuilder respControl = RespControl.builder();

        respControl.idCode( control.getIdCode() );
        respControl.description( control.getDescription() );
        respControl.icono( control.getIcono() );
        respControl.iconClass( control.getIconClass() );
        respControl.status( control.getStatus() );
        respControl.styleClass( control.getStyleClass() );
        respControl.id( control.getId() );
        respControl.text( control.getText() );

        return respControl.build();
    }

    @Override
    public List<RespControl> controlsToRespControls(List<Control> controls) {
        if ( controls == null ) {
            return null;
        }

        List<RespControl> list = new ArrayList<RespControl>( controls.size() );
        for ( Control control : controls ) {
            list.add( toRespControl( control ) );
        }

        return list;
    }

    @Override
    public Control toControl(ReqControl reqControl) {
        if ( reqControl == null ) {
            return null;
        }

        Control.ControlBuilder control = Control.builder();

        control.id( reqControl.getId() );
        control.idCode( reqControl.getIdCode() );
        control.description( reqControl.getDescription() );
        control.icono( reqControl.getIcono() );
        control.iconClass( reqControl.getIconClass() );
        control.status( reqControl.getStatus() );
        control.styleClass( reqControl.getStyleClass() );
        control.text( reqControl.getText() );

        return control.build();
    }
}
