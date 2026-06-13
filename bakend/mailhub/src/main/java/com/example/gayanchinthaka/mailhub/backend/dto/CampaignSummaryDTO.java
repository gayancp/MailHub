package com.example.gayanchinthaka.mailhub.backend.dto;

import com.example.gayanchinthaka.mailhub.backend.model.CampaignStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignSummaryDTO {

    private Long id;
    private String name;
    private String description;
    private CampaignStatus status;
    private int totalEmails;
    private int sentEmails;
    private double completionPercentage;
}
