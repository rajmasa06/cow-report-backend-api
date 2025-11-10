package com.example.cowreport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.cowreport.model.CowReport;
import reactor.core.publisher.Mono;

@Service
public class EmailService {

    private final WebClient webClient;

    @Value("${mailgun.api.key}")
    private String mailgunApiKey;

    @Value("${mailgun.domain}")
    private String mailgunDomain;

    @Value("${app.server.base-url:http://localhost:8080}")
    private String serverBaseUrl;

    private static final String VERIFIED_EMAIL = "rajmasa06@gmail.com"; // ✅ verified recipient in Mailgun

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.mailgun.net/v3").build();
    }

    public void sendReportAlert(CowReport report) {
        try {
            String subject = "🚨 URGENT: New Cow Report - ID: " + report.getId();

            String actionLink = serverBaseUrl + "/api/cow/status/update/" + report.getId();
            String fullImageUrl = report.getImageUrl().startsWith("http")
                    ? report.getImageUrl()
                    : serverBaseUrl + report.getImageUrl();

            String emailBody = String.format(
                    "Dear Rajmasa,\n\n"
                            + "A new cow report has been submitted.\n\n"
                            + "--- REPORT DETAILS ---\n"
                            + "Report ID: %s\n"
                            + "Submitted By: %s\n"
                            + "User Contact: %s\n"
                            + "Location: %s\n"
                            + "Image Link: %s\n\n"
                            + "--- ACTION REQUIRED ---\n"
                            + "After cleaning, please click below to mark this report as DONE:\n"
                            + "%s\n\n"
                            + "Regards,\nCow Report System",
                    report.getId(),
                    report.getUserName(),
                    report.getUserEmail(),
                    report.getCurrentLocation(),
                    fullImageUrl,
                    actionLink);

            webClient.post()
                    .uri("https://api.mailgun.net/v3/" + mailgunDomain + "/messages")
                    .headers(headers -> headers.setBasicAuth("api", mailgunApiKey))
                    .bodyValue("from=Mailgun Sandbox <postmaster@" + mailgunDomain + ">"
                            + "&to=" + VERIFIED_EMAIL
                            + "&subject=" + subject
                            + "&text=" + emailBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> System.out.println("✅ Mailgun Response: " + response))
                    .onErrorResume(e -> {
                        System.err.println("❌ Mail sending failed: " + e.getMessage());
                        return Mono.empty();
                    })
                    .subscribe();

            System.out.println("✅ Mailgun email sent successfully to verified address.");
        } catch (Exception e) {
            System.err.println("❌ Error preparing email: " + e.getMessage());
        }
    }
}
