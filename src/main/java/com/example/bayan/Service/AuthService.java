package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.OUT.CustomBrokerForAdminDTO;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private final EmailService emailService;
    public void acceptCustomBroker(Integer adminId, Integer customerId) {
        MyUser admin = authRepository.findMyUserById(adminId);
        if (!admin.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Sorry, you don't have the required permissions.");
        }

        CustomsBroker customsBroker = customBrokerRepository.findCustomsBrokerById(customerId);
        if (customsBroker == null) {
            throw new ApiException("Customs Broker with ID " + customerId + " doesn't exist.");
        }

        if (customsBroker.getIsActive()) {
            throw new ApiException("This broker is already active.");
        }

        // Activate the broker and assign the BROKER role
        customsBroker.setIsActive(true);
        customsBroker.getUser().setRole("BROKER");
        customBrokerRepository.save(customsBroker);

        // Send activation email
        sendActivationEmail(customsBroker);

        // Prepare and send WhatsApp notification
        String brokerPhone = customsBroker.getUser().getPhoneNumber();
        String activationMessage = String.format(
                "مرحبًا %s،\n\n" +
                        "تمت الموافقة على حسابك كمخلص جمركي. يمكنك الآن الوصول إلى النظام وتنفيذ جميع الميزات المتاحة.\n\n" +
                        "شكرًا لاستخدامك خدماتنا.\n\n" +
                        "مع تحيات فريقنا.",
                customsBroker.getUser().getFullName()
        );
        sendWhatsAppMessage(brokerPhone, activationMessage);
    }


    private void sendActivationEmail(CustomsBroker customsBroker) {
        String userEmail = customsBroker.getUser().getEmail();

        String subject = "تفعيل الحساب: مرحبًا بك كمخلص جمركي!";

        String body = "<html><body>" +
                "<p>عزيزي/عزيزتي " + customsBroker.getUser().getFullName() + "،</p>" +
                "<p>يسعدنا إعلامك بأنه قد تم تفعيل حسابك بنجاح.</p>" +
                "<p>يمكنك الآن الدخول إلى المنصة والبدء بتقديم خدمات التخليص الجمركي.</p>" +
                "<p>إذا كانت لديك أي استفسارات أو تحتاج إلى مساعدة، فلا تتردد في التواصل مع فريق الدعم الخاص بنا.</p>" +
                "<p>مع أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";

        try {
            emailService.sendEmail(userEmail, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /// Reject the Customs Broker
    public void rejectCustomBroker(Integer adminId, Integer customerId) {
        MyUser admin = authRepository.findMyUserById(adminId);
        if (!admin.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Sorry, you don't have the required permissions.");
        }

        CustomsBroker customsBroker = customBrokerRepository.findCustomsBrokerById(customerId);
        if (customsBroker == null) {
            throw new ApiException("Customs Broker with ID " + customerId + " doesn't exist.");
        }

        if (!customsBroker.getIsActive()) {
            throw new ApiException("This Customs Broker is already inactive.");
        }

        // Deactivate the broker and assign the UNACTIVE role
        customsBroker.setIsActive(false);
        customsBroker.getUser().setRole("UNACTIVE");
        customBrokerRepository.save(customsBroker);

        // Send rejection email
        sendRejectionEmail(customsBroker);

        // Prepare and send WhatsApp notification
        String brokerPhone = customsBroker.getUser().getPhoneNumber();
        String rejectionMessage = String.format(
                "مرحبًا %s،\n\n" +
                        "نأسف لإبلاغك بأنه تم رفض طلبك كمخلص جمركي. يمكنك التواصل مع فريق الدعم لمزيد من التفاصيل.\n\n" +
                        "شكرًا لتفهمك.\n\n" +
                        "مع تحيات فريقنا.",
                customsBroker.getUser().getFullName()
        );
        sendWhatsAppMessage(brokerPhone, rejectionMessage);
    }
    private void sendRejectionEmail(CustomsBroker customsBroker) {
        String userEmail = customsBroker.getUser().getEmail();

        String subject = "إشعار رفض الحساب";

        String body = "<html><body dir='rtl' style='font-family: Arial, sans-serif;'>" +
                "<p>عزيزي/عزيزتي " + customsBroker.getUser().getFullName() + "،</p>" +
                "<p>نأسف لإبلاغك بأنه قد تم رفض طلبك لتفعيل حسابك كمخلص جمركي على منصتنا.</p>" +
                "<p>إذا كنت تعتقد أن هناك خطأ أو لديك أي استفسارات، يرجى التواصل مع فريق الدعم الخاص بنا لمراجعة طلبك.</p>" +
                "<p>مع أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";

        try {
            emailService.sendEmail(userEmail, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace(); // يمكن معالجة الاستثناء هنا إذا كان يحتاج مزيد من التعامل
        }
    }


    public List<CustomBrokerForAdminDTO> getAllCustomBroker(Integer adminId) {
        MyUser admin= authRepository.findMyUserById(adminId);
        if (!admin.getRole().equalsIgnoreCase("ADMIN")){
            throw new ApiException("Sorry,you can't");
        }
        List<CustomsBroker> customsBrokers = customBrokerRepository.findAll();

        List<CustomBrokerForAdminDTO> brokerDTOS = new ArrayList<>();
        for (CustomsBroker c : customsBrokers) {
            CustomBrokerForAdminDTO cDto = new CustomBrokerForAdminDTO();
            cDto.setUsername(c.getUser().getUsername());
            cDto.setEmail(c.getUser().getEmail());
            cDto.setLicenseNumber(c.getLicenseNumber());
            cDto.setCompanyName(c.getCompanyName());
            cDto.setCommercialLicense(c.getCommercialLicense());
            cDto.setLicenseType(c.getLicenseType());
            cDto.setIsActive(c.getIsActive());

            brokerDTOS.add(cDto);
        }

        return brokerDTOS;
    }


    private void sendWhatsAppMessage(String to, String message) {
        try {
            Unirest.setTimeouts(5000, 10000); // 5 seconds to connect, 10 seconds for response
            HttpResponse<String> response = Unirest.post("https://api.ultramsg.com/instance103253/messages/chat")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("token", "66e2lh7c7hkrtj3o")
                    .field("to", to)
                    .field("body", message)
                    .asString();

            System.out.println("WhatsApp message sent successfully: " + response.getBody());
        } catch (Exception e) {
            throw new ApiException("Failed to send WhatsApp message: " + e.getMessage());
        }
    }

}

