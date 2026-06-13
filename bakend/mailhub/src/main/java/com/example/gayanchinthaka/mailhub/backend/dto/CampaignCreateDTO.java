package com.example.gayanchinthaka.mailhub.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignCreateDTO {

    @NotBlank(message = "Campaign name is required")
    private String name;

    private String description;
}
