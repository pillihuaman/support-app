package pillihuaman.com.pe.support.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.QueryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.basebd.product.Color;
import pillihuaman.com.pe.basebd.product.Size;
import pillihuaman.com.pe.basebd.product.Stock;
import pillihuaman.com.pe.lib.commons.Parameters;
import pillihuaman.com.pe.lib.request.CorouselImage;
import pillihuaman.com.pe.lib.request.ImagenDetail;
import pillihuaman.com.pe.lib.request.ReqImagenByProduct;
import pillihuaman.com.pe.lib.response.RespImagenGeneral;
import pillihuaman.com.pe.support.Help.Constantes;
import pillihuaman.com.pe.support.Service.ImagenService;
import pillihuaman.com.pe.basebd.common.ProductStock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pillihuaman.com.pe.basebd.common.MyJsonWebToken;
import jakarta.validation.Valid;
import pillihuaman.com.pe.lib.response.RespBase;

@RestController
//@RequestMapping("Product/")

public class ImagenController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ImagenService imagenService;
    @Autowired(required = false)
    protected final Log log = LogFactory.getLog(getClass());



    @GetMapping(path = {Constantes.BASE_ENDPOINT + "/search/getTopImagen"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RespBase<List<RespImagenGeneral>>> getTopImagen(@PathVariable String access,
                                                                          @QueryParam("page") int page, @QueryParam("perPage") int perPage) throws JsonProcessingException {

        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");

        ObjectMapper mapper = new ObjectMapper();
        ProductStock productStock = new ProductStock();
        productStock.set_id(new ObjectId());
        productStock.setIdProduct(new ObjectId());
        productStock.setState(1);
        productStock.setCreationDate(new Date());
        productStock.setExpirationDate(new Date());
        Stock sto = new Stock();
        Size six = new Size();

        //six.setId(new ObjectId());
        Color co = new Color();
        List<Color> colors = new ArrayList<>();
        co.set_id(new ObjectId());
        //co.setIdProduct(new ObjectId());
        List<ImagenDetail> lstim = new ArrayList<>();
        ImagenDetail im = new ImagenDetail();
        im.setIndex(1);
        im.setName("nom");
        im.setValue("gdfgdf");
        lstim.add(im);
        co.setListImagen(lstim);
        colors.add(co);
        six.setColor(colors);
        //six.setId(new ObjectId());
        Parameters arameter = new Parameters();
        arameter.set_id(new ObjectId());
        arameter.setDescription("Descripcion Parameter");
        arameter.setName("NOmbre Paarameter");
        arameter.setIdCode("fdfd");
        //arameter.setIdParameter(new ObjectId());
        six.setParameter(arameter);
        List<Size> lstsix = new ArrayList<>();
        lstsix.add(six);
        sto.setSize(lstsix);


        productStock.setStock(sto);

        String json = mapper.writeValueAsString(productStock);
        RespBase<List<RespImagenGeneral>> response = imagenService.getTopImagen(page, perPage);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Boolean> saveClickCountImagen(@RequestBody CorouselImage corouselImage) {

        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
        Boolean response = imagenService.saveClickCountImagen(corouselImage);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Boolean> saveImagenByProduct(
            @PathVariable String access,
            @Valid @RequestBody ReqImagenByProduct request) throws Exception {

        MyJsonWebToken jwt = (MyJsonWebToken) httpServletRequest.getAttribute("jwt");
        Boolean response = imagenService.saveImagenByProduct(jwt, request);
        return ResponseEntity.ok(response);
    }

}