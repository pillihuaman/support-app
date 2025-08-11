package pillihuaman.com.pe.support.Service.Implement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.lib.common.MyJsonWebToken;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.security.dto.NotificationRequestDTO;
import pillihuaman.com.pe.security.entity.notify.ChannelType;
import pillihuaman.com.pe.support.RequestResponse.RespContact;
import pillihuaman.com.pe.support.RequestResponse.dto.bussiness.ReqContact;
import pillihuaman.com.pe.support.Service.ContactService;
import pillihuaman.com.pe.support.Service.mapper.ContactMapper;
import pillihuaman.com.pe.support.foreing.NotificationServiceClient;
import pillihuaman.com.pe.support.repository.company.ContactDAO;
import pillihuaman.com.pe.support.repository.contact.AddressEmbeddable;
import pillihuaman.com.pe.support.repository.contact.Contact;
import pillihuaman.com.pe.support.repository.contact.EmailEmbeddable;
import pillihuaman.com.pe.support.repository.contact.PhoneEmbeddable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    @Autowired
    private ContactDAO contactDAO;
    private final NotificationServiceClient notificationClient;
    private final ContactMapper mapper = ContactMapper.INSTANCE;

    public ContactServiceImpl(NotificationServiceClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Override
    public List<RespContact> getContactsByTenant(ReqContact req) {
        List<Contact> contacts = contactDAO.listContactsByTenant(req);
        return mapper.toRespContactList(contacts);
    }
    @Override
    public RespContact saveContact(ReqContact req, MyJsonWebToken token,String tokenStrin) {
        // 1. Lógica existente para mapear y ensamblar el objeto Contact
        Contact contact = mapper.toContactEntity(req);
        assembleComplexObjects(contact, req);

        // 2. Lógica existente para guardar el contacto en la base de datos
        Contact savedContact = contactDAO.saveContact(contact, token);

        // 3. NUEVO: Después de guardar, enviar la notificación
        sendContactNotification(req,tokenStrin);

        // 4. Devolver la respuesta como antes
        return mapper.toRespContact(savedContact);
    }
    private void sendContactNotification(ReqContact req ,String token) {
        // Asegurarnos de que hay una dirección de correo a la cual enviar
        if (req.getEmails() == null || req.getEmails().isEmpty()) {
            logger.warn("No se puede enviar notificación para el contacto '{}' porque no tiene correos electrónicos.", req.getName());
            return;
        }

        // Usar el primer correo de la lista como destinatario principal
        String recipientEmail = req.getEmails().get(0).getAddress();

        logger.info("Preparando notificación por correo para el nuevo contacto: {}", recipientEmail);

        // Construir el DTO para el servicio de notificaciones
        NotificationRequestDTO notificationRequest = NotificationRequestDTO.builder()
                .channel(ChannelType.EMAIL) // El canal será EMAIL
                .recipient(recipientEmail) // El destinatario
                .templateId("NEW_CONTACT_FORM") // Un ID de plantilla que tu servicio de notificaciones reconocerá
                .payload(Map.of( // Datos dinámicos para la plantilla
                        "contactName", req.getName(),
                        "message", req.getMessage(),
                        "contactDate", java.time.LocalDate.now().toString()
                ))
                .language("es")
                .build();

        try {
            // Llamar al cliente de notificaciones. Es una operación "fire-and-forget" (dispara y olvida).
            // No detendremos el flujo principal si la notificación falla, solo lo registraremos.
            RespBase<Object> notificationResponse = notificationClient.sendNotification(notificationRequest,token);

            if (notificationResponse.getStatus().getSuccess()) {
                logger.info("Solicitud de notificación para '{}' encolada exitosamente.", recipientEmail);
            } else {
                // El servicio respondió, pero indicó un error
                logger.error("El servicio de notificaciones devolvió un error: {}", notificationResponse.getStatus().getError().getMessages());
            }
        } catch (Exception e) {
            // El servicio no pudo ser contactado
            logger.error("Fallo crítico al llamar al servicio de notificaciones. Causa: ", e);
        }
    }


    private void assembleComplexObjects(Contact contact, ReqContact req) {
        // Ensamblar Dirección
        contact.setAddress(AddressEmbeddable.builder()
                .street(req.getStreet())
                .city(req.getCity())
                .region(req.getRegion())
                .country(req.getCountry())
                .build());

        // Ensamblar Teléfonos
        if (req.getPhones() != null) {
            contact.setPhones(req.getPhones().stream()
                    .map(p -> PhoneEmbeddable.builder().type(p.getType()).number(p.getNumber()).build())
                    .collect(Collectors.toList()));
        }

        // Ensamblar Correos
        if (req.getEmails() != null) {
            contact.setEmails(req.getEmails().stream()
                    .map(e -> EmailEmbeddable.builder().type(e.getType()).address(e.getAddress()).build())
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public boolean deleteContact(String id, MyJsonWebToken token) {
        return contactDAO.deleteContact(id, token);
    }
}