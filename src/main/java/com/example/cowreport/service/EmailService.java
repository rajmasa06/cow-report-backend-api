package com.example.cowreport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
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

    // ✅ Verified recipient email (sandbox mode me ye email Mailgun dashboard me verify hona chahiye)
    private static final String VERIFIED_EMAIL = "rajmasa06@gmail.com";

    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.mailgun.net/v3").build();
    }

    public void sendReportAlert(CowReport report) {
        try {
            // ✉️ Subject line
            String subject = "🐄 Cow Report Submitted by " + report.getUserName();

            // 🔗 Action link (to update status)
            String actionLink = serverBaseUrl + "/api/cow/status/update/" + report.getId();

            // 🖼 Full image URL (if local path then convert to full URL)
            String fullImageUrl = report.getImageUrl().startsWith("http")
                    ? report.getImageUrl()
                    : serverBaseUrl + report.getImageUrl();

            // 🌈 HTML Email Content
            String htmlMessage = """
            <html>
              <body style='font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;'>
                <div style='background-color:#fff; border-radius:10px; padding:20px; max-width:600px; margin:auto; box-shadow:0 0 10px rgba(0,0,0,0.1);'>
                  <h2 style='color:#2e6c80;'>🐄 New Cow Report Received!</h2>
                  <p>Dear <b>%s</b>,</p>
                  <p>Your cow report has been successfully submitted to the system.</p>

                  <p><b>Report ID:</b> %s</p>
                  <p><b>Location:</b> %s</p>
                  <p><b>Reporter Email:</b> %s</p>

                  <div style='text-align:center; margin:20px 0;'>
                    <img src="%s" alt="Cow Image" style="max-width:100%%; border-radius:10px;"/>
                  </div>

                  <div style='text-align:center;'>
                    <a href="%s" style="background:#4CAF50; color:white; padding:10px 20px; text-decoration:none; border-radius:5px;">View Report</a>
                  </div>

                  <p style='margin-top:20px; color:#555;'>Regards,<br>Cow Report System</p>
                </div>
              </body>
            </html>
            """.formatted(
                    report.getUserName(),
                    report.getId(),
                    report.getCurrentLocation(),
                    report.getUserEmail(),
                    fullImageUrl,
                    actionLink
            );

            // 🚀 Mailgun API Call
            webClient.post()
                    .uri("https://api.mailgun.net/v3/" + mailgunDomain + "/messages")
                    .headers(headers -> headers.setBasicAuth("api", mailgunApiKey))
                    .body(BodyInserters
                            .fromFormData("from", "Cow Report System <postmaster@" + mailgunDomain + ">")
                            .with("to", VERIFIED_EMAIL)
                            .with("subject", subject)
                            .with("html", htmlMessage)) // ✅ HTML body
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
