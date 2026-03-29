package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.ResetPasswordRequest;
import com.inmap.restfulApiInMap.entity.Password_reset_tokens;
import com.inmap.restfulApiInMap.entity.Usuario;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.repository.Password_reset_tokensRepository;
import com.inmap.restfulApiInMap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class EmailServiceImplementation implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private Password_reset_tokensRepository password_reset_tokensRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // Expresión regular estándar para validar emails
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public void sendTextEmail(String to, String subject, String body) {
        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("GestionAppInMap@yahoo.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);*/
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey("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");

        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();

        sendSmtpEmail.setSender(new SendSmtpEmailSender().email("GestionAppInMap@yahoo.com"));
        sendSmtpEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(to)));
        sendSmtpEmail.setSubject(subject);
        sendSmtpEmail.setHtmlContent(body);

        try {
            apiInstance.sendTransacEmail(sendSmtpEmail);
            System.out.println("Email enviado con éxito mediante API!");
        } catch (Exception e) {
            System.err.println("Error al enviar vía API: " + e.getMessage());
        }
    }
    public void requestPasswordReset(String data) throws NotFoundException {
        Usuario usuario = new Usuario();
        if (Pattern.compile(EMAIL_REGEX).matcher(data).matches()) {
            usuario = usuarioRepository.findByEmail(data)
                    .orElseThrow(() -> new NotFoundException("No existe un usuario asociado a ese email."));
        }
        else
        {
            // Si no es mail, se busca en la columna username
            usuario = usuarioRepository.findByUsername(data)
                    .orElseThrow(() -> new NotFoundException("El nombre de usuario no existe."));
        }
        Password_reset_tokens resetToken = Password_reset_tokens .builder()
                .token(UUID.randomUUID().toString())
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusMinutes(15)) // Expira en 15 min
                .build();
        password_reset_tokensRepository.save(resetToken);
        String uuid = resetToken.getToken();
        String link = "https://tu-app-web.com/reset-password?token=" + uuid;
        sendTextEmail(usuario.getEmail(), "Recuperación de contraseña - InMap", "Hola " + usuario.getUsername() + ", haz clic aquí para cambiar tu clave: " + link);


    }
    public void newPasswordReset(ResetPasswordRequest reset) throws NotFoundException {
        Password_reset_tokens password_reset_tokens = password_reset_tokensRepository.findByToken(reset.getToken()).orElseThrow(() -> new NotFoundException("El token no existe o ya expiró."));
        Usuario usuario = password_reset_tokens.getUsuario();
        usuario.setPassword(passwordEncoder.encode(reset.getNewPassword()));
        usuarioRepository.save(usuario);
        //Se elimina el token
        password_reset_tokensRepository.delete(password_reset_tokens);
    }
}
