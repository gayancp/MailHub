package com.example.gayanchinthaka.mailhub.backend.dto;

import com.example.gayanchinthaka.mailhub.backend.model.CampaignStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDetailDTO {

    private Long id;
    private String name;
    private String description;
    private CampaignStatus status;
    private int totalEmails;
    private int sentEmails;
    private int deliveredEmails;
    private int clickedEmails;
    private String templateContent;
    private boolean isHtml;
    private long sendIntervalMs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double completionPercentage;
}
