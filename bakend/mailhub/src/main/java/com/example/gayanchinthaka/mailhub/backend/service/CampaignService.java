package com.example.gayanchinthaka.mailhub.backend.service;

import com.example.gayanchinthaka.mailhub.backend.dto.CampaignCreateDTO;
import com.example.gayanchinthaka.mailhub.backend.dto.CampaignDetailDTO;
import com.example.gayanchinthaka.mailhub.backend.dto.CampaignSummaryDTO;
import com.example.gayanchinthaka.mailhub.backend.exception.ResourceNotFoundException;
import com.example.gayanchinthaka.mailhub.backend.model.Campaign;
import com.example.gayanchinthaka.mailhub.backend.model.CampaignStatus;
import com.example.gayanchinthaka.mailhub.backend.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampaignService {

    private final CampaignRepository campaignRepository;

    /**
     * Returns all campaigns as summary DTOs for home page tiles.
     */
    public List<CampaignSummaryDTO> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns campaigns filtered by status.
     */
    public List<CampaignSummaryDTO> getCampaignsByStatus(CampaignStatus status) {
        return campaignRepository.findByStatus(status)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    /**
     * Returns full campaign details by ID.
     */
    public CampaignDetailDTO getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", id));
        return toDetailDTO(campaign);
    }

    /**
     * Creates a new campaign in DRAFT status.
     */
    @Transactional
    public CampaignDetailDTO createCampaign(CampaignCreateDTO dto) {
        Campaign campaign = Campaign.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(CampaignStatus.DRAFT)
                .build();
        Campaign saved = campaignRepository.save(campaign);
        return toDetailDTO(saved);
    }

    /**
     * Updates an existing campaign's mutable fields.
     */
    @Transactional
    public CampaignDetailDTO updateCampaign(Long id, CampaignCreateDTO dto) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", id));
        campaign.setName(dto.getName());
        campaign.setDescription(dto.getDescription());
        Campaign saved = campaignRepository.save(campaign);
        return toDetailDTO(saved);
    }

    /**
     * Deletes a campaign by ID.
     */
    @Transactional
    public void deleteCampaign(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campaign", id);
        }
        campaignRepository.deleteById(id);
    }

    // ── Mapping helpers ─────────────────────────────────────────────────

    private CampaignSummaryDTO toSummaryDTO(Campaign campaign) {
        return CampaignSummaryDTO.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .description(campaign.getDescription())
                .status(campaign.getStatus())
                .totalEmails(campaign.getTotalEmails())
                .sentEmails(campaign.getSentEmails())
                .completionPercentage(campaign.getCompletionPercentage())
                .build();
    }

    private CampaignDetailDTO toDetailDTO(Campaign campaign) {
        return CampaignDetailDTO.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .description(campaign.getDescription())
                .status(campaign.getStatus())
                .totalEmails(campaign.getTotalEmails())
                .sentEmails(campaign.getSentEmails())
                .deliveredEmails(campaign.getDeliveredEmails())
                .clickedEmails(campaign.getClickedEmails())
                .templateContent(campaign.getTemplateContent())
                .isHtml(campaign.isHtml())
                .sendIntervalMs(campaign.getSendIntervalMs())
                .createdAt(campaign.getCreatedAt())
                .updatedAt(campaign.getUpdatedAt())
                .completionPercentage(campaign.getCompletionPercentage())
                .build();
    }
}
